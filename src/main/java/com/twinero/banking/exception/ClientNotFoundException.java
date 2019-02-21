package com.twinero.banking.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String email) {
        super("Could not find client with email " + email);
    }
}
