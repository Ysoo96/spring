package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.memberProject.service.RegisterMail;

public class AccountController {
	@Autowired
	RegisterMail registerMail;
	
	@PostMapping("/login/mailConfirm")
	@ResponseBody
	String mailConfirm(@RequestParam("email") String email) throws Exception {
		String code = registerMail.sendSimpleMessage(email);
		System.out.println("인증코드 : " + code);
		
		return code;
	}
}
