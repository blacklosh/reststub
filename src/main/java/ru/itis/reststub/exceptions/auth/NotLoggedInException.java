package ru.itis.reststub.exceptions.auth;

import org.springframework.http.HttpStatus;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;

public class NotLoggedInException extends GeneralServiceException {

    public NotLoggedInException() {
        super(ErrorCode.NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);
    }
}
