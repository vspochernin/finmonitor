package ru.hackathon.finmonitor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hackathon.finmonitor.controller.dto.Dashboard;
import ru.hackathon.finmonitor.controller.dto.TransactionConverter;
import ru.hackathon.finmonitor.controller.dto.TransactionDto;
import ru.hackathon.finmonitor.controller.dto.TransactionFilterDto;
import ru.hackathon.finmonitor.model.Transaction;
import ru.hackathon.finmonitor.service.TransactionDashboardService;
import ru.hackathon.finmonitor.service.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final TransactionConverter converter;
    private final TransactionDashboardService transactionDashboardService;

    @GetMapping
    public List<TransactionDto> getAll() {
        return service.getAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getById(@PathVariable Long id) {
        Optional<Transaction> tx = service.getById(id);
        return tx.map(t -> ResponseEntity.ok(converter.toDto(t)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto dto) {
        Transaction saved = service.create(converter.toEntity(dto));
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> update(@PathVariable Long id,
                                                 @RequestBody TransactionDto dto) {
        try {
            Transaction updated = service.update(id, converter.toEntity(dto));
            return ResponseEntity.ok(converter.toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<TransactionDto>> filter(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(result.stream().map(converter::toDto).toList());
    }

    @PostMapping("/dashboard/OperationDynamics")
    public ResponseEntity<Dashboard.OperationDynamics> operationDynamicsResponseEntity(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(transactionDashboardService.getOperationDynamics(result));
    }

    @PostMapping("/dashboard/OperationTypeDynamics")
    public ResponseEntity<Dashboard.OperationTypeDynamics> operationTypeDynamicsResponseEntity(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(transactionDashboardService.getOperationsTypeDynamics(result));
    }

    @PostMapping("/dashboard/IncomeExpensesComparison")
    public ResponseEntity<Dashboard.IncomeExpensesComparison> incomeExpensesComparisonResponseEntity(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(transactionDashboardService.getIncomeExpensesComparison(result));
    }

    @PostMapping("/dashboard/OperationCount")
    public ResponseEntity<Dashboard.OperationCount> operationCountResponseEntity(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(transactionDashboardService.getOperationCount(result));
    }

    @PostMapping("/dashboard/BankStatistics")
    public ResponseEntity<Dashboard.BankStatistics> bankStatisticsResponseEntity(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(transactionDashboardService.getBankStatistics(result));
    }

    @PostMapping("/dashboard/CategoryStatistics")
    public ResponseEntity<Dashboard.CategoryStatistics> categoryStatisticsResponseEntity(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = (service.filterTransactions(filterDto));
        return ResponseEntity.ok(transactionDashboardService.getCategoryStatistics(result));
    }
}