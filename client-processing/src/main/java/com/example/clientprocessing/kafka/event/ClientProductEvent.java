package com.example.clientprocessing.kafka.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@Getter
@Setter
public class ClientProductEvent {
    public Long id;
    public String productId; // key + id, e.g. "DC1"
    public Long clientId;
    public String key;      // DC, CC...
    public String name;
    public LocalDate createDate;
}