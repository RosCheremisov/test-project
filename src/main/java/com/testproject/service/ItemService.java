package com.testproject.service;

import com.testproject.dto.ItemResponse;
import com.testproject.entity.db.Item;
import com.testproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public List<ItemResponse> findItemByMinPrice() {
        Optional<Item> optionalItem = repository.findItemByMinPrice();
        if (optionalItem.isPresent() && optionalItem.get().getQuantity() > 0) {
            return List.of(convertItemResponse(optionalItem.get()));
        }
        log.info("Not enough items. Other items in storage: ");
        return repository.findAllByQuantity().stream().map(this::convertItemResponse).collect(Collectors.toList());
    }


    private ItemResponse convertItemResponse(Item item) {
       return ItemResponse.builder().id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}
