package com.testproject.service;

import com.testproject.dto.ItemResponse;
import com.testproject.entity.db.Item;
import com.testproject.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
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
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void findItemByMinPrice() {
        List<ItemResponse> expected = List.of(prepareItemResponse());
        when(itemRepository.findItemByMinPrice()).thenReturn(Optional.of(prepareItem()));
        assertArrayEquals(expected.toArray(), itemService.findItemByMinPrice().toArray());
        verify(itemRepository, times(1)).findItemByMinPrice();
    }

    @Test
    void findItemByMinPriceReturnOtherItems() {
        List<ItemResponse> expected = List.of(prepareItemResponse());
        when(itemRepository.findItemByMinPrice()).thenReturn(Optional.empty());
        when(itemRepository.findAllByQuantity()).thenReturn(List.of(prepareItem()));
        assertArrayEquals(expected.toArray(), itemService.findItemByMinPrice().toArray());
        verify(itemRepository,times(1)).findItemByMinPrice();
        verify(itemRepository, times(1)).findAllByQuantity();
    }

    private Item prepareItem() {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(10);
        item.setPrice(1000);
        return item;
    }

    private ItemResponse prepareItemResponse() {
        return ItemResponse.builder().id(1L)
                .price(1000)
                .quantity(10)
                .build();
    }
}