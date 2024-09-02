package com.javateam.foodCrawlingDemo.shopHistory;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.NutriInfoVO;
import com.javateam.foodCrawlingDemo.domain.ShoppingHistoryVO;
import com.javateam.foodCrawlingDemo.repository.NutriInfoRepository;
import com.javateam.foodCrawlingDemo.repository.ShoppingHistoryRepository;
import com.javateam.foodCrawlingDemo.service.RandomShoppingHistoryMakerService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FinalShoppingHistoryRandomMakerTest {

	@Autowired
	NutriInfoRepository nutriInfoRepository;

	@Autowired
	ShoppingHistoryRepository shoppingHistoryRepository;

	@Autowired
	RandomShoppingHistoryMakerService randomShoppingHistoryMakerService;

	@Test
	public void test() {

		ShoppingHistoryVO shoppingHistoryVO;

		// n개의 임의의 구매 이력 데이터 생성  ex) 가령 5만건 이상
		//
		// -- 생성 규정
		// - 레코드 : 한 사용자가 여러 번의 레코드 생성 가능
		// - 사용자 아이디 : "mbc_" + 4자리수(증가) 로 구성  ex) 간혹 구매 이력이 없는 회원이 있을 수 있음.
		// - 구매 상품 : 임의 생성
		// - 구매 수량 : 1 ~ 10개 사이에서 임의 생성  ex) 편의점에서는 대형 마트과 달리 소량 구매하는 일반적 고객 편향성 반영
		// - 구매일 : 오늘 이전 날짜로 2022 ~ 오늘까지의 임의 데이터 생성

		// NUTRI_INFO_TBL에서 상품 추출
		// 추출 방식은 food_name (총 270개) List 적재후 random() 메소드로 추출

		List<NutriInfoVO> foodList = (List<NutriInfoVO>) nutriInfoRepository.findAll();

		log.info("--------------------------------------------- : " + foodList.size());

		List<String> foodNames = foodList.stream()
								         .map(NutriInfoVO::getFoodName)
								         .toList();

		foodNames.forEach(x -> { log.info("{}", x); });

		for (int i=0; i<1000; i++) { // 약 5만건 이상 구매이력 생성

			log.info("-- {} --", i);

			// 고객 임의 아이디 생성 : 추후 고객정보 생성시에는 이를 반영하여
			String userId = "mbc_" + new DecimalFormat("0000").format((int)(Math.random() * 100) + 1);

			log.info("userId : " + userId);

			List<String> chosenFoods = randomShoppingHistoryMakerService.chooseRandomFoods(foodNames);

			// 전체 식품 중 자주 먹는 식품들에 들어가는 단어들(밥, 컵, 면)의 비중 : 정규식(regex)으로 검출
			long len = chosenFoods.stream()
								  .filter(x->Pattern.matches("((?=.*[밥|컵|면])(?=.*[가-힣])).{1,}", x))
								  .count();

			long totLen = chosenFoods.size();

			log.info("전체 식품 중 자주 먹는 식품들에 들어가는 단어들(밥, 컵, 면)의 비중 : {}/{}", len, totLen);

			for (String foodName : chosenFoods) {

				shoppingHistoryVO = new ShoppingHistoryVO();

				int quantity = (int)(Math.random() * 10) + 1;
				Date purchaseDate = randomShoppingHistoryMakerService.makeDate();

				shoppingHistoryVO.setUserId(userId);
				shoppingHistoryVO.setFoodName(foodName);
				shoppingHistoryVO.setQuantity(quantity);
				shoppingHistoryVO.setPurchaseDate(purchaseDate);

				log.info("-- {}", shoppingHistoryVO.getFoodName());

				shoppingHistoryRepository.save(shoppingHistoryVO);

			} // for

		} // for

	} //

}