package com.example.creditprocessing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "product_registry")
public class ProductRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Хранится только ID клиента
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    // Хранится только ID аккаунта
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    // Хранится только ID продукта
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    public ProductRegistry() {}

    public ProductRegistry(Long clientId, Long accountId, Long productId, BigDecimal interestRate, LocalDate openDate) {
        this.clientId = clientId;
        this.accountId = accountId;
        this.productId = productId;
        this.interestRate = interestRate;
        this.openDate = openDate;
    }
}
