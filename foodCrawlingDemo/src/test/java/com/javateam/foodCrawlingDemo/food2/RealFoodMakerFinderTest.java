package com.javateam.foodCrawlingDemo.food2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.service.RealFoodMakerFinder;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class RealFoodMakerFinderTest {
	
	@Autowired
	RealFoodMakerFinder realFoodMakerFinder;
	
	@Test
	public void test() {

		// String productName = "육개장큰사발컵";
		String productName = "풀무원)소가부침두부290g"; // 
		log.info("추출된 상품명 : " + realFoodMakerFinder.extractPureProductName(productName));
		// 결과) 소가부침두부
	}

}