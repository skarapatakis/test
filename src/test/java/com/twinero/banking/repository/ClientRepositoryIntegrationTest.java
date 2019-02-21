package com.twinero.banking.repository;

import com.twinero.banking.entity.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void findByEmail_should_return_client() {

        Client client = new Client("testemail@provider.tld", "1234");

        testEntityManager.persist(client);
        testEntityManager.flush();

        Optional<Client> clientOptional = clientRepository.findByEmail("testemail@provider.tld");

        assertTrue(clientOptional.isPresent());
    }

    @Test
    public void when_findByEmail_email_wrong_should_return_empty() {

        Client client = new Client("someEmail@provider.tld", "1234");

        testEntityManager.persist(client);
        testEntityManager.flush();

        Optional<Client> clientOptional = clientRepository.findByEmail("testemail@provider.tld");

        assertFalse(clientOptional.isPresent());
    }

    @Test
    public void when_findByEmailAndPassword_should_return_client() {

        Client client = new Client("email@a.b", "aaaa");

        testEntityManager.persist(client);
        testEntityManager.flush();

        Optional<Client> optionalClient = clientRepository.findByEmailAndPassword(client.getEmail(), client.getPassword());

        assertTrue(optionalClient.isPresent());
        assertEquals("email@a.b", optionalClient.get().getEmail());
    }

    @Test
    public void when_findByEmailAndPassword_incorrect_email_should_return_empty() {

        Client client = new Client("email@a.b", "aaaa");

        testEntityManager.persist(client);
        testEntityManager.flush();

        Optional<Client> optionalClient = clientRepository.findByEmailAndPassword("incorrect@a.b", "24");

        assertFalse(optionalClient.isPresent());
    }
}