package com.javateam.springParamProc.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.javateam.springParamProc.domain.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

// @RestController <= @Controller + @ResponseBody
@Controller // Controller 역할 자바빈(Java Bean)
// @Log // lombok 로그(log) 객체(log) 생성
@Slf4j // lombok SLF4J 로그(log) 객체(log) 생성 
// 참고) Spring Boot 기본 로그툴 => logback
public class HomeController {
	
	//private static final Logger log 
	//	= LoggerFactory.getLogger(HomeController.class);
	
	// 호출 : http://localhost:8181/springParamProc/
	// @RequestMapping("/")
	// @RequestMapping(value = "/", method = RequestMethod.GET)
	@GetMapping("/")
	public String home(Model model, 
					   HttpServletRequest request) {
		log.info("home !!!");
		log.warn("home !!!");
		
		// 교재 p.198
		// forward 방식 전용 인자
		model.addAttribute("newjeans", "하니");
		request.setAttribute("newjeans2", "다니엘");
		
		// 화면 이동 : forward
		// 비교) response.sendRedirect()
		// return "redirect:/Mapping 주소"; // @RequestMapping
		// return "forward:/타임리프/JSP 주소"; 
		return "index"; // thymeleaf "index.html" 
	} //
	
	@RequestMapping(value="action", 
					method = {RequestMethod.POST})
	public void action(Model model, // 1)
	// @RequestMapping(value="action" => action.html (forward)
	// public String action(Model model, // 2)
				       HttpServletRequest request,
				       @RequestParam("name") String name,
				       @RequestParam Map<String, String> map,
				       @RequestBody String str,
				       @ModelAttribute("member") MemberVO member) throws UnsupportedEncodingException {
		
		log.info("action");
		
		// @RequestParam Map<String, String>
		// : Map<String, String[]> => 
		// request.getParameterMap();
		// 값 => String[] : checkbox 배열값 전송 가능 
		
		log.info("getParameter : " 
				+ request.getParameter("name"));
		
		model.addAttribute("param1", name);
		request.setAttribute("param2", name);				
		model.addAttribute("param3", map.get("name"));
		
		// @RequestBody는 아래와 같이 URLDecoder.decode()로 한글 변환 처리해야 됨.
		model.addAttribute("param4", URLDecoder.decode(str.split("=")[1], "UTF-8"));
		// model.addAttribute("param4", str.split("=")[1]);
		model.addAttribute("param5", member.getName());
		
		// return "action"; // 2) forward action.html
	} //
	
	@RequestMapping(value="action2", method=RequestMethod.POST) 
	public ModelAndView action2(@RequestParam("name") String name) {
		
		log.info("ModelAndView action");

		// ModelAndView modelAndView = new ModelAndView("action2");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("action2"); // forward 페이지

		modelAndView.addObject("param1", name); // forward 인자
		
		return modelAndView;
	} //
	
	// @GetMapping : since Spring 4.3 
//	@GetMapping("/formREST2")
//	public String formREST2() {
//		
//		log.info("formREST2 !");
//		
//		return "formREST2";
//	} //
	
	@RequestMapping(value="action3/name/{name}/grade/{grade}", 
					method= {RequestMethod.GET, RequestMethod.POST})
	public String action3(@PathVariable("name") String name,
						  @PathVariable("grade") int grade,
						  Model model) {
		
		log.info("PathVariable action");
		log.info("name : " + name);
		log.info("grade : " + grade);
		
		model.addAttribute("msg", "성공");
		return "action3";
	} //
	
}