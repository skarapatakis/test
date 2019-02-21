package com.twinero.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WrongEmailOrPasswordAdvice {

    @ExceptionHandler(WrongEmailOrPasswordException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleWrongEmailOrPasswordException(WrongEmailOrPasswordException exception) {
        return exception.getMessage();
    }
}
