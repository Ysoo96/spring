package com.javateam.memberProject.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.memberProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class InsertMemberTest {

	@Autowired
	MemberDAO memberDAO;

	MemberVO memberVO;

	// 삽입(생성)할 회원정보 준비
	@BeforeEach
	public void setUp() {
		// howto-1)
		// memberVO = new MemberVO();
		// memberVO.setId("abcd1111");

		// howto-2)
		// memberVO = new MemberVO("abcd111", "#Abcd1234", ...(중략));

		// howto-3) lombok builder
		memberVO = MemberVO.builder()
						   .id("abcd1111")
						   .pw("#Abcd1234")
						   .name("홍길동")
						   .gender("m")
						   .email("abcd1111@abcd.com")
						   .mobile("010-1111-3333")
						   .phone("02-1111-2222")
						   .zip("08290")
						   .roadAddress("서울특별시 관악구 신림로 340")
						   .jibunAddress("서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰")
						   .detailAddress("6층 MBC 아카데미")
						   .birthday(Date.valueOf("2000-01-01"))
						   // .joindate(new Date(System.currentTimeMillis()))
						   .build();
	}

	// case-1) 주어진 회원정보(MemberVO)를 테이블에서 레코드 생성 점검
	// case-1-1) 트랜잭션을 활용 롤백(rollback) => 실제 테이블 영향 (X) => 테스트
	// case-1-2) 판정시(assertXXXX) 정확한 판정(단언) 결과값 필요
	// true/false
	@Transactional
	@Rollback(false)
	// true => 취소(rollback), false => 반영(commit)
	@Test
	void testInsertMember() {
		log.info("memberVO : {}", memberVO);
		
		// memberDAO.insertMember(memberVO);
		// int result = memberDAO.insertMember(memberVO);
		boolean result = memberDAO.insertMember(memberVO);
		// assertEquals(1, result);
		// assertThat(1, equalTo(result));
		// assertTrue(result == 1 ? true : false);
		
		assertTrue(result); 
	}

}
