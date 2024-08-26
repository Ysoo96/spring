package com.javateam.springJPAValidatorDemo.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberVO {
	
	private String id;
	private String pw;
	private String name;
	private String email;
	private Date birthday;
	
}