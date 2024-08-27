package com.javateam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
	@GetMapping("/")
	public String demo(Model model) {
		System.out.println("demo");
		model.addAttribute("java", 21);
		return "demo"; // demo.jsp
	}
}
