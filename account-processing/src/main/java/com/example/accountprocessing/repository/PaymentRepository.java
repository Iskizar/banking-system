package com.example.accountprocessing.repository;

import com.example.accountprocessing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountId(Long accountId);

    // платежи на дату
    Optional<Payment> findByAccountIdAndPaymentDate(Long accountId, LocalDate paymentDate);

    // непогашенные платежи
    List<Payment> findByAccountIdAndExpiredFalse(Long accountId);
    List<Payment> findByAccountIdAndPayedAtIsNull(Long accountId);
}
