package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;
import com.javateam.foodCrawlingDemo.domain.CuNutriInfoVO;

public interface CuNutriInfoRepository extends CrudRepository<CuNutriInfoVO, Integer> {

	public CuNutriInfoVO findByFoodName(String foodName);
}