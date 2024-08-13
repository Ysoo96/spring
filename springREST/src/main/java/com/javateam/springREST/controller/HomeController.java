package com.javateam.springREST.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@RequestMapping("/")
	public String home() {

		log.info("home");
		
		return "redirect:/restClient";
	} //
	
	@RequestMapping("/restClient")
	public void restClient() {
		
		log.info("restClient");
	} // 
	
	@RequestMapping("/restClient2")
	public void restClient2() {
		
		log.info("restClient2");
	} // 
	
}	