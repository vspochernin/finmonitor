package ru.hackathon.finmonitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hackathon.finmonitor.controller.dto.TransactionFilterDto;
import ru.hackathon.finmonitor.exception.FinmonitorErrorType;
import ru.hackathon.finmonitor.exception.FinmonitorException;
import ru.hackathon.finmonitor.model.Transaction;
import ru.hackathon.finmonitor.model.TransactionStatus;
import ru.hackathon.finmonitor.repository.TransactionRepository;
import ru.hackathon.finmonitor.repository.TransactionSpecification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final Set<TransactionStatus> FORBIDDEN_DELETION_STATUSES = Set.of(
            TransactionStatus.CONFIRMED,
            TransactionStatus.IN_PROCESS,
            TransactionStatus.CANCELED,
            TransactionStatus.COMPLETED,
            TransactionStatus.RETURNED);

    private static final Set<TransactionStatus> FORBIDDEN_UPDATE_STATUSES = Set.of(
            TransactionStatus.CONFIRMED,
            TransactionStatus.IN_PROCESS,
            TransactionStatus.CANCELED,
            TransactionStatus.COMPLETED,
            TransactionStatus.DELETED,
            TransactionStatus.RETURNED);

    private final TransactionRepository repository;

    @Transactional(readOnly = true)
    public List<Transaction> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Transaction create(Transaction tx) {
        tx.setId(null);
        return repository.save(tx);
    }

    @Transactional
    public Transaction update(Long id, Transaction changed) {
        Transaction current = repository.findById(id)
                .orElseThrow(() -> new FinmonitorException(
                        FinmonitorErrorType.NOT_FOUND,
                        "Транзакция со следующим id не найдена: " + id));

        if (FORBIDDEN_UPDATE_STATUSES.contains(current.getStatus())) {
            throw new FinmonitorException(
                    FinmonitorErrorType.TRANSACTION_UPDATE_FORBIDDEN,
                    "Редактирование транзакции с текущим статусом " + current.getStatus() + " не допускается.");
        }

        current.setPersonType(changed.getPersonType());
        current.setOperationDateTime(changed.getOperationDateTime());
        current.setComment(changed.getComment());
        current.setAmount(changed.getAmount());
        current.setStatus(changed.getStatus());
        current.setSenderBank(changed.getSenderBank());
        current.setReceiverBank(changed.getReceiverBank());
        current.setReceiverInn(changed.getReceiverInn());
        current.setCategory(changed.getCategory());
        current.setReceiverPhone(changed.getReceiverPhone());

        return repository.save(current);
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new FinmonitorException(
                        FinmonitorErrorType.NOT_FOUND,
                        "Транзакция со следующим id не найдена: " + id));

        if (FORBIDDEN_DELETION_STATUSES.contains(transaction.getStatus())) {
            throw new FinmonitorException(
                    FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN,
                    "Статус транзакции: " + transaction.getStatus());
        }

        transaction.setStatus(TransactionStatus.DELETED);
        repository.save(transaction);
    }

    public List<Transaction> filterTransactions(TransactionFilterDto filterDto) {
        var specification = TransactionSpecification.withFilters(filterDto);
        return repository.findAll(specification).stream().toList();
    }
}