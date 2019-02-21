package com.twinero.banking.service;

import com.twinero.banking.entity.Account;
import com.twinero.banking.entity.Client;
import com.twinero.banking.entity.ClientDto;
import com.twinero.banking.entity.Withdraw;
import com.twinero.banking.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void emailExists() {

        clientService.emailExists("email@pro.tld");

        verify(clientRepository, times(1)).findByEmail("email@pro.tld");
    }

    @Test
    public void save() {

        Client client = new Client("test@a.b", "2222");
        clientService.save(client);

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void doDeposit() {

        String email = "email@a.b";
        Client client = mock(Client.class);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        clientService.doDeposit(email, 150D);

        verify(clientRepository, times(1)).findByEmail(email);
        verify(transactionService, times(1)).makeDeposit(client, 150D);
    }

    @Test
    public void doWithdraw() {

        String email = "email@a.b";
        Account account = mock(Account.class);
        Client client = mock(Client.class);
        Withdraw withdraw = mock(Withdraw.class);

        when(withdraw.getClientDto()).thenReturn(new ClientDto(email, "1234"));
        when(withdraw.getAmount()).thenReturn(30D);
        when(account.getBalance()).thenReturn(30D);
        when(client.getAccount()).thenReturn(account);
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        clientService.doWithdraw(withdraw);

        verify(clientRepository, times(1)).findByEmail(email);
        verify(transactionService, times(1)).makeWithdraw(client, 30D);
    }

    @Test
    public void findByEmailAndPassword() {

        ClientDto clientdto = new ClientDto("a@b.c", "1111");
        clientService.findByEmailAndPassword(clientdto);

        verify(clientRepository, times(1)).findByEmailAndPassword(clientdto.getEmail(), clientdto.getPassword());
    }
}