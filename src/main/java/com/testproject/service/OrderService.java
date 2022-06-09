package com.testproject.service;

import com.testproject.entity.db.Item;
import com.testproject.entity.db.Order;
import com.testproject.entity.db.OrderItem;
import com.testproject.entity.redis.ItemTemplate;
import com.testproject.entity.redis.UnpaidOrder;
import com.testproject.exception.ItemNotFoundException;
import com.testproject.exception.OrderNotFoundException;
import com.testproject.exception.TestProjectException;
import com.testproject.repository.ItemRepository;
import com.testproject.repository.OrderRepository;
import com.testproject.repository.RedisOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final RedisOrderRepository redisOrderRepository;


    @Transactional
    public Order payAndSave(Long orderId) {
        Optional<UnpaidOrder> orderOptional = redisOrderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            for (ItemTemplate item : orderOptional.get().getItems()) {
                if (!isPositiveBalance(item)) {
                    log.error("Item with ID : " + item.getId() + " haven't enough quantity");
                    throw new TestProjectException("Haven't enough quantity");
                }
                itemRepository.reduceBalance(calculateQuantity(item), item.getItemName());
            }
            return orderRepository.save(prepareOrder(orderOptional.get()));
        }
        log.error("Order not found or storage time is over");
        throw new OrderNotFoundException("Order not found or storage time is over");
    }

    private boolean isPositiveBalance(ItemTemplate itemTemplate) {
        List<Item> items = (List<Item>) itemRepository.findAll();
        return items.stream().filter(i -> i.getId().equals(itemTemplate.getId()))
                .anyMatch(i -> i.getQuantity() >= itemTemplate.getQuantity());
    }

    private Order prepareOrder(UnpaidOrder unpaidOrder) {
        Order order = new Order();
        order.setPrice(unpaidOrder.getPrice());
        order.setQuantity(unpaidOrder.getQuantity());
        order.setItems(unpaidOrder.getItems().stream().map(this::convertItem).collect(Collectors.toList()));
        return order;
    }

    private OrderItem convertItem(ItemTemplate itemTemplate) {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(itemTemplate.getItemName());
        orderItem.setPrice(itemTemplate.getPrice());
        orderItem.setQuantity(itemTemplate.getQuantity());
        return orderItem;
    }

    private int calculateQuantity(ItemTemplate itemTemplate) {
        Optional<Item> item = itemRepository.findById(itemTemplate.getId());
        if (item.isPresent()) {
            return item.get().getQuantity() - itemTemplate.getQuantity();
        }
        log.error("Item not found");
        throw new ItemNotFoundException("Item not found");
    }
}

