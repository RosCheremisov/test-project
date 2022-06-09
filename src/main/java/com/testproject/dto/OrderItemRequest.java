package com.testproject.dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderItemRequest {

    long orderId;
    long itemId;
    int quantity;

}
