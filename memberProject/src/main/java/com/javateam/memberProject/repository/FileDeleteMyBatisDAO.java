package com.javateam.memberProject.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.memberProject.mapper.FileDeleteMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FileDeleteMyBatisDAO {

	@Autowired
	SqlSession sqlSession;
	
	public void deleteFile(int id) {
		
		sqlSession.getMapper(FileDeleteMapper.class).deleteFile(id);
	}
}
