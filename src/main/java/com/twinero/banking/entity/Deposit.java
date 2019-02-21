package com.twinero.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * POJO for holding deposit data
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Deposit extends TransactionMove {

    @Email
    @NotBlank
    private String email;
}
