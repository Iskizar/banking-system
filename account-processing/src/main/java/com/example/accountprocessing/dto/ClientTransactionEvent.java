package com.example.accountprocessing.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ClientTransactionEvent {
    private UUID key;           // ключ сообщения — UUID
    private Long accountId;     // id счёта
    private String cardId;      // id карты (может быть UUID в строке)
    private String type;        // "DEBIT" или "CREDIT"
    private BigDecimal amount;
    private Instant timestamp;  // момент транзакции
}
