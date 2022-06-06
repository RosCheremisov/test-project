package com.testproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.testproject.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDto {

    private Long id;
    private int price;
    private int quality;
    private List<Item> items;

}
