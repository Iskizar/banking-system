package com.example.accountprocessing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO для входящего сообщения
public class AccountMessage {
    private String accountNumber;
    private String payload;

}
