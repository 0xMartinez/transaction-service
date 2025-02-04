package com.service.transaction_service.service;

import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import com.service.transaction_service.util.TimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionMonitorServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private TimeUtil timeUtil;

    @InjectMocks
    private TransactionMonitorService transactionMonitorService;

    @Captor
    private ArgumentCaptor<String> kafkaMessageCaptor;

    private Transaction completedTransaction;
    private Transaction pendingTransaction;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        completedTransaction = new Transaction();
        completedTransaction.setId(1L);
        completedTransaction.setAmount(BigDecimal.valueOf(100.50));
        completedTransaction.setCurrency("PLN");
        completedTransaction.setTransactionStatus(TransactionStatus.COMPLETED);
        completedTransaction.setUpdatedAt(now);

        pendingTransaction = new Transaction();
        pendingTransaction.setId(2L);
        pendingTransaction.setAmount(BigDecimal.valueOf(200.75));
        pendingTransaction.setCurrency("EUR");
        pendingTransaction.setTransactionStatus(TransactionStatus.PENDING);
        pendingTransaction.setCreatedAt(now.minusSeconds(40));
    }

    @Test
    void shouldProcessCompletedTransactionsAndSendToKafka() {
        when(transactionRepository.findByTransactionStatusAndUpdatedAtAfter(eq(TransactionStatus.COMPLETED), any()))
                .thenReturn(List.of(completedTransaction));

        transactionMonitorService.processTransactions();

        verify(kafkaTemplate).send(eq("transakcje-zrealizowane"), kafkaMessageCaptor.capture());

        String expectedMessage = createTransactionMessage(completedTransaction);
        assertEquals(expectedMessage, kafkaMessageCaptor.getValue());

        verify(transactionRepository, times(1))
                .findByTransactionStatusAndUpdatedAtAfter(eq(TransactionStatus.COMPLETED), any());
    }

    @Test
    void shouldProcessPendingTransactionsAndMarkAsExpired() {
        when(transactionRepository.findByTransactionStatusAndCreatedAtBefore(eq(TransactionStatus.PENDING), any()))
                .thenReturn(List.of(pendingTransaction));

        when(timeUtil.getCurrentZonedDateTime()).thenReturn(java.time.ZonedDateTime.now());

        transactionMonitorService.processTransactions();

        assertEquals(TransactionStatus.EXPIRED, pendingTransaction.getTransactionStatus());
        verify(transactionRepository).saveAndFlush(pendingTransaction);

        verify(kafkaTemplate).send(eq("transakcje-przeterminowane"), kafkaMessageCaptor.capture());

        String expectedMessage = createTransactionMessage(pendingTransaction);
        assertEquals(expectedMessage, kafkaMessageCaptor.getValue());

        verify(transactionRepository, times(1))
                .findByTransactionStatusAndCreatedAtBefore(eq(TransactionStatus.PENDING), any());
    }

    @Test
    void shouldNotSendKafkaMessageWhenNoCompletedTransactions() {
        when(transactionRepository.findByTransactionStatusAndUpdatedAtAfter(eq(TransactionStatus.COMPLETED), any()))
                .thenReturn(Collections.emptyList());

        transactionMonitorService.processTransactions();

        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void shouldNotProcessExpiredTransactionsWhenNoneArePending() {
        when(transactionRepository.findByTransactionStatusAndCreatedAtBefore(eq(TransactionStatus.PENDING), any()))
                .thenReturn(Collections.emptyList());

        transactionMonitorService.processTransactions();

        verify(transactionRepository, never()).saveAndFlush(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    private String createTransactionMessage(Transaction transaction) {
        return String.format(
                "{\"id\":%d,\"amount\":%.2f,\"currency\":\"%s\",\"status\":\"%s\"}",
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionStatus()
        );
    }
}
