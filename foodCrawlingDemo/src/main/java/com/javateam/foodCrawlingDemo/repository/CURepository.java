package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;
import com.javateam.foodCrawlingDemo.domain.CUVO;

public interface CURepository extends CrudRepository<CUVO, Integer> {
	
	
}