package com.javateam.memberProject.domain;

import java.sql.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 테이블 값 객체(VO, Entity object)
 * 
 * @author java
 */
@Data
@Builder
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 오버로딩된 생성자 -> @Builder
public class MemberVO {

	/** 아이디 */
	private String id;
	/** 패스워드 */
	private String pw;
	/** 이름 */
	private String name;
	/** 성별 */
	private String gender;
	/** 이메일 */
	private String email;
	/** 연락처1(휴대폰) */
	private String mobile;
	/** 연락처2(일반전화) */
	private String phone;
	/** 우편번호 */
	private String zip;
	/** 도로명 주소 */
	private String roadAddress;
	/** 지번 주소 */
	private String jibunAddress;
	/** 상세 주소 */
	private String detailAddress;
	/** 생년월일 */
	private Date birthday;
	/** 가입일 */
	private Date joindate;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberVO other = (MemberVO) obj;
		return Objects.equals(birthday, other.birthday) && Objects.equals(detailAddress, other.detailAddress)
				&& Objects.equals(email, other.email) && Objects.equals(gender, other.gender)
				&& Objects.equals(id, other.id) && Objects.equals(jibunAddress, other.jibunAddress)
				&& Objects.equals(mobile, other.mobile) && Objects.equals(name, other.name)
				&& Objects.equals(phone, other.phone) && Objects.equals(pw, other.pw)
				&& Objects.equals(roadAddress, other.roadAddress) && Objects.equals(zip, other.zip);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(birthday, detailAddress, email, gender, id, jibunAddress, mobile, name, phone, pw,
				roadAddress, zip);
	}
	
	/*
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getRoadAddress() {
		return roadAddress;
	}
	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}
	public String getJibunAddress() {
		return jibunAddress;
	}
	public void setJibunAddress(String jibunAddress) {
		this.jibunAddress = jibunAddress;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getJoindate() {
		return joindate;
	}
	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}
	
	@Override
	public String toString() {
		return "MemberVO [id=" + id + ", pw=" + pw + ", name=" + name + ", gender=" + gender + ", email=" + email
				+ ", mobile=" + mobile + ", phone=" + phone + ", zip=" + zip + ", roadAddress=" + roadAddress
				+ ", jibunAddress=" + jibunAddress + ", detailAddress=" + detailAddress + ", birthday=" + birthday
				+ ", joindate=" + joindate + "]";
	}
	*/
}
