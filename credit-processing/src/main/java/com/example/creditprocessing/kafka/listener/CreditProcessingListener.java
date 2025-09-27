package com.example.creditprocessing.kafka.listener;

import com.example.creditprocessing.dto.ClientCreditProductEvent;
import com.example.creditprocessing.service.CreditProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditProcessingListener {

    private final CreditProcessingService creditProcessingService;

    @KafkaListener(topics = "client_credit_products", groupId = "credit-processing")
    public void listen(ClientCreditProductEvent event) {
        try {
            creditProcessingService.processCreditRequest(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
