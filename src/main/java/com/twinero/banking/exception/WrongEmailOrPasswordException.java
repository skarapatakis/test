package com.twinero.banking.exception;

public class WrongEmailOrPasswordException extends RuntimeException {

    public WrongEmailOrPasswordException() {
        super("Incorrect email and/or password provided");
    }
}
