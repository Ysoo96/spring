package com.javateam.springJPA.controller;

import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.springJPA.domain.DemoVO;
import com.javateam.springJPA.service.PagingJpaService;

import lombok.extern.slf4j.Slf4j;
 
@Controller
@Slf4j
public class PagingController {
   
    @Autowired
    private PagingJpaService svc;
    
    @RequestMapping("/")
    public String home() {
    	
    	return "redirect:/paging?page=1";
    }
   
    @RequestMapping("/sort")
    public String sort(Model model) {
       
        log.info("sort");
       
        model.addAttribute("list", svc.findAll("오름차순"));
       
        return "sorted";
    } //
   
   
    @GetMapping("/paging")
    public String paging(@RequestParam("page") int page, Model model) {
       
        log.info("paging");
        
        Page<DemoVO> pageList = svc.findAllByPaging(page, 5);
        List<DemoVO> list = pageList.getContent();
       
        model.addAttribute("total_page", pageList.getTotalPages());
        model.addAttribute("curr_page", page);
        model.addAttribute("list", list);
       
        return "paging";
    } //
   
    @RequestMapping("/member/{id}")
    @ResponseBody
    public DemoVO getOne(@PathVariable int id) {
       
        log.info("getOne");
       
        return svc.findById(id);
    } //
 
}