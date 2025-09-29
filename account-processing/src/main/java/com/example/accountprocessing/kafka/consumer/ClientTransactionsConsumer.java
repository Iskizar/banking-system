package com.example.accountprocessing.kafka.consumer;

import com.example.accountprocessing.entity.Account;
import com.example.accountprocessing.entity.Transaction;
import com.example.accountprocessing.repository.AccountRepository;
import com.example.accountprocessing.repository.TransactionRepository;
import com.example.clientprocessing.kafka.event.ClientTransactionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// слушатель client_transactions
@Component
public class ClientTransactionsConsumer {
    private final ObjectMapper mapper;
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;

    public ClientTransactionsConsumer(ObjectMapper mapper,
                                      AccountRepository accountRepo,
                                      TransactionRepository txRepo) {
        this.mapper = mapper;
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
    }

    @KafkaListener(topics = "client_transactions", groupId = "account-processing")
    public void consume(String payload) throws Exception {
        ClientTransactionEvent ev = mapper.readValue(payload, ClientTransactionEvent.class);

        Transaction tx = new Transaction();
        tx.setAccountId(ev.accountId);
        tx.setCardId(ev.cardId);
        tx.setAmount(BigDecimal.valueOf(ev.amount));
        tx.setTimestamp(ev.timestamp.atStartOfDay());
        tx.setStatus(Transaction.TransactionStatus.PROCESSING);
        txRepo.save(tx);

        accountRepo.findById(ev.accountId).ifPresentOrElse(account -> {
            if (account.getStatus() != Account.AccountStatus.ACTIVE) {
                tx.setStatus(Transaction.TransactionStatus.BLOCKED);
            } else if ("DEBIT".equalsIgnoreCase(ev.type)
                    && account.getBalance().compareTo(BigDecimal.valueOf(ev.amount)) >= 0) {
                account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(ev.amount)));
                tx.setStatus(Transaction.TransactionStatus.COMPLETE);
                accountRepo.save(account);
            } else if ("CREDIT".equalsIgnoreCase(ev.type)) {
                account.setBalance(account.getBalance().add(BigDecimal.valueOf(ev.amount)));
                tx.setStatus(Transaction.TransactionStatus.COMPLETE);
                accountRepo.save(account);
            } else {
                tx.setStatus(Transaction.TransactionStatus.CANCELLED);
            }
            txRepo.save(tx);
        }, () -> {
            tx.setStatus(Transaction.TransactionStatus.CANCELLED);
            txRepo.save(tx);
            System.out.println("[ClientTransactionsConsumer] Transaction is saved: " + tx.getId());
        });
    }
}
