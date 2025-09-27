package com.example.accountprocessing.repository;

import com.example.accountprocessing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}
