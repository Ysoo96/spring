package com.javateam.memberProject.service;

import java.util.Random;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	
	private final JavaMailSender javaMailSender;
	private static final String senderEmail = "yeon96@dongyang.ac.kr";

	// 랜덤 숫자 생성
	public String createNumber() {
		Random random = new Random();
		StringBuilder Key = new StringBuilder();
		
		for (int i = 0; i < 8; i++) { // 인증 코드 8자리
			int index = random.nextInt(3);
			
			switch (index) {
			case 0 -> Key.append((char) (random.nextInt(26) + 97)); // 소문자
			case 1 -> Key.append((char) (random.nextInt(26) + 65)); // 대문자
			case 2 -> Key.append(random.nextInt(10)); // 숫자
			}
		} // for
		return Key.toString();
	}
	
	public MimeMessage createMail(String mail, String number) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		message.setFrom(senderEmail);
		message.setRecipients(MimeMessage.RecipientType.TO,	mail);
		message.setSubject("이메일 인증");
		String body = "";
		body += "<h3>요청하신 인증 번호 입니다.</h3>";
		body += "<h1>" + number + "</h1>";
		body += "<h3>감사합니다.</h3>";
		message.setText(body, "UTF-8", "html");
		
		return message;
	}
	
	public String sendSimpleMessage(String sendEmail) throws MessagingException {
		String number = createNumber();
		
		MimeMessage message = createMail(sendEmail, number);
		
		try {
			javaMailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
		}
		
		return number;
	}
}
