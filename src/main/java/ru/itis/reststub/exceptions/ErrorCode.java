package ru.itis.reststub.exceptions;

public enum ErrorCode {
    // General
    FIELDS_ERROR,
    UNSUPPORTED_APPLICATION,
    NO_BEARER,

    // Auth
    INCORRECT_PASSWORD,
    EMAIL_TAKEN,
    NICKNAME_TAKEN,
    INCORRECT_CONFIRM_CODE,
    NOT_LOGGED_IN,
    NOT_CONFIRMED,

}
