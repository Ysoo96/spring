package com.javateam.memberProject.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.memberProject.domain.MailDTO;
import com.javateam.memberProject.service.MailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailController {
	private final MailService mailService;
	
	@ResponseBody
	@PostMapping("emailCheck")
	public String emailCheck(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException {
		String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
		return authCode;
	}
}
