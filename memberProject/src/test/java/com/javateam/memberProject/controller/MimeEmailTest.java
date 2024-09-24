package com.javateam.memberProject.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MimeEmailTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testDemo() {
		log.info("Email Test");
		
		try {
			String msg = mockMvc.perform(get("/mimeTest"))
								.andExpect(status().isOk())
								.andExpect(content().contentType("text/plain; charset=UTF-8"))
								.andDo(print())
								.andReturn()
								.getResponse()
								.getContentAsString();
			
			log.info("msg : " + msg);
			
			assertThat(msg, is(equalTo("메일 전송")));
		} catch (Exception e) {
			log.error("Email Test error : " + e);
			e.printStackTrace();
		}
	}

}
