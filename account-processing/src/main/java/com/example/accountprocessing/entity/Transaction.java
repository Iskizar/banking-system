package com.example.accountprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на аккаунт через ID
    @Column(name = "account_id")
    private Long accountId;

    // Ссылка на карту через ID
    @Column(name = "card_id")
    private String  cardId;

    @Column(name = "key")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type = TransactionType.OTHER;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER, PAYMENT, REFUND, OTHER
    }

    public enum TransactionStatus {
        ALLOWED, PROCESSING, COMPLETE, BLOCKED, CANCELLED
    }
}
