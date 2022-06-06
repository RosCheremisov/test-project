package com.testproject.controller;

import com.testproject.entity.Order;
import com.testproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;


    @PostMapping("/new")
    public ResponseEntity<Order> saveOrder(@RequestBody List<String> itemNames) throws Exception {
        return ResponseEntity.ok(orderService.createOrder(itemNames));
    }

}
