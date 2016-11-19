package com.example.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "Cards")
public class Card {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @OneToOne
    @JoinColumn(name = "AccountID", columnDefinition = "INT NOT NULL FOREIGN KEY REFERENCES Account(ID)")
    private Account account;

    @Column(name = "PINNumber", columnDefinition = "varchar(4) NOT NULL")
    private String pin;

    @OneToOne
    @JoinColumn(name = "LimitID", columnDefinition = "INT NOT NULL FOREIGN KEY REFERENCES Limits(ID)")
    private Limit limit;

    @Column(name = "CardType", columnDefinition = "VARCHAR(20) NOT NULL")
    private String cardType;

    @Column(name = "IsStolen", columnDefinition = "bit NULL")
    private boolean stolen;
}
