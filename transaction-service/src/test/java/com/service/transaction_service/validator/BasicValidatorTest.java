package com.service.transaction_service.validator;

import com.example.demo.model.UpdateTransactionRequest;
import com.service.transaction_service.util.BasicValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BasicValidatorTest {

    @InjectMocks
    private BasicValidator basicValidator;

    @BeforeEach
    void setUp() {
        basicValidator = new BasicValidator();
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNullForTransactionCreation() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runBasicFieldsValidationForTransactionCreation(null, "USD"));
        assertEquals(BasicValidator.AMOUNT_NOT_NULL_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsNullForTransactionCreation() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runBasicFieldsValidationForTransactionCreation(BigDecimal.TEN, null));
        assertEquals(BasicValidator.VALUE_NOT_NULL_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCorrectionAmountIsNull() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runBasicFieldsValidationForTransactionCorrection(null));
        assertEquals(BasicValidator.CORRECTION_AMOUNT_NOT_NULL_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCorrectionAmountIsZero() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runBasicFieldsValidationForTransactionCorrection(BigDecimal.ZERO));
        assertEquals(BasicValidator.CORRECTION_AMOUNT_NOT_NULL_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateTransactionRequestIsEmpty() {
        final UpdateTransactionRequest request = new UpdateTransactionRequest();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runUpdateTransactionValidation(request));
        assertEquals(BasicValidator.EMPTY_REQUEST_BODY_NOT_ALLOWED_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runBasicFieldsValidation(new BigDecimal("-5"), "USD", null));
        assertEquals(BasicValidator.AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basicValidator.runBasicFieldsValidation(BigDecimal.TEN, "US", null));
        assertEquals(BasicValidator.INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE, exception.getMessage());
    }
}