package com.javateam.foodCrawlingDemo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.foodCrawlingDemo.repository.NutriInfoRepository;
import com.javateam.foodCrawlingDemo.repository.ShoppingHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RandomShoppingHistoryMakerService {

	@Autowired
	NutriInfoRepository nutriInfoRepository;
	
	@Autowired
	ShoppingHistoryRepository shoppingHistoryRepository;
	
	// 구매 이력 임의 생성
	public List<String> chooseRandomFoods(List<String> foodNames) {
		
		int limit = 20 + (int) (Math.random() * 10);
		int count = 0;
		List<String> result = new ArrayList<>();
		
		// 인자로 입력된 음식 리스트 중에서 선정하되,
		// 편의점에서 대체로 선호되는 식품들에 들어가는 단어들(밥, 컵, 면)이 빈도있게 선정되도록 조정
		while (true) {
			
			String food = foodNames.get((int)(Math.random() * foodNames.size()));
			
			if (Pattern.matches("((?=.*[밥|컵|면])(?=.*[가-힣])).{1,}", food)) {
				
				result.add(food);
				
				// 자주 먹는 식품(밥, 컵, 면 등)에 대한 최대 가중치 5 + 10%로 추가
				// 가중치는 필요시 조정할 수 있다
				for (int i = 0; i < 5 + (int)(limit * 0.10f); i++) {
					result.add(food);
				} // for
			} else {
				result.add(food);
			}
			
			count++;
			
			if (count > limit) break;
		} // while
		
		Collections.shuffle(result);
		
		long riceNoodlelen = result.stream().filter(x -> Pattern.matches("((?=.*[쌀|컵|면])(?=.*[가-힣])).{1,}", x)).count();
		
		log.info("추출한 음식수 : {}/{}", riceNoodlelen, result.size());
		
		return result;
	}
	
	// 임의 날짜 생성 : 2023 ~ 2024년 (금일) 생성 : 필요시 이전일자의 시작점은 조정 가능
	public Date makeDate() {
		
		Date result = null;
		
		while (true) {
			
			String year = String.format("202%s", (int)(Math.random() * 3 + 2));
			int month = (int)(Math.random() * 12) + 1;
			String monthStr = month < 10 ? "0" + month : "" + month;
			
			int date = month == 2 ? 28 : (int)(Math.random() * 31) + 1;
			String dateStr = date < 10 ? "0" + date : "" + date;
			
//			log.info("year : {}", year);
//			log.info("monthStr : {}",monthStr);
//			log.info("dateStr : {}", dateStr);
			
			String dateTemp = year + "-" + monthStr + "-" + dateStr;
			
			Date purchaseDate = Date.valueOf(dateTemp);
			
			// 오늘 날짜봗 미래인지 점검하여 조건에 맞을 때까지 날짜 추출
			if (purchaseDate.getTime() > System.currentTimeMillis()) {
				continue;
			} else {
				result = purchaseDate;
				break;
			}
		} // while
		
		return result;
		
	}
	
}
