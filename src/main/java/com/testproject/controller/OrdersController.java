package com.testproject.controller;

import com.testproject.dto.OrderItemRequest;
import com.testproject.dto.OrderResponse;
import com.testproject.entity.db.Order;
import com.testproject.entity.redis.UnpaidOrder;
import com.testproject.service.OrderService;
import com.testproject.service.RedisOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;
    private final RedisOrderService redisOrderService;

    @PostMapping("/new")
    public ResponseEntity<UnpaidOrder> createNewOrder() {
        return ResponseEntity.ok(redisOrderService.createOrder());
    }

    @PostMapping("/add-item")
    public ResponseEntity<Void> addItem(@RequestBody OrderItemRequest itemRequest) throws Exception {
        redisOrderService.addItem(itemRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<Order> payAndSave(@PathVariable Long orderId) throws Exception {
        return ResponseEntity.ok(orderService.payAndSave(orderId));
    }


    @GetMapping("/get/{orderId}")
    public ResponseEntity<UnpaidOrder> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

}
