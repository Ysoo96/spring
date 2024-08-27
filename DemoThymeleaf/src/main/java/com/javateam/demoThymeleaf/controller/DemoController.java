package com.javateam.demoThymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DemoController {
	@GetMapping("/")
	public String demo(Model model) {
		model.addAttribute("spring_boot", "3.3.2");
		return "demo"; // demo.html (thymeleaf)
	}
}
