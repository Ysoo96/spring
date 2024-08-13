package com.javateam.memberProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeleteMemberTest {

	@Autowired
	MemberService memberService;

	// case-1) 존재하는 회원정보를 삭제하려고 할 때
	// case-1-1) 존재하는 회원정보(abcd1111) => true
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	@Rollback(true)
	@Test
	void testDeleteMember() {
		assertTrue(memberService.deleteMember("abcd1111"));
	}

	// case-2) 존재하지 않는 회원정보를 삭제하려고 할 때
	// case-2-1) 존재하지 않는 회원정보(abcdabcd1111) => 메시징(log) 출력 여부
	// 메시지 : "삭제할 회원정보가 존재하지 않습니다." => false
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	@Rollback(true)
	@Test
	void testDeleteMemberAbsent() {
		assertFalse(memberService.deleteMember("abcdabcd1111"));
	}

}
