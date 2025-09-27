package com.example.clientprocessing.DTO;

import lombok.Data;

@Data
public class CreateCardRequest {
    private Long accountId; // к какому счету привязываем
    private String cardType;// "DC" или "CC"
    private Long cardId;
}