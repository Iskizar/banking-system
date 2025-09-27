package com.example.accountprocessing.repository;

import com.example.accountprocessing.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByClientIdAndProductId(Long clientId, Long productId);

    Optional<Account> findById(Long id);
}
