package com.example.clientprocessing.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardEvent {
    private Long clientId;
    private Long accountId;
    private Long clientProductId;
    private String cardType;
}
