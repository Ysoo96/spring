package com.javateam.springREST.controller;

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
class RestDemoControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
	WebApplicationContext ctx;

	@BeforeEach
	void setUp() throws Exception {
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	// case-1) 해당되는 URL이 연결되는지, 응답 정보로써 JSON 정보가 정상적으로 생성되는지 점검  
	@Test
	void testJsonFeed3() {
		
		try {
			
			// 참고) JsonPath : https://goessner.net/articles/JsonPath/
			// 위의 컨트롤러 코드에서 TestVO testVO = null; 로 변경하여 의도적으로 null 상황을 연출하여 테스팅할 수 있습니다.
			
			mockMvc.perform(get("/jsonFeed3").param("id", "abcd"))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("id").value("abcd"))
				   .andExpect(jsonPath("name").value("홍길동"))
				   .andExpect(jsonPath("address").value("서울 강남"))
				   .andDo(print());
			
		} catch (Exception e) {			
			log.error("RestDemoControllerTest 에러 : " + e);
			e.printStackTrace();
		}
		
	}
	
	// case-2) 해당되는 URL이 연결되는지, 응답 정보로써 null이 리턴될 경우 204(Http 상태 코드)가 생성되는지 점검  
	@Test
	void testJsonFeed3Null() {
		
		try {
			
			// 참고) JsonPath : https://goessner.net/articles/JsonPath/
			
			mockMvc.perform(get("/jsonFeed3").param("id", "abcd"))
				   .andExpect(status().isNoContent())
				   .andDo(print());
			
		} catch (Exception e) {			
			log.error("RestDemoControllerTest 에러 : " + e);
			e.printStackTrace();
		}
		
	}

}
