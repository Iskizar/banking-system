package com.example.accountprocessing.service;

import com.example.accountprocessing.dto.AccountMessage;
import com.example.accountprocessing.entity.Account;
import com.example.accountprocessing.entity.Card;
import com.example.accountprocessing.repository.AccountRepository;
import com.example.accountprocessing.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public AccountService(AccountRepository accountRepository,
                          CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    public Map<String, Object> processAccountMessage(AccountMessage message) {
        return accountRepository.findById(Long.valueOf(message.getAccountNumber()))
                .filter(account -> !account.isBlocked()) // проверка, что счёт не заблокирован
                .map(account -> {
                    // создаём карту
                    Card card = new Card();
                    card.setAccountId(account.getId());
                    card.setCardId(UUID.randomUUID().toString());
                    card.setPaymentSystem("VISA");
                    card.setStatus(Card.CardStatus.valueOf("ACTIVE"));
                    cardRepository.save(card);

                    Map<String, Object> result = new HashMap<>();
                    result.put("accountNumber", account.getId());
                    result.put("cardId", card.getCardId());
                    result.put("timestamp", System.currentTimeMillis());
                    return result;
                })
                .orElseGet(() -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("error", "Account not found or blocked");
                    return result;
                });
    }
}
