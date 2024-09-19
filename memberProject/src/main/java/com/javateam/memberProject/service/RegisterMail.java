package com.javateam.memberProject.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

@Service
public class RegisterMail implements MailServiceInter {
	@Autowired
	JavaMailSender emailSender;
	
	private String ePw;
	
	@Override
	public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
		System.out.println("메일받을 사용자 : " + to);
		System.out.println("인증번호 : " + ePw);
		
		MimeMessage message = emailSender.createMimeMessage();
		
		message.addRecipients(RecipientType.TO, to);
		message.setSubject("이메일 인증코드 입니다.");
		
		String msgg = "";
		msgg += "<h1>안녕하세요</h1>";
		msgg += "<h1>치매 예방 레시피 플랫폼 메모리테이블 입니다.</h1>";
		msgg += "<br>";
		msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>회원가입 인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>" ;
        msgg += "</div>";
        
        message.setText(msgg, "UTF-8", "html");
        message.setFrom(new InternetAddress("116sally@naver.com", "메모리 테이블"));
        
        return message;
	}
	
	@Override
	public String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 8; i++) { // 인증코드 8자리
			int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

			switch (index) {
			case 0:
				key.append((char) ((int) (rnd.nextInt(26)) + 97));
				// a~z (ex. 1+97=98 => (char)98 = 'b')
				break;
			case 1:
				key.append((char) ((int) (rnd.nextInt(26)) + 65));
				// A~Z
				break;
			case 2:
				key.append((rnd.nextInt(10)));
				// 0~9
				break;
			} // switch
		} // for

		return key.toString();
	}
	
	@Override
	public String sendSimpleMessage(String to) throws Exception {
		ePw = createKey();
		
		MimeMessage message = createMessage(to);
		
		try {
			emailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		return ePw;
	}
}
