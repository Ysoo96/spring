package com.javateam.memberProject.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.memberProject.service.EmailService;
import com.javateam.memberProject.service.RandomMakeService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmailController {

	@Autowired
	EmailService emailService;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	RandomMakeService randomMakeService;
	
	// 이메일 중복점검을 위한 인증번호 발급 & 메일 전송
	@GetMapping("/mailSend/{email}/{minutes}")
	public ResponseEntity<String> mimeSendNoAppend (@PathVariable("email") String email,
													@PathVariable("minutes") String minutes,
													HttpSession session) {
		
		log.info("이메일 전송 : " + email);

		// 정수난수 4자리 발생
		String randomNum = randomMakeService.makeRandom();
		
		// 랜덤 문자 세션화
		session.setAttribute("SESS_RANDOM_NUM", randomNum);
			
		// 메일 내용 구성
		String msg = "<h1 style='font-size: 30px; padding-right: 30px; padding-left: 30px;'>이메일"
				+ "	주소 확인</h1>"
				+ "<p style='font-size: 17px; padding-right: 30px; padding-left: 30px;'>아래"
				+ "	확인 코드를 회원가입 화면에 입력해주세요.</p>"
				+ "<div"
				+ "	style='padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;'>"
				+ "	<table"
				+ "		style='border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;'>"
				+ "		<tbody>"
				+ "			<tr>"
				+ "				<td"
				+ "					style='text-align: center; vertical-align: middle; font-size: 30px;'>"
				+ randomNum
				+ "				</td>"
				+ "			</tr>"
				+ "		</tbody>"
				+ "	</table>"
				+ "</div>";
			
		// 메일 전송 (MIME)
		emailService.sendMIMEMessage("116sally@naver.com",
				 					 email,
				 					 "[Memory Table] 회원가입 인증 코드",
				 					 msg);

		return new ResponseEntity<>(randomNum, HttpStatus.OK);
	} //
	
	// 이메일 중복점검을 위한 인증번호 점검
	@GetMapping("/checkRandomNum/{randomNum}")
	public ResponseEntity<Boolean> checkRandomNum(@PathVariable("randomNum") String randomNum, HttpSession session) {

		log.info("인증번호 점검");

		boolean result = false;

		// 랜덤 문자 세션화
		String randomNumSess = "";

		if (session.getAttribute("SESS_RANDOM_NUM") != null) {
			randomNumSess = session.getAttribute("SESS_RANDOM_NUM").toString();

			if (randomNum.trim().equals(randomNumSess.trim())) {
				// 일치
				result = true;
				
				// 세션 변수 소거
				session.removeAttribute("SESS_RANDOM_NUM");
			} else {
				// 불일치
				result = false;
			}
		} else {
			new ResponseEntity<>(result, HttpStatus.NO_CONTENT); // 에러 유발
		} //

		return new ResponseEntity<>(result, HttpStatus.OK);
	} //
	
}
