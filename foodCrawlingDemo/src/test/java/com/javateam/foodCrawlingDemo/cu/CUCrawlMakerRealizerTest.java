package com.javateam.foodCrawlingDemo.cu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.javateam.foodCrawlingDemo.domain.CUVO;
import com.javateam.foodCrawlingDemo.repository.CURepository;
import com.javateam.foodCrawlingDemo.service.CUCrawlService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CUCrawlMakerRealizerTest {
	
	// 식약처 DB를 통한 영양성분 추출을 위한 제조회사(Maker) 추출
	// ex) 동서)둥글레차25T → 동서 →  	
	
	// https://various.foodsafetykorea.go.kr/nutrient/detail/search/list.do
	
	@Test
	public void test() {
		
		String url = "https://various.foodsafetykorea.go.kr/nutrient/simple/search/companyCode/listPop.do?searchText=동서";
		// 결과 HTML 피드
		
		RestTemplate restTemplate = new RestTemplate();
		
		// HTTP header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		 
		// 응답(response) 정보
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class); 
		
		// https://jsoup.org/cookbook/input/parse-document-from-string
		Document doc = Jsoup.parse(response.getBody().trim()); // response.getBody();
//		log.info("doc : " + doc.html());
		
		Elements elements = doc.select("div#pop-wrap div#content form#listForm table.table td input");
		log.info("요소 인쇄 : " + elements.get(0).attr("id"));
						
	} //

}