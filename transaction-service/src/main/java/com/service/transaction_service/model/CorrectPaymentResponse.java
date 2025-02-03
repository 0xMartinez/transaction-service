package com.service.transaction_service.model;

import com.service.transaction_service.repository.model.TransactionStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@XmlRootElement(name = "CorrectPaymentResponse", namespace = "http://example.com/payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class CorrectPaymentResponse {

    @XmlElement(name = "transactionStatus", required = true, namespace = "http://example.com/payment")
    private TransactionStatus transactionStatus;
    @XmlElement(name = "correctionAmount", required = true, namespace = "http://example.com/payment")
    private BigDecimal correctionAmount;

}
