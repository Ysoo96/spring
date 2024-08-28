package com.javateam.foodCrawlingDemo.repository;

import org.springframework.data.repository.CrudRepository;

import com.javateam.foodCrawlingDemo.domain.InfiniteFoodVO;

public interface InfiniteFoodRepository extends CrudRepository<InfiniteFoodVO, Integer>  {

}
