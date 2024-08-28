package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.javateam.foodCrawlingDemo.domain.CUVO;

public interface CURepository extends CrudRepository<CUVO, Long> {
	
	
}