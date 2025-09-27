package com.example.clientprocessing.kafka.event;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCardEvent {
    public Long id;
    public Long accountId;
    public Long clientId;
    public String cardId;
    public String paymentSystem;
    public String status;
}