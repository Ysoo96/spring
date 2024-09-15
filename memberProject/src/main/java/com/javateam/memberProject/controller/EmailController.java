package com.javateam.memberProject.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.memberProject.service.EmailService;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class EmailController {
	@Autowired
	EmailService emailService;
	
	@Autowired
	ServletContext application;
	
	// 일반 text 메일
	@GetMapping("/")
	@ResponseBody
	public String demo() {
		log.info("demo");
		
		emailService.sendSimpleMessage("116sally@naver.com",
										"yeon96@dongyang.ac.kr",
										"데모 메일",
										"<span style='background-color:yellow'>Mime 메일 송부합니다.</span>"
										+ "<a href='https://youtu.be/bw4AuPrLWeA?feature=shared'>세븐틴 - 청춘창가</a>");
		return "메일전송";
	}
	
	// MIME
	@GetMapping("/mime")
	@ResponseBody
	public String mimeSend() {
		log.info("demo");
		
		String img = application.getRealPath("/image/1.jpg");
		
		log.info("img : " + img);
		
		emailService.sendMIMEMessage("116sally@naver.com",
										"yeon96@dongyang.ac.kr",
										"MIME 데모 메일",
										"<span style='background-color:yellow'>MIME 메일 송부합니다.</span>"
										+ "<a href='https://youtu.be/bw4AuPrLWeA?feature=shared'>세븐틴 - 청춘창가</a>", new FileSystemResource(new File(img)));
		
		return "메일전송";
	}
}
