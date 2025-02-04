package com.service.transaction_service.util;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionLogger {

    private static final Logger logger = LoggerFactory.getLogger(TransactionLogger.class);

    @PrePersist
    public void logInsert(Object entity) {
        logger.info("Dodano nową encję: {}", entity);
    }

    @PreUpdate
    public void logUpdate(Object entity) {
        logger.info("Zaktualizowano encję: {}", entity);
    }

    @PreRemove
    public void logDelete(Object entity) {
        logger.info("Usunięto encję: {}", entity);
    }
}