package com.example.accountprocessing.service;

import com.example.accountprocessing.dto.ClientTransactionEvent;
import com.example.accountprocessing.entity.Account;
import com.example.accountprocessing.entity.Payment;
import com.example.accountprocessing.entity.Transaction;
import com.example.accountprocessing.repository.AccountRepository;
import com.example.accountprocessing.repository.PaymentRepository;
import com.example.accountprocessing.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Value("${fraud.tx.threshold:5}")
    private int fraudThreshold;

    @Value("${fraud.tx.window.minutes:1}")
    private int fraudWindowMinutes;

    @Value("${credit.default.months:12}")
    private int defaultCreditMonths;

    /**
     * Основной метод обработки транзакции.
     */
    @Transactional
    public void processTransaction(ClientTransactionEvent ev) {
        UUID key = ev.getKey() != null ? ev.getKey() : UUID.randomUUID();
        // Создаём запись транзакции в статусе PROCESSING
        Transaction tx = new Transaction();
        tx.setKey(key.toString());
        tx.setAccountId(ev.getAccountId());
        tx.setCardId(ev.getCardId());
        tx.setType(Transaction.TransactionType.valueOf(ev.getType()));
        tx.setAmount(ev.getAmount());
        tx.setTimestamp(ev.getTimestamp() != null ? LocalDateTime.ofInstant(ev.getTimestamp(), ZoneId.systemDefault()) : LocalDateTime.now());
        tx.setStatus(Transaction.TransactionStatus.PROCESSING);

        transactionRepository.save(tx);

        // Проверки и обработка
        Optional<Account> optAcc = accountRepository.findById(ev.getAccountId());
        if (optAcc.isEmpty()) {
            tx.setStatus(Transaction.TransactionStatus.CANCELLED);
            transactionRepository.save(tx);
            return;
        }

        Account account = optAcc.get();

        // d) Проверка на спайк транзакций по карте (fraud)
        if (ev.getCardId() != null && !ev.getCardId().isBlank()) {
            Instant now = Instant.now();
            Instant windowStart = now.minus(Duration.ofMinutes(fraudWindowMinutes));
            long recentCount = transactionRepository.countByCardIdAndTimestampBetween(ev.getCardId(), windowStart, now);
            if (recentCount >= fraudThreshold) {
                // заморозить транзакции и заблокировать счёт
                blockAccount(account, "fraud: threshold exceeded (" + recentCount + ")");
                // пометим текущую транзакцию как BLOCKED
                tx.setStatus(Transaction.TransactionStatus.BLOCKED);
                transactionRepository.save(tx);
                return;
            }
        }

        // b) Если счёт заблокирован / арестован — блокируем транзакцию
        if (account.getStatus() == Account.AccountStatus.BLOCKED
                || account.getStatus() == Account.AccountStatus.ARRESTED) {
            tx.setStatus(Transaction.TransactionStatus.BLOCKED);
            transactionRepository.save(tx);
            return;
        }

        // Обработка по типу
        String typeUpper = ev.getType() != null ? ev.getType().toUpperCase() : "UNKNOWN";
        if ("DEBIT".equals(typeUpper)) {
            processDebit(tx, account);
        } else if ("CREDIT".equals(typeUpper)) {
            processCredit(tx, account);
        } else {
            tx.setStatus(Transaction.TransactionStatus.CANCELLED);
            transactionRepository.save(tx);
        }

        // c) Если счёт кредитный (isRecalc == true) — и ещё нет платежного графика, создаём его
        if (Boolean.TRUE.equals(account.getIsRecalc())) {
            List<Payment> payments = paymentRepository.findByAccountId(account.getId());
            if (payments.isEmpty()) {
                // создаём график на defaultCreditMonths, по ставке account.getInterestRate()
                createPaymentScheduleForCreditAccount(account, account.getBalance(), defaultCreditMonths);
            }
        }

        // e) Если тип транзакции — начисление (CREDIT), счёт кредитный, есть платёж на дату tx и сегодня — попытка автоматического списания
        if ("CREDIT".equals(typeUpper) && Boolean.TRUE.equals(account.getIsRecalc())) {
            LocalDate txDate = tx.getTimestamp().toLocalDate();
            Optional<Payment> maybePayment = paymentRepository.findByAccountIdAndPaymentDate(account.getId(), txDate);
            if (maybePayment.isPresent()) {
                Payment payment = maybePayment.get();
                BigDecimal paymentAmount = payment.getAmount();
                if (account.getBalance().compareTo(paymentAmount) >= 0) {
                    // списываем
                    account.setBalance(account.getBalance().subtract(paymentAmount));
                    payment.setPayedAt(LocalDateTime.now());
                    payment.setExpired(false);
                    paymentRepository.save(payment);
                    accountRepository.save(account);
                    // лог
                    System.out.println("Auto-collected payment " + payment.getId() + " for account " + account.getId());
                } else {
                    // недостаточно средств — помечаем expired (не списали)
                    payment.setExpired(true);
                    paymentRepository.save(payment);
                    System.out.println("Not enough funds to collect payment " + payment.getId() + " for account " + account.getId());
                }
            }
        }
    }

    private void processDebit(Transaction tx, Account account) {
        BigDecimal amount = tx.getAmount();
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            tx.setStatus(Transaction.TransactionStatus.COMPLETE);
            accountRepository.save(account);
            transactionRepository.save(tx);
        } else {
            tx.setStatus(Transaction.TransactionStatus.CANCELLED);
            transactionRepository.save(tx);
        }
    }

    private void processCredit(Transaction tx, Account account) {
        BigDecimal amount = tx.getAmount();
        account.setBalance(account.getBalance().add(amount));
        tx.setStatus(Transaction.TransactionStatus.COMPLETE);
        accountRepository.save(account);
        transactionRepository.save(tx);
    }

    private void blockAccount(Account account, String reason) {
        account.setStatus(Account.AccountStatus.BLOCKED);
        accountRepository.save(account);

        // пометим последние транзакции по карте как BLOCKED (если есть карта)
        // можно расширить: получить последние N транзакций и обновить статус
        System.out.println("Account " + account.getId() + " blocked: " + reason);
    }

    /**
     * Создаёт упрощённый график платежей для кредитного счёта (аннуитет).
     * principal — текущая задолженность (balance), months — количество месяцев.
     */
    private void createPaymentScheduleForCreditAccount(Account account, BigDecimal principal, int months) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) return;
        BigDecimal annualRate = account.getInterestRate() != null ? account.getInterestRate() : BigDecimal.ZERO; // в процентах
        BigDecimal monthlyPayment = calculateAnnuity(principal, annualRate, months);

        BigDecimal remaining = principal;
        for (int m = 1; m <= months; m++) {
            BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
            BigDecimal interest = remaining.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPart = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            remaining = remaining.subtract(principalPart).setScale(2, RoundingMode.HALF_UP);

            Payment p = new Payment();
            p.setAccountId(account.getId());
            p.setPaymentDate(LocalDate.now().plusMonths(m).atStartOfDay()); // даты платежей — от сегодня
            p.setAmount(monthlyPayment);
            p.setInterestRateAmount(interest);
            p.setDebtAmount(principalPart);
            p.setExpired(false);
            p.setPaymentExpirationDate(LocalDate.now().plusMonths(m));
            p.setPayedAt(null);

            paymentRepository.save(p);
        }
        System.out.println("Payment schedule created for account " + account.getId() + ", monthly = " + monthlyPayment);
    }

    private BigDecimal calculateAnnuity(BigDecimal principal, BigDecimal annualRatePercent, int months) {
        if (annualRatePercent == null || annualRatePercent.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        }
        BigDecimal i = annualRatePercent.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusPow = (BigDecimal.ONE.add(i)).pow(months);
        BigDecimal numerator = principal.multiply(i).multiply(onePlusPow);
        BigDecimal denominator = onePlusPow.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
