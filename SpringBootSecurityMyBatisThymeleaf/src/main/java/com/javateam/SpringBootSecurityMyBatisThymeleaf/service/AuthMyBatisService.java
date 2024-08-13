/**
 * 
 */
package com.javateam.SpringBootSecurityMyBatisThymeleaf.service;

import com.javateam.SpringBootSecurityMyBatisThymeleaf.domain.Users;

/**
 * @author javateam
 *
 */
public interface AuthMyBatisService {
	
	boolean hasUsername(String username);
	void insertUsers(Users users, String role);

} //