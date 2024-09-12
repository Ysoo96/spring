package com.javateam.memberProject.service;

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
class MemberServiceTest1 {
	@Autowired
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
						   .build();
	}

	@Transactional
	@Rollback(true)
	@Test
	void testInsertMember() {
		assertTrue(memberService.insertMember(memberVO));
	}

}
