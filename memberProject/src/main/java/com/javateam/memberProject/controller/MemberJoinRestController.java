package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.memberProject.domain.MemberDTO;
import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("member")
@Slf4j
public class MemberJoinRestController {

	@Autowired
	MemberService memberService;
	
	BCryptPasswordEncoder bCryptPasswordEncoder; 
	
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
	}
	
}