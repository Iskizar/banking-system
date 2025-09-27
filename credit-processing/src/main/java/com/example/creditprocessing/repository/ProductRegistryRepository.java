package com.example.creditprocessing.repository;

import com.example.creditprocessing.model.ProductRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {

    List<ProductRegistry> findByClientId(Long clientId);

}
