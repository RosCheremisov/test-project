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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private RedisOrderRepository redisOrderRepository;

    @Test
    void payAndSave() {
        Order expected = prepareOrder();
        when(redisOrderRepository.findById(anyLong())).thenReturn(Optional.of(prepareUnpaidOrder()));
        doNothing().when(itemRepository).reduceBalance(anyInt(), anyString());
        when(itemRepository.findAll()).thenReturn(List.of(prepareItem(10)));
        when(orderRepository.save(any())).thenReturn(prepareOrder());
        when(itemRepository.findById(anyLong())).thenReturn((Optional.of(prepareItem(10))));

        assertEquals(expected, orderService.payAndSave(1L));
        verify(redisOrderRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findAll();
        verify(orderRepository, times(1)).save(prepareOrder());
        verify(itemRepository, times(1)).findById(1L);

    }

    @Test
    void shouldReturnExceptionHaveNotItems() {
        when(redisOrderRepository.findById(anyLong())).thenReturn(Optional.of(prepareUnpaidOrder()));
        when(itemRepository.findAll()).thenReturn(List.of(prepareItem(1)));
        TestProjectException exception = assertThrows(TestProjectException.class, () -> orderService.payAndSave(1L));
        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldReturnExceptionOrderNotFound() {
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.payAndSave(null));
        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldReturnExceptionItemNotFound() {
        when(redisOrderRepository.findById(anyLong())).thenReturn(Optional.of(prepareUnpaidOrder()));
        when(itemRepository.findAll()).thenReturn(List.of(prepareItem(10)));
        when(itemRepository.findById(anyLong())).thenReturn((Optional.empty()));

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> orderService.payAndSave(1L));
        assertNotNull(exception.getMessage());
    }

    private UnpaidOrder prepareUnpaidOrder() {
        UnpaidOrder unpaidOrder = new UnpaidOrder();
        unpaidOrder.setId(1L);
        unpaidOrder.setQuantity(1);
        unpaidOrder.setPrice(1000);
        unpaidOrder.setItems(List.of(prepareItemTemplate()));
        return unpaidOrder;
    }

    private Order prepareOrder() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setName("phone");
        item.setPrice(1000);
        item.setQuantity(1);
        order.setQuantity(1);
        order.setPrice(1000);
        order.setItems(List.of(item));
        return order;
    }

    private ItemTemplate prepareItemTemplate() {
        ItemTemplate itemTemplate = new ItemTemplate();
        itemTemplate.setId(1L);
        itemTemplate.setItemName("phone");
        itemTemplate.setPrice(1000);
        itemTemplate.setQuantity(1);
        return itemTemplate;
    }

    private Item prepareItem(int quantity) {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(quantity);
        return item;
    }
}