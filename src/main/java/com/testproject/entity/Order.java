package com.testproject.entity;

import lombok.Data;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    private int price;
    private int quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id",  referencedColumnName = "id")
    private List<Item> items;

}
