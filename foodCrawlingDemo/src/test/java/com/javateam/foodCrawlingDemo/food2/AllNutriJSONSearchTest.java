package com.javateam.foodCrawlingDemo.food2;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.CUVO;
import com.javateam.foodCrawlingDemo.domain.NutriInfoVO;
import com.javateam.foodCrawlingDemo.repository.CURepository;
import com.javateam.foodCrawlingDemo.repository.NutriInfoRepository;
import com.javateam.foodCrawlingDemo.service.NutriJSONSearch;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class AllNutriJSONSearchTest {
	
	@Autowired
	NutriJSONSearch nutriJSONSearch;
	
	@Autowired
	CURepository cuRepository;
	
	@Autowired
	NutriInfoRepository nutriInfoRepository; 
	
	@Test
	public void test() {
		
		// 모든 CU_TBL 상품 요소 검색하여 영양성분이 있는지 카운터
		// 
		List<CUVO> list = (List<CUVO>)cuRepository.findAll();
		log.info("리스트 크기 : " + list.size());
		
		int count = 0;		
		// int limit = list.size();
		
		CUVO cuVO = null;
		
		// for (CUVO cuVO : list) {
		for (int i=0; i<list.size(); i++) {
			
			cuVO = list.get(i);
			
			String words[] = cuVO.getFoodName().split("\\)");		
			String realProductName = words.length == 1 ? words[0] : words[1];
			
			// 검색어 유효 검색어화
			// 접미어 "숫자, g" 제거
			// ex) 동원참치150g ==> 동원참치 
			// ex) ASMR꾸덕쫀득크림면 : 그대로 유지
			// ex) 대두유500ml ==> ml(단위) 제거 
			
			// 제조회사 제외한 제품명 정규식 규정
			// ([\w | 가-힣]*)([가-힣]+)(\w*+)
			realProductName = realProductName.replaceAll("[HEYROO | \\d+ | g | T | ml]", "");
			
			// 제조사명 : ")" 구분자로 분리
			// 특이사항) 제조사 : 헤이루(HEYROO) ==> "팔도" 로 변경
			String makerName = words.length == 1 ? "없음" : words[0];
			makerName = words[0].startsWith("HEYROO") ? "팔도" : makerName;
			
			log.info("상품 아이디 : {}, 상품명 : {}, 제조사 : {}", cuVO.getId(), realProductName, makerName);
						
			Map<String, String> map = nutriJSONSearch.getNutriJSONMap(cuVO.getId(), realProductName, makerName);
			log.info("map 크기 : " + map.size());
			
			if (map.size() > 0) {
				
				map.entrySet().forEach(x->log.info("{}", x));
				
				// 레코드 저장
				NutriInfoVO nutriInfoVO = new NutriInfoVO(map);
				log.info("NutriInfoVO : {}", nutriInfoVO);
				nutriInfoRepository.save(nutriInfoVO);
				
				count++;
			} //
			
			log.info("--- 총 상품 검색 {}건 중 총 영양소 정보가 포함된 검색 식품 수(누적수) {}건", i+1, count);
		
		} // list
		
		log.info("총 영양소 정보가 포함된 검색 식품 수 : " + count);
	} //

}
