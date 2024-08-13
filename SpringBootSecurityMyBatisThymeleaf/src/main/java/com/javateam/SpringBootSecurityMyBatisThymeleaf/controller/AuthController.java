package com.javateam.SpringBootSecurityMyBatisThymeleaf.controller;


import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;

import com.javateam.SpringBootSecurityMyBatisThymeleaf.domain.Users;
import com.javateam.SpringBootSecurityMyBatisThymeleaf.domain.CustomUser;
import com.javateam.SpringBootSecurityMyBatisThymeleaf.service.AuthMyBatisService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
 
@Controller
@Slf4j
public class AuthController {
	
	@Autowired
	private AuthMyBatisService authMyBatisService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder; 
	
	@RequestMapping("/")
	public String home() {
		
		log.info("home");
		
		return "redirect:/login";
	}
	
	// 회원 가입폼
	@RequestMapping("/join")
	public void join() {
		
		log.info("join");
	}
	
	// 회원가입 처리
	@RequestMapping("/joinAction")
	public String join(@RequestParam("username") String username,
					   @RequestParam("password") String password,
					   Model model) {
		
		log.info("join Action");
		String path = "redirect:/login";
		
		String hashedPassword = ""; 
		// 패쓰워드 암호화
		hashedPassword = passwordEncoder.encode(password); 

		log.info("hashedPassword : "+hashedPassword);

		// 사용자 정보 생성
		Users users = new Users(username, hashedPassword, 1); 

		try {
			// 사용자 권한 할당 및 사용자 정보 저장
			authMyBatisService.insertUsers(users, "ROLE_USER"); 
				
		} catch (Exception e) {
			log.error("db 에러 발생 : " + e.getMessage());
			model.addAttribute("db_error", "DB 접속에 문제가 있습니다.");
			path = "/error/db-err";
		} //
		
		return path;
	} // 
	
	// myPage
	@GetMapping("/myPage")
	public String myPage() {
	
		log.info("myPage");
		
		return "myPage";
	} //
    
    @GetMapping("/helloworld")
    public String helloWorld(ModelMap model) {
    	
    	log.info("hellowWolrd");
    	
	    model.addAttribute("message", "Welcome to the Hello World page");
	    
	    return "helloworld";
    }
        
    // 관리자용 주소
    @GetMapping("/admin/home")
	public String securedAdminHome(ModelMap model) {
	
    	log.info("/admin/home");
    	
    	// Security pricipal 객체 (사용자 인증/인가 정보 객체)
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();
		
		CustomUser user = null;
		
		if (principal instanceof CustomUser) {
			user = ((CustomUser)principal);
		}
		
		log.info("user : " +user);
		
		String name = user.getUsername();
		model.addAttribute("username", name);
		model.addAttribute("message", "관리자 페이지에 들어오셨습니다.");
		
		return "/admin/home";
    }
    
    // (일반)사용자용 주소
    @GetMapping("/secured/home")
    public String securedHome(ModelMap model) {
    	
    	log.info("/secured/home");
    	
        Object principal = SecurityContextHolder.getContext()
        										.getAuthentication()
        										.getPrincipal();
        
        CustomUser user = null;
        
        if (principal instanceof CustomUser) {
        	user = ((CustomUser)principal);
        }
        
        log.info("user : "+user);
     
	    String name = user.getUsername();
	    model.addAttribute("username", name);
	    model.addAttribute("message", "일반 사용자 페이지에 들어오셨습니다.");
	    
	    return "/secured/home";
    }
    
    // 로그인 폼
    @GetMapping("/login")
    public String login(ModelMap model) {
    	
    	log.info("loginForm");
    	
    	return "loginForm";
    }
    
    // 로그아웃 처리
    @GetMapping("/logoutProc")
    public String logout(ModelMap model,
    					 HttpServletRequest request,
    					 HttpServletResponse response) {
    	
    	log.info("logout");
    	
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    
	    log.info("auth : "+auth);
	    
	    // logout !
	    if (auth != null) {    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }

	    return "logout";
    } //
    
    // 로그인(인증) 에러
    @GetMapping("/loginError")
    public String loginError(ModelMap model, HttpSession session) {
    	
		// Spring CustomProvider 인증(Authentication) 에러 메시지 처리
		Object secuSess = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
	
		log.info("#### 인증 오류 메시지-1 : " + secuSess);
		log.info("#### 인증 오류 메시지-2 : " + secuSess.toString());
	
		model.addAttribute("error", "true");
		model.addAttribute("msg", secuSess);
	
		return "loginForm";
    } //
    
    
    // id 존재 여부 점검
 	@RequestMapping("/login/idCheck")
 	@ResponseBody
 	public String idCheck(@RequestParam("username") String username,
 						  Model model) {
 		
 		log.info("username : ", username);
 		
 		boolean flag = authMyBatisService.hasUsername(username);
 		log.info("flag : ", flag);
 		
 		return flag + "";
 	} //
 	
 	
 	// access denied(403) : 인가(Authorization) 에러 : 접근 권한 부족 
 	@GetMapping("/403")
 	public String error403() {
 		
 		return "/error/403";
 	}
} //