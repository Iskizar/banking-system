package com.example.accountprocessing.kafka.consumer;

import com.example.accountprocessing.entity.Account;
import com.example.accountprocessing.entity.Card;
import com.example.accountprocessing.repository.AccountRepository;
import com.example.accountprocessing.repository.CardRepository;
import com.example.clientprocessing.kafka.event.ClientCardEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClientCardsConsumer {
    private final ObjectMapper mapper;
    private final AccountRepository accountRepo;
    private final CardRepository cardRepo;

    public ClientCardsConsumer(ObjectMapper mapper, AccountRepository accountRepo, CardRepository cardRepo) {
        this.mapper = mapper;
        this.accountRepo = accountRepo;
        this.cardRepo = cardRepo;
    }

    @KafkaListener(topics = "client_cards", groupId = "account-processing")
    public void consume(String payload) throws Exception {
        ClientCardEvent ev = mapper.readValue(payload, ClientCardEvent.class);
        accountRepo.findById(ev.accountId).ifPresent(account -> {
            if (account.getStatus() == Account.AccountStatus.BLOCKED || account.getStatus() == Account.AccountStatus.ARRESTED) {
                return; // не создаём карту
            }
            Card card = new Card();
            card.setAccountId(account.getId());
            card.setCardId(ev.cardId);
            card.setPaymentSystem(ev.paymentSystem);
            card.setStatus(Card.CardStatus.ACTIVE);
            cardRepo.save(card);

            account.setCardExist(true);
            accountRepo.save(account);
            System.out.println("[ClientCardsConsumer] Card created: " + card.getCardId());
        });
    }
}
