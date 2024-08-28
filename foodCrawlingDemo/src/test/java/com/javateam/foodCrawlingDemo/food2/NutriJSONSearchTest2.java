package com.javateam.foodCrawlingDemo.food2;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.service.NutriJSONSearch;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class NutriJSONSearchTest2 {

	@Autowired
	NutriJSONSearch nutriJSONSearch;

	@Test
	public void test() {

		String productName = "신라면블랙컵"; // ex 실제 상품 대응) 신라면큰사발"면"
		String makerName = "농심";
		Map<String, String> map = nutriJSONSearch.getNutriJSONMap(59, productName, makerName);

		map.entrySet().forEach(x->log.info("{}", x));
	} //

}
