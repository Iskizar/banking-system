package com.example.creditprocessing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payment_registry")
public class PaymentRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Хранится только ID ProductRegistry
    @Column(name = "product_registry_id", nullable = false)
    private Long productRegistryId;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "interest_rate_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal interestRateAmount;

    @Column(name = "debt_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal debtAmount;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "payment_expiration_date", nullable = false)
    private LocalDate paymentExpirationDate;

    public PaymentRegistry() {}

    public PaymentRegistry(Long productRegistryId,
                           LocalDate paymentDate,
                           BigDecimal amount,
                           BigDecimal interestRateAmount,
                           BigDecimal debtAmount,
                           Boolean expired,
                           LocalDate paymentExpirationDate) {
        this.productRegistryId = productRegistryId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.interestRateAmount = interestRateAmount;
        this.debtAmount = debtAmount;
        this.expired = expired;
        this.paymentExpirationDate = paymentExpirationDate;
    }
}
