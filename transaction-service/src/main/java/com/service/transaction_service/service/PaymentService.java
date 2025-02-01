package com.service.transaction_service.service;

import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionService transactionService;

    public Transaction createPayment(final BigDecimal amount, final String currency) {
        return transactionService.createTransaction(amount, currency, TransactionStatus.PENDING);
    }

    public Transaction correctPayment(final Long transactionId, final BigDecimal correctionAmount) {
        return transactionService.updateTransaction(correctionAmount, null, null, transactionId);
    }

}
