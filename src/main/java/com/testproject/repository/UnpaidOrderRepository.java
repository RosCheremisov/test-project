package com.testproject.repository;

import com.testproject.entity.UnpaidOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnpaidOrderRepository extends CrudRepository<UnpaidOrder, Long> {

    List<UnpaidOrder> findAll();

}
