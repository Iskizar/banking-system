package com.example.accountprocessing.controller;

import com.example.accountprocessing.service.TestService;
import com.example.accountprocessing.util.JsonUtil;
import com.example.clientprocessing.kafka.event.ClientCardEvent;
import com.example.clientprocessing.kafka.event.ClientProductEvent;
import com.example.clientprocessing.kafka.event.ClientTransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/test")
public class TestController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TestService someService;

    private final JsonUtil jsonUtil;

    public TestController(KafkaTemplate<String, String> kafkaTemplate, TestService someService, JsonUtil jsonUtil) {
        this.kafkaTemplate = kafkaTemplate;
        this.someService = someService;
        this.jsonUtil = jsonUtil;
    }


    @PostMapping("/client_cards")
    public String sendCardTest(@RequestParam Long accountId) throws Exception {
        ClientCardEvent event = new ClientCardEvent();
        event.setAccountId(accountId);
        event.setCardId("CARD-" + System.currentTimeMillis());
        event.setPaymentSystem("VISA");

        String json = JsonUtil.toJson(event);
        kafkaTemplate.send("client_cards", json);

        return "Sent to client_cards: " + json;
    }

    @PostMapping("/client_products")
    public String sendProductTest(@RequestParam Long clientId,
                                  @RequestParam String productId) throws Exception {
        ClientProductEvent event = new ClientProductEvent();
        event.setClientId(clientId);
        event.setProductId(productId);

        String json = JsonUtil.toJson(event);
        kafkaTemplate.send("client_products", json);

        return "Sent to client_products: " + json;
    }


    @PostMapping("/client_transactions")
    public String sendTransactionTest(@RequestParam Long accountId,
                                      @RequestParam String cardId,
                                      @RequestParam String type,
                                      @RequestParam BigDecimal amount) throws Exception {
        ClientTransactionEvent event = new ClientTransactionEvent();
        event.setAccountId(accountId);
        event.setCardId(cardId);
        event.setType(type);
        event.setAmount((int) amount.doubleValue());
        event.setTimestamp(LocalDate.now());

        String json = jsonUtil.toJson(event);
        ;
        kafkaTemplate.send("client_transactions", json);

        return "Sent to client_transactions: " + json;
    }
    @GetMapping("/error")
    public String triggerError() {
        someService.testError();
        return "ok";
    }

}
