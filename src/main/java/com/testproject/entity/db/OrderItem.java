package com.testproject.entity.db;

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
public class OrderItem {


    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "order_item_generator")
    @SequenceGenerator(name = "order_item_generator", sequenceName = "order_item_id_seq", allocationSize = 1)
    private Long id;

    private String name;
    private int price;

    private int quantity;

    @ManyToMany(mappedBy = "items")
    @JsonBackReference
    private List<Order> orders = new ArrayList<>();

}
