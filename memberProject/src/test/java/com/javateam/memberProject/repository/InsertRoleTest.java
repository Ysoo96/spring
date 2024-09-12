package com.javateam.memberProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class InsertRoleTest {
	@Autowired
	MemberDAO memberDAO;

	// case-1) 존재하는 id(abcd1111)에 대한 관리자 롤(ROLE_ADMIN)을 할당
	@Transactional
	@Rollback(true)
	@Test
	void testInsertRole() {
		assertTrue(memberDAO.insertRole("abcd1111", "ROLE_ADMIN"));
	}

	// case-2) 존재하지 않는 id(abcd3333)에 대한 관리자 롤(ROLE_USER)을 할당
	// 기댓값 => false
	@Transactional
	@Rollback(true)
	@Test
	void testInsertRole2() {
		assertFalse(memberDAO.insertRole("abcd3333", "ROLE_USER"));
	}

}
