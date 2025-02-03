package com.service.transaction_service.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@XmlRootElement(name = "CorrectPaymentRequest", namespace = "http://example.com/payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class CorrectPaymentRequest {

    @XmlElement(name = "transactionId", required = true, namespace = "http://example.com/payment")
    private Long transactionId;
    @XmlElement(name = "correctionAmount", required = true, namespace = "http://example.com/payment")
    private BigDecimal correctionAmount;
}
