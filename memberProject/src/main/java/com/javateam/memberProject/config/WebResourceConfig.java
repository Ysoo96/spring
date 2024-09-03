package com.javateam.memberProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebResourceConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/bootstrap/**")
				.addResourceLocations("classpath:/META-INF/resources/"
										+ "webjars/bootstrap/");
		// axios
		registry.addResourceHandler("/axios/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/axios/");
		
		// bootstrap icons
		registry.addResourceHandler("/bootstrap-icons/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap-icons/");
		
		// jQuery
		 registry.addResourceHandler("/jquery/**")
		 		 .addResourceLocations("classpath:/META-INF/resources/webjars/jquery/");

		// 게시판 관련 : summernote 자원 경로 추가
		 registry.addResourceHandler("/summernote/**")
		 		 .addResourceLocations("classpath:/META-INF/resources/webjars/summernote/");

	}

}
