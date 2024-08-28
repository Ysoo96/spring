package com.javateam.demo2.repository;

import com.javateam.demo2.domain.MemberVO;

public interface MemberDAO {

	/**
	 * 개별 회원정보 조회
	 * 
	 * @param id 회원 아이디
	 * @return 회원정보
	 */
	MemberVO selectMemberById(String id);
	
}
