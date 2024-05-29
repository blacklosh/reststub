package ru.itis.reststub.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.itis.reststub.dto.response.ErrorResponse;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.GeneralServiceException;
import org.springframework.validation.BindingResult;
import ru.itis.reststub.dto.response.ValidError;
import org.springframework.validation.FieldError;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandler {

    @ExceptionHandler(value = GeneralServiceException.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(GeneralServiceException e, HttpServletRequest request) {
        log.warn("Got exception " + e.getMessage() + " (status " + e.getHttpStatusCode() + ") " +
                "from URL " + request.getRequestURI());

        return new ResponseEntity<>(ErrorResponse.builder()
                .errorDescription(e.getMessage())
                .errorCode(e.getErrorCode().name())
                .build(), e.getHttpStatusCode());
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleValidException(BindException e, HttpServletRequest request) {

        log.warn(String.format("Got validation exception %s from URL %s", e.getMessage(), request.getRequestURI()));

        ErrorResponse errorValidDto = ErrorResponse.builder()
                .errorCode(ErrorCode.FIELDS_ERROR.name())
                .errorDescription("Validation error")
                .validErrors(processFieldErrors(e.getBindingResult()))
                .build();

        return new ResponseEntity<>(errorValidDto, HttpStatus.BAD_REQUEST);
    }

    private List<ValidError> processFieldErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map( e -> {
                    if (e instanceof FieldError error) {
                        return new ValidError(error.getField(),error.getDefaultMessage());
                    }
                    return new ValidError(e.getObjectName(), e.getDefaultMessage());
                })
                .collect(Collectors.toList());
    }
}
