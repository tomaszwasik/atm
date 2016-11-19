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
@Table(name = "Bank")
public class Bank {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @Column(name = "Names", columnDefinition = "VARCHAR(40) NOT NULL")
    private String name;

    @OneToMany(mappedBy = "bank")
    private List<BankAccount> bankAccountList = new ArrayList<>();
}

