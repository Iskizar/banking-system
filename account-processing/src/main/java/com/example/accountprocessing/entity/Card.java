package com.example.accountprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "card_id", nullable = false, unique = true)
    private String cardId;

    @Column(name = "payment_system")
    private String paymentSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardStatus status;

    public enum CardStatus {
        ACTIVE, BLOCKED, EXPIRED, CANCELLED
    }
}
