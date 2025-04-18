package ru.hackathon.finmonitor.controller.dto;

import org.springframework.stereotype.Component;
import ru.hackathon.finmonitor.model.Account;
import ru.hackathon.finmonitor.model.Bank;
import ru.hackathon.finmonitor.model.Category;
import ru.hackathon.finmonitor.model.Transaction;

@Component
public class TransactionConverter {

    public TransactionDto toDto(Transaction entity) {
        TransactionDto dto = new TransactionDto();
        dto.setId(entity.getId());
        dto.setPersonType(entity.getPersonType());
        dto.setOperationDateTime(entity.getOperationDateTime());
        dto.setTransactionType(entity.getTransactionType());
        dto.setComment(entity.getComment());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setSenderBankId(entity.getSenderBank().getId());
        dto.setSenderAccountId(entity.getSenderAccount().getId());
        dto.setReceiverBankId(entity.getReceiverBank().getId());
        dto.setReceiverAccountId(entity.getReceiverAccount().getId());
        dto.setReceiverInn(entity.getReceiverInn());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setReceiverPhone(entity.getReceiverPhone());
        return dto;
    }

    public Transaction toEntity(TransactionDto dto) {
        Transaction entity = new Transaction();
        entity.setId(dto.getId());
        entity.setPersonType(dto.getPersonType());
        entity.setOperationDateTime(dto.getOperationDateTime());
        entity.setTransactionType(dto.getTransactionType());
        entity.setComment(dto.getComment());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setSenderBank(new Bank(dto.getSenderBankId()));
        entity.setSenderAccount(new Account(dto.getSenderAccountId()));
        entity.setReceiverBank(new Bank(dto.getReceiverBankId()));
        entity.setReceiverAccount(new Account(dto.getReceiverAccountId()));
        entity.setReceiverInn(dto.getReceiverInn());
        entity.setCategory(new Category(dto.getCategoryId()));
        entity.setReceiverPhone(dto.getReceiverPhone());
        return entity;
    }

    /**
     * patch‑update существующей Entity данными из DTO
     */
    public void copyToEntity(TransactionDto src, Transaction target) {
        target.setPersonType(src.getPersonType());
        target.setOperationDateTime(src.getOperationDateTime());
        target.setTransactionType(src.getTransactionType());
        target.setComment(src.getComment());
        target.setAmount(src.getAmount());
        target.setStatus(src.getStatus());
        target.setSenderBank(new Bank(src.getSenderBankId()));
        target.setSenderAccount(new Account(src.getSenderAccountId()));
        target.setReceiverBank(new Bank(src.getReceiverBankId()));
        target.setReceiverAccount(new Account(src.getReceiverAccountId()));
        target.setReceiverInn(src.getReceiverInn());
        target.setCategory(new Category(src.getCategoryId()));
        target.setReceiverPhone(src.getReceiverPhone());
    }
}

