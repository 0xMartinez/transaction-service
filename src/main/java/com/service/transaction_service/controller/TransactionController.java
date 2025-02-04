package com.service.transaction_service.controller;

import com.transaction.api.TransactionsApi;
import com.transaction.model.CreateTransactionRequest;
import com.transaction.model.TransactionDto;
import com.transaction.model.UpdateTransactionRequest;
import com.service.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TransactionController implements TransactionsApi {

    private final TransactionService transactionService;


    @Override
    public ResponseEntity<Long> createTransaction(@RequestBody final CreateTransactionRequest request) {
        final Long transactionId = transactionService.createTransaction(request);
        return ResponseEntity.ok(transactionId);
    }

    @Override
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        return ResponseEntity.ok(transactionService.getTransactions());
    }

    @Override
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @Override
    public ResponseEntity<Void> updateTransaction(@PathVariable("id") final Long id, @RequestBody final UpdateTransactionRequest request) {
        ResponseEntity.ok(transactionService.updateTransaction(id, request));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") final Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
