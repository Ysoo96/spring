package com.javateam.memberProject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MemberRestControllerTest2 {
	
	MockMvc mockMvc;
	
	@Autowired
	WebApplicationContext ctx;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc 
			= MockMvcBuilders.webAppContextSetup(ctx).build();
			// = MockMvcBuilders.standaloneSetup(new MemberRestController()).build();
		    // = MockMvcBuilders.standaloneSetup(MemberRestController.class).build();
	}

	// case-1) 회원 수정(변경) => 이메일 수정 
	// : 이미 가입된 회원 아이디(abcd1111)와 기존 자신의 이메일(abcd1111@abcd.com)로 변경(회귀)
	// => 중복 여부(사용가) 점검
	// case-1-1)
	// URL "/member/hasFldForUpdate/abcd1111/EMAIL/abcd1111@abcd.com"
	// Http Method : GET
	// 기댓값 => 204 (No Content) => 중복 레코드가 없음 : 사용가
	// false
	@Test
	void testHasFldForUpdateEmail() throws Exception {
		
		mockMvc.perform(get("/member/hasFldForUpdate/abcd1111/EMAIL/abcd1111@abcd.com"))
			   .andExpect(status().isNoContent()) // 204
			   .andExpect(content().string("false")) // false
			   .andDo(print());
	}
	
	// case-2) 회원 수정(변경) => 이메일 수정 
	// : 이미 가입된 회원 아이디(abcd1111)와 다른 회원의 이메일(mbc_23@abcd.com)로 변경
	// => 중복 여부(사용가) 점검
	// case-2-1)
	// URL "/member/hasFldForUpdate/abcd1111/EMAIL/mbc_23@abcd.com"
	// Http Method : GET
	// 기댓값 => 200 (SUCCESSFUL) => 중복 레코드가 있음 : 사용불가
	// true
	@Test
	void testHasFldForUpdateEmail2() throws Exception {
		
		mockMvc.perform(get("/member/hasFldForUpdate/abcd1111/EMAIL/mbc_23@abcd.com"))
		   .andExpect(status().isOk()) // 200
		   .andExpect(content().string("true")) // true
		   .andDo(print());
	}
	
	// case-3) 회원 수정(변경) => 이메일 수정 
	// : 이미 가입된 회원 아이디(abcd1111)와 신규 이메일(abcdabcd1111@naver.com)로 변경
	// => 중복 여부(사용가) 점검
	// case-3-1)
	// URL "/member/hasFldForUpdate/abcd1111/EMAIL/abcdabcd1111@naver.com"
	// Http Method : GET
	// 기댓값 => 204 (No Content) => 중복 레코드가 없음 : 사용가
	// false
	@Test
	void testHasFldForUpdateEmail3() throws Exception {
		
		mockMvc.perform(get("/member/hasFldForUpdate/abcd1111/EMAIL/abcdabcd1111@naver.com"))
		   .andExpect(status().isNoContent()) // 204
		   .andExpect(content().string("false")) // false
		   .andDo(print());
	}

}
