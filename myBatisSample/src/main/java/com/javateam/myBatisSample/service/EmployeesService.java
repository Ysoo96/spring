package com.javateam.myBatisSample.service;

import java.util.List;

import com.javateam.myBatisSample.domain.Employees;

public interface EmployeesService {

	List<Employees> getEmployeesList();
	
	Employees getMember(int employeeId);

}