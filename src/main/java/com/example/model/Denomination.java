package com.example.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "Denominations")
public class Denomination {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @Column(name = "Denomination", columnDefinition = "decimal(5,2) NOT NULL UNIQUE")
    private BigDecimal denomination;

    @OneToMany(mappedBy = "denomination")
    private List<DenominationCurrency> denominationCurrencyList = new ArrayList<>();

}

