package ru.itis.reststub.exceptions.auth;

import org.springframework.http.HttpStatus;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;

public class WrongCredentialsException extends GeneralServiceException {
    public WrongCredentialsException() {
        super(ErrorCode.INCORRECT_PASSWORD, HttpStatus.FORBIDDEN);
    }
}
