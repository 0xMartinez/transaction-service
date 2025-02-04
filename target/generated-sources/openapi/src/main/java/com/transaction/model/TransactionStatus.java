package com.transaction.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets TransactionStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-04T08:33:28.256333+01:00[Europe/Warsaw]", comments = "Generator version: 7.10.0")
public enum TransactionStatus {
  
  PENDING("PENDING"),
  
  COMPLETED("COMPLETED"),
  
  EXPIRED("EXPIRED");

  private String value;

  TransactionStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TransactionStatus fromValue(String value) {
    for (TransactionStatus b : TransactionStatus.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

