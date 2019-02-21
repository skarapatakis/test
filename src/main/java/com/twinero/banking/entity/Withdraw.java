package com.twinero.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * POJO for holding withdraw data
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Withdraw extends TransactionMove {

    @NotNull
    @Valid
    private ClientDto clientDto;
}
