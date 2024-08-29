package com.javateam.memberProject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.domain.PageVO;
import com.javateam.memberProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

	@Autowired
	MemberService memberService;

	@GetMapping("/")
	public String admin() {

		log.info("관리자 화면 admin");
		// return "redirect:/admin/viewAllWithRoles";
		return "/welcome";
	}

	@GetMapping("/viewAll")
	public String adminViewWithRoles(@RequestParam(value="currPage", defaultValue="1", required=true) int currPage,
									 @RequestParam(value="limit", defaultValue="10") int limit,
									 Model model) {

		log.info("관리자 화면 : role 표시");
		List<Map<String, Object>> members = new ArrayList<>();

		// members = memberService.selectMembersWithRolesByPaging(currPage, limit);
		members = memberService.selectMembersBySearchingAndPaging(null, null, currPage, limit, null, null);

		// 총 인원 수
		int listCount = memberService.selectCountAll();

		log.info("총 인원수 : {}", listCount);

		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, limit);
		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,...)
		int startPage = PageVO.getStartPage(currPage, limit);
		// 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30, ...)
   	    int endPage = startPage + 10;

   	    if (endPage > maxPage) endPage = maxPage;

   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(currPage);
		pageVO.setStartPage(startPage);

		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);

		model.addAttribute("pageVO", pageVO);
		model.addAttribute("members", members);

		return "/admin/viewAll";
	} //

} //