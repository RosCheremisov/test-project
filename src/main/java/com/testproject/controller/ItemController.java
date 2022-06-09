package com.testproject.controller;

import com.testproject.dto.ItemResponse;
import com.testproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/min")
    public List<ItemResponse> fetchItemByMinPrice(){
        return itemService.findItemByMinPrice();
    }


}
