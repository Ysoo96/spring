package com.javateam.memberProject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MemberJoinControllerTest {
	// context 객체(wac)
	@Autowired
	WebApplicationContext ctx;
	
	// 목(mock) 객체
	@Autowired
	MockMvc mockMvc;
	
	// 서비스 객체 ex) @AfterEach : 회원정보 삭제
	@Autowired
	MemberService memberService;
	
	// VO
	MemberVO memberVO;

	// 목(mock) 객체 생성, 회원정보 준비
	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		
		memberVO = MemberVO.builder()
						   .id("springjava1111")
						   .pw("#Abcd1234")
						   .name("홍길동")
						   .gender("m")
						   .email("springjava1111@abcd.com")
						   .mobile("010-8989-7878")
						   .phone("02-1111-2222")
						   .zip("08290")
						   .roadAddress("서울특별시 관악구 신림로 340")
						   .jibunAddress("서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰")
						   .detailAddress("6층 MBC 아카데미")
						   .birthday(Date.valueOf("2000-01-01"))
						   .build();
	}

	// 후처리 : 테스트 시 가입된 회원삭제
	@AfterEach
	void tearDown() throws Exception {
		memberService.deleteMember("springjava1111");
	}

	// case-1) URL/Http 메소드 => 인자(준비된 회원정보) => 인자 JSON화(일괄처리)
	// => 연결 여부(Http 상태 코드 : 200)
	// => 성공 여부(메시징) 점검 => 이동될 html(thymeleaf) 점검
	@Test
	void testJoinProc() {
		ObjectMapper om = new ObjectMapper();
		String json = "";
		try {
			json = om.writeValueAsString(memberVO);
		} catch (JsonProcessingException e1) {
			log.error("json 에러 : " + e1);
			e1.printStackTrace();
		}
		
		try {
			mockMvc.perform(post("/member/joinProc")
							.contentType(MediaType.APPLICATION_JSON)
							.content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(model().attribute("msg", "회원 가입에 성공하셨습니다."))
					.andExpect(view().name("/member/result"));
		} catch (Exception e) {
			log.error("MemberJoinController 에러 : " + e);
			e.printStackTrace();
		}
	}

}
