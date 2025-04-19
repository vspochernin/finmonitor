package ru.hackathon.finmonitor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FinmonitorErrorType {
    UNEXPECTED_ERROR(0, "Непредвиденная ошибка", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_EXISTS(1, "Пользователь с таким логином уже существует", HttpStatus.BAD_REQUEST),
    BAD_CREDENTIALS(2, "Пользователь с таким логином или паролем не найден", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST_BODY(3, "Некорректное тело запроса", HttpStatus.BAD_REQUEST),
    NOT_FOUND(4, "Запрашиваемый элемент не найден", HttpStatus.NOT_FOUND),
    ;

    private final int id;
    private final String description;
    private final HttpStatus httpStatus;

    FinmonitorErrorType(int id, String description, HttpStatus httpStatus) {
        this.id = id;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
