package com.javateam.memberProject.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RandomMakeService {

	public String makeRandom() {
		
		String result = "";
		
		for (int i = 0; i < 4; i++) {
			int temp = (int)(Math.random() * 10);
			result += temp;
		}
		
		return result;
	}
}
