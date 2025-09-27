package com.example.creditprocessing.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreditRequestDto {
    private Long clientId;
    private Long productId;
    private BigDecimal sum;
    private Integer monthCount;
    private BigDecimal amount;
    private Long accountId;
    private BigDecimal interestRate;
}
