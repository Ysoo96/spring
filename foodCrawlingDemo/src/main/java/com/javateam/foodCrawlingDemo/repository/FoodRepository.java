package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;

import com.javateam.foodCrawlingDemo.domain.FoodVO;

public interface FoodRepository extends CrudRepository<FoodVO, Long> {

	
}