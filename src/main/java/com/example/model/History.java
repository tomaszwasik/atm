package com.example.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "History")
public class History {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @OneToOne
    @JoinColumn(name = "AccountFromId", columnDefinition = "INT NOT NULL FOREIGN KEY REFERENCES Account(ID)")
    private Account accountFrom;

    @OneToOne
    @JoinColumn(name = "AccountToId", columnDefinition = "INT NOT NULL FOREIGN KEY REFERENCES Account(ID)")
    private Account accountTo;

    @Column(name = "TypeOfTransaction", columnDefinition = "VARCHAR(15) NOT NULL")
    private String typeOfTransaction;

    @Column(name = "Amount", columnDefinition = "money NOT NULL")
    private BigDecimal amount;

    @Column(name = "DateOfTransaction", columnDefinition="DateTime NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfTransaction;


}
