package com.testproject.service;

import com.testproject.dto.OrderItemRequest;
import com.testproject.entity.db.Item;
import com.testproject.entity.redis.ItemTemplate;
import com.testproject.entity.redis.UnpaidOrder;
import com.testproject.exception.ItemNotFoundException;
import com.testproject.repository.ItemRepository;
import com.testproject.repository.RedisOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisOrderServiceTest {

    @InjectMocks
    private RedisOrderService redisOrderService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RedisOrderRepository redisOrderRepository;

    @Test
    void createOrder() {
        UnpaidOrder expected = prepareUnpaidOrder();
        when(redisOrderRepository.save(any())).thenReturn(prepareUnpaidOrder());

        assertEquals(expected, redisOrderService.createOrder());
        verify(redisOrderRepository, times(1)).save(new UnpaidOrder());
    }

    @Test
    void addItem() {
        when(redisOrderRepository.findById(any())).thenReturn(Optional.of(prepareUnpaidOrder()));
        when(redisOrderRepository.save(any())).thenReturn(prepareUnpaidOrder());
        when(itemRepository.findById(any())).thenReturn(Optional.of(prepareItem(1)));
        redisOrderService.addItem(prepareOrderItemRequest());
        verify(redisOrderRepository, times(1)).findById(1L);
        verify(redisOrderRepository, times(1)).save(prepareUnpaidOrderForVerify());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnExceptionItemNotFound() {
        when(redisOrderRepository.findById(any())).thenReturn(Optional.of(prepareUnpaidOrder()));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> redisOrderService.addItem(prepareOrderItemRequest()));
        assertNotNull(exception.getMessage());
    }



    private UnpaidOrder prepareUnpaidOrder() {
        UnpaidOrder unpaidOrder = new UnpaidOrder();
        unpaidOrder.setId(1L);
        unpaidOrder.setQuantity(5);
        unpaidOrder.setPrice(1000);
        return unpaidOrder;
    }

    private UnpaidOrder prepareUnpaidOrderForVerify() {
        UnpaidOrder unpaidOrder = new UnpaidOrder();
        unpaidOrder.setId(1L);
        unpaidOrder.setQuantity(2);
        unpaidOrder.setPrice(1000);
        unpaidOrder.setItems(List.of(prepareItemTemplate()));
        return unpaidOrder;
    }

    private ItemTemplate prepareItemTemplate() {
        ItemTemplate itemTemplate = new ItemTemplate();
        itemTemplate.setId(1L);
        itemTemplate.setQuantity(2);
        return itemTemplate;
    }

    private OrderItemRequest prepareOrderItemRequest(){
        return OrderItemRequest.builder().itemId(1L).quantity(2).orderId(1L).build();
    }

    private Item prepareItem(int quantity) {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(quantity);
        return item;
    }
}