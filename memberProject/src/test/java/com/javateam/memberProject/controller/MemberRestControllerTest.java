package com.javateam.memberProject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
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
class MemberRestControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
	WebApplicationContext ctx;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		// this.mockMvc = MockMvcBuilders.standaloneSetup(MemberRestController.class);
	}

	// case-1) 존재하지 않는 아이디(javaspring1234)로 점검
	// case-1-1)
	// URI : /member/hasFld/ID/javaspring1234
	// Http method : GET
	// 기댓값 => 204 : No Content => 중복 안됨 : 사용가
	@Test
	void testHasFldAbsent() throws Exception {
		mockMvc.perform(get("/member/hasFld/ID/javaspring1234"))
			   .andExpect(status().isNoContent()) // 204
			   .andExpect(content().string("false")) // false
			   .andDo(print()); // 결과 로그 출력
	}
	
	// case-2) 존재하는 아이디(abcd1111)로 점검
	// case-2-1)
	// URI : /member/hasFld/ID/abcd1111
	// Http method : GET
	// 기댓값 => 200 : OK => 중복 : 사용뷸가
	@Test
	void testHasFldPresent() throws Exception {
		mockMvc.perform(get("/member/hasFld/ID/abcd1111"))
			   .andExpect(status().isOk()) // 200
			   .andExpect(content().string("true")) // true
			   .andDo(print()); // 결과 로그 출력
	}

}
