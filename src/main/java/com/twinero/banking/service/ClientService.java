package com.twinero.banking.service;


import com.twinero.banking.entity.Client;
import com.twinero.banking.entity.ClientDto;
import com.twinero.banking.entity.Withdraw;

import java.util.Optional;

public interface ClientService {

    boolean emailExists(String email);

    Client save(Client clientToSave);

    Client doDeposit(String email, double amount);

    Client doWithdraw(Withdraw withdraw);

    Optional<Client> findByEmailAndPassword(ClientDto clientDto);
}
