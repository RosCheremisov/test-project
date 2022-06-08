package com.testproject.service;

import com.testproject.dto.OrderItemRequest;
import com.testproject.dto.OrderResponse;
import com.testproject.entity.db.OrderItem;
import com.testproject.entity.redis.ItemTemplate;
import com.testproject.entity.db.Item;
import com.testproject.entity.db.Order;
import com.testproject.entity.redis.UnpaidOrder;
import com.testproject.repository.ItemRepository;
import com.testproject.repository.OrderRepository;
import com.testproject.repository.RedisOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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



    public UnpaidOrder createOrder() {
        return redisOrderRepository.save(new UnpaidOrder());
    }

    public void addItem(OrderItemRequest itemRequest) throws Exception {
        Optional<UnpaidOrder> orderOptional = redisOrderRepository.findById(itemRequest.getOrderId());
        if(orderOptional.isPresent()){
            UnpaidOrder unpaidOrder = prepareUnpaidOrder(orderOptional.get(), prepareItemTemplate(itemRequest));
            unpaidOrder.setId(itemRequest.getOrderId());
            redisOrderRepository.save(unpaidOrder);
        }else throw new Exception("Exception");
    }

    @Transactional
    public Order payAndSave(Long orderId) throws Exception {
        Optional<UnpaidOrder> orderOptional = redisOrderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            for(ItemTemplate item: orderOptional.get().getItems()){
                if(isPositiveBalance(item)){
                    itemRepository.reduceBalance(calculateQuantity(item), item.getItemName());
                }else throw new Exception("Haven't enough item");
            }
            return orderRepository.save(prepareOrder(orderOptional.get()));
        }
        else throw new Exception("Exception");
    }

    private boolean isPositiveBalance(ItemTemplate itemTemplate){
        List<Item> items = (List<Item>) itemRepository.findAll();
        return items.stream().filter(i -> i.getId().equals(itemTemplate.getId()))
                .anyMatch(i -> i.getQuantity() >= itemTemplate.getQuantity());
    }

    private UnpaidOrder prepareUnpaidOrder(UnpaidOrder unpaidOrder, ItemTemplate itemTemplate){
        unpaidOrder.setPrice(unpaidOrder.getPrice() + (itemTemplate.getPrice() * itemTemplate.getQuantity()));
        unpaidOrder.getItems().add(itemTemplate);
        unpaidOrder.setQuantity(countItem(unpaidOrder.getItems()));
        return unpaidOrder;
    }

    private Order prepareOrder(UnpaidOrder unpaidOrder){
        Order order = new Order();
        order.setPrice(unpaidOrder.getPrice());
        order.setQuantity(unpaidOrder.getQuantity());
        order.setItems(unpaidOrder.getItems().stream().map(this::convertItem).collect(Collectors.toList()));
        return order;
    }


    private ItemTemplate prepareItemTemplate(OrderItemRequest orderItemRequest) throws Exception {
        Optional<Item> itemOptional = itemRepository.findById(orderItemRequest.getItemId());
        if (itemOptional.isPresent()){
            ItemTemplate itemTemplate = new ItemTemplate();
            itemTemplate.setId(itemOptional.get().getId());
            itemTemplate.setItemName(itemOptional.get().getName());
            itemTemplate.setPrice(itemOptional.get().getPrice());
            itemTemplate.setQuantity(orderItemRequest.getQuantity());
            return itemTemplate;
        }else throw new Exception("exception");
    }

    private OrderItem convertItem(ItemTemplate itemTemplate){
        OrderItem orderItem = new OrderItem();
        orderItem.setName(itemTemplate.getItemName());
        orderItem.setPrice(itemTemplate.getPrice());
        orderItem.setQuantity(itemTemplate.getQuantity());
        return orderItem;
    }

    private int countItem(List<ItemTemplate> items) {
        return items.stream().mapToInt(ItemTemplate::getQuantity).sum();
    }

    private int calculateQuantity(ItemTemplate itemTemplate) throws Exception {
        Optional<Item> item = itemRepository.findById(itemTemplate.getId());
        if (item.isPresent()){
            return item.get().getQuantity() - itemTemplate.getQuantity();
        }else throw new Exception("Exception");
    }

    public UnpaidOrder getOrderById(Long orderId){
        return redisOrderRepository.findById(orderId).get();
    }

}
