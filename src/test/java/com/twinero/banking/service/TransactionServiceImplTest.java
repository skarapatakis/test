package com.twinero.banking.service;

import com.twinero.banking.entity.Account;
import com.twinero.banking.entity.Client;
import com.twinero.banking.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void makeDeposit() {

        Client client = new Client("a@b.c", "1111");
        transactionService.makeDeposit(client, 100D);

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void makeDeposit_negative_value_should_not_call_repository() {

        Client client = new Client("a@b.c", "1111");
        transactionService.makeDeposit(client, -8D);

        verify(clientRepository, never()).save(client);
    }

    @Test
    public void makeWithdraw() {

        Client client = new Client("a@b.c", "1111");
        client.setAccount(new Account(10.5D));

        transactionService.makeWithdraw(client, 10D);

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void makeWithdraw_insufficient_amount_should_not_call_repository() {

        Client client = new Client("a@b.c", "1111");
        client.setAccount(new Account(5D));

        transactionService.makeWithdraw(client, 5.1D);

        verify(clientRepository, never()).save(client);
    }

    @Test
    public void makeWithdraw_amount_negative_should_not_call_repository() {

        Client client = new Client("a@b.c", "1111");
        transactionService.makeWithdraw(client, -9D);

        verify(clientRepository, never()).save(client);
    }
}