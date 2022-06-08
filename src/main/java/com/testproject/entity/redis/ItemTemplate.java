package com.testproject.entity.redis;


import lombok.Data;

@Data
public class ItemTemplate {

    private Long id;
    private String itemName;
    private int price;
    private int quantity;

}
