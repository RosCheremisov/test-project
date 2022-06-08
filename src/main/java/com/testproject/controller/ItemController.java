package com.testproject.controller;

import com.testproject.dto.ItemResponse;
import com.testproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/min")
    public ItemResponse fetchItemByMinPrice(){
        return itemService.findItemByMinPrice();
    }


}
