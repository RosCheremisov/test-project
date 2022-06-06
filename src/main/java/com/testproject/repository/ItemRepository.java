package com.testproject.repository;

import com.testproject.entity.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {


    @Query("SELECT item from Item item where item.price =(SELECT MAX(item.price) FROM item)")
    Optional<Item> findItemByMinPrice();

    List<Item> findItemsByItemNameIn(List<String> itemNames);

}
