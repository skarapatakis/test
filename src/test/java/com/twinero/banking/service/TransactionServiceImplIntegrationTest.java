package com.twinero.banking.service;

import com.twinero.banking.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class TransactionServiceImplIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void makeWithdraw() {

        Client client = new Client("test@provider.tld", "1234");
        client.setAccount(new Account(10D));

        clientService.save(client);

        Withdraw withdraw = new Withdraw(new ClientDto("test@provider.tld", "1234"));

        withdraw.setAmount(10D);

        Client clientUpdated = clientService.doWithdraw(withdraw);

        assertEquals(1, clientUpdated.getAccount().getTransactions().size());
        assertEquals(TransactionType.WITHDRAW, clientUpdated.getAccount().getTransactions().get(0).getTransactionType());
        assertEquals(10D, clientUpdated.getAccount().getTransactions().get(0).getAmount(), 0.0001D);
    }

    @Test
    public void makeWithdraw_incorrect_email_should_return_empty_client_no_transaction_added() {

        clientService.save(new Client("test@provider.tld", "1234"));

        Withdraw withdraw = new Withdraw(new ClientDto("test@p.tld", "1234"));

        withdraw.setAmount(10D);

        Client clientUpdated = clientService.doWithdraw(withdraw);

        assertTrue(clientUpdated.getAccount().getTransactions().isEmpty());
    }

    @Test
    public void makeWithdraw_not_enough_balance_should_return_empty_client_no_transaction_added() {

        clientService.save(new Client("test@provider.tld", "1234"));

        Withdraw withdraw = new Withdraw(new ClientDto("test@provider.tld", "1234"));

        withdraw.setAmount(10D);

        Client clientUpdated = clientService.doWithdraw(withdraw);

        assertTrue(clientUpdated.getAccount().getTransactions().isEmpty());
    }

    @Test
    public void makeDeposit_should_add_it_to_the_transactions() {

        String email = "testemail@provider.tld";

        clientService.save(new Client(email, "1234"));

        Client clientUpdated = clientService.doDeposit(email, 1025D);

        assertEquals(1, clientUpdated.getAccount().getTransactions().size());
        assertEquals(TransactionType.DEPOSIT, clientUpdated.getAccount().getTransactions().get(0).getTransactionType());
        assertEquals(1025D, clientUpdated.getAccount().getTransactions().get(0).getAmount(), 0.0001D);
    }

    @Test
    public void makeDeposit_incorrect_email_should_return_empty_client() {

        String email = "testemail@provider.tld";

        clientService.save(new Client(email, "1234"));

        Client clientUpdated = clientService.doDeposit("someOtherEmail@a", 1025D);

        assertTrue(clientUpdated.getAccount().getTransactions().isEmpty());
        assertTrue(clientUpdated.getEmail().isEmpty());
        assertTrue(clientUpdated.getPassword().isEmpty());
    }

    @Test
    public void make_few_Transactions_should_be_added_to_transactions_and_balance_updated() {

        String email = "moves@provider.tld";

        clientService.save(new Client(email, "1234"));
        clientService.doDeposit(email, 1250D);
        clientService.doDeposit(email, 1250D);
        clientService.doDeposit(email, 1250D);

        Client client = clientService.doDeposit(email, 1250D);

        assertEquals(5000D, client.getAccount().getBalance(), 0.0001D);

        Withdraw withdraw = new Withdraw(new ClientDto(email, "1234"));

        withdraw.setAmount(100D);

        clientService.doWithdraw(withdraw);
        clientService.doWithdraw(withdraw);
        client = clientService.doWithdraw(withdraw);

        assertEquals(5000D - 300D, client.getAccount().getBalance(), 0.0001D);
        assertEquals(7, client.getAccount().getTransactions().size());

        List<Transaction> depositTransactions = client.getAccount().getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.DEPOSIT))
                .collect(toList());

        List<Transaction> withdrwaTransactions = client.getAccount().getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.WITHDRAW))
                .collect(toList());

        assertEquals(4, depositTransactions.size());
        assertEquals(3, withdrwaTransactions.size());
    }
}