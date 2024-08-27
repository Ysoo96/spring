package com.javateam.myBatisSample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.javateam.myBatisSample.domain.Employees;
import com.javateam.myBatisSample.service.EmployeesService;

@Controller
// @RequestMapping("/member") // 공통 경로 
public class EmployeesController {

	@Autowired
	private EmployeesService employeesService;

	@RequestMapping("/")
	// @RequestMapping(value="/", method=RequestMethod.GET)
	public String home() {

		return "redirect:/employeesList";
	}

	@RequestMapping("/employeesList")
	// @ResponseBody
	public ModelAndView getEmployeesList(){
	// public String getEmployeesList() {
	// public ResponseEntity<List<Employees>> getEmployeesList() {

		ModelAndView result = new ModelAndView();
		List<Employees> employeesList = employeesService.getEmployeesList();

		result.addObject("employeesList", employeesList);
		result.setViewName("/employeesList");

		return result;
		// return employeesList.toString();
		// return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * 
	 * @RequestMapping("/employeesList") public String
	 * getEmployeesList(HttpServletRequest request) {
	 * 
	 * List<Employees> employeesList = employeesService.getEmployeesList();
	 * request.setAttribute("employeesList", employeesList);
	 * 
	 * return "/employeesList"; }
	 */
	@RequestMapping("/member")
	public String getMember(@RequestParam("id") int id, Model model) {

		model.addAttribute("member", employeesService.getMember(id));

		return "/member";
	}
}