package com.service.transaction_service.model;

import com.service.transaction_service.repository.model.TransactionStatus;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@XmlRootElement(name = "CreatePaymentResponse", namespace = "http://example.com/payment")
public class CreatePaymentResponse {

    private TransactionStatus transactionStatus;
    private BigDecimal amount;
}
