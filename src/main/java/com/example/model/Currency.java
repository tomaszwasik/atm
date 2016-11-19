package com.example.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "Currency")
public class Currency {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @Column (name = "Symbol", columnDefinition = "VARCHAR(3)")
    private String symbol;

    @Column (name = "Country", columnDefinition = "VARCHAR(15) NOT NULL")
    private String country;

    @Column(name = "DecimalPlaces", columnDefinition = "int NOT NULL")
    private int decimalPlaces;

    @Column(name = "Changeable", columnDefinition = "bit NOT NULL")
    private boolean changeable;

    @OneToMany(mappedBy = "currency")
    private List<DenominationCurrency> denominationCurrencyList = new ArrayList<>();

}







