package com.javateam.SpringJPA2.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.SpringJPA2.domain.TestVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SelectTest {

	@Autowired
	TestRepository testRepo;
	
	List<TestVO> list;
	
	@BeforeEach
	public void setUp() {
		list = testRepo.findByName("오상욱");
	}
	
	// case-1) 이름으로 검색 메소드 검색 : findByName
	// case-1-1) Null 점검
	// case-1-2) 리스트 크기(길이) 점검
	// case-1-3) 전체/특정값(ex. id, address, age 등) 점검
	@Test
	void test() {	
		// case-1-1) Null 점검
		assertNotNull(list);
	}
	
	// case-1-2) 리스트 크기(길이) 점검
	@Test
	void testSize() {
		assertThat(1, equalTo(list.size()));
	}
	
	// case-1-3) 특정값(ex. id, address, age 등) 점검
	@Test
	void testTestVO() {
		TestVO exptectedTestVO = TestVO.builder()
								       .id(1)
								       .name("오상욱")
								       .address("대전")
								       .age(27)
								       .build();
		
		TestVO actualTestVO = list.get(0);
		
		log.info("exp hash : " + exptectedTestVO.hashCode());
		log.info("act hash : " + actualTestVO.hashCode());
		
		// 한꺼번에 다수의 필드들을 비교
		assertTrue(exptectedTestVO.equals(actualTestVO));
		
		// assertThat(exptectedTestVO, equalTo(actualTestVO));
	}

}
