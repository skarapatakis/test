package com.twinero.banking.controller;

import com.twinero.banking.controller.assembler.ClientAssembler;
import com.twinero.banking.entity.*;
import com.twinero.banking.exception.ClientEmailExistsException;
import com.twinero.banking.exception.ClientNotFoundException;
import com.twinero.banking.exception.InsufficientMoneyException;
import com.twinero.banking.exception.WrongEmailOrPasswordException;
import com.twinero.banking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ClientController {

    private final ClientAssembler clientAssembler;
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientAssembler clientAssembler, ClientService clientService) {
        this.clientAssembler = clientAssembler;
        this.clientService = clientService;
    }

    @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public Resource signUp(@RequestBody @Valid ClientDto clientDto) {

        if (clientService.emailExists(clientDto.getEmail())) {
            throw new ClientEmailExistsException();
        }

        Client clientToSignUp = new Client(clientDto);
        Client savedClient = clientService.save(clientToSignUp);

        return clientAssembler.toResource(savedClient);
    }

    @PostMapping(value = "deposit", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public Resource deposit(@RequestBody @Valid Deposit deposit) {

        if (!clientService.emailExists(deposit.getEmail())) {
            throw new ClientNotFoundException(deposit.getEmail());
        }

        Client updatedClient = clientService.doDeposit(deposit.getEmail(), deposit.getAmount());

        return clientAssembler.toResource(updatedClient);
    }

    @PostMapping(value = "withdraw", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public Resource withdraw(@RequestBody @Valid Withdraw withdraw) {

        Optional<Client> clientOptional = clientService.findByEmailAndPassword(withdraw.getClientDto());

        if (!clientOptional.isPresent()) {
            throw new WrongEmailOrPasswordException();
        }

        if (withdraw.getAmount() > clientOptional.get().getAccount().getBalance()) {
            throw new InsufficientMoneyException();
        }

        Client updatedClient = clientService.doWithdraw(withdraw);

        return clientAssembler.toResource(updatedClient);
    }

    /**
     * Another approach to the client checking her balance is to generate a unique token with short expiration period.
     * This will allow the endpoint to be converted into a GET.
     * A new method would handle the login process and persist the token which can be used in the requests to
     * identify the client.
     */
    @PostMapping(value = "account", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public Resource balanceByEmailAndPassword(@RequestBody @Valid ClientDto clientDto) {

        Optional<Client> clientOptional = clientService.findByEmailAndPassword(clientDto);

        if (!clientOptional.isPresent()) {
            throw new WrongEmailOrPasswordException();
        }

        Account account = clientOptional.get().getAccount();

        return clientAssembler.toResource(account, clientDto);
    }
}