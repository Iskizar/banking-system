package com.example.clientprocessing.kafka.producer;

import com.example.clientprocessing.model.ClientProduct;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//package com.example.clientprocessing.kafka.producer;
//
//import com.example.clientprocessing.model.ClientProduct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ClientProductProducer {
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    public void sendToTopic(ClientProduct clientProduct, String productKey) {
//        String topic;
//        if (List.of("DC", "CC", "NS", "PENS").contains(productKey)) {
//            topic = "client_products";
//        } else if (List.of("IPO", "PC", "AC").contains(productKey)) {
//            topic = "client_credit_products";
//        } else {
//            throw new IllegalArgumentException("Unknown product key: " + productKey);
//        }
//
//        kafkaTemplate.send(topic, clientProduct);
//    }
//}
@Service
public class ClientProductProducer {

    private final KafkaTemplate<String, ClientProduct> kafkaTemplate;

    public ClientProductProducer(KafkaTemplate<String, ClientProduct> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToTopic(ClientProduct clientProduct) {
        kafkaTemplate.send("client_products", clientProduct);
        System.out.println("Сообщение отправлено: " + clientProduct);
    }
}
