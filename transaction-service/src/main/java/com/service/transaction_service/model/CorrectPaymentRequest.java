package com.service.transaction_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@XmlRootElement(name = "CorrectPaymentRequest", namespace = "http://example.com/payment")
public class CorrectPaymentRequest {

    private Long id;
    private BigDecimal correctionAmount;
}
