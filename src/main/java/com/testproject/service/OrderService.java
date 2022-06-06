package com.testproject.service;

import com.testproject.entity.Item;
import com.testproject.entity.Order;
import com.testproject.repository.ItemRepository;
import com.testproject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;


    public Order createOrder(List<String> itemNames) throws Exception {
        if(!itemNames.isEmpty()){
            Order order = new Order();
            List<Item> itemsForOrder = new ArrayList<>();
            List<Item> itemsWithDB = itemRepository.findItemsByItemNameIn(itemNames);
            for (String itemName: itemNames){
                for(Item item : itemsWithDB){
                    if(itemName.equals(item.getItemName())){
                        order.setPrice(order.getPrice() + item.getPrice());
                        itemsForOrder.add(item);
                        break;
                    }
                }
            }
            order.setQuantity(itemsForOrder.size());
            order.setItems(itemsForOrder);
            return orderRepository.save(order);
        }
        throw new Exception("failed save");
    }

}
