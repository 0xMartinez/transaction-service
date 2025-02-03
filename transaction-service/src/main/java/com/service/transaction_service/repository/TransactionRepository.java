package com.service.transaction_service.repository;

import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionStatusAndUpdatedAtAfter(TransactionStatus status, LocalDateTime time);
    List<Transaction> findByTransactionStatusAndCreatedAtBefore(TransactionStatus status, LocalDateTime time);
}