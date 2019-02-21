package com.twinero.banking.service;

import com.twinero.banking.entity.Client;
import com.twinero.banking.entity.ClientDto;
import com.twinero.banking.entity.Withdraw;
import com.twinero.banking.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionService transactionService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, TransactionService transactionService) {
        this.clientRepository = clientRepository;
        this.transactionService = transactionService;
    }

    @Override
    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public Client save(Client clientToSave) {
        return clientRepository.save(clientToSave);
    }

    @Override
    public Client doDeposit(String email, double amount) {

        Optional<Client> clientOptional = findByEmail(email);

        if (!clientOptional.isPresent()) {
            logger.error("Deposit failed. The email doesn't exist!");
            return new Client();
        }

        Client client = clientOptional.get();

        return transactionService.makeDeposit(client, amount);
    }

    @Override
    public Client doWithdraw(Withdraw withdraw) {

        Optional<Client> clientOptional = findByEmail(withdraw.getClientDto().getEmail());

        if (clientOptional.isPresent() && clientOptional.get().getAccount().getBalance() >= withdraw.getAmount()) {
            return transactionService.makeWithdraw(clientOptional.get(), withdraw.getAmount());
        }

        logger.error("Withdraw failed. The email doesn't exist and/or amount insufficient!");
        return new Client();
    }

    @Override
    public Optional<Client> findByEmailAndPassword(ClientDto clientDto) {
        return clientRepository.findByEmailAndPassword(clientDto.getEmail(), clientDto.getPassword());
    }

    private Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email.trim());
    }
}
