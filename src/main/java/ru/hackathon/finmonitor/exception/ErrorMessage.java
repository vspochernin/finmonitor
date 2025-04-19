package ru.hackathon.finmonitor.exception;

public record ErrorMessage(
        int id,
        String description,
        String errorType,
        String additionalInfo)
{

    public static ErrorMessage fromErrorTypeWithAdditionalInfo(FinmonitorErrorType errorType, String additionalInfo) {
        return new ErrorMessage(errorType.getId(), errorType.getDescription(), errorType.name(), additionalInfo);
    }

    public static ErrorMessage fromFinmonitorException(FinmonitorException exception) {
        FinmonitorErrorType errorType = exception.getErrorType();

        return new ErrorMessage(
                errorType.getId(),
                errorType.getDescription(),
                errorType.name(),
                exception.getAdditionalInfo());
    }
}
