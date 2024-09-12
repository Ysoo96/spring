package com.javateam.memberProject.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.memberProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberDAOTest2 {

	@Autowired
	MemberDAO memberDAO;
	List<MemberVO> members;

	// 공통 코드 => 선처리(before)
	@BeforeEach
	public void setUp() {
		members = memberDAO.selectAllMembers();

	}

	// case-1) 데이터 유무(null) 점검
	// case-2) 총 데이터(레코드)의 갯수(인원수)
	// ex) SELECT count(*) FROM MEMBER_TBL;
	// case-3) 맨 처음/끝 레코드의 이름(데이터) 점검
	@Test
	void testSelectAllMembers() {
		// assertNull(members);
		assertNotNull(members);
	}

	// case-2) 총 데이터(레코드)의 갯수(인원수)
	// ex) SELECT count(*) FROM MEMBER_TBL;
	// 기댓값 : 100
	@Test
	void testCountSelectAllMembers() {
		// assertEquals(100, members.size());
		assertThat(100, equalTo(members.size()));
	}

	// case-3) 맨 처음/끝 레코드의 이름(데이터) 점검
	/*
	 * 첫번째 회원 조회
	SELECT *
	FROM (SELECT M.*, rownum rowNo
	      FROM (SELECT *
	            FROM MEMBER_TBL
	            ) M
	     )
	WHERE rowNo = 1;

	마지막 회원 조회
	SELECT *
	FROM (SELECT M.*, rownum rowNo
	      FROM (SELECT *
	            FROM MEMBER_TBL
	            ) M
	     )
	WHERE rowNo = 100;
	*/
	@Test
	void testFirstMembers() {
		// 첫번째 회원 조회
		// 고민재
		// assertEquals("고민재", members.get(0).getName());
		assertThat("고민재", equalTo(members.get(0).getName()));
	}
	
	@Test
	void testLastMembers() {
		// 마지막 회원 조회
		// 양순연
		assertEquals("양순연", members.get(99).getName());
		// assertThat("양순연", equalTo(members.get(99).getName()));
	}

}
