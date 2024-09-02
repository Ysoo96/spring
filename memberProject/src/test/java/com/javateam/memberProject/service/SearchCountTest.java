package com.javateam.memberProject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SearchCountTest {
	
	@Autowired
	MemberService memberService;

	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-1) 검색 구분(주소) 검색어(신림)으로 검색시 기댓값(ex.20) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingAddress() {
		
		int expectedNum = 20;
		int actualNum = memberService.selectCountBySearching("address", "신림");
		assertThat(actualNum).isEqualTo(expectedNum);
	}
	
	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-2) 검색 구분(이름) 검색어(강)으로 검색시 기댓값(ex.9) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingName() {
		
		int expectedNum = 9;
		int actualNum = memberService.selectCountBySearching("name", "강");
		assertThat(actualNum).isEqualTo(expectedNum);
	}
	
	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-3) 검색 구분(이메일) 검색어(abcd1111@naver.com)으로 검색시 기댓값(ex.1) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingEmail() {
		
		int expectedNum = 1;
		int actualNum = memberService.selectCountBySearching("email", "abcd1111@naver.com");
		assertThat(actualNum).isEqualTo(expectedNum);
	}
	
	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-4) 검색 구분(롤) 검색어(관리자)으로 검색시 기댓값(ex.1) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingRole() {
		
		int expectedNum = 1;
		int actualNum = memberService.selectCountBySearching("role", "ROLE_ADMIN");
		assertThat(actualNum).isEqualTo(expectedNum);
	}
	
	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-5) 검색 구분(성별) 검색어(여)으로 검색시 기댓값(ex.54) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingGender() {
		
		int expectedNum = 54;
		int actualNum = memberService.selectCountBySearching("gender", "f");
		assertThat(actualNum).isEqualTo(expectedNum);
	}
	
	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-6) 검색 구분(아이디) 검색어(abcd1111)으로 검색시 기댓값(ex.1) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingID() {
		
		int expectedNum = 1;
		int actualNum = memberService.selectCountBySearching("id", "abcd1111");
		assertThat(actualNum).isEqualTo(expectedNum);
	}
	
	// case-1) 검색시 현재의 인원 데이터에서 특정 검색 구분에 대한 검색 레코드 수를 점검
	// case-1-7) 검색 구분(가입일) 검색어(2022-07-28)으로 검색시 기댓값(ex.1) 인지 점검
	// 단, 임의 회원정보 생성시 랜덤하게 생성되었으므로, 기댓값은 다를 수 있습니다.
	@Test
	void testSelectCountBySearchingJoindate() {
		
		int expectedNum = 1;
		int actualNum = memberService.selectCountBySearching("joindate", "2022-07-28");
		assertThat(actualNum).isEqualTo(expectedNum);
	}

}
