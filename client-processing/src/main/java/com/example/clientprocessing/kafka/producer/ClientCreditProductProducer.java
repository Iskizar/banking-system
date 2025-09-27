package com.example.clientprocessing.kafka.producer;

import com.example.clientprocessing.model.ClientProduct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientCreditProductProducer {

    private final KafkaTemplate<String, ClientProduct> kafkaTemplate;

    private final String topic = "client_credit_products"; // можешь вынести в application.yml

    public void sendToTopic(ClientProduct clientProduct) {
        String key = String.valueOf(clientProduct.getClientId());

        kafkaTemplate.send(topic, key, clientProduct)
                .whenComplete((SendResult<String, ClientProduct> result, Throwable ex) -> {
                    if (ex == null && result != null) {
                        RecordMetadata meta = result.getRecordMetadata();
                        System.out.printf("Credit product отправлен: %s, topic=%s, partition=%d, offset=%d%n",
                                clientProduct, meta.topic(), meta.partition(), meta.offset());
                    } else {
                        System.err.println("Ошибка при отправке credit product: " + ex.getMessage());
                    }
                });
    }
}
