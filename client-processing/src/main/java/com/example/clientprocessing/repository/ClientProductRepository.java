package com.example.clientprocessing.repository;

import com.example.clientprocessing.model.ClientProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientProductRepository extends JpaRepository<ClientProduct, Long> {
    List<ClientProduct> findByClientId(Long clientId);
}
