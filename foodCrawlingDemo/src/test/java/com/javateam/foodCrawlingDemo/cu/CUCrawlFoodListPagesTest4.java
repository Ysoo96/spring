package com.javateam.foodCrawlingDemo.cu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.CUVO;
import com.javateam.foodCrawlingDemo.repository.CURepository;
import com.javateam.foodCrawlingDemo.service.CUCrawlService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CUCrawlFoodListPagesTest4 {
	
	@Autowired
	CUCrawlService cuCrawlService;
	
	@Autowired
	CURepository cuRepository;

	@Test
	public void test() {

//		 - 메인 카테고리(searchMainCategory) (현재) 
//		 : 간편식사=10, 즉석조리=20, 과자류=30, 아이스크림=40, 식품=50, 음료=60, (생활용품=70)
//		 	
//		 - 각 카테고리별 총페이지 수 (현재)
//		 : 간편식사=6, 즉석조리=3, 과자류=45, 아이스크림=9, 식품=67, 음료=27, (생활용품=70)
		 
		// 간편식사(대분류)에 따른 식품 크롤링 
		List<CUVO> cuList = cuCrawlService.crawlProductList("간편식사", 6);
		// cuList.forEach(x-> { log.info("x : " + x); });
		
		for (CUVO cuVO : cuList) {
			cuRepository.save(cuVO);
		} //
						
	} //

}