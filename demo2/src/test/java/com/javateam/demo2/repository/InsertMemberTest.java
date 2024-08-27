package com.javateam.demo2.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.demo2.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class InsertMemberTest {

	@Autowired
	MemberVO memberVO;
	
	MemberDAO memberDAO;
	
	@BeforeEach
	public void setup() {
		memberVO = MemberVO.builder()
						   .id("abcd1111")
						   .pw("#Abcd1111")
						   .name("홍길동")
						   .gender("m")
						   .phone("010-1234-5678")
						   .zip("08290")
						   .address("서울특별시 관악구 신림로 340")
						   .detailAddress("6층 MBC 아카데미")
						   .birthday(Date.valueOf("2000-01-01"))
						   .build();
	}
	
	@Transactional
	@Rollback(false)
	@Test
	void testInsertMember() {
		log.info("memberVO : {}", memberVO);
		
		boolean result = memberDAO.insertMember(memberVO);
		
		assertTrue(result);
	}

}
