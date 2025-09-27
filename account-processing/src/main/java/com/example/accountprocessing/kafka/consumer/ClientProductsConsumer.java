package com.example.accountprocessing.kafka.consumer;

import com.example.accountprocessing.entity.Account;
import com.example.accountprocessing.repository.AccountRepository;
import com.example.clientprocessing.kafka.event.ClientProductEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class ClientProductsConsumer {
    private final ObjectMapper mapper;
    private final AccountRepository accountRepo;

    public ClientProductsConsumer(ObjectMapper mapper, AccountRepository accountRepo) {
        this.mapper = mapper;
        this.accountRepo = accountRepo;
    }

    @KafkaListener(topics = "client_products", groupId = "account-processing")
    public void consume(String payload) throws Exception {
        ClientProductEvent ev = mapper.readValue(payload, ClientProductEvent.class);
        accountRepo.findByClientIdAndProductId(ev.clientId, Long.valueOf(ev.productId))
                .orElseGet(() -> {
                    Account acc = new Account();
                    acc.setClientId(ev.clientId);
                    acc.setProductId(Long.valueOf(ev.productId));
                    acc.setBalance(BigDecimal.ZERO);
                    acc.setInterestRate(BigDecimal.ZERO);
                    acc.setStatus(Account.AccountStatus.ACTIVE);
                    return accountRepo.save(acc);
                });

    }
}
