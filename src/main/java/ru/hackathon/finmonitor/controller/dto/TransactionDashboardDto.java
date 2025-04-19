package ru.hackathon.finmonitor.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class TransactionDashboardDto {

    // Общее количество транзакций
    private Long totalCount;

    // Кол-во дебетовых транзакций
    private Long debitTransactionStats;

    // Кол-во кредитовых транзакций
    private Long creditTransactionStats;

    // Общая сумма поступлений (кредит)
    private BigDecimal totalIncome;

    // Общая сумма расходов (дебет)
    private BigDecimal totalOutcome;

    // Количество завершённых транзакций
    private Long completedTransactionsCount;

    // Количество отменённых транзакций
    private Long cancelledTransactionsCount;

    // Статистика по банкам отправителей
    private Map<String, Long> senderBankStats;

    // Статистика по банкам получателей
    private Map<String, Long> receiverBankStats;

    // Статистика по категориям (общая, может быть как доходы, так и расходы)
    private Map<String, BigDecimal> categoryStats;

    // Статистика по периоду (неделя/месяц/год)
    private Map<String, Long> timeSeriesStats;
}
