package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;

import com.javateam.foodCrawlingDemo.domain.NutriVO;

public interface NutriRepository extends CrudRepository<NutriVO, Integer> {

}