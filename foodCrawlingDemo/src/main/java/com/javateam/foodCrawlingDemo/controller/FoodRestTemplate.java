package com.javateam.foodCrawlingDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.javateam.foodCrawlingDemo.service.RealFoodMakerFinder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FoodRestTemplate {
	
	@Autowired
	RealFoodMakerFinder realFoodMakerFinder;

	// https://www.foodsafetykorea.go.kr/api/openApiInfo.do?menu_grp=MENU_GRP31&menu_no=661&show_cnt=10&start_idx=1&svc_no=I2790&svc_type_cd=API_TYPE06#
	
	// ex) http://openapi.foodsafetykorea.go.kr/api/27bc9f6ae67646b7b680/I2790/json/1/5/DESC_KOR=고추참치
	
	// curl test) curl -v -X GET "http://openapi.foodsafetykorea.go.kr/api/27bc9f6ae67646b7b680/I2790/json/1/1/DESC_KOR=고추	참치"
	
	@GetMapping("/foodRest")
	public ResponseEntity<String> proxyAction(@RequestParam("productName") String productName, 
											  @RequestParam(value="makerName", defaultValue="없음") String makerName) {
		
		log.info("Rest test 시작");
		
		/* 원(original) 결과물 예시(JSON)
		 * 
		 {"NUTR_CONT8":"2","NUTR_CONT9":"0","NUTR_CONT4":"10","NUTR_CONT5":"2",
		  "NUTR_CONT6":"520","NUM":"1","NUTR_CONT7":"15","NUTR_CONT1":"280",
		  "NUTR_CONT2":"39","SUB_REF_NAME":"식약처(\u002719)","NUTR_CONT3":"8",
		  "RESEARCH_YEAR":"2019","MAKER_NAME":"㈜지엠에프","GROUP_NAME":"즉석식품류",
		  "SERVING_SIZE":"100","SERVING_UNIT":"104","SAMPLING_REGION_NAME":"전국(대표)",
		  "SAMPLING_MONTH_CD":"AVG","SAMPLING_MONTH_NAME":"평균","DESC_KOR":"속을 꽉채운 왕교자만두",
		  "SAMPLING_REGION_CD":"ZZ","FOOD_CD":"P027588"}
		 */
		
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
		
		String authKey = "여기에 인증키를 입력합니다."; // 인증키
		
		// String productName = "왕교자만두";
		// String productName = "참깨라면";
		// String productName = "옛날왕교자만두";
		// String productName = "열라면";
		// String productName = "불닭볶음면";
		// String makerName = "오뚜기";
		// String makerName = "CJ";
		
		int limit = 20;
		
		// ex) 롯데)미니비엔나70g → 롯데
		// String makerName = productName.split("\\)")[0];
		
		makerName = realFoodMakerFinder.findRealMaker(productName, makerName);
		
		log.info("makerName : " + makerName);
		
		String realProductName = realFoodMakerFinder.extractPureProductName(productName);
		
		log.info("realProductName : " + realProductName);
	
		
		String url = "http://openapi.foodsafetykorea.go.kr/api/"+authKey
				   + "/I2790/json/1/"+limit
				   + "/DESC_KOR="+realProductName;
		
		if (makerName.equals("") == false || makerName.equals("없음") == false) {
			
			url += "&MAKER_NAME="+makerName;
		}
			
		
		log.info("url :{}", url);
		
        // 실질적인 proxy 컨트롤러의 핵심은 RestTemplate입니다.
        // 웹 브라우저에서 노출 위험이 있는 정보(가령, 개인 REST key 등)을 백단에서 처리하기 때문에 
        // 보다 안전하게 REST Service를 구현할 수 있습니다.
        // RestTemplate
		RestTemplate restTemplate = new RestTemplate();
				
		// HTTP header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		 
		// 응답(response) 정보
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class); 
		
		log.info("response : " + response);
		log.info("response Code : " + response.getStatusCode());
		log.info("response Body : " + response.getBody());
		
		log.info("Rest test 끝");
		
		// 추후에 할 수 있다면 try ~ catch 구문을 두어서 HTTP error 들에 대비된 코드를 삽입하여 보완하는 것이 좋습니다.
		return response; 
	} //
	
} //