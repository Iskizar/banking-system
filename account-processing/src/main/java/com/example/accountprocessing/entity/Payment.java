package com.example.accountprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "interest_rate_amount")
    private BigDecimal interestRateAmount;

    @Column(name = "debt_amount")
    private BigDecimal debtAmount;

    @Column(name = "payment_expiration_date")
    private LocalDate paymentExpirationDate;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "is_credit", nullable = false)
    private boolean isCredit;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PaymentType type;

    public enum PaymentType {
        TRANSFER, CASH, CARD, ONLINE, OTHER
    }
}
