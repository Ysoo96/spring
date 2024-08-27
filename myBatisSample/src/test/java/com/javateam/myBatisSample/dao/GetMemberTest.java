package com.javateam.myBatisSample.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.myBatisSample.dao.EmployeesDAO;
import com.javateam.myBatisSample.domain.Employees;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class GetMemberTest {
	
	@Autowired
	EmployeesDAO dao;
	
	@Test
	public void testSize() {
		
		assertEquals(107, dao.getEmployeesList().size());
	}
	
	@Test
	public void testOne() {
		
		Employees emp = dao.getMember(100);
		assertEquals("King", emp.getLastName());
	}

}