package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;
import com.javateam.foodCrawlingDemo.domain.NutriInfoVO;

public interface NutriInfoRepository extends CrudRepository<NutriInfoVO, Integer> {

	public NutriInfoVO findByFoodName(String foodName);
	
}