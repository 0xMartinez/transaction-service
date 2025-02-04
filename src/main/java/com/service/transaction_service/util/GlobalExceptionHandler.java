package com.service.transaction_service.util;
import com.transaction.model.ErrorResponse;
import com.transaction.model.NotFoundErrorResponse;
import com.transaction.model.BadRequestErrorResponse;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BadRequestErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        BadRequestErrorResponse errorResponse = new BadRequestErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "Bad Request",
                LocalDateTime.now().atOffset(ZoneOffset.UTC)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NotFoundErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        NotFoundErrorResponse errorResponse = new NotFoundErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "Not Found",
                LocalDateTime.now().atOffset(ZoneOffset.UTC)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Wystąpił nieoczekiwany błąd",
                "Internal Server Error",
                LocalDateTime.now().atOffset(ZoneOffset.UTC)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}