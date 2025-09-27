package com.example.clientprocessing.kafka.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ClientTransactionEvent {
    public Long id;
    public Long accountId;
    public String cardId;
    public String type; // e.g. PAYMENT, REFUND...
    public Integer amount;
    public LocalDate timestamp;
}