package ru.itis.reststub.exceptions.auth;

import org.springframework.http.HttpStatus;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;

public class NotConfirmedException extends GeneralServiceException {

    public NotConfirmedException() {
        super(ErrorCode.NOT_CONFIRMED, HttpStatus.UNAUTHORIZED);
    }
}
