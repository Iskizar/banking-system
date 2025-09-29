package com.example.accountprocessing.repository;

import com.example.accountprocessing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardIdAndTimestampBetween(String cardId, Instant from, Instant to);

    long countByCardIdAndTimestampBetween(String cardId, Instant from, Instant to);
}
