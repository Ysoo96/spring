package com.javateam.SpringBootSecurityMyBatisThymeleaf.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.javateam.SpringBootSecurityMyBatisThymeleaf.domain.Users;

import lombok.extern.slf4j.Slf4j;
/*
 * Spring JDBC 사용 : javateacher
 */
@Repository("authJdbcService")
@Slf4j
public class AuthJdbcService {
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public boolean hasUsername(String username) {
    	
    	boolean flag = false;
    	String sql = "SELECT * FROM users WHERE username=?";
    	
    	log.info("hasUsername : " + username);
    	
    	try {
    		
    		 Users user = (Users)this.jdbcTemplate
    				 				 .queryForObject(sql, 
								   				     new BeanPropertyRowMapper<Users>(Users.class),
								   				     new Object[]{ username });
    		 
    		 flag = user != null ? true : false;
    		 
		} catch (Exception e) {
			flag=false;
		}
    	
    	return flag; // 
    } //
    
    
    public void insertUsers(Users users, String role) {
    	
    	log.info("insertUsers");
    	
    	String sql  = "INSERT INTO users VALUES (?,?,1)";
    	String sql2 = "INSERT INTO user_roles VALUES "
    				+ "(user_roles_seq.nextval,?,?)";
    	
    	this.jdbcTemplate.update(sql, 
    							 new Object[] { users.getUsername(), 
											    users.getPassword() });
    	
    	this.jdbcTemplate.update(sql2, 
    							 new Object[] { users.getUsername(),
											    role });
    	
    } //

} //