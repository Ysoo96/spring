package com.javateam.memberProject.service;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public interface MailServiceInter {
	MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;
	
	String createKey();
	
	String sendSimpleMessage(String to) throws Exception;
}
