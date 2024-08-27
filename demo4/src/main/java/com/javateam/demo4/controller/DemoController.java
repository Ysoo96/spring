package com.javateam.demo4.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.javateam.demo4.domain.MemberVO;

@Controller
public class DemoController {
	
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	
	@GetMapping("/")
	public String home() {
		
		return "redirect:/viewMembers";
	}
	
	@GetMapping("/viewMember/{id}")
	public String viewMember(@PathVariable("id") String id, Model model) {
		
		log.info("viewMember");
		model.addAttribute("member", new MemberVO(id, "1234567", "홍길동", new Date(System.currentTimeMillis())));
		
		return "viewMember";
	}
	
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable("id") String id, Model model) {
		
		log.info("modify");
		log.info("#### id : "+id);
		
		model.addAttribute("member", 
				new MemberVO(id, "1234567", "홍길동", new Date(System.currentTimeMillis())));
		
		return "modify";
	}
	
	@GetMapping("/viewMembers")
	public String viewMembers(Model model) {
		
		log.info("viewMembers");
		List<MemberVO> members = new ArrayList<>();
		
		members.add(new MemberVO("springboot", "1234567", "홍길동", new Date(System.currentTimeMillis())));
		members.add(new MemberVO("springlegacy", "1234567", "장길산", new Date(System.currentTimeMillis())));
		members.add(new MemberVO("thymeleaf", "1234567", "임꺽정", new Date(System.currentTimeMillis())));
		
		model.addAttribute("members", members);
		
		model.addAttribute("result", "성공");
		
		return "viewMembers";
	}
	
	@GetMapping("/demoUtilExp")
	public String demoUtilExp(Model model) {
		
		log.info("demoUtilExp");
		
		model.addAttribute("price", 123456789);
		model.addAttribute("sentence", "It is a Spring Boot");
		
		return "demoUtilExp";
	}
	
	@GetMapping("/templateDemo")
	public String templateDemo(Model model) {
		
		log.info("templateDemo");
		
		return "template";
	}

}