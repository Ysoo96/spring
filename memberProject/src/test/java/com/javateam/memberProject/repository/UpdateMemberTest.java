package com.javateam.memberProject.repository;

import static org.junit.jupiter.api.Assertions.*;

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
class UpdateMemberTest {

	@Autowired
	MemberDAO memberDAO;
	
	MemberVO memberVO;
	
	@BeforeEach
	void setUp() throws Exception {
		// 기존 Mapper : 동적(dynamic) SQL 사용 전
		// memberVO = memberDAO.selectMemberById("abcd1111");
		// 수정할 데이터(필드)
		// memberVO.setPw("#Java1111");
		// memberVO.setEmail("#Spring1111");
		
		// 수정할 필드만 수정하고자 할 때
		// howto-1)
//		memberVO.setId("#abcd1111");
//		memberVO.setPw("#Java1111");
//		memberVO.setEmail("#Spring1111");
		
		// howto-2)
		memberVO = MemberVO.builder()
						   .id("abcd1111")
						   .pw("#Java1111")
						   // .email("#Spring1111")
						   .build();
	}

	// case-1) 패스워드만 변경(수정)할 경우
	// case-1-1) 기존 VO 패스워드와 수정 패스워드 동등 비교
	@Transactional
	@Rollback(false)
	@Test
	void testUpdateMemberByPw() {
		memberVO = MemberVO.builder()
						   .id("abcd1111")
						   .pw("#Java1111")
						   .build();
		
		// log.info("MemberV0 : " + memberVO);
		boolean result = memberDAO.updateMember(memberVO);
		assertTrue(result);
		
		// case-1-1) 기존 VO 패스워드와 수정 패스워드 동등 비교
		String pw2 = memberDAO.selectMemberById(memberVO.getId()).getPw();
		assertEquals("#Java2222", pw2);
	}
	
	// case-2) 이메일만 변경(수정)할 경우
	// case-2-1) 기존 VO 이메일과 수정 이메일 동등 비교
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberByEmail() {
		memberVO = MemberVO.builder()
							.id("abcd1111")
							.email("JavaSpring@naver.com")
							.build();
			
		boolean result = memberDAO.updateMember(memberVO);
		assertTrue(result);
			
		// case-2-1) 기존 VO 이메일과 수정 이메일 동등 비교
		String email2 = memberDAO.selectMemberById(memberVO.getId()).getEmail();
		assertEquals("JavaSpring@naver.com", email2);
	}
	
	// case-3) 휴대폰/일반전화 변경(수정)할 경우
	// case-3-1) 기존 VO 휴대폰/일반전화와 수정 휴대폰/일반전화 동등 비교
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberByMobileAndPhone() {
		memberVO = MemberVO.builder()
							.id("abcd1111")
							.mobile("010-5656-7878")
							.phone("02-6555-8282")
							.build();
				
		boolean result = memberDAO.updateMember(memberVO);
		assertTrue(result);
				
		// case-3-1) 기존 VO 휴대폰/일반전화와 수정 휴대폰/일반전화 동등 비교
		MemberVO memberVO2 = memberDAO.selectMemberById(memberVO.getId());
		String mobile2 = memberVO2.getMobile();
		String phone2 = memberVO2.getPhone();
		assertEquals("010-5656-7878", mobile2);
		assertEquals("02-6555-8282", phone2);
	}
}
