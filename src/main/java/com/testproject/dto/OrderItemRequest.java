package com.testproject.dto;


import lombok.Value;

@Value
public class OrderItemRequest {

    long orderId;
    long itemId;
    int quantity;

}
