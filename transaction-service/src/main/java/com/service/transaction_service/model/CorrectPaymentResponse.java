package com.service.transaction_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@XmlRootElement(name = "CorrectPaymentResponse", namespace = "http://example.com/payment")
public class CorrectPaymentResponse {

    private BigDecimal amount;

}
