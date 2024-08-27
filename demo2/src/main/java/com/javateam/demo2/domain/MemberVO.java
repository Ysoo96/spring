package com.javateam.demo2.domain;

import java.sql.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 테이블 값 객체 (VO, Entity object)
 * 
 * @author java
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	/** 휴대전화 */
	private String phone;
	/** 우편번호 */
	private String zip;
	/** 도로명 주소 */
	private String address;
	/** 상세 주소 */
	private String detailAddress;
	/** 생년월일 */
	private Date birthday;
	/** 가입일 */
	private Date joindate;
	/** 회원 활성화 여부 */
	private int enabled;
	
	@Override
	public int hashCode() {
		return Objects.hash(address, birthday, detailAddress, email, gender, id, joindate, name, phone, pw, zip);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberVO other = (MemberVO) obj;
		return Objects.equals(address, other.address) && Objects.equals(birthday, other.birthday)
				&& Objects.equals(detailAddress, other.detailAddress) && Objects.equals(email, other.email)
				&& Objects.equals(gender, other.gender) && Objects.equals(id, other.id)
				&& Objects.equals(joindate, other.joindate) && Objects.equals(name, other.name)
				&& Objects.equals(phone, other.phone) && Objects.equals(pw, other.pw) && Objects.equals(zip, other.zip);
	}
}
