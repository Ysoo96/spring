package com.javateam.memberProject.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileDeleteMapper {

	@Delete("DELETE FROM UPLOAD_FILE_TBL WHERE ID = #{id}")
	public void deleteFile(@Param("id") int id);

}