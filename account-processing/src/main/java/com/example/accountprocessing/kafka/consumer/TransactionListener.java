package com.example.accountprocessing.kafka.consumer;

import com.example.accountprocessing.dto.ClientTransactionEvent;
import com.example.accountprocessing.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionListener {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "client_transactions", groupId = "account-processing")
    public void listen(String payload) {
        try {
            ClientTransactionEvent ev = objectMapper.readValue(payload, ClientTransactionEvent.class);
            transactionService.processTransaction(ev);
        } catch (Exception e) {
            // лог — не даём падать контейнеру
            e.printStackTrace();
        }
    }
}
