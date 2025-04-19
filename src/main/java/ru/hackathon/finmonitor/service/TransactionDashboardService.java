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
        List<Transaction> newTransaction = new ArrayList<>(transactions);
        newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
        Map<String, Long> map = new LinkedHashMap<>();
        switch (period) {
            case WEEK -> map = getTransactionCountByWeek(newTransaction);
            case MONTH -> map = getTransactionCountByMonth(newTransaction);
            case QUARTER -> map = getTransactionCountByQuarter(newTransaction);
            case YEAR -> map = getTransactionCountByYear(newTransaction);
        }
        operationDynamics.setPeriod(map);
        return operationDynamics;
    }

    public Dashboard.OperationTypeDynamics getOperationsTypeDynamics(List<Transaction> transactions, Period period) {
        Dashboard.OperationTypeDynamics operationTypeDynamics = new Dashboard.OperationTypeDynamics();
        List<Transaction> newTransaction;
        switch (period) {
            case WEEK -> {
                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodCredit(getTransactionCountByWeek(newTransaction));

                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByWeek(newTransaction));
            }
            case MONTH -> {
                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodCredit(getTransactionCountByMonth(newTransaction));

                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByMonth(newTransaction));
            }
            case QUARTER -> {
                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodCredit(getTransactionCountByQuarter(newTransaction));

                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByQuarter(newTransaction));
            }
            case YEAR -> {
                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.CREDIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodCredit(getTransactionCountByYear(newTransaction));

                newTransaction = new ArrayList<>(transactions.stream()
                        .filter(x -> x.getTransactionType().equals(TransactionType.DEBIT)).toList());
                newTransaction.sort(Comparator.comparing(Transaction::getOperationDateTime));
                operationTypeDynamics.setPeriodDebit(getTransactionCountByYear(newTransaction));
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


    public Map<String, Long> getTransactionCountByMonth(List<Transaction> transactions) {
        Map<String, Long> occurrenceMap = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            String s = formatMonth(t.getOperationDateTime().toLocalDate());
            if (occurrenceMap.containsKey(s)) {
                occurrenceMap.put(s, occurrenceMap.get(s) + 1);
            } else {
                occurrenceMap.put(s, 1L);
            }
        }
        return occurrenceMap;
    }

    public Map<String, Long> getTransactionCountByQuarter(List<Transaction> transactions) {
        Map<String, Long> occurrenceMap = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            String s = String.valueOf(getQuarter(t.getOperationDateTime().toLocalDate()));
            if (occurrenceMap.containsKey(s)) {
                occurrenceMap.put(s, occurrenceMap.get(s) + 1);
            } else {
                occurrenceMap.put(s, 1L);
            }
        }
        return occurrenceMap;
    }

    public Map<String, Long> getTransactionCountByWeek(List<Transaction> transactions) {
        Map<String, Long> occurrenceMap = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            String s = getWeekOfYear(t.getOperationDateTime().toLocalDate());
            if (occurrenceMap.containsKey(s)) {
                occurrenceMap.put(s, occurrenceMap.get(s) + 1);
            } else {
                occurrenceMap.put(s, 1L);
            }
        }
        return occurrenceMap;
    }

    public Map<String, Long> getTransactionCountByYear(List<Transaction> transactions) {
        Map<String, Long> occurrenceMap = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            String s = String.valueOf(t.getOperationDateTime().toLocalDate().getYear());
            if (occurrenceMap.containsKey(s)) {
                occurrenceMap.put(s, occurrenceMap.get(s) + 1);
            } else {
                occurrenceMap.put(s, 1L);
            }
        }
        return occurrenceMap;
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
