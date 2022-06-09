package com.testproject.service;


import com.testproject.dto.OrderItemRequest;
import com.testproject.entity.db.Item;
import com.testproject.entity.redis.ItemTemplate;
import com.testproject.entity.redis.UnpaidOrder;
import com.testproject.exception.ItemNotFoundException;
import com.testproject.repository.ItemRepository;
import com.testproject.repository.RedisOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisOrderService {


    private final ItemRepository itemRepository;
    private final RedisOrderRepository redisOrderRepository;


    public UnpaidOrder createOrder() {
        return redisOrderRepository.save(new UnpaidOrder());
    }

    public void addItem(OrderItemRequest itemRequest) {
        Optional<UnpaidOrder> orderOptional = redisOrderRepository.findById(itemRequest.getOrderId());
        if (orderOptional.isPresent()) {
            UnpaidOrder unpaidOrder = prepareUnpaidOrder(orderOptional.get(), prepareItemTemplate(itemRequest));
            unpaidOrder.setId(itemRequest.getOrderId());
            redisOrderRepository.save(unpaidOrder);
        } else throw new ItemNotFoundException("Item not found");
    }

    private ItemTemplate prepareItemTemplate(OrderItemRequest orderItemRequest) {
        Optional<Item> itemOptional = itemRepository.findById(orderItemRequest.getItemId());
        if (itemOptional.isPresent()) {
            ItemTemplate itemTemplate = new ItemTemplate();
            itemTemplate.setId(itemOptional.get().getId());
            itemTemplate.setItemName(itemOptional.get().getName());
            itemTemplate.setPrice(itemOptional.get().getPrice());
            itemTemplate.setQuantity(orderItemRequest.getQuantity());
            return itemTemplate;
        }
        throw new ItemNotFoundException("Item not found");
    }

    private UnpaidOrder prepareUnpaidOrder(UnpaidOrder unpaidOrder, ItemTemplate itemTemplate) {
        unpaidOrder.setPrice(unpaidOrder.getPrice() + (itemTemplate.getPrice() * itemTemplate.getQuantity()));
        unpaidOrder.getItems().add(itemTemplate);
        unpaidOrder.setQuantity(countItem(unpaidOrder.getItems()));
        return unpaidOrder;
    }

    private int countItem(List<ItemTemplate> items) {
        return items.stream().mapToInt(ItemTemplate::getQuantity).sum();
    }
}
