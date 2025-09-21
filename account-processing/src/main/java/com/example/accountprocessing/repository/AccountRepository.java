package com.example.accountprocessing.repository;

import com.example.accountprocessing.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
