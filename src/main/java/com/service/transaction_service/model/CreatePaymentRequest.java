package com.service.transaction_service.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@XmlRootElement(name = "CreatePaymentRequest", namespace = "http://example.com/payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreatePaymentRequest {

    @XmlElement(name = "amount", namespace = "http://example.com/payment")
    private BigDecimal amount;
    @XmlElement(name = "currency", namespace = "http://example.com/payment")
    private String currency;
}
