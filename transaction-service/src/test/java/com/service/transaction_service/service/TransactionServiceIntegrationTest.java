package com.service.transaction_service.service;

import com.example.demo.model.CreateTransactionRequest;
import com.example.demo.model.TransactionDto;
import com.example.demo.model.TransactionStatus;
import com.example.demo.model.UpdateTransactionRequest;
import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.util.TimeUtil;
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
        CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal("200.00"), "EUR");
        Long transactionId = transactionService.createTransaction(request);

        TransactionDto retrievedTransaction = transactionService.getTransaction(transactionId);
        assertThat(retrievedTransaction).isNotNull();
        assertThat(retrievedTransaction.getAmount()).isEqualTo("200.00");
        assertThat(retrievedTransaction.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void shouldUpdateTransaction() {
        final BigDecimal amount = new BigDecimal("500.00");
        final String currency = "GBP";
        CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal("100.00"), "USD");
        Long transactionId = transactionService.createTransaction(request);

        UpdateTransactionRequest updateRequest = new UpdateTransactionRequest(amount, currency, TransactionStatus.COMPLETED);
        TransactionDto updatedTransaction = transactionService.updateTransaction(transactionId, updateRequest);

        assertThat(updatedTransaction.getAmount()).isEqualTo(amount);
        assertThat(updatedTransaction.getCurrency()).isEqualTo(currency);
    }

    @Test
    void shouldDeleteTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal(100.00), "USD");
        Long transactionId = transactionService.createTransaction(request);

        transactionService.deleteTransaction(transactionId);

        assertThat(transactionRepository.findById(transactionId)).isEmpty();
    }
}
