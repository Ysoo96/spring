/**
 * 
 */
package com.javateam.SpringBootSecurityMyBatisThymeleaf.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author javateam
 *
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.javateam.SpringBootSecurityMyBatisThymeleaf.service.CustomProvider;

import lombok.extern.slf4j.Slf4j;

// spring & thymeleaf : 
// https://www.thymeleaf.org/doc/articles/springsecurity.html

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // 추가
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomProvider customProvider;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	   return super.authenticationManagerBean();
	}

	private UserDetailsService userDetailsService;

	private DataSource dataSource;

	public WebSecurityConfig(UserDetailsService userDetailsService, DataSource dataSource) {

		log.info("생성자 주입 wiring");
		this.dataSource = dataSource;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
		
		// customProvider 
		auth.authenticationProvider(customProvider);

		// 주의사항) 아래의 코드 부류들을 삽입/대체시 지속적 Stackoverflow error !!!
		// auth.parentAuthenticationManager(authenticationManagerBean());
		// auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
		// auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
		// // 추가??
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		log.info("configure Spring Boot Security");

		// h2 console 사용을 위한 설정
		// http.headers().frameOptions().sameOrigin();

		http.authorizeRequests()
				// 해당 url을 허용한다.
				.antMatchers("/resources/**", "/loginError", "/join", "/joinAction", 
							"/login/idCheck", "/login")
				.permitAll()
				// admin 폴더에 경우 admin(ROLE_ADMIN) 롤이 있는 사용자에게만 허용
				.antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN")
				// user 폴더에 경우 user(ROLE_USER) 롤이 있는 사용자에게만 허용
				.antMatchers("/user/**").hasAnyRole("ROLE_USER").anyRequest().authenticated();

		// csrf
		// http.csrf().disable();

		// 로그인 처리
		http.formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password")
				.defaultSuccessUrl("/welcome").failureUrl("/loginError")
				// .successHandler(new CustomAuthenticationSuccess()) // 로그인 성공 핸들러
				// .failureHandler(new CustomAuthenticationFailure()) // 로그인 실패 핸들러
				.permitAll().and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/403"); // 권한이 없을경우
																											// 해당 url로
																											// 이동

		// 추가된 부분 : remember-me 관련
		// remember-me cookie
		// => 사용자이름 + cookie expired time(만료 시간) + 패쓰워드 => Base64(Md5방식) 암호화
		/*
		 * base64(username + ":" + expirationTime + ":" + md5Hex(username + ":" +
		 * expirationTime + ":" password + ":" + key))
		 */
		http.rememberMe().key("javateam").userDetailsService(userDetailsService).tokenRepository(getJDBCRepository())
				.tokenValiditySeconds(60 * 60 * 24); // 24시간(1일)
	}

	// 추가된 remember-me 관련 메서드
	private PersistentTokenRepository getJDBCRepository() {

		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();

		repo.setDataSource(dataSource);

		return repo;
	} //

}