package com.javateam.SpringJPA2.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.javateam.SpringJPA2.domain.TestVO;

// @Repository
public interface TestRepository extends CrudRepository<TestVO, Long> {
	// 전체 검색
	List<TestVO> findAll();

	// Query Method 작성법
	// https://arahansa.github.io/docs_spring/jpa.html#jpa.query-methods
	// 이름(name)으로 동등 검색
	List<TestVO> findByName(String name);
	
	// 이름(name)으로 유사 검색
	List<TestVO> findByNameLike(String name);
}
