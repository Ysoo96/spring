package com.javateam.foodCrawlingDemo.cu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.CUVO;
import com.javateam.foodCrawlingDemo.repository.CURepository;
import com.javateam.foodCrawlingDemo.service.CUCrawlService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CUCrawlFoodListPagesTest6 {

	@Autowired
	CUCrawlService cuCrawlService;

	@Autowired
	CURepository cuRepository;

	@Test
	public void test() {

		// 메인 카테고리(searchMainCategory) (현재)
		// : 간편식사=10, 즉석조리=20, 과자류=30, 아이스크림=40, 식품=50, 음료=60, (생활용품=70)

		// 각 카테고리별 총페이지 수 (현재)
		// : 간편식사=6, 즉석조리=3, 과자류=45, 아이스크림=9, 식품=67, 음료=27, (생활용품=70)

		// ex) 간편식사 카테고리
		// https://cu.bgfretail.com/product/productAjax.do?pageIndex=1&listtType=2&searchMainCategory=10

		// 간편식사(대분류)에 따른 식품 크롤링
		Map<String, Integer> map = new HashMap<>();
		map.put("간편식사", 6);
		map.put("즉석조리", 3);
		map.put("과자류", 45);
		map.put("아이스크림", 9);
		map.put("식품", 67);
		map.put("음료", 27);

		List<List<CUVO>> cuLists = new ArrayList<>();

		map.forEach((k,v) -> {
			cuLists.add(cuCrawlService.crawlProductList(k,v));
		});

		for (List<CUVO> list : cuLists) {

			for (CUVO cuVO : list) {

				cuRepository.save(cuVO);
				log.info("cu : " + cuVO);

			} //

		} //

	} //

}