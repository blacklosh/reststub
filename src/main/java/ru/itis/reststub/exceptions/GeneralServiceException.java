package ru.itis.reststub.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralServiceException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatusCode;

    public GeneralServiceException(ErrorCode errorCode, HttpStatus httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }
}
