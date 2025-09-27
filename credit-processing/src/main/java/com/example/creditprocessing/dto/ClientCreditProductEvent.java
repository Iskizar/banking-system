package com.example.creditprocessing.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ClientCreditProductEvent {
    private Long clientId;
    private Long accountId;
    private Long productId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer monthCount;
}
