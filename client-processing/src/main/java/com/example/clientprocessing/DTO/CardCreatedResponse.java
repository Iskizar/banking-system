package com.example.clientprocessing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardCreatedResponse {
    private Long clientProductId;
    private String productKey;
    private Long clientId;
    private Long accountId;
}
