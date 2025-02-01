package com.service.transaction_service.model;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final String error;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}