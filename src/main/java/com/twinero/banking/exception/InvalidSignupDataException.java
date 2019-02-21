package com.twinero.banking.exception;

public class InvalidSignupDataException extends RuntimeException {

    public InvalidSignupDataException() {
        super("Invalid data entered for signing up client! email and password  must be provided");
    }
}
