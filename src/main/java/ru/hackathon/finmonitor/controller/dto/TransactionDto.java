package ru.hackathon.finmonitor.controller.dto;


import lombok.Data;
import ru.hackathon.finmonitor.model.PersonType;
import ru.hackathon.finmonitor.model.TransactionStatus;
import ru.hackathon.finmonitor.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long id;

    private PersonType personType;

    private LocalDateTime operationDateTime;

    private TransactionType transactionType;

    private String comment;

    private BigDecimal amount;

    private TransactionStatus status;

    private Long senderBankId;

    private Long senderAccountId;

    private Long receiverBankId;

    private Long receiverAccountId;

    private String receiverInn;

    private Long categoryId;

    private String receiverPhone;
}
