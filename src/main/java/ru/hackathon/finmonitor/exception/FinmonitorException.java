package ru.hackathon.finmonitor.exception;

import lombok.Getter;

@Getter
public class FinmonitorException extends RuntimeException {

    private static final String DEFAULT_ADDITIONAL_INFO = "";

    private final FinmonitorErrorType errorType;
    private final String additionalInfo;

    public FinmonitorException(FinmonitorErrorType errorType) {
        this.errorType = errorType;
        this.additionalInfo = DEFAULT_ADDITIONAL_INFO;
    }

    public FinmonitorException(FinmonitorErrorType errorType, String additionalInfo) {
        this.errorType = errorType;
        this.additionalInfo = additionalInfo;
    }
}
