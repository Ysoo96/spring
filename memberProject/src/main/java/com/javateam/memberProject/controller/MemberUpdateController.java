package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.memberProject.domain.CustomUser;
import com.javateam.memberProject.domain.MemberUpdateDTO;
import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberUpdateController {

	@Autowired
	MemberService memberService;
	
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/update")
	public String update (Model model) {
		
		log.info("회원정보 수정");
		
		// Spring Security Pricipal(Session) 조회
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();
		
		CustomUser customUser = (CustomUser)principal;
		log.info("principal : {}", principal);
		log.info("id : {}", customUser.getUsername()); // 로그인 아이디
		
		String id = customUser.getUsername();
		
		MemberVO memberVO = memberService.selectMemberById(id);
		
		if (memberVO == null) {
			// 에러 처리
			model.addAttribute("errMsg", "회원 정보가 존재하지 않습니다.");
			return "/error/error";
		} else {
			
			MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO(memberVO);
			model.addAttribute("memberUpdateDTO", memberUpdateDTO);
		}
		
		return "/member/update";
	}
	
	@PostMapping("/updateProc")
	public String updateProc(@ModelAttribute("memberUpdateDTO") MemberUpdateDTO memberUpdateDTO, RedirectAttributes ra) {
		
		log.info("회원정보 수정 처리 : {}", memberUpdateDTO);
		
		// 신규 패스워드가 공백이 아닐면 패스워드 변경
		// 공백이면 패스워드가 변경 의사가 없는 것으로 간주하여 기존 패스워드 그래도 사용
		if (memberUpdateDTO.getPassword1().trim().equals("") != true) {
			// 패스워드 암호화
			// 주의) 변경된 패스워드(password1 혹은 password2) => 암호화 => 기존 패스워드(password)에 대입
			bCryptPasswordEncoder = new BCryptPasswordEncoder();
			memberUpdateDTO.setPw(bCryptPasswordEncoder.encode(memberUpdateDTO.getPassword1()));
		}
		
		log.info("updateProc-1(암호화 이후) : {}", memberUpdateDTO);
		
		String msg = "";
		String movePath = "";
		
		boolean result = memberService.updateMember(memberUpdateDTO);
		
		if (result == true) {
			
			msg = "회원정보 정보 수정에 성공하였습니다.";
			movePath = "redirect:/member/view";
		} else {
			
			msg = "회원정보 정보 수정에 실패하였습니다.";
			movePath = "redirect:/member/update";
		}
		
		log.info("result : {}", msg);
		ra.addFlashAttribute("msg", msg);
		
		return movePath;
	} //
	
}
