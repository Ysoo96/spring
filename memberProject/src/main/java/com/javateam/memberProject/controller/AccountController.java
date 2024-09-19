package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.memberProject.domain.MemberDTO;
import com.javateam.memberProject.service.MemberService;
import com.javateam.memberProject.service.RegisterMail;

@Controller
public class AccountController {
	@Autowired
	MemberService ms;
	
	@Autowired
	private PasswordEncoder passwdEncoder;
	
	// 회원가입 메일 서비스
	@Autowired
	RegisterMail registerMail;
	
	// 임시 패스워드 발송 서비스
	
	// 회원가입 완료
	@PostMapping("login/registerOkUser")
	public String registerOkUser(@ModelAttribute("dto") MemberDTO dto) {
		System.out.println("dto : " + dto);
		
		// dto 에서 패스워드 가져와서 다시 인코딩에서 set
		dto.setPw(passwdEncoder.encode(dto.getPw()));
		
		int result = ms.insertMember(dto);
	}
}
