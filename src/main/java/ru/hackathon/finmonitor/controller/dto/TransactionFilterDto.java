package ru.hackathon.finmonitor.controller.dto;

import lombok.Data;
import ru.hackathon.finmonitor.model.TransactionStatus;
import ru.hackathon.finmonitor.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionFilterDto {

    private Long senderBankId;
    private Long receiverBankId;

    private LocalDate startDate;
    private LocalDate endDate;

    private TransactionStatus status;

    private String receiverInn;

    private BigDecimal amountFrom;
    private BigDecimal amountTo;

    private TransactionType transactionType;

    private Long categoryId;
}
