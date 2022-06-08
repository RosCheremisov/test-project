package com.testproject.service;

import com.testproject.dto.ItemResponse;
import com.testproject.entity.db.Item;
import com.testproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public ItemResponse findItemByMinPrice() {
//        return repository.findItemByMinPrice().get();
        // Map with MapStruct
        return null;
    }
}
