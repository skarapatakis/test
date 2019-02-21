package com.twinero.banking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 8)
    @JsonIgnore
    private String password;

    @Email
    @NotNull
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonBackReference
    private Account account;

    public Client() {
        this("", "");
    }

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
        setAccount(new Account());
    }

    public Client(ClientDto clientDto) {
        this(clientDto.getEmail(), clientDto.getPassword());
    }

    public void setAccount(Account account) {

        if (account == null) {
            if (this.account != null) {
                this.account.setClient(null);
            }
        } else {
            account.setClient(this);
        }

        this.account = account;
    }
}
