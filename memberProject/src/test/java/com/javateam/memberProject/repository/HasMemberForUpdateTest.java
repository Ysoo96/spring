package com.javateam.memberProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class HasMemberForUpdateTest {

	@Autowired
	MemberDAO memberDAO;

	// case-1) 이메일 중복 점검
	// case-1-1) 회원 기존 메일로 재사용 점검
	@Test
	void testHasMemberForUpdateE() {
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "EMAIL", "abcd1111@abcd.com"));
	}

	// case-1-2) 신규 메일 사용 => 다른 회원과 중복되는 메일 사용 점검
	@Test
	void testHasMemberForUpdateE2() {
		assertTrue(memberDAO.hasMemberForUpdate("abcd1111", "EMAIL", "mbc_23@abcd.com"));
	}

	// case-1-3) 신규 메일 사용 => 다른 회원과 중복되지 않는 메일 사용 점검
	@Test
	void testHasMemberForUpdateE3() {
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "EMAIL", "mbcmbc@abcd.com"));
	}
	
	// case-2) 휴대폰 중복 점검
	// case-2-1) 회원 기존 휴대폰 재사용 점검
	@Test
	void testHasMemberForUpdateM() {
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "MOBILE", "010-1111-3333"));
	}

	// case-2-2) 신규 휴대폰 사용 => 다른 회원과 중복되는 휴대폰 사용 점검
	@Test
	void testHasMemberForUpdateM2() {
		assertTrue(memberDAO.hasMemberForUpdate("abcd1111", "MOBILE", "010-1234-1023"));
	}

	// case-2-3) 신규 휴대폰 사용 => 다른 회원과 중복되지 않는 휴대폰 사용 점검
	@Test
	void testHasMemberForUpdatem3() {
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "MOBILE", "010-2222-3434"));
	}
}
