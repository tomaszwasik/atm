package com.example.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
@Entity
@Table(name = "Limits")
public class Limit {

    @Id
    @Column(name = "ID", columnDefinition = "int identity(1,1)")
    private Long id;

    @Column(name = "TypeOfLimit", columnDefinition = "VARCHAR(20) NOT NULL")
    private String typeOfLimit;

    @Column(name = "Quantity", columnDefinition = "INT NOT NULL")
    private int quantity;
}