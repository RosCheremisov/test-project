package com.testproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RedisHash("order")
public class UnpaidOrder {

    @Id
    private Long id;

    @TimeToLive
    private int ttl;

    private int price;
    private int quantity;

    private List<ItemTemplate> items = new ArrayList<>();

}
