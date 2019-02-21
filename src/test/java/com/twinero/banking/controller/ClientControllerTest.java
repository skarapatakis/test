package com.twinero.banking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twinero.banking.controller.assembler.ClientAssembler;
import com.twinero.banking.entity.*;
import com.twinero.banking.repository.ClientRepository;
import com.twinero.banking.service.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientAssembler clientAssembler;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    public void signUp() throws Exception {

        String email = "a@b.c";

        when(clientService.emailExists(email)).thenReturn(false);

        MockHttpServletResponse response = mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                objectMapper.writeValueAsString(
                                        new ClientDto("a@b.c", "4345")
                                )
                        )
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void signUp_email_exists_should_return_conflict() throws Exception {

        String email = "a@b.c";

        when(clientService.emailExists(email)).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                objectMapper.writeValueAsString(
                                        new ClientDto("a@b.c", "4345")
                                )
                        )
        ).andReturn().getResponse();

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("The email entered is already taken. Please use a different one", response.getContentAsString());
    }


    @Test
    public void signUp_password_empty_should_return_bad_request() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                objectMapper.writeValueAsString(
                                        new ClientDto("a@b.c", "")
                                )
                        )
        ).andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void deposit() throws Exception {

        String email = "a@b.c";
        Deposit deposit = new Deposit(email);

        deposit.setAmount(1024D);
        when(clientService.emailExists(email)).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                objectMapper.writeValueAsString(deposit)
                        )
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void deposit_email_not_exists_should_return_not_found() throws Exception {

        String email = "a@b.c";
        Deposit deposit = new Deposit(email);

        deposit.setAmount(1024D);
        when(clientService.emailExists(email)).thenReturn(false);

        MockHttpServletResponse response = mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                objectMapper.writeValueAsString(deposit)
                        )
        ).andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Could not find client with email a@b.c", response.getContentAsString());
    }

    @Test
    public void deposit_invalid_post_data_should_return_bad_request() throws Exception {

        String invalidEmail = "invalidEmailAddress";
        Deposit deposit = new Deposit(invalidEmail);

        deposit.setAmount(-45D);

        MockHttpServletResponse response = mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                objectMapper.writeValueAsString(deposit)
                        )
        ).andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void withdraw() throws Exception {

        String email = "a@b.c";
        String password = "1221";
        ClientDto clientDto = new ClientDto(email, password);
        Client client = mock(Client.class);
        Withdraw withdraw = new Withdraw(clientDto);
        Account account = mock(Account.class);

        withdraw.setAmount(1024D);
        when(clientService.findByEmailAndPassword(any())).thenReturn(Optional.of(client));

        when(client.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1024.1D);

        MockHttpServletResponse response = mockMvc.perform(
                post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(withdraw))
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void withdraw_insufficient_money_should_return_not_acceptable() throws Exception {

        String email = "a@b.c";
        String password = "1221";
        ClientDto clientDto = new ClientDto(email, password);
        Client client = mock(Client.class);
        Withdraw withdraw = new Withdraw(clientDto);
        Account account = mock(Account.class);

        withdraw.setAmount(1024.11D);
        when(clientService.findByEmailAndPassword(any())).thenReturn(Optional.of(client));

        when(client.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1024.1D);

        MockHttpServletResponse response = mockMvc.perform(
                post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(withdraw))
        ).andReturn().getResponse();

        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
        assertEquals("Insufficient amount", response.getContentAsString());
    }

    @Test
    public void withdraw_invalid_email_and_password_combination_should_return_unauthorized() throws Exception {

        String email = "a@b.c";
        String password = "1221";
        ClientDto clientDto = new ClientDto(email, password);
        Withdraw withdraw = new Withdraw(clientDto);

        withdraw.setAmount(4D);
        when(clientService.findByEmailAndPassword(any())).thenReturn(Optional.empty());

        MockHttpServletResponse response = mockMvc.perform(
                post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(withdraw))
        ).andReturn().getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("Incorrect email and/or password provided", response.getContentAsString());
    }

    @Test
    public void balanceByEmailAndPassword() throws Exception {

        String email = "a@b.c";
        String password = "1221";
        ClientDto clientDto = new ClientDto(email, password);

        Client client = mock(Client.class);
        Account account = mock(Account.class);

        when(clientService.findByEmailAndPassword(any())).thenReturn(Optional.of(client));

        when(client.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1024.1D);

        MockHttpServletResponse response = mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(clientDto))
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void balanceByEmailAndPassword_incorrect_password_should_return_unathorized() throws Exception {

        String email = "a@b.c";
        String password = "1221";
        ClientDto clientDto = new ClientDto(email, password);

        Client client = mock(Client.class);
        Account account = mock(Account.class);

        when(clientService.findByEmailAndPassword(any())).thenReturn(Optional.empty());

        when(client.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1024.1D);

        MockHttpServletResponse response = mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(clientDto))
        ).andReturn().getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("Incorrect email and/or password provided", response.getContentAsString());
    }
}