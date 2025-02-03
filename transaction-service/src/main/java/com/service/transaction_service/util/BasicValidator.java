package com.service.transaction_service.util;

import com.example.demo.model.UpdateTransactionRequest;
import com.service.transaction_service.repository.model.TransactionStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class BasicValidator {

    public final static String AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE = "Amount must be greater then 0";
    public final static String INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE = "Currency symbol must be 3 letter string format";
    public final static String AMOUNT_NOT_NULL_MESSAGE = "Amount cannot be null";
    public final static String VALUE_NOT_NULL_MESSAGE = "Value cannot be null";
    public final static String EMPTY_REQUEST_BODY_NOT_ALLOWED_MESSAGE = "Empty request body not ";
    public final static String CORRECTION_AMOUNT_NOT_NULL_MESSAGE = "Correction amount cannot be null";


    public void runBasicFieldsValidationForTransactionCreation(final BigDecimal amount, final String currency) {

        if (amount == null) {
            throw new IllegalArgumentException(AMOUNT_NOT_NULL_MESSAGE);
        }
        if (currency == null) {
            throw new IllegalArgumentException(VALUE_NOT_NULL_MESSAGE);
        }
    }

    public void runBasicFieldsValidationForTransactionCorrection(final BigDecimal correctionAmount) {

        if (correctionAmount == null || correctionAmount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException(CORRECTION_AMOUNT_NOT_NULL_MESSAGE);
        }
    }

    public void runUpdateTransactionValidation(final UpdateTransactionRequest request) {

        if (request != null && request.getAmount() == null
                && request.getCurrency() == null
                && request.getTransactionStatus() == null) {
            throw new IllegalArgumentException(EMPTY_REQUEST_BODY_NOT_ALLOWED_MESSAGE);
        }
    }

    public void runBasicFieldsValidation(final BigDecimal amount, final String currency, final TransactionStatus transactionStatus) {

        if (amount != null && amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE);
        }
        if (currency != null && currency.length() != 3) {
            throw new IllegalArgumentException(INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE);
        }
    }
}