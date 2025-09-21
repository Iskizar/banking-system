package com.example.clientprocessing.repository;

import com.example.clientprocessing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
