package com.testproject.service;

import com.testproject.entity.Item;
import com.testproject.entity.ItemTemplate;
import com.testproject.entity.Order;
import com.testproject.entity.UnpaidOrder;
import com.testproject.repository.ItemRepository;
import com.testproject.repository.OrderRepository;
import com.testproject.repository.UnpaidOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    @Value("${redis.time-to-live-in-seconds}")
    private int timeToLive;

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UnpaidOrderRepository unpaidOrderRepository;


    public UnpaidOrder createOrder(Map<String, Integer> selectedItems) throws Exception {
        if(!selectedItems.isEmpty()){
            UnpaidOrder order = new UnpaidOrder();
            List<Item> items = itemRepository.findItemsByItemNameIn(selectedItems.keySet());
            for (String itemName: selectedItems.keySet()){
                for(Item item : items) {
                    if (itemName.equals(item.getItemName())) {
                        if (item.getQuantity() >= item.getQuantity()) {
                            order.setPrice(order.getPrice() + (item.getPrice() * selectedItems.get(itemName)));
                            order.getItems().add(createItemTemplate(item, selectedItems.get(itemName)));
                            break;
                        } else throw new Exception("Haven't enough items: " + itemName);
                    }
                }
            }
            order.setTtl(timeToLive);
            order.setQuantity(countItem(order.getItems()));
            return unpaidOrderRepository.save(order);
        }
        throw new Exception("failed save");
    }

    public Order payAndSaveOrder(Long orderId) throws Exception {
        Optional<UnpaidOrder> unpaidOrder = unpaidOrderRepository.findById(orderId);
        if(unpaidOrder.isPresent()){
            UnpaidOrder noOrder = unpaidOrder.get();
            Order order =  new Order();
            order.setPrice(noOrder.getPrice());
            order.setQuantity(noOrder.getQuantity());

            List<Item> items = itemRepository.findItemsByItemNameIn(order.getItems().stream().map(Item::getItemName).collect(Collectors.toSet()));
            for(ItemTemplate template: noOrder.getItems()){
                for(Item item: items){
                    if(template.getItemName().equals(item.getItemName())){
                        item.setQuantity(template.getQuantity());
                        order.getItems().add(item);
                        itemRepository.residueReduction(template.getItemName(), template.getQuantity());
                    }
                }
            }
            return orderRepository.save(order);
        }
        throw new Exception("Check out time is over");
    }

    public List<UnpaidOrder> findAll(){
        return unpaidOrderRepository.findAll();
    }


    private ItemTemplate createItemTemplate(Item item, int quantity){
        ItemTemplate itemTemplate = new ItemTemplate();
        itemTemplate.setItemName(item.getItemName());
        itemTemplate.setPrice(item.getPrice());
        itemTemplate.setQuantity(quantity);
        return itemTemplate;
    }


    private int countItem(List<ItemTemplate> items){
        return items.stream().mapToInt(ItemTemplate::getQuantity).sum();
    }

}
