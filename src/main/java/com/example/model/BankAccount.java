package com.example.model;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Bank_Account")
public class BankAccount {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BankID", columnDefinition = "INT not null FOREIGN KEY REFERENCES Bank(ID)")
    private Bank bank;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AccountID", columnDefinition = "INT not null FOREIGN KEY REFERENCES Account(ID)")
    private Account account;
}
