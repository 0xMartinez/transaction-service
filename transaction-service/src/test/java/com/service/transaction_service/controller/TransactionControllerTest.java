package com.service.transaction_service.controller;

import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        final String requestBody = """
                {
                    "amount": 150.00,
                    "currency": "USD"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(content().string(matchesRegex("\\d+")));
    }

    @Test
    void shouldGetTransaction() throws Exception {

        final Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setCurrency("USD");
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.saveAndFlush(transaction);

        mockMvc.perform(get("/transactions/{id}", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(10))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void shouldGetAllTransactions() throws Exception {

        final Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setCurrency("USD");
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.saveAndFlush(transaction);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(jsonPath("$[0].amount").value(10))
                .andExpect(jsonPath("$[0].currency").value("USD"));
    }

    @Test
    void shouldDeleteExistingTransaction() throws Exception {

        final Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setCurrency("USD");
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.saveAndFlush(transaction);

        mockMvc.perform(delete("/transactions/{id}", transaction.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateTransaction() throws Exception {

        final Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setCurrency("USD");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transactionRepository.saveAndFlush(transaction);
        final String requestBody = """
                {
                    "amount": 150.00,
                    "currency": "PLN"
                }
                """;

        mockMvc.perform(put("/transactions/{id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundForNonExistentTransaction() throws Exception {
        mockMvc.perform(get("/transactions/999"))
                .andExpect(status().isNotFound());
    }
}
