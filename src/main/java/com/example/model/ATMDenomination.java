package com.example.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "ATM_Denominations")
public class ATMDenomination {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @OneToOne
    @JoinColumn(name = "DenominationsCurrencyID", columnDefinition = "INT NOT NULL FOREIGN KEY REFERENCES Denominations_Currency(ID)")
    private DenominationCurrency denominationCurrency;

    @Column(name = "MaxQuantity", columnDefinition = "INT")
    private int maxQuantity;

    @Column(name = "Quantity", columnDefinition = "INT")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "AccountID", columnDefinition = "INT NOT NULL FOREIGN KEY REFERENCES Account(ID)")
    private Account account;
}

