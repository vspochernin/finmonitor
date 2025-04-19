package ru.hackathon.finmonitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hackathon.finmonitor.controller.dto.Dashboard;
import ru.hackathon.finmonitor.model.Period;
import ru.hackathon.finmonitor.model.Transaction;
import ru.hackathon.finmonitor.model.TransactionStatus;
import ru.hackathon.finmonitor.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionDashboardService {


    public Dashboard.OperationDynamics getOperationDynamics(List<Transaction> transactions, Period period) {
        Dashboard.OperationDynamics operationDynamics = new Dashboard.OperationDynamics();
        switch (period) {
            case WEEK -> {
                operationDynamics.setPeriod(getTransactionCountByWeek(transactions));
            }
            case MONTH -> {
                operationDynamics.setPeriod(getTransactionCountByMonth(transactions));
            }
            case QUARTER -> {
                operationDynamics.setPeriod(getTransactionCountByQuarter(transactions));
            }
            case YEAR -> {
                operationDynamics.setPeriod(getTransactionCountByYear(transactions));
            }
        }
        return null;
    }

    public Dashboard.OperationTypeDynamics getOperationsTypeDynamics(List<Transaction> transactions, Period period) {
        Dashboard.OperationTypeDynamics operationTypeDynamics = new Dashboard.OperationTypeDynamics();
        switch (period) {
            case WEEK -> {
                operationTypeDynamics.setPeriodCredit(getTransactionCountByWeek(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList()));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByWeek(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList()));
            }
            case MONTH -> {
                operationTypeDynamics.setPeriodCredit(getTransactionCountByMonth(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList()));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByMonth(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList()));
            }
            case QUARTER -> {
                operationTypeDynamics.setPeriodCredit(getTransactionCountByQuarter(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList()));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByQuarter(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList()));
            }
            case YEAR -> {
                operationTypeDynamics.setPeriodCredit(getTransactionCountByYear(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList()));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByYear(transactions
                        .stream().filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList()));
            }
        }
        return operationTypeDynamics;
    }

    public Dashboard.IncomeExpensesComparison getIncomeExpensesComparison(List<Transaction> transactions) {
        Dashboard.IncomeExpensesComparison incomeExpensesComparison = new Dashboard.IncomeExpensesComparison();
        incomeExpensesComparison.setDebit(
                transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.DEBIT))
                        .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        incomeExpensesComparison.setCredit(
                transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.CREDIT))
                        .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        return incomeExpensesComparison;
    }


    public Dashboard.OperationCount getOperationCount(List<Transaction> transactions) {
        Dashboard.OperationCount operationCount = new Dashboard.OperationCount();
        operationCount.setCompletedTransactions(transactions.stream()
                .filter(x -> x.getStatus().equals(TransactionStatus.COMPLETED)).count());
        operationCount.setCanceledTransactions(transactions.stream()
                .filter(x -> x.getStatus().equals(TransactionStatus.CANCELED)).count());
        return operationCount;
    }

    public Dashboard.BankStatistics getBankStatistics(List<Transaction> transactions) {
        Dashboard.BankStatistics bankStatistics = new Dashboard.BankStatistics();
        bankStatistics.setBankReceiver(
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                Transaction::getReceiverBank,
                                Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                        ))
        );
        bankStatistics.setBankSender(
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                Transaction::getSenderBank,
                                Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                        ))
        );
        return bankStatistics;
    }

    public Dashboard.CategoryStatistics getCategoryStatistics(List<Transaction> transactions) {
        Dashboard.CategoryStatistics categoryStatistics = new Dashboard.CategoryStatistics();
        categoryStatistics.setDebitCategory(
                transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.DEBIT))
                        .collect(Collectors.groupingBy(
                                Transaction::getCategory,
                                Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                        ))
        );
        categoryStatistics.setCreditCategory(
                transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.CREDIT))
                        .collect(Collectors.groupingBy(
                                Transaction::getCategory,
                                Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                        ))
        );
        return categoryStatistics;
    }


    public SortedMap<String, Long> getTransactionCountByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> formatMonth(transaction.getOperationDateTime().toLocalDate()),
                        Collectors.counting()
                )).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка по ключу
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // В случае коллизии, берём первое значение
                        TreeMap::new // Используем TreeMap для сортировки
                ));
    }

    public SortedMap<String, Long> getTransactionCountByQuarter(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> String.valueOf(getQuarter(transaction.getOperationDateTime().toLocalDate())),
                        Collectors.counting()
                )).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
    }

    public SortedMap<String, Long> getTransactionCountByWeek(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> getWeekOfYear(transaction.getOperationDateTime().toLocalDate()),
                        Collectors.counting()
                )).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
    }

    public SortedMap<String, Long> getTransactionCountByYear(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> String.valueOf(transaction.getOperationDateTime().toLocalDate().getYear()),
                        Collectors.counting()
                )).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
    }

    private String formatMonth(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY.M");

        return date.format(formatter);
    }

    private int getQuarter(LocalDate date) {
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    private String getWeekOfYear(LocalDate date) {
        return date.getYear() + "." + date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }
}
