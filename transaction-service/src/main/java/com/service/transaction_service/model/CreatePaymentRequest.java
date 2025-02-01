package com.service.transaction_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class CreatePaymentRequest {

    private BigDecimal amount;
    private String currency;
}
