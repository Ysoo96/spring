package com.javateam.memberProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.javateam.memberProject.domain.MemberVO;
import com.javateam.memberProject.repository.MemberDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
	// howto-1)
	@Autowired
	// DataSourceTransactionManager dsTxManager;
	PlatformTransactionManager dsTxManager;

	// howto-2)
//	private final PlatformTransactionManager dsTxManager;
//	public MemberServiceImpl(PlatformTransactionManager dsTxManager) {
//		this.dsTxManager = dsTxManager;
//	}

	// howto-1)
	// @Autowired
	// TransactionTemplate txTemplate;

	// howto-2)
	TransactionTemplate txTemplate;

	@Autowired
	void setTransactionTemplate(PlatformTransactionManager txManager) {
		this.txTemplate = new TransactionTemplate(txManager);
	}

	@Autowired
	MemberDAO memberDAO;

	@Transactional(readOnly = true)
	@Override
	public MemberVO selectMemberById(String id) {
		return memberDAO.selectMemberById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MemberVO> selectMembersByPaging(int page, int limit) {
		return memberDAO.selectMembersByPaging(page, limit);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MemberVO> selectAllMembers() {
		return memberDAO.selectAllMembers();
	}

	/*
	 * @Override public boolean insertMember(MemberVO memberVO) { return
	 * txTemplate.execute(new TransactionCallback<Boolean>() {
	 * 
	 * @Override public Boolean doInTransaction(TransactionStatus status) { boolean
	 * result = false; try { result = memberDAO.insertMember(memberVO); result =
	 * memberDAO.insertRole(memberVO.getId(), "ROLE_USER"); } catch (Exception e) {
	 * result = false; log.error("MemberService.insertMember 에러 : " + e);
	 * status.setRollbackOnly(); } return result; }
	 * 
	 * }); }
	 */

	@Override
	public boolean insertMember(MemberVO memberVO) {
		return txTemplate.execute(status -> {
			boolean result = false;

			// 회원정보 삽입
			try {
				log.info("기존 회원 존재여부 : " + memberDAO.hasMemberByFld("ID", memberVO.getId()));

				// 기존 회원 존재 여부
				// if (memberDAO.selectMemberById(memberVO.getId()) != null) {
				if (memberDAO.hasMemberByFld("ID", memberVO.getId()) == true) {
					throw new Exception("중복되는 회원정보가 존재합니다.");
				}

				log.info("-- memberVO : " + memberVO);

				result = memberDAO.insertMember(memberVO);
				// result = memberDAO.insertRole(memberVO.getId(), "ROLE_USER");
			} catch (Exception e) {
				result = false;
				log.error("MemberService.insertMember 에러 : " + e);
				status.setRollbackOnly();
			}

			// 버그 패치 : 상위 CRUD 수행시 중복회원 있을 경우 에러 리턴
			// 상위 CRUD 실행시 에러 없을 경우만 아래 구문 실행
			if (result == true) {
				// 회원 롤(Role) 생성
				try {
					// 중요) 버그 패치
					// 기존 회원 존재 여부 : 기존회원이 존재해야 롤 추가 가능
					// if (memberDAO.selectMemberById(memberVO.getId()) == null) {
					if (memberDAO.hasMemberByFld("ID", memberVO.getId()) == false) {
						throw new Exception("중복되는 회원정보가 존재하지 않습니다.");
					}

					result = memberDAO.insertRole(memberVO.getId(), "ROLE_USER");
				} catch (Exception e) {
					result = false;
					log.error("MemberService.insertMember(Role) 에러 : " + e);
					status.setRollbackOnly();
				}
			}

			return result;

		});
	}

	/*
	 * @Override public boolean insertMember(MemberVO memberVO) { boolean result =
	 * false; TransactionStatus txStatus = dsTxManager.getTransaction(new
	 * DefaultTransactionDefinition());
	 * 
	 * try { result = memberDAO.insertMember(memberVO); result =
	 * memberDAO.insertRole(memberVO.getId(), "ROLE_USER");
	 * dsTxManager.commit(txStatus); // result = true; } catch (Exception e) {
	 * dsTxManager.rollback(txStatus); result = false;
	 * log.error("MemberService.insertMember 에러 : " + e); // throw e; } //
	 * dsTxManager.commit(txStatus); return result; }
	 */

	@Override
	public MemberVO insertMember2(MemberVO memberVO) {
		return txTemplate.execute(new TransactionCallback<MemberVO>() {

			@Override
			public MemberVO doInTransaction(TransactionStatus status) {
				MemberVO resultVO = null;

				try {
					// 기존 회원 존재 여부
					// if (memberDAO.selectMemberById(memberVO.getId()) != null) {
					if (memberDAO.hasMemberByFld("ID", memberVO.getId()) == true) {
						throw new Exception("중복되는 회원정보가 존재합니다.");
					}

					if (memberDAO.insertMember(memberVO) == true
							&& memberDAO.insertRole(memberVO.getId(), "ROLE_USER") == true) {
						// 회원정보 생성 이후 결과
						resultVO = memberDAO.selectMemberById(memberVO.getId());
					}
				} catch (Exception e) {
					resultVO = null;
					log.error("MemberService.insertMember 에러 : " + e);
					status.setRollbackOnly();
				}
				return resultVO;
			}

		});
	}

	@Override
	public boolean updateMember(MemberVO memberVO) {
		return txTemplate.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				boolean result = false;

				try {
					// 기존 회원 존재 여부
					// if (memberDAO.selectMemberById(memberVO.getId()) == null) {
					if (memberDAO.hasMemberByFld("ID", memberVO.getId()) == false) {
						throw new Exception("수정할 회원정보가 존재하지 않습니다.");
					}
					result = memberDAO.updateMember(memberVO);
				} catch (Exception e) {
					result = false;
					log.error("MemberService.updateMember 오류 : " + e);
					status.setRollbackOnly();
				}
				return result;
			}

		});
	}

	@Override
	public boolean deleteMember(String id) {
		return txTemplate.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				boolean result = false;

				try {
					// 기존 회원 존재 여부
					// if (memberDAO.selectMemberById(id) == null) {
					if (memberDAO.hasMemberByFld("ID", id) == false) {
						throw new Exception("삭제할 회원정보가 존재하지 않습니다.");
					}
					if (memberDAO.deleteRoles(id) == true && memberDAO.deleteMemberById(id) == true) {
						result = true;
					}
				} catch (Exception e) {
					result = false;
					log.error("MemberService.deleteMember 오류 : " + e);
					status.setRollbackOnly();
				}
				return result;
			}

		});
	}

	@Transactional(readOnly = true)
	@Override
	public boolean hasMemberByFld(String fld, String val) {
		return memberDAO.hasMemberByFld(fld, val);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean hasMemberForUpdate(String id, String fld, String val) {
		return memberDAO.hasMemberForUpdate(id, fld, val);
	}
}
