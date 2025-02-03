package com.service.transaction_service.service;

import com.example.demo.model.CreateTransactionRequest;
import com.example.demo.model.TransactionDto;
import com.example.demo.model.UpdateTransactionRequest;
import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import com.service.transaction_service.util.BasicValidator;
import com.service.transaction_service.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TimeUtil timeUtil;

    private final BasicValidator basicValidator;

    public TransactionDto getTransaction(final Long transactionId) {
        final Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction with id %d doesn't exist", transactionId)));
        return mapTransaction(transaction);
    }

    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapTransaction)
                .collect(Collectors.toList());
    }

    public Long createTransaction(final CreateTransactionRequest request) {

        basicValidator.runBasicFieldsValidationForTransactionCreation(request.getAmount(), request.getCurrency());

        return createTransaction(request.getAmount(), request.getCurrency(), null).getId();
    }

    public void deleteTransaction(final Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public Transaction createTransaction(final BigDecimal amount, final String currency, final TransactionStatus transactionStatus) {

        basicValidator.runBasicFieldsValidation(amount, currency, transactionStatus);

        final Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setCurrency(currency);
        transaction.setCreatedAt(timeUtil.getCurrentZonedDateTime().toLocalDateTime());
        final Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        return savedTransaction;
    }

    public TransactionDto updateTransaction(final Long transactionId, final UpdateTransactionRequest request) {

        basicValidator.runUpdateTransactionValidation(request);
        final Transaction transaction = updateTransaction(request.getAmount(), TransactionStatus.valueOf(request.getTransactionStatus().name()), request.getCurrency(), transactionId);
        return mapTransaction(transaction);
    }

    public Transaction updateTransaction(final BigDecimal amount, final TransactionStatus transactionStatus, final String currency,
                                         final Long transactionId) {

        final Transaction transaction = transactionRepository.findById(transactionId)
                .filter(t -> TransactionStatus.EXPIRED != t.getTransactionStatus())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction with id %d is expired or doesn't exist", transactionId)));

        basicValidator.runBasicFieldsValidation(amount, currency, transactionStatus);
        if (amount != null) {
            transaction.setAmount(amount);
        }
        if (transactionStatus != null) {
            transaction.setTransactionStatus(transactionStatus);
        }
        if (currency != null) {
            transaction.setCurrency(currency);
        }
        transaction.setUpdatedAt(timeUtil.getCurrentZonedDateTime().toLocalDateTime());

        transactionRepository.saveAndFlush(transaction);

        return transaction;
    }

    private TransactionDto mapTransaction(final Transaction transaction) {

        final TransactionDto transactionDto = new TransactionDto();

        transactionDto.setId(transaction.getId());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionStatus(transaction.getTransactionStatus() != null
                ? com.example.demo.model.TransactionStatus.valueOf(transaction.getTransactionStatus().name())
                : null);
        transactionDto.setCurrency(transaction.getCurrency());
        transactionDto.setCreatedAt(transaction.getCreatedAt().atOffset(ZoneOffset.UTC));
        transactionDto.setUpdatedAt(transaction.getUpdatedAt() != null
                ? transaction.getUpdatedAt().atOffset(ZoneOffset.UTC)
                : null);

        return transactionDto;
    }
}
