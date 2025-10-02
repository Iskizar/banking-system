package com.example.common.logging.service;

import com.example.common.logging.entity.ErrorLog;
import com.example.common.logging.repository.ErrorLogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ErrorLogFallbackService {
    private final ErrorLogRepository repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ErrorLog log) {
        repo.save(log);
    }
}
