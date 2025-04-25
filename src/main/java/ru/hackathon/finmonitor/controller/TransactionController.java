package ru.hackathon.finmonitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hackathon.finmonitor.controller.dto.Dashboard;
import ru.hackathon.finmonitor.controller.dto.TransactionConverter;
import ru.hackathon.finmonitor.controller.dto.TransactionDto;
import ru.hackathon.finmonitor.controller.dto.TransactionFilterDto;
import ru.hackathon.finmonitor.model.Period;
import ru.hackathon.finmonitor.model.Transaction;
import ru.hackathon.finmonitor.service.TransactionDashboardService;
import ru.hackathon.finmonitor.service.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService service;
    private final TransactionConverter converter;
    private final TransactionDashboardService transactionDashboardService;

    @GetMapping("all/{limit}")
    public List<TransactionDto> getAll(@PathVariable("limit") Integer limit) {
        return service.getAll(limit)
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получить транзакцию по ID", description = "Ищет транзакцию по ID и возвращает её, если найдена")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция найдена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getById(@PathVariable Long id) {
        Optional<Transaction> tx = service.getById(id);
        return tx.map(t -> ResponseEntity.ok(converter.toDto(t)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Создать транзакцию", description = "Добавляет новую транзакцию в систему")
    @PostMapping
    public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto dto) {
        Transaction saved = service.create(converter.toEntity(dto));
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить транзакцию", description = "Обновляет существующую транзакцию по ID")
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> update(@PathVariable Long id,
                                                 @RequestBody TransactionDto dto) {
        Transaction updated = service.update(id, converter.toEntity(dto));
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @Operation(summary = "Удалить транзакцию", description = "Удаляет транзакцию по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Фильтрация транзакций", description = "Фильтрует транзакции по заданным критериям (даты, суммы, категории и т.д.)")
    @PostMapping("/filter")
    public ResponseEntity<List<TransactionDto>> filter(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto, filterDto.getLimit());
        return ResponseEntity.ok(result.stream().map(converter::toDto).collect(Collectors.toList()));
    }

    @Operation(summary = "Динамика операций", description = "Показывает, как изменялось количество операций во времени (по периодам)")
    @PostMapping("/dashboard/OperationDynamics")
    public ResponseEntity<Map<String, Long>> operationDynamicsResponseEntity(
            @RequestParam("period") Period period, @RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto);
        return ResponseEntity.ok(transactionDashboardService.getOperationDynamics(result, period).getPeriod());
    }

    @Operation(summary = "Динамика типов операций", description = "Анализирует изменения в количестве разных типов операций во времени")
    @PostMapping("/dashboard/OperationTypeDynamics")
    public ResponseEntity<Dashboard.OperationTypeDynamics> operationTypeDynamicsResponseEntity(
            @RequestParam("period") Period period, @RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto);
        return ResponseEntity.ok(transactionDashboardService.getOperationsTypeDynamics(result, period));
    }

    @Operation(summary = "Сравнение доходов и расходов", description = "Показывает соотношение сумм доходов и расходов")
    @PostMapping("/dashboard/IncomeExpensesComparison")
    public ResponseEntity<Dashboard.IncomeExpensesComparison> incomeExpensesComparisonResponseEntity(
            @RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto);
        return ResponseEntity.ok(transactionDashboardService.getIncomeExpensesComparison(result));
    }

    @Operation(summary = "Количество операций", description = "Возвращает общее количество операций по типам")
    @PostMapping("/dashboard/OperationCount")
    public ResponseEntity<Dashboard.OperationCount> operationCountResponseEntity(
            @RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto);
        return ResponseEntity.ok(transactionDashboardService.getOperationCount(result));
    }

    @Operation(summary = "Статистика по банкам", description = "Показывает статистику операций по банкам")
    @PostMapping("/dashboard/BankStatistics")
    public ResponseEntity<Dashboard.BankStatistics> bankStatisticsResponseEntity(
            @RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto);
        return ResponseEntity.ok(transactionDashboardService.getBankStatistics(result));
    }

    @Operation(summary = "Статистика по категориям", description = "Показывает статистику по категориям трат/доходов")
    @PostMapping("/dashboard/CategoryStatistics")
    public ResponseEntity<Dashboard.CategoryStatistics> categoryStatisticsResponseEntity(
            @RequestBody TransactionFilterDto filterDto) {
        List<Transaction> result = service.filterTransactions(filterDto);
        return ResponseEntity.ok(transactionDashboardService.getCategoryStatistics(result));
    }
}
