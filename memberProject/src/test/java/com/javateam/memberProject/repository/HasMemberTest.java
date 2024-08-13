package com.javateam.memberProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class HasMemberTest {

	@Autowired
	MemberDAO memberDAO;

	// case-1) 아이디 중복 점검
	// case-1-1) 중복되지 않는 아이디 입력/점검
	// @Transactional(readOnly = true)
	@Test
	void testHasMemberByFld() {
		// log.info("결과 : " + memberDAO.hasMemberByFld("ID", "javaSpring"));
		assertFalse(memberDAO.hasMemberByFld("ID", "javaSpring"));
		// assertTrue(memberDAO.hasMemberByFld("ID", "abcd1111"));
	}

	// case-1-2) 중복되는 아이디 입력/점검
	@Test
	void testHasMemberByFld2() {
		assertTrue(memberDAO.hasMemberByFld("ID", "abcd1111"));
	}

	// case-2) 이메일 중복 점검
	// case-2-1) 중복되지 않는 이메일 입력/점검
	@Test
	void testHasMemberByFldEmail() {
		assertFalse(memberDAO.hasMemberByFld("EMAIL", "javaspring@naver.com"));
	}

	// case-2-2) 중복되는 이메일 입력/점검
	@Test
	void testHasMemberByFldEmail2() {
		assertTrue(memberDAO.hasMemberByFld("EMAIL", "abcd1111@abcd.com"));
	}

	// case-3) 휴대폰 중복 점검
	// case-3-1) 중복되지 않는 휴대폰 입력/점검
	@Test
	void testHasMemberByFldMobile() {
		assertFalse(memberDAO.hasMemberByFld("MOBILE", "010-2222-3434"));
	}

	// case-3-2) 중복되는 휴대폰 입력/점검
	@Test
	void testHasMemberByFldMobile2() {
		assertTrue(memberDAO.hasMemberByFld("MOBILE", "010-1234-1023"));
	}
}
