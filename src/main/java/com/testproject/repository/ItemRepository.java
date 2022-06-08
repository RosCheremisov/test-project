package com.testproject.repository;

import com.testproject.entity.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {


    @Query("SELECT item from Item item where item.price =(SELECT MAX(item.price) FROM item)")
    Optional<Item> findItemByMinPrice();

    @Query("UPDATE Item item SET item.quantity = item.quantity - :quantity WHERE item.itemName = :itemName")
    void residueReduction(String itemName, int quantity);

    List<Item> findItemsByItemNameIn(Set<String> itemNames);

}
