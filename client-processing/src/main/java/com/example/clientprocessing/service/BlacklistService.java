package com.example.clientprocessing.service;

import com.example.clientprocessing.repository.BlacklistRepository;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;

    public BlacklistService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    public boolean isBlacklisted(String documentId) {
        return blacklistRepository.findByDocumentId(documentId).isPresent();
    }
}
