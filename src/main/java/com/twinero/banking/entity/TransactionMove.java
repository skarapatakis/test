package com.twinero.banking.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
public abstract class TransactionMove {

    @Positive
    @Setter
    private double amount;
}
