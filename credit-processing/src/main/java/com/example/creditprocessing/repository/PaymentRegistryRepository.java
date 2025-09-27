package com.example.creditprocessing.repository;

import com.example.creditprocessing.model.PaymentRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRegistryRepository extends JpaRepository<PaymentRegistry, Long> {

    List<PaymentRegistry> findByProductRegistryId(Long productRegistryId);

    boolean existsByProductRegistryIdAndExpiredTrue(Long productRegistryId);

}
