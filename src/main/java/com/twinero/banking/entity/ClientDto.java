package com.twinero.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * POJO for holding email and password for a customer
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(max = 8)
    private String password;
}
