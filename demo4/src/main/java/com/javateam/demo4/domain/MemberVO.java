package com.javateam.demo4.domain;

import java.sql.Date;

public class MemberVO {
	
	private String id;
	private String pw;
	private String name;
	private Date joindate;
	
	public MemberVO() {}
	
	public MemberVO(String id, String pw, String name, Date joindate) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.joindate = joindate;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getJoindate() {
		return joindate;
	}
	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}
	
	@Override
	public String toString() {
		return "MemberVO [id=" + id + ", pw=" + pw + ", name=" + name + ", joindate=" + joindate + "]";
	}

}
