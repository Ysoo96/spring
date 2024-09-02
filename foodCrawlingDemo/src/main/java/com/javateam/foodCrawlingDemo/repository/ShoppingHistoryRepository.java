package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;
import com.javateam.foodCrawlingDemo.domain.ShoppingHistoryVO;

public interface ShoppingHistoryRepository extends CrudRepository<ShoppingHistoryVO, Integer> {

}