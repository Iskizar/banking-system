package com.example.clientprocessing.kafka.consumer;

import com.example.clientprocessing.model.ClientProduct;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClientProductConsumer {

    @KafkaListener(topics = "client_products", groupId = "client-products-group")
    public void consume(ClientProduct clientProduct) {
        System.out.println("Получено сообщение: " + clientProduct);
    }
}
