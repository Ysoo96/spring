package com.javateam.myBatisSample.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GetAllListTest {
	@Autowired
	EmployeesDAO dao;

	@Test
	public void testSize() {

		assertEquals(107, dao.getEmployeesList().size());
	}
}
