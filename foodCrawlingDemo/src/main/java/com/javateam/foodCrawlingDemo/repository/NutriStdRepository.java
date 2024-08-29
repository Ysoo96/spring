package com.javateam.foodCrawlingDemo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.javateam.foodCrawlingDemo.domain.NutriStdVO;

public interface NutriStdRepository extends CrudRepository<NutriStdVO, Integer> {

	public Optional<NutriStdVO> findByGenderAndAge(char gender, String ageRange);
	
}
