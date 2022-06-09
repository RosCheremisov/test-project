package com.testproject.repository;

import com.testproject.entity.db.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query("SELECT item from Item item where item.price = (SELECT MIN(item.price) FROM item)")
    Optional<Item> findItemByMinPrice();

    @Query("SELECT item from Item item where item.quantity > 0")
    List<Item> findAllByQuantity();

    @Modifying
    @Query(value = "UPDATE testproject.item SET quantity = :quantity WHERE name = :name" , nativeQuery= true)
    void reduceBalance(int quantity, String name);

}
