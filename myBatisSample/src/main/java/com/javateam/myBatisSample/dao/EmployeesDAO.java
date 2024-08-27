package com.javateam.myBatisSample.dao;

import java.util.List;

import com.javateam.myBatisSample.domain.Employees;

public interface EmployeesDAO {
	
	List<Employees> getEmployeesList();
	
	Employees getMember(int employeeId);

}