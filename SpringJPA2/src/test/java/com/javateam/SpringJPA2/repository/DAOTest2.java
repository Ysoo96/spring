package com.javateam.SpringJPA2.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.SpringJPA2.domain.TestVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DAOTest2 {

	@Autowired
	TestRepository testRepo;

	// case-1) 4명의 레코드를 삽입(샘플 데이터 입력)
	@Transactional
	@Rollback(false)
	//@Rollback(true)
	@Test
	void testInsert() {
		TestVO testVO;
		List<TestVO> list = new ArrayList<>();
		testVO = TestVO.builder()
		               .name("오상욱")
		               .address("대전")
		               .age(27)
		               .build();
		list.add(testVO);
		
		testVO = TestVO.builder()
		               .name("구본길")
		               .address("서울")
		               .age(35)
		               .build();
		list.add(testVO);
		
		testVO = TestVO.builder()
		               .name("박상원")
		               .address("대전")
		               .age(23)
		               .build();
		list.add(testVO);
		
		testVO = TestVO.builder()
		               .name("도경동")
		               .address("문경")
		               .age(24)
		               .build();
		list.add(testVO);
		
		List<TestVO> resultList = (List<TestVO>) testRepo.saveAll(list);
		
		assertEquals(4, resultList.size());
	}
	
	// case-2) 전체 인원 조회
	// case-2-1) 조회 결과 존재 여부(Null 여부) 점검
	// case-2-2) 조회수(4명) 점검
	// case-2-3) 첫/마지막 레코드의 특정값으로 점검
	@Test
	void testFindAll() {
		
		List<TestVO> list = testRepo.findAll();
		
		// case-2-1) 조회 결과 존재 여부(Null 여부) 점검
		assertNotNull(list);
		
		// case-2-2) 조회수(4명) 점검
		// assertEquals(4, list.size());
		assertThat(4, equalTo(list.size()));
		
		// 참고) assertJ Style
		// assertThat(4).isEqualTo(list.size());
		
		// case-2-3) 첫/마지막 레코드의 특정값으로 점검
		// 첫 레코드 : 기댓값(expected) : 오상욱
		String actual = list.get(0).getName();
		assertThat("오상욱", equalTo(actual));
		
		// 마지막 레코드 : 기댓값(expected) : "동경동"		
		actual = list.get(3).getName();
		assertThat("도경동", equalTo(actual));
	}

}
