package com.javateam.memberProject.config;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailConfig {
	
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.naver.com");
		mailSender.setPort(587);
		
		mailSender.setUsername("116sally@naver.com");
		mailSender.setPassword("DusTn@1602@");
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.stmp.auth", "true");
		props.put("mail.smtp.starttls.enabled", "true");
		props.put("mail.debug", "true");
		
		return mailSender;
	}
}
