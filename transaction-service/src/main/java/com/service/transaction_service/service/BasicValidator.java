package com.service.transaction_service.service;

import com.service.transaction_service.repository.model.TransactionStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class BasicValidator {

    public final static String AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE = "Amount must be greater then 0";
    public final static String INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE = "Currency symbol must be 3 letter string format";
    public final static String INCORRECT_TRANSACTION_STATUS_VALIDATION_MESSAGE = "Invalid transaction status";

    public final static Set<TransactionStatus> allowedStatuses =
            Set.of(TransactionStatus.PENDING,
                    TransactionStatus.COMPLETED,
                    TransactionStatus.EXPIRED);

    public void runBasicFieldsValidation(final BigDecimal amount, final String currency, final TransactionStatus transactionStatus) {

        if (amount != null && amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE);
        }
        if (currency != null && currency.length() != 3) {
            throw new IllegalArgumentException(INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE);
        }
    }
}
