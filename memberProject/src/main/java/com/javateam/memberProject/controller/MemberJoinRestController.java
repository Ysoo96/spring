package com.javateam.memberProject.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.memberProject.domain.MemberDTO;
import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.service.EmailService;
import com.javateam.memberProject.service.MemberService;
import com.javateam.memberProject.service.RandomMakeService;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("member")
@Slf4j
public class MemberJoinRestController {

	@Autowired
	MemberService memberService;

	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 이메일 인증번호
	@Autowired
	EmailService emailService;
	@Autowired
	ServletContext servletContext;
	@Autowired
	RandomMakeService randomMakeService;

	@PostMapping("joinProc2")
	// @ResponseBody
	public String joinProc2(@ModelAttribute("memberDTO") MemberDTO memberDTO) {

		log.info("회원가입처리 : " + memberDTO);
		return memberDTO.toString();
	}

//	@PostMapping("joinProcAjax")
//	@ResponseBody
//	public String joinProcAjax(@RequestBody MemberVO memberVO) {
//		
//		log.info("회원가입처리(AJAX) : " + memberVO);
//		return memberVO.toString();
//	}

	@PostMapping("joinProcAjax")
	// @ResponseBody
	public ResponseEntity<Boolean> joinProcAjax(@RequestBody MemberVO memberVO) {

		log.info("회원가입처리(AJAX) : " + memberVO);

		ResponseEntity<Boolean> resEntity = null;

		try {

			// 패쓰워드 암호화
			bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String encPw = bCryptPasswordEncoder.encode(memberVO.getPw());
			memberVO.setPw(encPw);
			// 회원 사용가
			memberVO.setEnabled(1);

			boolean result = memberService.insertMember(memberVO);

			log.info("회원 가입 성공 여부 : " + result);

			if (result == true) {
				// 회원가입 성공 : http 상태 코드 200 => 사용 불가
				resEntity = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				// 회원가입 실패 : http 상태 코드 204 (No Content) => 사용가
				resEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {

			log.error("MemberJoinRestController.joinProcAjax 에러 : " + e);
			// http 상태 코드 : 500 (서버 에러)
			resEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resEntity;
	} //

	@GetMapping("makeEmailCheckAuth/{email}")
	public ResponseEntity makeEmailCheckAuth(@PathVariable("email") String email) {
		
		ResponseEntity<Boolean> resEntity = null;

		try {

			// 정수난수 4자리 발생
			String randomNum = randomMakeService.makeRandom();
			
			// 메일 내용 구성
			String msg = "";
			msg += "<h1 style=\\\"font-size: 30px; padding-right: 30px; padding-left: 30px;\\\">이메일 주소 확인</h1>";
			msg += "<p style=\\\"font-size: 17px; padding-right: 30px; padding-left: 30px;\\\">아래 확인 코드를 회원가입 화면에 입력해주세요.</p>";
			msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
	        msg += randomNum;
	        msg += "</td></tr></tbody></table></div>";
			
			// 메일 전송 (MIME)
			emailService.sendMIMEMessage("116sally@naver.com",
					 					 email,
					 					 "[Memory Table] 회원가입 인증 코드",
					 					 msg);
			
		} catch (Exception e) {

			log.error("MemberJoinRestController.makeEmailCheckAuth 에러 : " + e);
			// http 상태 코드 : 500 (서버 에러)
			resEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resEntity;
	} //

}