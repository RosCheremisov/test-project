package com.testproject.dto;

import com.testproject.entity.redis.ItemTemplate;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private Long id;
    private int quantity;
    private List<ItemTemplate> items;

}
