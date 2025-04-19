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

@Service
@RequiredArgsConstructor
public class TransactionService {

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
        // копируем вручную или через BeanUtils / MapStruct
        current.setPersonType(changed.getPersonType());
        current.setOperationDateTime(changed.getOperationDateTime());
        current.setTransactionType(changed.getTransactionType());
        current.setComment(changed.getComment());
        current.setAmount(changed.getAmount());
        current.setStatus(changed.getStatus());
        current.setSenderBank(changed.getSenderBank());
        current.setSenderAccount(changed.getSenderAccount());
        current.setReceiverBank(changed.getReceiverBank());
        current.setReceiverAccount(changed.getReceiverAccount());
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

        if (isDeletionForbidden(transaction.getStatus())) {
            throw new FinmonitorException(
                    FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN,
                    String.format("Удаление транзакции со статусом %s запрещено", transaction.getStatus())
            );
        }

        transaction.setStatus(TransactionStatus.DELETED);
        repository.save(transaction);
    }

    private boolean isDeletionForbidden(TransactionStatus status) {
        return status == TransactionStatus.CONFIRMED ||
                status == TransactionStatus.IN_PROCESS ||
                status == TransactionStatus.CANCELED ||
                status == TransactionStatus.COMPLETED ||
                status == TransactionStatus.RETURNED;
    }

    public List<Transaction> filterTransactions(TransactionFilterDto filterDto) {
        var specification = TransactionSpecification.withFilters(filterDto);
        return repository.findAll(specification).stream().toList();
    }
}