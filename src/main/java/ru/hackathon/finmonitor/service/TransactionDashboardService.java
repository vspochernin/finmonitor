package ru.hackathon.finmonitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hackathon.finmonitor.controller.dto.Dashboard;
import ru.hackathon.finmonitor.model.Transaction;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDashboardService {


    public Dashboard.OperationDynamics getOperationDynamics(List<Transaction> transactions) {
        return new Dashboard.OperationDynamics();
    }

    public Dashboard.OperationTypeDynamics getOperationsTypeDynamics(List<Transaction> transactions) {
        return new Dashboard.OperationTypeDynamics();
    }

    public Dashboard.IncomeExpensesComparison getIncomeExpensesComparison(List<Transaction> transactions) {
        return new Dashboard.IncomeExpensesComparison();
    }


    public Dashboard.OperationCount getOperationCount(List<Transaction> transactions) {
        return new Dashboard.OperationCount();
    }

    public Dashboard.BankStatistics getBankStatistics(List<Transaction> transactions) {
        return new Dashboard.BankStatistics();
    }

    public Dashboard.CategoryStatistics getCategoryStatistics(List<Transaction> transactions) {
        return new Dashboard.CategoryStatistics();
    }
}
