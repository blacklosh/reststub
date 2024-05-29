package ru.itis.reststub.exceptions.auth;

import org.springframework.http.HttpStatus;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;

public class IncorrectConfirmCodeException extends GeneralServiceException {

    public IncorrectConfirmCodeException() {
        super(ErrorCode.INCORRECT_CONFIRM_CODE, HttpStatus.NOT_FOUND);
    }
}
