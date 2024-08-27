package com.javateam.demo2.repository;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.demo2.domain.MemberVO;
import com.javateam.demo2.repository.MemberDAOImpl;

@Repository
public class MemberDAOImpl implements MemberDAO {
	private static final Logger log = LoggerFactory.getLogger(MemberDAOImpl.class);

	@Autowired
	SqlSession sqlSession;
	
	@Override
	public boolean insertMember(MemberVO memberVO) {
		boolean returnValue = false;
		try {
			int result = sqlSession.insert("mapper.DemoMapper.insertMember", memberVO);
			returnValue = true;
		} catch (Exception e) {
			log.error("insertMember 오류 : " + e);
			e.printStackTrace();
			returnValue = false;
		}
		return returnValue;
	}

}
