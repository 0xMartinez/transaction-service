package com.service.transaction_service.validator;

import com.service.transaction_service.repository.model.TransactionStatus;
import com.service.transaction_service.util.BasicValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.service.transaction_service.util.BasicValidator.AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE;
import static com.service.transaction_service.util.BasicValidator.INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class BasicValidationTest {

    @InjectMocks
    private BasicValidator basicValidator;

    @Test
    void shouldThrowExceptionWhenValidatingNegativeAmount() {
        assertThatThrownBy(() -> basicValidator.runBasicFieldsValidation(new BigDecimal("-100.00"), "USD", TransactionStatus.PENDING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(AMOUNT_EQUAL_OR_LOWER_0_VALIDATION_MESSAGE);
    }
    @Test
    void shouldThrowExceptionWhenValidatingInvalidCurrency() {
        assertThatThrownBy(() -> basicValidator.runBasicFieldsValidation(null, "USUSddsasd", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INCORRECT_CURRENCY_SYMBOL_VALIDATION_MESSAGE);
    }
}
