package com.twinero.banking.exception;

public class ClientEmailExistsException extends RuntimeException {

    public ClientEmailExistsException() {
        super("The email entered is already taken. Please use a different one");
    }
}
