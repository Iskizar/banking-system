package com.example.clientprocessing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 3)
    private String key; // DC, CC, AC, IPO, PC, PENS, NS, INS, BS

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId; // key + id


    @PrePersist
    @PreUpdate
    private void updateProductId() {
        if (this.key != null && this.id != null) {
            this.productId = this.key + this.id;
        }
    }
}
