package com.transaction.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.transaction.model.TransactionStatus;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateTransactionRequest
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-04T08:33:28.256333+01:00[Europe/Warsaw]", comments = "Generator version: 7.10.0")
public class UpdateTransactionRequest {

  private BigDecimal amount;

  private String currency;

  private TransactionStatus transactionStatus;

}

