package com.testproject.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@EqualsAndHashCode(exclude = "items")
@ToString(exclude = "items")
@Data
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    private int price;
    private int quantity;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_orderitem",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id"))
    private List<OrderItem> items =  new ArrayList<>();

}
