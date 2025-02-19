package com.service.transaction_service.service;

import com.service.transaction_service.config.KafkaConfig;
import com.service.transaction_service.repository.TransactionRepository;
import com.service.transaction_service.repository.model.Transaction;
import com.service.transaction_service.repository.model.TransactionStatus;
import com.service.transaction_service.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionMonitorService {

    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TimeUtil timeUtil;
    private final KafkaConfig kafkaConfig;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void processTransactions() {
        log.info("Rozpoczęcie przetwarzania transakcji...");
        handleCompletedTransactions();
        handlePendingTransactions();
    }

    private void handleCompletedTransactions() {
        final LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
        final List<Transaction> completedTransactions = transactionRepository.findByTransactionStatusAndUpdatedAtAfter(
                TransactionStatus.COMPLETED, thirtySecondsAgo);

        for (Transaction transaction : completedTransactions) {
            String message = createTransactionMessage(transaction);
            kafkaTemplate.send(kafkaConfig.completedTransactionsTopic().name(), message);
            log.info("Wysłano do Kafka ({}): {}", kafkaConfig.completedTransactionsTopic().name(), message);
        }
    }

    private void handlePendingTransactions() {
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
        List<Transaction> pendingTransactions = transactionRepository.findByTransactionStatusAndCreatedAtBefore(
                TransactionStatus.PENDING, thirtySecondsAgo);

        for (Transaction transaction : pendingTransactions) {
            transaction.setTransactionStatus(TransactionStatus.EXPIRED);
            transaction.setUpdatedAt(timeUtil.getCurrentZonedDateTime().toLocalDateTime());
            transactionRepository.saveAndFlush(transaction);

            String message = createTransactionMessage(transaction);
            kafkaTemplate.send(kafkaConfig.expiredTransactionsTopic().name(), message);
            log.info("Wysłano do Kafka ({}): {}", kafkaConfig.expiredTransactionsTopic().name(), message);
        }
    }

    private String createTransactionMessage(Transaction transaction) {
        return String.format(
                "{\"id\":%d,\"amount\":%.2f,\"currency\":\"%s\",\"status\":\"%s\"}",
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionStatus()
        );
    }
    

}

