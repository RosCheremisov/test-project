package com.testproject.service;

import com.testproject.entity.Item;
import com.testproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;


    public Item findItemByMinPrice() {
        return repository.findItemByMinPrice().get();
    }
}
