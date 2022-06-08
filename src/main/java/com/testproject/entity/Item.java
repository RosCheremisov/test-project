package com.testproject.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;
@EqualsAndHashCode(exclude = "orders")
@ToString(exclude = "orders")
@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "item_generator")
    @SequenceGenerator(name = "item_generator", sequenceName = "item_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name  = "price")
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(mappedBy = "items")
    @JsonBackReference
    List<Order> orders = new ArrayList<>();
}
