package com.javateam.foodCrawlingDemo.food3;

import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.CuNutriInfoVO;
import com.javateam.foodCrawlingDemo.repository.CURepository;
import com.javateam.foodCrawlingDemo.repository.CuNutriInfoRepository;
import com.javateam.foodCrawlingDemo.service.RealFoodMakerFinder;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class NutriCollectTest3 {
	
	@Autowired
	RealFoodMakerFinder realFoodMakerFinder;
	
	@Autowired
	CURepository cuRepository;
	
	@Autowired
	CuNutriInfoRepository cuNuriInfoRepository;

	// CU 식품 테이블 식품들 중에서 식약처에 영양정보가 있는 식품이 몇가지 인지 테스트 
	@Test
	public void test() throws Exception {
		
		int count = 0; // 실제 계수된 상품 수
		Map<Integer, Integer> map = new TreeMap<>();
		
		for (int i=1; i<20; i++) {
			map.put(i, 0);	
		}
		
		// 주의) 저장간 중간 시간간격을 주지 않으면 HTTP error code 429 발생하므로 일정한 간격으로 반복 카운터를 나누어서 크롤링 진행할 것 
		// HTTP 429 Too Many Requests 응답 상태 코드는 사용자가 주어진 시간 동안 너무 많은 요청을 보냈음을 나타냅니다("속도 제한").  
		// 총 251 페이지 분량 
		for (int i=1; i<=251; i++) {
		// for (int i=1; i<=2; i++) { // OK
		// for (int i=3; i<=3; i++) { // OK
		// for (int i=4; i<=10; i++) { // OK
		// for (int i=11; i<=20; i++) { // OK
		// for (int i=21; i<=251; i++) { // OK
			
			// 새로운 영양소 검색 사이트 활용
			// https://www.fatsecret.kr/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search
			// String url = contextPath + "/https://www.fatsecret.kr/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search?q=";

			Document doc;
			
			// 전체 CU 상품 목록(영양소 정보)
			// https://www.fatsecret.kr/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search?q=CU&pg=251
			String url = "https://www.fatsecret.kr/칼로리-영양소/search?q=CU&pg="+i;
						
			try {
				
				doc = Jsoup.connect(url).get();
				
				Elements products = doc.select("table[class='generic searchResult'] td.borderBottom a[class='prominent']");
				
				int pageProductsLen = products.size();
				
				log.info("{} 페이지 상품수 : {}", i, pageProductsLen);
				
				for (int j=0; j<pageProductsLen; j++) {
					
					Element element = products.get(j);
					String productName = element.text(); // 식품명
					String productDetailURL = element.attr("href"); // 개별 상품 링크
					
					//////////////////////////////////////////////////////////////////////////////////////////
					
					productDetailURL = "/칼로리-영양소/cu/" + productName.replaceAll(" ", "-") + "/1인분";
					
					log.info("상품명 : {}, productDetailURL : {}", productName, productDetailURL);
									
					
					doc = Jsoup.connect("https://www.fatsecret.kr" + productDetailURL).get();
				
					/*
					 * https://www.foodsafetykorea.go.kr/api/newDatasetDetail.do
					 * 
					 * 필드 설명
					 * 
					 * 번호	항목	설명
						1	NUM	번호
						2	FOOD_CD	식품코드
						3	SAMPLING_REGION_NAME	지역명
						4	SAMPLING_MONTH_NAME	채취월
						5	SAMPLING_REGION_CD	지역코드
						6	SAMPLING_MONTH_CD	채취월코드
						7	GROUP_NAME	식품군
						8	DESC_KOR	식품이름
						9	RESEARCH_YEAR	조사년도
						10	MAKER_NAME	제조사명
						11	SUB_REF_NAME	자료출처
						12	SERVING_SIZE	총내용량
						13	SERVING_UNIT	총내용량단위
						14	NUTR_CONT1	열량(kcal)(1회제공량당)
						15	NUTR_CONT2	탄수화물(g)(1회제공량당)
						16	NUTR_CONT3	단백질(g)(1회제공량당)
						17	NUTR_CONT4	지방(g)(1회제공량당)
						18	NUTR_CONT5	당류(g)(1회제공량당)
						19	NUTR_CONT6	나트륨(mg)(1회제공량당)
						20	NUTR_CONT7	콜레스테롤(mg)(1회제공량당)
						21	NUTR_CONT8	포화지방산(g)(1회제공량당)
						22	NUTR_CONT9	트랜스지방(g)(1회제공량당)
					 */
					
					// 열량1    열량2     탄수화물  설탕당(당류)  단백질  지방    포화지방  트랜스지방   콜레스테롤  나트륨 
					// 3527 kJ 843 kcal 100.00g 8.00g      32.00g 35.00g 10.000g 0.000g     124mg     987mg
					// 단위 정보 제거															   
					log.info("정보 : " + doc.select("div[class='nutrition_facts international'] div[class$='right tRight']").text());
					
					String nutrConts[] = doc.select("div[class='nutrition_facts international'] div[class$='right tRight']").text()
											.replaceAll("[a-zA-Z]","") // 단위 정보 제거
											.replaceAll("  ", " ")
											.split("\\s");
					
					log.info("영양소 갯수 : {}", nutrConts.length);
					
					map.put(nutrConts.length, map.get(nutrConts.length) + 1);
										
					float nutrCont1 = 0;
					float nutrCont2 = 0;
					float nutrCont3 = 0;
					float nutrCont4 = 0;
					float nutrCont5 = 0;
					float nutrCont6 = 0;
					float nutrCont7 = 0;
					float nutrCont8 = 0;
					float nutrCont9 = 0;
					
					// 공통
					if (nutrConts.length >= 7 && nutrConts.length <= 14) {
						
						nutrCont1 = Float.parseFloat(nutrConts[1].trim()); // 열량
						nutrCont2 = Float.parseFloat(nutrConts[2].trim()); // 탄수화물 
						nutrCont3 = Float.parseFloat(nutrConts[4].trim()); // 단백질 
						nutrCont4 = Float.parseFloat(nutrConts[5].trim()); // 지방 
						nutrCont5 = Float.parseFloat(nutrConts[3].trim()); // 당류
					}
					
					
					if (nutrConts.length == 14) { // ex) 리치참치마요
						
						// 1423 kJ 340 kcal 47.00g 1.00g 11.00g 12.00g 1.900g 0.000g 0.000g 0.000g 15mg 0.0g 640mg 0mg
						nutrCont6 = Float.parseFloat(nutrConts[12].trim()); // 나트륨
						nutrCont7 = Float.parseFloat(nutrConts[10].trim()); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat(nutrConts[7].trim()); // 트랜스지방
						
					} else if (nutrConts.length == 13) { // ex) 하프앤하프 샌드위치	
						
						// 1234 kJ 295 kcal 28.00g 7.00g 12.00g 15.00g 4.200g 0.000g 0.000g 0.000g 75mg 0.0g 800mg
						
						nutrCont6 = Float.parseFloat(nutrConts[10].trim()); // 나트륨
						nutrCont7 = Float.parseFloat(nutrConts[9].trim()); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat(nutrConts[7].trim()); // 트랜스지방
					
					} else if (nutrConts.length == 12) { //	ex) 통참치김밥
						
						// 2176 kJ 520 kcal 75.00g 9.00g 19.00g 16.00g 2.700g 0.000g 0.000g 0.000g 20mg 1330mg
						
						nutrCont6 = Float.parseFloat(nutrConts[11].trim()); // 나트륨
						nutrCont7 = Float.parseFloat(nutrConts[10].trim()); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat(nutrConts[7].trim()); // 트랜스지방
					
					} else if (nutrConts.length == 11) { // ex) 3XL 돈까스
						
						// 1389 kJ 332 kcal 48.00g 2.00g 8.00g 12.00g 2.700g 0.100g 2.700g 15mg 490mg
						
						nutrCont6 = Float.parseFloat(nutrConts[10].trim()); // 나트륨
						nutrCont7 = Float.parseFloat(nutrConts[9].trim()); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat(nutrConts[7].trim()); // 트랜스지방
						
					
					} else if (nutrConts.length == 10) { // 김치볶음참치마요
						
						nutrCont6 = Float.parseFloat(nutrConts[9].trim()); // 나트륨
						nutrCont7 = Float.parseFloat(nutrConts[8].trim()); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat(nutrConts[7].trim()); // 트랜스지방
						
					} else if (nutrConts.length == 9) { // ex) 매운 돼지 갈비찜
						
						// 1565 kJ 374 kcal 27.00g 3.00g 42.00g 11.00g 3.000g 78mg 1174mg
					
						nutrCont6 = Float.parseFloat(nutrConts[8].trim()); // 나트륨
						nutrCont7 = Float.parseFloat(nutrConts[7].trim()); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat("0f"); // 트랜스지방
					
					} else if (nutrConts.length == 8) { // ex) 신선 야채 김밥
					
						nutrCont6 = Float.parseFloat(nutrConts[7].trim()); // 나트륨
						nutrCont7 = Float.parseFloat("0f"); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat("0f"); // 트랜스지방
						
					} else if (nutrConts.length == 7) {

						nutrCont6 = Float.parseFloat(nutrConts[9].trim()); // 나트륨
						nutrCont7 = Float.parseFloat("0f"); // 콜레스테롤 
						nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산 
						nutrCont9 = Float.parseFloat("0f"); // 트랜스지방
						
					} else if (nutrConts.length == 5) { // ex) 김치제육
						
						// 2925 kJ 699 kcal 113.00g 28.00g 15.00g

						nutrCont1 = Float.parseFloat(nutrConts[1].trim()); // 열량
						nutrCont2 = Float.parseFloat(nutrConts[2].trim()); // 탄수화물 
						nutrCont3 = Float.parseFloat(nutrConts[3].trim()); // 단백질 
						nutrCont4 = Float.parseFloat(nutrConts[4].trim()); // 지방 
						nutrCont5 = Float.parseFloat("0f"); // 당류
						
						nutrCont6 = Float.parseFloat("0f"); // 나트륨
						nutrCont7 = Float.parseFloat("0f"); // 콜레스테롤 
						nutrCont8 = Float.parseFloat("0f"); // 포화지방산 
						nutrCont9 = Float.parseFloat("0f"); // 트랜스지방
					}
								
					
					log.info("열량(nutrCont1) : {}", nutrCont1);
					log.info("탄수화물(nutrCont2) : {}", nutrCont2);
					log.info("단백질(nutrCont3) : {}", nutrCont3);
					log.info("지방(nutrCont4) : {}", nutrCont4);
					log.info("당류(nutrCont5) : {}", nutrCont5);
					log.info("나트륨(nutrCont6) : {}", nutrCont6);
					log.info("콜레스테롤(nutrCont7) : {}", nutrCont7);
					log.info("포화지방산(nutrCont8) : {}", nutrCont8);
					log.info("트랜스지방(nutrCont9) : {}", nutrCont9);
					
					
					CuNutriInfoVO cuNutriInfoVO
							= CuNutriInfoVO.builder()							 			   	
									 	  .foodName(productName)
										  .makerName("CU")
										  .nutrCont1(nutrCont1)
										  .nutrCont2(nutrCont2)
										  .nutrCont3(nutrCont3)
										  .nutrCont4(nutrCont4)
										  .nutrCont5(nutrCont5)
										  .nutrCont6(nutrCont6)
										  .nutrCont7(nutrCont7)
										  .nutrCont8(nutrCont8)
										  .nutrCont9(nutrCont9)
										  .build();
					
					cuNuriInfoRepository.save(cuNutriInfoVO);
				
					count++; // 검색된 상품수
					
					Thread.sleep(2000 + (int)(Math.random() * 3000)); // HTTP error code 429 방지 시간 간격 주기
					
				} // for j		
				
			} catch (Exception e) {
				
				log.error("크롤링 예외 " + e);
				e.printStackTrace();
				continue;
			} //
			
			// 200초 + a
			Thread.sleep(200000 + (int)(Math.random() * 3000)); // HTTP error code 429 방지 시간 간격 주기

		} // for i
		
		log.info("###### count : {}", count);
		
		for (int j=1; j<20; j++) {
			log.info("{} => {}", j, map.get(j));	
		}
		
	} //

}