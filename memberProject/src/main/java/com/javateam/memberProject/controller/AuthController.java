package com.javateam.memberProject.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.memberProject.domain.Users;
import com.javateam.memberProject.service.EmailService;
import com.javateam.memberProject.service.MemberService;
import com.javateam.memberProject.service.RegisterMail;
import com.javateam.memberProject.domain.CustomUser;
import com.javateam.memberProject.domain.MemberDTO;
import com.javateam.memberProject.domain.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {

	@Autowired
	MemberService memberService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	RegisterMail registerMail;

	@RequestMapping("/")
	public String home() {

		log.info("home");

		return "redirect:/login";
	}

//	// 회원 가입폼
//	@RequestMapping("/join")
//	public void join() {
//		
//		log.info("join");
//	}
//	
//	// 회원가입 처리
//	@RequestMapping("/joinAction")
//	public String join(MemberVO memberVO, Model model) {
//		
//		log.info("join Action");
//		String path = "redirect:/login";
//		
//		String hashedPassword = ""; 
//		// 패쓰워드 암호화
//		hashedPassword = passwordEncoder.encode(memberVO.getPw()); 
//
//		log.info("hashedPassword : "+hashedPassword);
//
//		// 사용자 정보 생성
//		Users users = new Users(memberVO.getId(), hashedPassword, 1); 
//
//		try {
//			// 사용자 권한 할당 및 사용자 정보 저장
//			memberService.insertMember(memberVO); 
//				
//		} catch (Exception e) {
//			log.error("db 에러 발생 : " + e.getMessage());
//			model.addAttribute("db_error", "DB 접속에 문제가 있습니다.");
//			path = "/error/db-err";
//		} //
//		
//		return path;
//	} // 
	
	
	@GetMapping("/home")
	public String home(ModelMap model) {
		log.info("home");
		
		return "home";
	}

	// 관리자용 주소
	@GetMapping("/admin/home")
	public String securedAdminHome(ModelMap model) {

		log.info("/admin/home");

		// Security pricipal 객체 (사용자 인증/인가 정보 객체)
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		CustomUser user = null;

		if (principal instanceof CustomUser) {
			user = ((CustomUser) principal);
		}

		log.info("user : " + user);

		String name = user.getUsername();
		model.addAttribute("username", name);
		model.addAttribute("message", "관리자 페이지에 들어오셨습니다.");

		return "/admin/home";
	}

	// (일반)사용자용 주소
	@GetMapping("/secured/home")
	public String securedHome(ModelMap model) {

		log.info("/secured/home");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		CustomUser user = null;

		if (principal instanceof CustomUser) {
			user = ((CustomUser) principal);
		}

		log.info("user : " + user);

		String name = user.getUsername();
		model.addAttribute("username", name);
		model.addAttribute("message", "일반 사용자 페이지에 들어오셨습니다.");

		return "/secured/home";
	}
	
	@GetMapping("/join")
	public String join(Model model) {
		log.info("회원가입폼");		
		model.addAttribute("memberDTO", new MemberVO());
		return "join";
	}

	// 로그인 폼
	@GetMapping("/login")
	public String login() {

		log.info("loginForm");

		// 로그인/로그아웃 (인증) 처리
		// loginProcessingUrl("/login") 추가
		// : Spring Security 6.x의 404 에러 간헐 발생에 따른 버그 패치 조치
	    // 이슈(issue) 참고) https://github.com/spring-projects/spring-security/issues/12635

     	// 버그 패치) Spring Security Login Page Redirect(페이지 이동 문제)에 따른 조치
    	// 버그 리포트(bug report) : 일반적으로 최초로 로그인할 경우 발생
    	// 버그 증상) 로그인 성공시에도 불구하고 myPage로 이동되지 않고 loginForm 페이지에 머물러 있음
    	// 버그 구체적인 패치 대응) 로그인 상태를 Authentication 정보를 통해서 수동 점검하여
    	// forward 페이지를 인증/미인증별로 선택적으로 설정
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String path="";
		
		// 로그인 인증 안되었을 경우 인증정보(Authentication) dump
    	//
    	// AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED],
    	// Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1,
    	// SessionId=C4B68A207EF642308F11925E998570FC], Granted Authorities=[ROLE_ANONYMOUS]
		log.info("login시 인증정보 : " + auth);
		
		if (auth.getPrincipal() == null || auth.getPrincipal().toString().equals("anonymousUser")) { // 로그인 인증이 안되었을 경우
			
			log.info("로그인 인증 안됨");
			path = "findId";
		} else {
			
			log.info("로그인 인증됨");
			path = "home";
		} //
		
		// return "loginForm";
		return path;
	}

	// 로그아웃 처리
	@GetMapping("/logoutProc")
	public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		log.info("logout");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		log.info("auth : " + auth);

		// logout !
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "logout";
	} //

	// 로그인(인증) 에러
	@GetMapping("/loginError")
	public String loginError(HttpSession session, RedirectAttributes ra) {

		// Spring CustomProvider 인증(Authentication) 에러 메시지 처리
		Object secuSess = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

		log.info("#### 인증 오류 메시지 : " + secuSess);

		ra.addFlashAttribute("error", "true");
		ra.addFlashAttribute("msg", secuSess);

		return "redirect:/login";
	} //

	// id 존재 여부 점검
	@RequestMapping("/login/idCheck")
	@ResponseBody
	public String idCheck(@RequestParam("username") String username, Model model) {

		log.info("username : ", username);

		boolean flag = memberService.hasMemberByFld("ID", username);
		log.info("flag : ", flag);

		return flag + "";
	} //

	// access denied(403) : 인가(Authorization) 에러 : 접근 권한 부족
	@GetMapping("/403")
	public String error403() {

		return "/error/403";
	}
	
	// 이메일 인증
	@PostMapping("login/mailConfirm")
	@ResponseBody
	String mailConfirm(@RequestParam("email") String email) throws Exception {

		String code = registerMail.sendSimpleMessage(email);
		System.out.println("인증코드 : " + code);
		return code;
	}
} //