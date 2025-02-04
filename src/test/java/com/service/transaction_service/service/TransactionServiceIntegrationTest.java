package com.service.transaction_service.service;

import com.transaction.model.CreateTransactionRequest;
import com.transaction.model.TransactionDto;
import com.transaction.model.TransactionStatus;
import com.transaction.model.UpdateTransactionRequest;
import com.service.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;


    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldGetTransaction() {
        final CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal("200.00"), "EUR");
        final Long transactionId = transactionService.createTransaction(request);

        final TransactionDto retrievedTransaction = transactionService.getTransaction(transactionId);
        assertThat(retrievedTransaction).isNotNull();
        assertThat(retrievedTransaction.getAmount()).isEqualTo("200.00");
        assertThat(retrievedTransaction.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void shouldUpdateTransaction() {
        final BigDecimal amount = new BigDecimal("500.00");
        final String currency = "GBP";
        final CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal("100.00"), "USD");
        final Long transactionId = transactionService.createTransaction(request);

        final UpdateTransactionRequest updateRequest = new UpdateTransactionRequest(amount, currency, TransactionStatus.COMPLETED);
        final TransactionDto updatedTransaction = transactionService.updateTransaction(transactionId, updateRequest);

        assertThat(updatedTransaction.getAmount()).isEqualTo(amount);
        assertThat(updatedTransaction.getCurrency()).isEqualTo(currency);
    }

    @Test
    void shouldDeleteTransaction() {
        final CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal(100.00), "USD");
        final Long transactionId = transactionService.createTransaction(request);

        transactionService.deleteTransaction(transactionId);

        assertThat(transactionRepository.findById(transactionId)).isEmpty();
    }
}
