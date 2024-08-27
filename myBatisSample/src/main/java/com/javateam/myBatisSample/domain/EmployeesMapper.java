package com.javateam.myBatisSample.domain;

import java.util.List;

public interface EmployeesMapper {

	 List<Employees> getEmployeesList();
	 
	 Employees getMember(int employeeId);
	 
	 Employees getMemberVO(int employeeId);

}
