package com.example.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @Column(name = "Names", columnDefinition = "VARCHAR(50) NOT NULL")
    private String names;

    @Column(name = "Surname", columnDefinition = "VARCHAR(50) NULL")
    private String surname;

    @Column(name = "Phone", columnDefinition = "VARCHAR(20) NULL")
    private String phone;

    @Column(name = "Adress", columnDefinition = "VARCHAR(50) NOT NULL")
    private String address;

    @Column(name = "City", columnDefinition = "VARCHAR(50) NOT NULL")
    private String city;

    @Column(name = "ZipNumber", columnDefinition = "VARCHAR(6) NOT NULL")
    private String zipNumber;

    @Column(name = "Pesel", columnDefinition = "VARCHAR(11) NULL")
    private String pesel;

    @Column(name = "Passwordd", columnDefinition = "VARCHAR(20) NOT NULL")
    private String password;

    @OneToOne
    @JoinColumn(name = "MainCurrency", columnDefinition = "int NOT NULL FOREIGN KEY REFERENCES Currency(ID)")
    private Currency currency;

    @Column(name = "CurrentMoney", columnDefinition = "Money NULL")
    private BigDecimal currentMoney;

    @Column(name = "CurrentVersion", columnDefinition = "int NULL")
    private Long currentVersion;

    @Column(name = "CreatedDate", columnDefinition="DateTime NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "IsATM", columnDefinition = "bit NOT NULL")
    private boolean atm;

    @Column(name = "ATMSaldo", columnDefinition = "money null")
    private BigDecimal atmSaldo;

    @OneToMany(mappedBy = "account")
    private List<BankAccount> bankAccountList = new ArrayList<>();
}

