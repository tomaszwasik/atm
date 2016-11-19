package com.example.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "Denominations_Currency")
public class DenominationCurrency {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DenominationsId", columnDefinition = "INT not null FOREIGN KEY REFERENCES Denominations(ID)")
    private Denomination denomination;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CurrencyID", columnDefinition = "INT not null FOREIGN KEY REFERENCES Currency(ID)")
    private Currency currency;
}
