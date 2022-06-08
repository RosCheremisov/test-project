package com.testproject.entity.db;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "item_generator")
    @SequenceGenerator(name = "item_generator", sequenceName = "item_id_seq", allocationSize = 1)
    private Long id;

    private String name;
    private int price;

    private int quantity;
}
