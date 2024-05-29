package ru.itis.reststub.exceptions.auth;

import org.springframework.http.HttpStatus;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;

public class EmailAlreadyTakenException extends GeneralServiceException {

    public EmailAlreadyTakenException() {
        super(ErrorCode.EMAIL_TAKEN, HttpStatus.BAD_REQUEST);
    }
}
