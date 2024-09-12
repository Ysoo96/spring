package com.javateam.memberProject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwGenTest {

	public static void main(String[] args) {
		
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		String pw = "#Abcd1234";
		
		for (int i=0; i < 10; i++) {
			System.out.println(pwEncoder.encode(pw));
		}

	}

}
