package com.javateam.myBatisSample.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.myBatisSample.dao.EmployeesDAO;
import com.javateam.myBatisSample.domain.Employees;

@Service
public class EmployeesServiceImpl implements EmployeesService {

	@Autowired
	private EmployeesDAO dao;

	@Transactional(readOnly = true)
	public List<Employees> getEmployeesList() {

		return dao.getEmployeesList();
	}

	@Transactional(readOnly = true)
	@Override
	public Employees getMember(int employeeId) {
		
		return dao.getMember(employeeId);
	} //

}