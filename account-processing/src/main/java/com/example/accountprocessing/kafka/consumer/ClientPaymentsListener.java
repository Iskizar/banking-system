package com.example.accountprocessing.kafka.consumer;

import com.example.accountprocessing.dto.ClientPaymentMessage;
import com.example.accountprocessing.entity.Account;
import com.example.accountprocessing.entity.Payment;
import com.example.accountprocessing.repository.AccountRepository;
import com.example.accountprocessing.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientPaymentsListener {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "client_payments", groupId = "credit-service")
    public void listen(ClientPaymentMessage message) {
        Optional<Account> accountOpt = accountRepository.findById(message.getAccountId());

        if (accountOpt.isEmpty()) {
            System.out.println("Счёт не найден: " + message.getAccountId());
            return;
        }

        Account account = accountOpt.get();

        // Считаем задолженность (сумма всех платежей без payed_at)
        BigDecimal debt = paymentRepository.findByAccountIdAndPayedAtIsNull(account.getId())
                .stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Проверка: платёж равен задолженности
        if (message.getAmount().compareTo(debt) == 0) {
            // Пересчитываем баланс
            account.setBalance(account.getBalance().add(message.getAmount()));
            accountRepository.save(account);

            // Создаём новый платёж
            Payment newPayment = new Payment();
            newPayment.setAccountId(account.getId());
            newPayment.setAmount(message.getAmount());
            newPayment.setPaymentDate(LocalDate.now().atStartOfDay());
            newPayment.setPayedAt(LocalDateTime.now());
            newPayment.setType(Payment.PaymentType.valueOf("CREDIT_REPAYMENT"));
            paymentRepository.save(newPayment);

            // Обновляем все старые платежи по счёту
            List<Payment> existing = paymentRepository.findByAccountIdAndPayedAtIsNull(account.getId());
            for (Payment p : existing) {
                p.setPayedAt(LocalDateTime.now());
            }
            paymentRepository.saveAll(existing);

            System.out.println("Платёж обработан: " + message.getAmount() + " для счёта " + account.getId());
        } else {
            System.out.println("Сумма платежа не совпадает с задолженностью (ожидалось: " + debt + ")");
        }
    }
}
