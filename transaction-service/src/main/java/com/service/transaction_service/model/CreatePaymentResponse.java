package com.service.transaction_service.model;

import com.service.transaction_service.repository.model.TransactionStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name = "CreatePaymentResponse", namespace = "http://example.com/payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreatePaymentResponse {

    @XmlElement(name = "transactionId", required = true, namespace = "http://example.com/payment")
    private Long transactionId;
    @XmlElement(name = "status", required = true, namespace = "http://example.com/payment")
    private TransactionStatus transactionStatus;
}
