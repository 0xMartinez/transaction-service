package com.service.transaction_service.controller;

import com.service.transaction_service.model.CorrectPaymentRequest;
import com.service.transaction_service.model.CorrectPaymentResponse;
import com.service.transaction_service.model.CreatePaymentRequest;
import com.service.transaction_service.model.CreatePaymentResponse;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.service.PaymentService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.namespace.QName;

@Endpoint
public class PaymentEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/payment";

    private final PaymentService paymentService;

    public PaymentEndpoint(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreatePaymentRequest")
    @ResponsePayload
    public CreatePaymentResponse createPayment(@RequestPayload CreatePaymentRequest request) {
        final Transaction transaction = paymentService.createPayment(request.getAmount(), request.getCurrency());
        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setTransactionId(transaction.getId());
        response.setTransactionStatus(transaction.getTransactionStatus());

        return response;

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CorrectPaymentRequest")
    @ResponsePayload
    public CorrectPaymentResponse correctPayment(@RequestPayload CorrectPaymentRequest request) {
        final Transaction transaction = paymentService.correctPayment(request.getTransactionId(), request.getCorrectionAmount());
        final CorrectPaymentResponse response = new CorrectPaymentResponse();
        response.setTransactionStatus(transaction.getTransactionStatus());
        response.setCorrectionAmount(transaction.getAmount());

        return response;
    }
}