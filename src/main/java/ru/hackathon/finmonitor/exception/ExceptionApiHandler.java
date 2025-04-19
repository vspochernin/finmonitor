package ru.hackathon.finmonitor.exception;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(FinmonitorException.class)
    public ResponseEntity<ErrorMessage> finmonitorException(FinmonitorException exception) {
        return ResponseEntity
                .status(exception.getErrorType().getHttpStatus())
                .body(ErrorMessage.fromFinmonitorException(exception));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> badCredentialsException(BadCredentialsException exception) {
        FinmonitorErrorType errorType = FinmonitorErrorType.BAD_CREDENTIALS;
        return ResponseEntity
                .status(errorType.getHttpStatus())
                .body(ErrorMessage.fromErrorTypeWithAdditionalInfo(errorType, exception.toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FinmonitorErrorType errorType = FinmonitorErrorType.BAD_REQUEST_BODY;
        return ResponseEntity
                .status(errorType.getHttpStatus())
                .body(ErrorMessage.fromErrorTypeWithAdditionalInfo(errorType, exception.toString()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        FinmonitorErrorType errorType = FinmonitorErrorType.BAD_REQUEST_BODY;
        return ResponseEntity
                .status(errorType.getHttpStatus())
                .body(ErrorMessage.fromErrorTypeWithAdditionalInfo(errorType, exception.toString()));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorMessage> numberFormatException(NumberFormatException exception) {
        FinmonitorErrorType errorType = FinmonitorErrorType.BAD_REQUEST_BODY;
        return ResponseEntity
                .status(errorType.getHttpStatus())
                .body(ErrorMessage.fromErrorTypeWithAdditionalInfo(errorType, exception.toString()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> noSuchElementException(NoSuchElementException exception) {
        FinmonitorErrorType errorType = FinmonitorErrorType.NOT_FOUND;
        return ResponseEntity
                .status(errorType.getHttpStatus())
                .body(ErrorMessage.fromErrorTypeWithAdditionalInfo(errorType, exception.toString()));
    }
}
