/**
 * 
 */
package com.javateam.memberProject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import com.javateam.memberProject.controller.AuthController;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

// spring security
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;

import lombok.extern.slf4j.Slf4j;


/**
 * 로그인 MVC 테스트
 * 
 * 참고) Spring Boot & Test : https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing
 * 
 * - setup : https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/setup.html
 * - 소스(source) : https://github.com/spring-projects/spring-security/tree/6.3.x/test/src/test/java/org/springframework/security/test/web 
 * 
 * @author java
 */
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Slf4j
public class LoginTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private AuthController authController;
	
	private MockMvc mockMvc;
	
	private String id;
	private String pw;
		
	@BeforeEach
	public void init() {
		
		// mockMvc = MockMvcBuilders.webAppContextSetup(wac)
		mockMvc = MockMvcBuilders.standaloneSetup(authController)
			     .apply(springSecurity(springSecurityFilterChain))
			     .build();
	}
		
	// case-1) 아이디/패쓰워드 일치할 경우(정상 가입된 회원정보)
	// case-1-1) 아이디(mbc_1097) / 패쓰워드(#Abcd1234)
	// jUnit eclipse 패널에서 함수 이름대신 @DisplayName 지정한 이름이 출력됨
	@DisplayName("로그인 테스트 : 아이디/패쓰워드 일치할 경우(정상 가입된 회원정보)")
	@Test
	public void testAuth1() throws Exception {
		
		id = "mbc_1097"; // 관리자 계정 : ROLE_USER
		pw = "#Abcd1234"; 
		
		mockMvc.perform(formLogin("/login")
					.user("username", id)
					.password("password", pw))
			   .andExpect(authenticated().withUsername(id))
			   .andDo(print());
		
	}
	
	// case-2) 아이디가 일치하지만, 패쓰워드 불일치할 경우(정상 가입된 회원정보)
	// case-2-1) 아이디(mbc_1097) / 패쓰워드(#Abcd123456789)
	@DisplayName("로그인 테스트 : 아이디가 일치하지만, 패쓰워드 불일치할 경우(정상 가입된 회원정보)")
	@Test
	public void testAuth2() throws Exception {
		
		id = "mbc_1097";
		pw = "#Abcd123456789";
		
		MvcResult mvcResult = mockMvc.perform(formLogin("/login")
								.user("username", id)
								.password("password", pw))
						   .andExpect(unauthenticated()) // 로그인 인증되지 않음
						   .andExpect(redirectedUrl("/loginError")) // 로그인 에러시 이동 페이지
						   .andDo(print())
						   .andReturn();
		
		// Http 상태 코드 : 302
		// 
		// 참고) 302 Found : 
		// 이 응답 코드는 요청한 리소스의 URI가 일시적으로 변경되었음을 의미 
		// 새롭게 변경된 URI는 나중에 만들어질 수 있음 
		// 참고링크) https://developer.mozilla.org/ko/docs/Web/HTTP/Status/302
		
		// Spring Security 에러 메시지 : 로그인 점검 서비스(CustomProvider.java)에서 발생한 오류 메시지
		String sessionMsg = mvcResult.getRequest()
									 .getSession()
									 .getAttribute("SPRING_SECURITY_LAST_EXCEPTION")
									 .toString();
		
		// 에러 메시지 확인 : Session Attrs = {SPRING_SECURITY_LAST_EXCEPTION=org.springframework.security
		// .authentication.InternalAuthenticationServiceException: 비밀번호가 일치하지 않습니다.}
		// 오류 메시지 포함 여부 점검 
		assertThat("비밀번호가 일치하지 않습니다.").isSubstringOf(sessionMsg);
		
	}
	
	// case-3) 아이디도 없고 패쓰워드 불일치할 경우(DB에 등록되지 않은 회원정보)
	// case-3-1) 아이디(abcd123456789) / 패쓰워드(#Abcd123456789)
	@DisplayName("로그인 테스트 : 아이디도 없고 패쓰워드 불일치할 경우(DB에 등록되지 않은 회원정보)")
	@Test
	public void testAuth3() throws Exception {
		
		id = "abcd123456789";
		pw = "#Abcd123456789";
		
		MvcResult mvcResult = mockMvc.perform(formLogin("/login")
										.user("username", id)
										.password("password", pw))
								   .andExpect(unauthenticated()) // 로그인 인증되지 않음
								   .andExpect(redirectedUrl("/loginError")) // 로그인 에러시 이동 페이지
								   .andDo(print())
								   .andReturn();
		
		// Spring Security 에러 메시지 : 로그인 점검 서비스(CustomProvider.java)에서 발생한 오류 메시지
		String sessionMsg = mvcResult.getRequest()
									 .getSession()
									 .getAttribute("SPRING_SECURITY_LAST_EXCEPTION")
									 .toString();
		
		// 에러 메시지 확인 : Session Attrs = {SPRING_SECURITY_LAST_EXCEPTION=org.springframework.security
		// .authentication.InternalAuthenticationServiceException: 회원 아이디가 없습니다.}
		// 오류 메시지 포함 여부 점검 
		assertThat("회원 아이디가 없습니다.").isSubstringOf(sessionMsg);
	}

}