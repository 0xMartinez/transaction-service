package com.service.transaction_service.service;

import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    void shouldCreatePaymentTransactionWithStatusPending() {

        final Transaction newTransaction = new Transaction();
        newTransaction.setTransactionStatus(TransactionStatus.PENDING);

        when(transactionService.createTransaction(any(), any(), eq(TransactionStatus.PENDING))).thenReturn(newTransaction);
        final Transaction transaction = paymentService.createPayment(BigDecimal.valueOf(1), "PLN");

        assertThat(transaction.getTransactionStatus()).isEqualTo(TransactionStatus.PENDING);
    }
}
