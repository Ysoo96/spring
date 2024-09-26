package com.javateam.memberProject.repository;

import java.util.List;
import java.util.Map;

import com.javateam.memberProject.domain.MemberDTO;
import com.javateam.memberProject.domain.MemberVO;

/**
 * 회원정보 DAO
 *
 * @author java
 *
 */
public interface MemberDAO {

	/**
	 * 개별 회원정보 조회(검색)
	 *
	 * @param id 회원 아이디
	 * @return 회원정보
	 */
	MemberVO selectMemberById(String id);

	/**
	 * 페이징에 의한(페이지 별로) 회원정보 조회(검색)
	 *
	 * @param page 페이지
	 * @param limit 페이지당 회원정보 갯수
	 * @return 회원정보(들)
	 */
	List<MemberVO> selectMembersByPaging(int page, int limit);

	/**
	 * 전체 회원정보 조회
	 *
	 * @return 전체 회원정보
	 */
	List<MemberVO> selectAllMembers();

	/**
	 * 회원정보 삽입(생성)
	 *
	 * @param memberVO 회원정보
	 * @return 삽입된(생성된) 레코드 수(int) / 삽입(생성) 여부(boolean)
	 * ex) 1/true(레코드 생성), 0/false(레코드 미생성)
	 */
	// void insertMember(MemberVO memberVO);
	// int insertMember(MemberVO memberVO);
	boolean insertMember(MemberVO memberVO);

	/**
	 * 회원정보 수정(갱신)
	 *
	 * @param memberVO 회원정보
	 * @return 회원정보 수정(갱신) 성공 여부
	 */
	// boolean updateMember(MemberVO memberVO);
	boolean updateMember(MemberVO memberVO);

	/**
	 * 회원정보 중복점검 (회원가입시)
	 * ex) 중복점검 대상 : 아이디, 이메일, 휴대폰번호
	 *
	 * @param fld 중복점검 대상 필드
	 * @param val 중복점검 대상 필드의 값
	 * @return 중복 여부  ex) 중복 : true, 중복 없음(사용가) : false
	 */
	boolean hasMemberByFld(String fld, String val);

	/**
	 * 회원정보 중복점검 (회원수정시)
	 * ex) 중복점검 대상 : 이메일, 휴대폰번호
	 *
	 * @param id 회원 아이디
	 * @param fld 중복점검 대상 필드
	 * @param val 중복점검 대상 필드의 값
	 * @return 중복 여부  ex) 중복 : true, 중복 없음(사용가) : false
	 */
	boolean hasMemberForUpdate(String id, String fld, String val);

    /**
	 * 회원정보 검색(페이징) : 주소, 이름, 성별, 휴대폰, 일반전화, (이메일)
	 *
	 * @param searchKey 검색 구분(종목) ex) ADDRESS(주소)
	 * @param searchWord 검색어  ex) 신림
	 * @param page 현재 페이지 ex) 1
	 * @param limit 페이지당 레코드 수 ex) 10
	 * @param isLikeOrEquals 동등/유사검색 여부 ex) like(유사), equals(동등)
	 * @param ordering 정렬 ex) ASC(오름차순), DESC(내림차순)
	 * @return (검색된) 회원정보
	 */
	List<Map<String, Object>> selectMembersBySearchingAndPaging(
							    String searchKey,
								String searchWord, int page, int limit,
								String isLikeOrEquals, String ordering);

	/**
	 * 개별 회원정보 조회(검색)-2 : 롤(role) 정보 포함
	 *
	 * @param fld 필드 ex) 회원 아이디/이메일/휴대폰
	 * @param val 필드값 ex) 'abcd1111'/abcd@abcdcom/010-1234-5678
	 * @return 회원정보
	 */
	Map<String, Object> selectMemberByFld(String fld, Object val);

	/**
	 * 개별 회원 롤(Role) 생성
	 *
	 * @param id 회원 아이디
	 * @param role 롤 ex) ROLE_USER, ROLE_ADMIN
	 * @return 생성 성공 여부
	 */
	boolean insertRole(String id, String role);

	/**
	 * 개별 회원 롤(Role) 삭제
	 *
	 * @param id 회원 아이디
	 * @return 삭제 성공 여부
	 */
	boolean deleteRoles(String id);

	/**
	 * 개별 회원정보 삭제
	 *
	 * @param id 회원 아이디
	 * @return 삭제 성공 여부
	 */
	boolean deleteMemberById(String id);

	/**
	 * 총 회원수 조회
	 *
	 * @return 총 회원수
	 */
	int selectCountAll();

	/**
	 * 회원 롤(role) 정보 조회 : 1인당 2개 허용 가능(ROLE_USER, ROLE_ADMIN)
	 *
	 * @param id 회원 아이디
	 * @return 롤(role) 정보
	 */
	List<String> selectRolesById(String id);

	/**
	 * 회원 롤(role) 정보 삭제
	 *
	 * @param id 회원 아이디
	 * @param role 롤 정보
	 */
	void deleteRoleById(String id, String role);

	/**
	 * 회원의 enabled 필드 상태를 변경
	 *
	 * @param id 회원 아이디
	 * @param enabled enabled 필드 상태
	 */
	void changeEnabled(String id, int enabled);
	
	/**
	 * 검색된 총 회원정보 수 조회
	 * 
	 * @param searchKey 검색 키워드(구분)
	 * @param searchWord 검색어
	 * @return 검색된 총 회원정보 수
	 */
	public int selectCountBySearching(String searchKey, String searchWord);
	
	/**
	 * 09.26 회원 아이디조회
	 * 
	 * @param email
	 * @return
	 */
	boolean findId(String email);
}