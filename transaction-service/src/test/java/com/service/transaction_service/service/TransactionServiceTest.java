package com.service.transaction_service.service;

import com.example.demo.model.CreateTransactionRequest;
import com.example.demo.model.TransactionDto;
import com.example.demo.model.UpdateTransactionRequest;
import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import com.service.transaction_service.util.BasicValidator;
import com.service.transaction_service.util.TimeUtil;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TimeUtil timeUtil;

    @Mock
    private BasicValidator basicValidator;


    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now(ZoneOffset.UTC);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.50"));
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setCurrency("USD");
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
    }

    @Test
    void shouldReturnTransactionById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        final TransactionDto result = transactionService.getTransaction(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("100.50"));
        assertThat(result.getTransactionStatus().name()).isEqualTo(TransactionStatus.PENDING.name());
        assertThat(result.getCurrency()).isEqualTo("USD");

        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransaction(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction with id 1 doesn't exist");

        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        final List<TransactionDto> result = transactionService.getTransactions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateTransaction() {
        final CreateTransactionRequest request = new CreateTransactionRequest(new BigDecimal("200.00"), "EUR");
        final Transaction newTransaction = new Transaction();
        newTransaction.setId(2L);
        newTransaction.setAmount(request.getAmount());
        newTransaction.setCurrency(request.getCurrency());
        newTransaction.setCreatedAt(now);

        when(timeUtil.getCurrentZonedDateTime()).thenReturn(ZonedDateTime.now());
        when(transactionRepository.saveAndFlush(any())).thenReturn(newTransaction);

        transactionService.createTransaction(request);

        assertThat(transactionRepository.findById(2L)).isNotNull();
        verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class));
    }

    @Test
    void shouldDeleteTransaction() {

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldUpdateTransaction() {
        final UpdateTransactionRequest request = new UpdateTransactionRequest(new BigDecimal("500.00"), "GBP", com.example.demo.model.TransactionStatus.COMPLETED);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.saveAndFlush(any(Transaction.class))).thenReturn(transaction);
        when(timeUtil.getCurrentZonedDateTime()).thenReturn(ZonedDateTime.now());

        final TransactionDto result = transactionService.updateTransaction(1L, request);

        assertThat(result.getAmount()).isEqualTo(new BigDecimal("500.00"));
        assertThat(result.getTransactionStatus().name()).isEqualTo(TransactionStatus.COMPLETED.name());
        assertThat(result.getCurrency()).isEqualTo("GBP");

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTransaction() {
        transaction.setTransactionStatus(TransactionStatus.EXPIRED);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        final UpdateTransactionRequest request = new UpdateTransactionRequest(new BigDecimal("500.00"), "GBP", com.example.demo.model.TransactionStatus.COMPLETED);

        assertThatThrownBy(() -> transactionService.updateTransaction(1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction with id 1 is expired or doesn't exist");

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, never()).saveAndFlush(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingExpiredTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        final UpdateTransactionRequest request = new UpdateTransactionRequest(new BigDecimal("500.00"), "GBP", com.example.demo.model.TransactionStatus.EXPIRED);

        assertThatThrownBy(() -> transactionService.updateTransaction(1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction with id 1 is expired or doesn't exist");

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, never()).saveAndFlush(any(Transaction.class));
    }
}
