package com.testproject.controller;

import com.testproject.entity.ItemTemplate;
import com.testproject.entity.Order;
import com.testproject.entity.UnpaidOrder;
import com.testproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;


    @PostMapping("/new")
    public ResponseEntity<UnpaidOrder> saveOrder(@RequestBody Map<String, Integer> selectedItems) throws Exception {
        return ResponseEntity.ok(orderService.createOrder(selectedItems));
    }

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<Order> payAndSaveOrder(@PathVariable("orderId") Long orderId) throws Exception {
        return ResponseEntity.ok(orderService.payAndSaveOrder(orderId));
    }

    @GetMapping("/get")
    public ResponseEntity<List<UnpaidOrder>> getAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

}
