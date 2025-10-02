package com.example.accountprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientPaymentMessage {
    private Long accountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
