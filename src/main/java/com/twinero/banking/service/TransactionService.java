package com.twinero.banking.service;

import com.twinero.banking.entity.Client;

public interface TransactionService {

    Client makeDeposit(Client client, double amount);

    Client makeWithdraw(Client client, double amount);
}
