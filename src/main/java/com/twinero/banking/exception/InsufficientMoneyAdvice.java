package com.twinero.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InsufficientMoneyAdvice {

    @ExceptionHandler(InsufficientMoneyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleInsufficientMoneyException(InsufficientMoneyException exception) {
        return exception.getMessage();
    }
}
