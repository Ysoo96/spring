package com.javateam.demo2.repository;

import com.javateam.demo2.domain.MemberVO;

public interface MemberDAO {

	/**
	 * 회원정보 삽입(생성)
	 * 
	 * @param memberVO 회원정보
	 * @return 삽입된 레코드 수 (int) / 삽입 여부(boolean)
	 * ex) 1/true (레코드 생성), 0/false (레코드 미생성)
	 */
	boolean insertMember(MemberVO memberVO);
	
}
