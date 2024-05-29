package ru.itis.reststub.exceptions.auth;

import org.springframework.http.HttpStatus;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;

public class UsernameAlreadyTakenException extends GeneralServiceException {

    public UsernameAlreadyTakenException() {
        super(ErrorCode.NICKNAME_TAKEN, HttpStatus.BAD_REQUEST);
    }
}
