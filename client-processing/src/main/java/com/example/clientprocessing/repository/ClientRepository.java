package com.example.clientprocessing.repository;

import com.example.clientprocessing.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
