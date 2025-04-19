package ru.hackathon.finmonitor.controller.dto;

import lombok.Data;
import ru.hackathon.finmonitor.model.Bank;
import ru.hackathon.finmonitor.model.Category;
import ru.hackathon.finmonitor.model.Period;

import java.util.HashMap;
import java.util.Map;

public class Dashboard {

    // Динамика по количеству операций в разрезе неделя/месяц/квартал/год.
    @Data
    public static class OperationDynamics {
        Map<Period, Long> period = new HashMap<>();
    }

    // Динамика по типу операции (отдельно по доходам, отдельно по расходам).
    @Data
    public static class OperationTypeDynamics {
        Map<Period, Long> periodDebit;
        Map<Period, Long> periodCredit;
    }

    // Сравнение количества поступивших средств и потраченных.
    @Data
    public static class IncomeExpensesComparison {
        Long debit;
        Long credit;
    }

    // Количество проведенных операций и отмененных операций.
    @Data
    public static class OperationCount {
        Long completedTransactions;
        Long canceledTransactions;
    }

    // Статистика по банкам отправителя и банкам получателей.
    @Data
    public static class BankStatistics {
        Map<Bank, Long> bankSender ;
        Map<Bank, Long> bankReceiver ;

    }

    // Статистический отчет по категориям расходов и категориям доходов.
    @Data
    public static class CategoryStatistics {
        Map<Category, Long> debitCategory;
        Map<Category, Long> creditCategory;
    }
}
