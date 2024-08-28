package com.javateam.demo2.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.demo2.domain.MemberVO;

@SpringBootTest
class MemberDAOTest {
	
	@Autowired
	MemberDAO memberDAO;

	@Test
	void testSelectMemberById() {
		MemberVO memberVO = memberDAO.selectMemberById("abcd1111");
		assertEquals("홍길동", memberVO.getName());
	}

}
