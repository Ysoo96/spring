package com.javateam.memberProject.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateDTO extends MemberVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 회원 패스워드(수정) */
	private String password1;
	
	/** 회원 패스워드(확인) */
	private String password2;
	
	public MemberUpdateDTO (MemberVO memberVO) {
		
		this.setId(memberVO.getId());
		this.setPw(memberVO.getPw());
		this.setName(memberVO.getName());
		this.setGender(memberVO.getGender());
		this.setEmail(memberVO.getEmail());
		this.setMobile(memberVO.getMobile());
		this.setPhone(memberVO.getPhone());
		this.setZip(memberVO.getZip());
		this.setRoadAddress(memberVO.getRoadAddress());
		this.setJibunAddress(memberVO.getJibunAddress());
		this.setDetailAddress(memberVO.getDetailAddress());
		this.setBirthday(memberVO.getBirthday());
		this.setJoindate(memberVO.getJoindate());
		this.setEnabled(memberVO.getEnabled());
	}

	@Override
	public String toString() {
		return "MemberUpdateDTO [password1=" + password1 + ", password2=" + password2 + ", getId()=" + getId()
				+ ", getPw()=" + getPw() + ", getName()=" + getName() + ", getGender()=" + getGender()
				+ ", getEmail()=" + getEmail() + ", getMobile()=" + getMobile() + ", getPhone()=" + getPhone()
				+ ", getZip()=" + getZip() + ", getRoadAddress()=" + getRoadAddress()
				+ ", getJibunAddress()=" + getJibunAddress() + ", getDetailAddress()=" + getDetailAddress()
				+ ", getBirthday()=" + getBirthday() + ", getJoindate()=" + getJoindate() + ", getEnabled()="
				+ getEnabled() + "]";
	}


}
