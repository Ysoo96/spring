package com.javateam.SpringJPA2.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.SpringJPA2.domain.TestVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DAOTest {

	@Autowired
	TestRepository testRepo;

	// case-1) 레토드 수 점검
	@Test
	void test() {
		int cnt = (int) testRepo.count();
		log.info("레코드 수 : " + cnt);
		assertEquals(1, cnt);
	}

	@Test
	void test2() {
		Optional<TestVO> optionVO = testRepo.findById((long) 1);
		assertFalse(optionVO.isEmpty());

		TestVO testVO = optionVO.get();
		log.info("testVO : " + testVO); // TestVO(id=1, name=허미미, address=서울)

		assertEquals("허미미", testVO.getName());
	}

	@Test
	@Transactional
	// @Rollback(true)
	@Rollback(false)
	void testInsertUpdateDelete() {
		TestVO testVO = new TestVO();
		testVO.setName("임종훈");
		testVO.setAddress("서울");

		// 존재하지 않는 최초 레코드로 감지되면 save => insert로 해석
		TestVO resultVO = testRepo.save(testVO);
		log.info("resultVO : " + resultVO);
		assertEquals(testVO.getName(), resultVO.getName());

		// 기존값 가져오기
		testVO.setId(resultVO.getId());
		// 변경 내용 반영
		testVO.setName("임종수");
		testVO.setAddress("프랑스");

		// 존재하는 기존의 레코드로 감지되면 save => update로 해석
		TestVO resultVO2 = testRepo.save(testVO);
		log.info("resultVO2 : " + resultVO2);
		assertEquals(testVO.getAddress(), resultVO2.getAddress());

		// 삭제
		testRepo.deleteById(resultVO2.getId());
	}

}
