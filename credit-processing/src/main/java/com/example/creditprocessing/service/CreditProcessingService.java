package com.example.creditprocessing.service;

import com.example.creditprocessing.dto.ClientCreditProductEvent;
import com.example.creditprocessing.dto.CreditRequestDto;
import com.example.creditprocessing.model.ProductRegistry;
import com.example.creditprocessing.model.PaymentRegistry;
import com.example.creditprocessing.repository.ProductRegistryRepository;
import com.example.creditprocessing.repository.PaymentRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditProcessingService {

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;

    @Value("${credit.limit}")
    private BigDecimal creditLimit; // лимит кредита из application.properties

    public void processCredit(Long clientId, Long productId, BigDecimal sum, Integer monthCount) {
        // Получаем все кредиты клиента
        List<ProductRegistry> existingProducts = productRegistryRepository.findByClientId(clientId);

        // Суммарная задолженность
        BigDecimal totalDebt = existingProducts.stream()
                .map(this::getTotalDebtForProduct)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 1. Проверка лимита
        if (totalDebt.add(sum).compareTo(creditLimit) > 0) {
            throw new RuntimeException("Сумма кредита превышает лимит");
        }

        // 2. Проверка просрочек
        boolean hasDelays = existingProducts.stream()
                .anyMatch(p -> paymentRegistryRepository.existsByProductRegistryIdAndExpiredTrue(p.getId()));

        if (hasDelays) {
            throw new RuntimeException("Отказ: есть просроченные платежи");
        }

        // 3. Создание записи ProductRegistry
        ProductRegistry registry = new ProductRegistry();
        registry.setClientId(clientId);
        registry.setAccountId(0L); // пока 0, так как у нас нет создания аккаунта
        registry.setProductId(productId);
        registry.setInterestRate(BigDecimal.valueOf(22.0)); // пример: 22% годовых
        registry.setOpenDate(LocalDate.now());
        registry.setMonthCount(monthCount);

        productRegistryRepository.save(registry);

        // 4. Создание графика платежей
        generatePaymentSchedule(registry, sum);

        System.out.println("Кредит успешно обработан");
    }

    private BigDecimal getTotalDebtForProduct(ProductRegistry productRegistry) {
        return paymentRegistryRepository.findByProductRegistryId(productRegistry.getId())
                .stream()
                .map(PaymentRegistry::getDebtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRatePowN = (BigDecimal.ONE.add(monthlyRate)).pow(months);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRatePowN);
        BigDecimal denominator = onePlusRatePowN.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private void generatePaymentSchedule(ProductRegistry productRegistry, BigDecimal principal) {
        int months = productRegistry.getMonthCount();
        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, productRegistry.getInterestRate(), months);

        BigDecimal remainingDebt = principal;

        for (int month = 1; month <= months; month++) {
            BigDecimal monthlyRate = productRegistry.getInterestRate().divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
            BigDecimal interest = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPayment = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);

            remainingDebt = remainingDebt.subtract(principalPayment).setScale(2, RoundingMode.HALF_UP);

            PaymentRegistry payment = new PaymentRegistry();
            payment.setProductRegistryId(productRegistry.getId());
            payment.setPaymentDate(LocalDate.now().plusMonths(month));
            payment.setAmount(monthlyPayment);
            payment.setInterestRateAmount(interest);
            payment.setDebtAmount(principalPayment);
            payment.setExpired(false);
            payment.setPaymentExpirationDate(LocalDate.now().plusMonths(month));

            paymentRegistryRepository.save(payment);
        }
    }

    public void processCreditRequest(CreditRequestDto request) {
        processCredit(
                request.getClientId(),
                request.getProductId(),
                request.getSum(),
                request.getMonthCount()
        );
    }

    public void processCreditRequest(ClientCreditProductEvent event) {
        processCredit(
                event.getClientId(),
                event.getProductId(),
                event.getAmount(),
                event.getMonthCount()
        );
    }
}
