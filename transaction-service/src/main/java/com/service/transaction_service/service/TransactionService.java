package com.service.transaction_service.service;

import com.example.demo.model.CreateTransactionRequest;
import com.example.demo.model.TransactionDto;
import com.example.demo.model.UpdateTransactionRequest;
import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import com.service.transaction_service.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TimeUtil timeUtil;

    public TransactionDto getTransaction(final Long transactionId) {
        final Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction with id %d doesn't exist", transactionId)));
        return mapTransaction(transaction);
    }

    public List<TransactionDto> getTransactions() {
        final List<Transaction> transaction = transactionRepository.findAll();
        return transaction.stream()
                .map(this::mapTransaction)
                .collect(Collectors.toList());
    }

    public Long createTransaction(final CreateTransactionRequest request) {
        return createTransaction(request.getAmount(), request.getCurrency(), null).getId();
    }

    public void deleteTransaction(final Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public Transaction createTransaction(final BigDecimal amount, final String currency, final TransactionStatus transactionStatus) {

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater then 0");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency symbol must be 3 letter string format");
        }
        final Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setCurrency(currency);
        transaction.setCreatedAt(timeUtil.getCurrentZonedDateTime().toLocalDateTime());

        transactionRepository.save(transaction);

        return transaction;
    }

    public TransactionDto updateTransaction(final Long transactionId, final UpdateTransactionRequest request) {

        final Transaction transaction = updateTransaction(request.getAmount(), TransactionStatus.valueOf(request.getTransactionStatus().name()), request.getCurrency(), transactionId);
        return mapTransaction(transaction);
    }

    public Transaction updateTransaction(final BigDecimal amount, final TransactionStatus transactionStatus, final String currency,
                                         final Long transactionId) {

        final Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transaction with id %d doesn't exist", transactionId)));

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

        transactionRepository.save(transaction);

        return transaction;
    }

    private TransactionDto mapTransaction(final Transaction transaction) {

        final TransactionDto transactionDto = new TransactionDto();

        transactionDto.setId(transaction.getId());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionStatus(com.example.demo.model.TransactionStatus.valueOf(transaction.getTransactionStatus().name()));
        transactionDto.setCurrency(transaction.getCurrency());
        transactionDto.setCreatedAt(transaction.getCreatedAt().atOffset(ZoneOffset.UTC));
        transactionDto.setUpdatedAt(transaction.getUpdatedAt().atOffset(ZoneOffset.UTC));

        return transactionDto;
    }
}
