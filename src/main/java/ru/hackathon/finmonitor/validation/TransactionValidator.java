package ru.hackathon.finmonitor.validation;

import org.springframework.stereotype.Component;
import ru.hackathon.finmonitor.controller.dto.TransactionDto;
import ru.hackathon.finmonitor.exception.FinmonitorErrorType;
import ru.hackathon.finmonitor.exception.FinmonitorException;

import java.util.regex.Pattern;

@Component
public class TransactionValidator {

    private static final Pattern DATE_PATTERN = Pattern.compile(ValidationPatterns.DATE_PATTERN);
    private static final Pattern INN_PATTERN = Pattern.compile(ValidationPatterns.INN_PATTERN);
    private static final Pattern PHONE_PATTERN = Pattern.compile(ValidationPatterns.PHONE_PATTERN);

    public void validate(TransactionDto dto) {
        validateDate(dto.getOperationDateTime());
        validateInn(dto.getReceiverInn());
        validatePhone(dto.getReceiverPhone());
    }

    private void validateDate(String date) {
        if (date != null && !DATE_PATTERN.matcher(date).matches()) {
            throw new FinmonitorException(FinmonitorErrorType.INCORRECT_DATE);
        }
    }

    private void validateInn(String inn) {
        if (inn != null && !INN_PATTERN.matcher(inn).matches()) {
            throw new FinmonitorException(FinmonitorErrorType.INCORRECT_INN);
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new FinmonitorException(FinmonitorErrorType.INCORRECT_PHONE);
        }
    }
} 