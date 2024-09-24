package com.javateam.memberProject.service;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;
	
	public void sendSimpleMessage(String from, String to, String subject, String text) {
        
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(from);
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
	
	// https://docs.spring.io/spring-framework/reference/6.0/integration/email.html#mail-usage-mime
	public void sendMIMEMessage(String from, String to, String subject, String text) {
		
		log.info("전송시작");
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage message) throws Exception {

				final MimeMessageHelper mailHelper = new MimeMessageHelper(message, true, "UTF-8");
				 
				mailHelper.setFrom(from);
				mailHelper.setTo(to); 
				mailHelper.setSubject(subject); 
				mailHelper.setText(text, true); // html = true
			} //
		
		};
		
        emailSender.send(preparator);
        
        log.info("전송끝");
    } //

}