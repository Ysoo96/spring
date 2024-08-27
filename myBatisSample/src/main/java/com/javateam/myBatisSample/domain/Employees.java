package com.javateam.myBatisSample.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class Employees {

	private int employeeId;
	private String lastName;
	private String firstName;
	private String email;
	private String phoneNumber;
	private Date hireDate;
	private String jobId;
	private long salary;
	private int commissionPct;
	private int managerId;
	private int departmentId;
	 
}