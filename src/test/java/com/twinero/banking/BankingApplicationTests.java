package com.twinero.banking;

import com.twinero.banking.controller.ClientController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankingApplicationTests {

    @Autowired
    private ClientController clientController;

    @Test
    public void contextLoads() {
        assertThat(clientController).isNotNull();
    }
}
