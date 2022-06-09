package com.testproject.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class ItemResponse {

    Long id;
    String name;
    int price;
    int quantity;

}
