package com.javateam.myBatisSample.dao;

import static org.junit.jupiter.api.Assertions.*;
// import static org.junit.jupiter.api.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.myBatisSample.dao.EmployeesDAO;
import com.javateam.myBatisSample.domain.Employees;

@SpringBootTest
class GetMemberTest2 {

	@Autowired
	EmployeesDAO employeesDAO;

	@Test
	void testgetEmploueesList() {
		// test case : 전체 회원 수(107) 점검
		int totSize = employeesDAO.getEmployeesList().size();
		int expectedSize = 107;

		// Assertions.assertTrue(totSize == expectedSize);
		// assertTrue(totSize == expectedSize);
		assertEquals(expectedSize, totSize);
	}

	@Test
	void testGetMember() {
		// test case : 사원 아이디가 100인 사람의 성이 King인지 점검
		Employees emp = employeesDAO.getMember(100);
		String actual = emp.getLastName();
		String expected = "King";

		assertEquals(expected, actual);
	}

}
