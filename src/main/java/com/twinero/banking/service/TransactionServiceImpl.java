package com.twinero.banking.service;

import com.twinero.banking.entity.Client;
import com.twinero.banking.entity.Transaction;
import com.twinero.banking.entity.TransactionType;
import com.twinero.banking.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final ClientRepository clientRepository;

    @Autowired
    public TransactionServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client makeDeposit(Client client, double amount) {

        if (amount <= 0) {
            logger.error("Deposit failed. Amount entered must be positive!");
            return new Client();
        }

        double newBalance = client.getAccount().getBalance() + amount;
        client.getAccount().setBalance(newBalance);

        return addTransaction(client, amount, TransactionType.DEPOSIT);
    }

    @Override
    public Client makeWithdraw(Client client, double amount) {

        double newBalance = client.getAccount().getBalance() - amount;

        if (amount <= 0 || newBalance < 0) {
            logger.error("Withdraw failed. Amount entered is either negative or insufficient amount!");
            return new Client();
        }

        client.getAccount().setBalance(newBalance);

        return addTransaction(client, amount, TransactionType.WITHDRAW);
    }

    private Client addTransaction(Client client, double amount, TransactionType type) {

        Transaction transaction = new Transaction(amount, type, LocalDateTime.now());

        client.getAccount().addTransaction(transaction);

        return clientRepository.save(client);
    }
}
