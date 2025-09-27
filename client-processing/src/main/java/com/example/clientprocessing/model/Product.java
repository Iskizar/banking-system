package com.example.clientprocessing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @SequenceGenerator(
            name = "products_seq",
            sequenceName = "products_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 3)
    private String key; // DC, CC ...

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId; // key + id

    @PrePersist
    private void prePersist() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
        // При SEQUENCE id уже назначен до insert -> можно использовать
        if (this.id != null && this.key != null) {
            this.productId = this.key + this.id;
        }
    }
}
