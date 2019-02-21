package com.twinero.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClientEmailExistsAdvice {

    @ExceptionHandler(ClientEmailExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public String clientEmailExistHandler(ClientEmailExistsException exception) {
        return exception.getMessage();
    }
}