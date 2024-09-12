package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.memberProject.domain.MemberDTO;
import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberJoinController {
	@Autowired
	MemberService memberService;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	@PostMapping("joinProcAjax")
//	@ResponseBody
//	public String joinProcAjax(@RequestBody MemberVO memberVO) {
//		
//		log.info("회원가입처리(AJAX) : " + memberVO);
//		return memberVO.toString();
//	}
	
	@PostMapping("joinProcDemo")
	// @ResponseBody
	public String joinProcDemo(@ModelAttribute MemberVO memberVO, Model model) {
		log.info("회원가입 처리 : {}", memberVO);
		String msg = ""; // 저장 성공/실패 메시지
		String path = ""; // 처리 후 이동 페이지
		
		bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encPw = bCryptPasswordEncoder.encode(memberVO.getPw());
		memberVO.setPw(encPw);
		
		// 회원 사용가
		memberVO.setEnabled(1);
		
		// return memberVO.toString();

		if (memberService.insertMember(memberVO) == true) {
			msg = "회원가입에 성공하셨습니다.";
			path = "/login";
		} else {
			msg = "회원가입에 실패하였습니다.";
			path = "/joinDemo";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("path", path);
		return "/member/result"; // result.html (thymeleaf)'
	}
	
	// @RequestMapping(value="/member/joinProc", method=RequestMethod.POST)
	// @PostMapping("/member/joinProc")
	@PostMapping("joinProc")
	public String joinProc(@RequestBody MemberVO memberVO, Model model) {
		log.info("회원가입 처리 : {}", memberVO);
		String msg = ""; // 저장 성공/실패 메시지
		// 회원 사용가
		memberVO.setEnabled(1);
		if (memberService.insertMember(memberVO) == true) {
			msg = "회원 가입에 성공하셨습니다.";
		} else {
			msg = "회원 가입에 실패하엿습니다.";
		}
		
		model.addAttribute("msg", msg);
		
		return "/member/result"; // result.html (thymeleaf)
	}
}
