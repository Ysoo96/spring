package com.javateam.memberProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.memberProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UpdateMemberTest {

	@Autowired
	MemberService memberService;
	
	MemberVO memberVO;
	
	// case-1) 패스워드만 변경(수정)
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMember() {
		memberVO = MemberVO.builder()
						   .id("abcd1111")
						   .pw("#Java3333")
						   .build();
	
		assertTrue(memberService.updateMember(memberVO));
	}
	
	// case-2) 존재하지 않는 회원정보를 수정하려고 할 때
	// case-2-1) 존재하지 않는 회원아이디(abcdabcd1111) => 메시징(log) 출력 여부
	// 메시지 : "수정할 회원정보가 존재하지 않습니다." => false
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberAbsent() {
		memberVO = MemberVO.builder()
						   .id("abcdabcd1111")
						   .pw("#Java3333")
						   .build();
	
		assertFalse(memberService.updateMember(memberVO));
	}
	
	// case-2) 이메일만 변경(수정)
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberByPw() {
		memberVO = MemberVO.builder()
						   .id("abcd1111")
						   .email("abcd1111@naver.com")
						   .build();
		assertTrue(memberService.updateMember(memberVO));
	}

}
