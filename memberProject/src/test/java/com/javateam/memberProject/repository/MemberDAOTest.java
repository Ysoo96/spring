package com.javateam.memberProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.memberProject.domain.MemberVO;

@SpringBootTest
class MemberDAOTest {
	@Autowired
	MemberDAO memberDAO;

	// 개별 회원 정보 조회
	// case) id(mbc_1065) 인 회원정보의 이름이 "고민재"인지 점검
	@Test
	void testSelectMemberById() {
		MemberVO memberVO = memberDAO.selectMemberById("mbc_1065");
		assertEquals("고민재", memberVO.getName());
		// assertNotEquals("고민재", memberVO.getName());
	}

	// 페이징에 의한 회원정보 조회
	// case-1) 레코드의 길이 = 10
	// case-2) page=1 에서 첫 인원의 이름(고민재), 마지막 인원의 이름(임원민)으로 검수
	@Test
	void selectMembersByPaging() {
		// case-1)
		List<MemberVO> members = memberDAO.selectMembersByPaging(1, 10);
		assertEquals(10, members.size());

		// case-2)
		assertEquals("고민재", members.get(0).getName());
		assertEquals("임원민", members.get(9).getName());
	}

}
