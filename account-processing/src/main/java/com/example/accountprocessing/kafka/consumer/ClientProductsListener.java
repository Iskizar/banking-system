package com.example.accountprocessing.kafka.consumer;


import com.example.clientprocessing.DTO.ClientProductMessage;
import com.example.accountprocessing.entity.ProductEntity;
import com.example.accountprocessing.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ClientProductsListener {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "client_products", groupId = "account-processing")
    public void listen(String message) {
        try {
            ClientProductMessage dto = objectMapper.readValue(message, ClientProductMessage.class);

            ProductEntity product = new ProductEntity();
            product.setProductId(dto.getKey() + "_" + dto.getClientId()); // пример
            product.setName(dto.getName());
            product.setKey(dto.getKey());
            product.setCreateDate(LocalDate.now());


            productRepository.save(product);

            System.out.println("Product saved: " + product.getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
