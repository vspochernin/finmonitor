package ru.hackathon.finmonitor.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.hackathon.finmonitor.controller.dto.TransactionDto;
import ru.hackathon.finmonitor.exception.FinmonitorException;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionValidatorTest {

    private final TransactionValidator validator = new TransactionValidator();

    @Test
    void validate_ValidData_NoException() {
        TransactionDto dto = createValidDto();
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "01.01.2025",  // Корректная дата.
            "31.12.2025",  // Корректная дата.
            "15.06.2025"   // Корректная дата.
    })
    void validate_ValidDates_NoException(String date) {
        TransactionDto dto = createValidDto();
        dto.setOperationDateTime(date);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "32.01.2025",  // Некорректный день.
            "00.01.2025",  // Некорректный день.
            "01.13.2025",  // Некорректный месяц.
            "01.00.2025",  // Некорректный месяц.
            "01.01.202",   // Некорректный год.
            "01-01-2025",  // Некорректный разделитель.
            "1.1.2025",    // Некорректный формат.
            "01.1.2025",   // Некорректный формат.
            "1.01.2025",    // Некорректный формат.
            ""    // Пустой.
    })
    void validate_InvalidDates_ThrowsException(String date) {
        TransactionDto dto = createValidDto();
        dto.setOperationDateTime(date);
        assertThrows(FinmonitorException.class, () -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678901",     // Корректный ИНН.
            "98765432109"      // Корректный ИНН.
    })
    void validate_ValidInn_NoException(String inn) {
        TransactionDto dto = createValidDto();
        dto.setReceiverInn(inn);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890",      // Слишком короткий.
            "123456789012",    // Слишком длинный.
            "1234567890a",     // Содержит буквы.
            "1234567890-",     // Содержит спецсимволы.
            ""                 // Пустой.
    })
    void validate_InvalidInn_ThrowsException(String inn) {
        TransactionDto dto = createValidDto();
        dto.setReceiverInn(inn);
        assertThrows(FinmonitorException.class, () -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+79123456789",    // Корректный с +7.
            "89123456789"      // Корректный с 8.
    })
    void validate_ValidPhone_NoException(String phone) {
        TransactionDto dto = createValidDto();
        dto.setReceiverPhone(phone);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "79123456789",     // Начинается с 7.
            "+89123456789",    // Начинается с +8.
            "+7912345678",     // Слишком короткий.
            "+791234567890",   // Слишком длинный.
            "+7912345678a",    // Содержит буквы.
            "+7912345678-",    // Содержит спецсимволы.
            ""                 // Пустой.
    })
    void validate_InvalidPhone_ThrowsException(String phone) {
        TransactionDto dto = createValidDto();
        dto.setReceiverPhone(phone);
        assertThrows(FinmonitorException.class, () -> validator.validate(dto));
    }

    private TransactionDto createValidDto() {
        TransactionDto dto = new TransactionDto();
        dto.setOperationDateTime("01.01.2025");
        dto.setReceiverInn("12345678901");
        dto.setReceiverPhone("+79123456789");
        return dto;
    }
} 