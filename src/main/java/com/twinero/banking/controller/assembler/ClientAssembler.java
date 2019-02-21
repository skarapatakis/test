package com.twinero.banking.controller.assembler;

import com.twinero.banking.controller.ClientController;
import com.twinero.banking.entity.*;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ClientAssembler implements ResourceAssembler<Client, Resource<Client>> {

    @Override
    public Resource<Client> toResource(Client client) {

        return new Resource<>(client,

                linkTo(methodOn(ClientController.class).deposit(new Deposit())).withSelfRel(),
                linkTo(methodOn(ClientController.class).withdraw(new Withdraw())).withSelfRel(),
                linkTo(methodOn(ClientController.class).balanceByEmailAndPassword(new ClientDto())).withSelfRel(),
                linkTo(methodOn(ClientController.class).signUp(null)).withRel("signup"));
    }

    public Resource toResource(Account account, ClientDto clientDto) {
        return new Resource<>(account,
                linkTo(methodOn(ClientController.class).deposit(new Deposit())).withSelfRel(),
                linkTo(methodOn(ClientController.class).withdraw(new Withdraw())).withSelfRel(),
                linkTo(methodOn(ClientController.class).balanceByEmailAndPassword(clientDto)).withSelfRel(),
                linkTo(methodOn(ClientController.class).signUp(null)).withRel("signup"));
    }
}
