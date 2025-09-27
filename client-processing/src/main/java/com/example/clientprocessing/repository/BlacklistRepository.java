package com.example.clientprocessing.repository;

import com.example.clientprocessing.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByDocumentId(String documentId);
}
