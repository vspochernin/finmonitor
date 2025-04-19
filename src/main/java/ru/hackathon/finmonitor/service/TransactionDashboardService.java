package ru.hackathon.finmonitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hackathon.finmonitor.controller.dto.Dashboard;
import ru.hackathon.finmonitor.model.Transaction;
import ru.hackathon.finmonitor.model.TransactionStatus;
import ru.hackathon.finmonitor.model.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionDashboardService {


    public Dashboard.OperationDynamics getOperationDynamics(List<Transaction> transactions) {
        Dashboard.OperationDynamics operationDynamics = new Dashboard.OperationDynamics();

        return new Dashboard.OperationDynamics();
    }

    public Dashboard.OperationTypeDynamics getOperationsTypeDynamics(List<Transaction> transactions) {
        Dashboard.OperationTypeDynamics operationTypeDynamics = new Dashboard.OperationTypeDynamics();
        return new Dashboard.OperationTypeDynamics();
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
}
