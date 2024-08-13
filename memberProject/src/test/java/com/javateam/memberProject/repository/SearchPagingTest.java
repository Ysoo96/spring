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
class SearchPagingTest {

	@Autowired
	MemberDAO memberDAO;

	String searchKey;
	String searchWord;
	String isLikeOrEquals;
	int page;
	int limit;
	String ordering;

	List<Map<String, Object>> members;

	// case-1) 주소 검색 : "신림" 이라는 검색어로 회원정보 검색
	// case-1-1) Null 여부 점검
	// case-1-2) 페이지당 레코드 길이 => 첫 페이지, 마지막 페이지
	// case-1-3) ID 정렬시(오름차순) => 처음/마지막 레코드 이름 비교
	// case-1-4) ID 정렬시(내림차순) => 처음/마지막 레코드 이름 비교
	@Test
	void testSelectMembersBySearchingAndPaging() {
		searchKey = "ADDRESS";
		searchWord = "신림";
		isLikeOrEquals = "like";
		page = 1;
		limit = 10;
		ordering = "ASC";

		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		// case-1-1) Null 여부 점검
		assertNotNull(members);

		// case-1-2) 페이지당 레코드 길이 => 첫 페이지, 마지막 페이지
		assertEquals(10, members.size());

		page = 2; // 마지막 페이지
		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		assertEquals(8, members.size());
	}

	// case-1-3) ID 정렬시(오름차순) => 처음/마지막 레코드 이름 비교
	@Test
	void testSelectMembersBySearchingAndPagingAsc() {
		searchKey = "ADDRESS";
		searchWord = "신림";
		isLikeOrEquals = "like";
		page = 2; // 2페이지(마지막 페이지)
		limit = 10;
		ordering = "ASC";

		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		// 2페이지의 처음/마지막 레코드 이름 : 한나준, 신갑섭
		assertThat("한나준", equalTo(members.get(0).get("NAME")));
		assertThat("신갑섭", equalTo(members.get(7).get("NAME")));
	}

	// case-1-4) ID 정렬시(내림차순) => 처음/마지막 레코드 이름 비교
	@Test
	void testSelectMembersBySearchingAndPagingDesc() {
		searchKey = "ADDRESS";
		searchWord = "신림";
		isLikeOrEquals = "like";
		page = 1; // 1페이지(첫 페이지)
		limit = 10;
		ordering = "DESC";
		members = memberDAO.selectMembersBySearchingAndPaging(searchKey, searchWord, page, limit, isLikeOrEquals,
				ordering);

		// 1페이지(첫 페이지)의 첫/마지막 레코드 이름 : 신갑섭, 성희영
		assertThat("신갑섭", equalTo(members.get(0).get("NAME")));
		assertThat("성희영", equalTo(members.get(9).get("NAME")));
	}

}
