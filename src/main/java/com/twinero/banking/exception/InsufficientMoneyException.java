package com.twinero.banking.exception;

public class InsufficientMoneyException extends RuntimeException {

    public InsufficientMoneyException() {
        super("Insufficient amount");
    }
}
