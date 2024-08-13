/**
 * 
 */
package com.javateam.SpringBootSecurityMyBatisThymeleaf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.javateam.SpringBootSecurityMyBatisThymeleaf.domain.Role;
import com.javateam.SpringBootSecurityMyBatisThymeleaf.domain.Users;



/**
 * mapper
 * 
 * @author javateam
 *
 */
public interface UserMapper {
	
	Users getUserByUsername(String userName);
	List<Role> getUserRolesByUsername(String userName);
	
	int hasUsername(String username);
	
	// Users loadUserByUsername(String userName);
	void insertUser(@Param("users") Users users);
	void insertUserRoles(@Param("username") String username, 
						 @Param("role") String role);

}