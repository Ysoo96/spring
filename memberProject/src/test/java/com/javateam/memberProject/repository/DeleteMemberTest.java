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
class DeleteMemberTest {
	
	@Autowired
	MemberDAO memberDAO;

	// case-1) 회원(abcd1111)의 롤(들) 삭제 점검
	@Transactional
	@Rollback(true)
	@Test
	void testDeleteRoles() {
		assertTrue(memberDAO.deleteRoles("abcd1111"));
	}

	// case-2) 회원정보 삭제
	// 주의) 참조 무결성을 고려하여 자녀(참고하는) 테이블을 먼저 삭제 후에 부모(참조당하는) 테이블의 레코드를 삭제해야 한다.
	@Transactional
	@Rollback(true)
	@Test
	void testDeleteMemberById() {
		assertTrue(memberDAO.deleteRoles("abcd1111"));
		assertTrue(memberDAO.deleteMemberById("abcd1111"));
	}

}
