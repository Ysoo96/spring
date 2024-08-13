package com.javateam.memberProject.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.memberProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberServiceTest2 {
	@Autowired
	// @Qualifier("") // 구현체가 여러개 있을 경우
	MemberService memberService;
	
	MemberVO memberVO;
	
	@BeforeEach
	public void setUp() {
		memberVO = MemberVO.builder()
						   .id("abcdjava1")
						   .pw("#Abcd1234")
						   .name("자바맨")
						   .gender("m")
						   .email("abcdJAVA1@abcd.com")
						   .mobile("010-1112-5454")
						   .phone("02-1111-2222")
						   .zip("08290")
						   .roadAddress("서울특별시 관악구 신림로 340")
						   .jibunAddress("서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰")
						   .detailAddress("6층 MBC 아카데미")
						   .birthday(Date.valueOf("1999-11-11"))
						   // .joindate(new Date(System.currentTimeMillis()))
						   .joindate(Date.valueOf("2024-08-05"))
						   .build();
	}

	@Test
	void testInsertMember2() {
		// assertThat(memberService.insertMember2(memberVO), equalTo(memberVO));
		MemberVO actualVO = memberService.insertMember2(memberVO);
		MemberVO expectedVO = memberVO;
		assertTrue(expectedVO.equals(actualVO));
	}

}
