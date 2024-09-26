package com.javateam.memberProject.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	private final ApplicationEnvironmentConfig envConfig;
	
	public String getNaverAuthorizeUrl(String type) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
		
		String baseUrl = envConfig.getConfigValue("login.naver.baseUrl");
		String clientId = envConfig.getConfigValue("login.naver.clientId");
	}
}
