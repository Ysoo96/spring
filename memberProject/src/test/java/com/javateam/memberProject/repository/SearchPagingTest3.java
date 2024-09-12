package com.javateam.memberProject.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SearchPagingTest3 {

	@Autowired
	MemberDAO memberDAO;

	String searchKey;
	String searchWord;
	String isLikeOrEquals;
	int page;
	int limit;
	String ordering;

	List<Map<String, Object>> members;

	// case-3) 성별 검색 : "m/f" 이라는 검색어로 회원정보 검색
	// case-3-1) Null 여부 점검
	// case-3-2) 페이지당 레코드 길이 => 첫 페이지, 마지막 페이지
	// case-3-3) ID 정렬시(오름차순) => 처음/마지막 레코드 이름 비교
	// case-3-4) ID 정렬시(내림차순) => 처음/마지막 레코드 이름 비교
	@Test
	void testSelectMembersBySearchingAndPaging() {
		searchKey = "GENDER";
		searchWord = "m"; // 남성
		isLikeOrEquals = "equals";
		page = 1;
		limit = 10;
		ordering = "ASC";

		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		// case-3-1) Null 여부 점검
		assertNotNull(members);

		// case-3-2) 페이지당 레코드 길이 => 첫 페이지, 마지막 페이지
		assertEquals(10, members.size());

		page = 5; // 마지막 페이지
		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		assertEquals(7, members.size());
	}

	// case-3-3) ID 정렬시(오름차순) => 처음/마지막 레코드 이름 비교
	@Test
	void testSelectMembersBySearchingAndPagingAsc() {
		searchKey = "GENDER";
		searchWord = "m";
		isLikeOrEquals = "equals";
		page = 5; // 5페이지(마지막 페이지)
		limit = 10;
		ordering = "ASC";

		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		// 5페이지의 처음/마지막 레코드 이름 : 선우구준, 양순연
		assertThat("선우구준", equalTo(members.get(0).get("NAME")));
		assertThat("양순연", equalTo(members.get(6).get("NAME")));
	}

	// case-3-4) ID 정렬시(내림차순) => 처음/마지막 레코드 이름 비교
	@Test
	void testSelectMembersBySearchingAndPagingDesc() {
		searchKey = "GENDER";
		searchWord = "m";
		isLikeOrEquals = "equals";
		page = 1; // 1페이지(첫 페이지)
		limit = 10;
		ordering = "DESC";
		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		// 1페이지(첫 페이지)의 첫/마지막 레코드 이름 : 양순연, 구구준
		assertThat("양순연", equalTo(members.get(0).get("NAME")));
		assertThat("구구준", equalTo(members.get(9).get("NAME")));
	}

}
