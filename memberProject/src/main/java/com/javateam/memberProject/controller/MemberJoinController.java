package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberJoinController {
	@Autowired
	MemberService memberService;
	
	// @RequestMapping(value="/member/joinProc", method=RequestMethod.POST)
	// @PostMapping("/member/joinProc")
	@PostMapping("joinProc")
	public String joinProc(@RequestBody MemberVO memberVO, Model model) {
		log.info("회원가입 처리 : {}", memberVO);
		String msg = ""; // 저장 성공/실패 메시지
		if (memberService.insertMember(memberVO) == true) {
			msg = "회원 가입에 성공하셨습니다.";
		} else {
			msg = "회원 가입에 실패하엿습니다.";
		}
		
		model.addAttribute("msg", msg);
		
		return "/member/result"; // result.html (thymeleaf)
	}
}
