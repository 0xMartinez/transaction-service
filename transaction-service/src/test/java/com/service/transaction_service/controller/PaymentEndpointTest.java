package com.service.transaction_service.controller;

import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import javax.xml.transform.Source;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentEndpointTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockitoBean
    private TransactionRepository transactionRepository;

    private MockWebServiceClient mockClient;


    @BeforeEach
    void setUp() {
        mockClient = MockWebServiceClient.createClient(applicationContext);

    }

    @Test
    void shouldCreatePaymentSuccessfully() throws IOException {

        final Transaction transaction = new Transaction();
        transaction.setId(12345L);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setAmount(BigDecimal.valueOf(100.50));
        transaction.setCurrency("USD");

        when(transactionRepository.saveAndFlush(any())).thenReturn(transaction);

        Source requestPayload = new StringSource(
                "<CreatePaymentRequest xmlns='http://example.com/payment'>" +
                        "elementFormDefault=\"qualified\"" +
                        "<amount>100.50</amount>" +
                        "<currency>USD</currency>" +
                        "</CreatePaymentRequest>"
        );

        mockClient
                .sendRequest(withPayload(requestPayload))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("payment.xsd")))
                .andExpect(xpath("//*[local-name()='CreatePaymentResponse']/*[local-name()='status']")
                        .evaluatesTo("PENDING"))
                .andExpect(xpath("//*[local-name()='CreatePaymentResponse']/*[local-name()='transactionId']")
                        .exists());
    }

    @Test
    void shouldCorrectPaymentSuccessfully() throws IOException {

        final Transaction transaction = new Transaction();
        transaction.setId(12345L);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setCurrency("PLN");

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        Source requestPayload = new StringSource(
                "<CorrectPaymentRequest xmlns='http://example.com/payment'>" +
                        "<transactionId>12345</transactionId>" +
                        "<correctionAmount>10</correctionAmount>" +
                        "</CorrectPaymentRequest>"
        );

        mockClient
                .sendRequest(withPayload(requestPayload))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("payment.xsd")))
                .andExpect(xpath("//*[local-name()='CorrectPaymentResponse']/*[local-name()='transactionStatus']")
                        .exists())
                .andExpect(xpath("//*[local-name()='CorrectPaymentResponse']/*[local-name()='correctionAmount']")
                        .evaluatesTo("10"));
    }
}
