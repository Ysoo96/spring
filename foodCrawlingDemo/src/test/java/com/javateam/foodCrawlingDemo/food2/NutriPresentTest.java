package com.javateam.foodCrawlingDemo.food2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.javateam.foodCrawlingDemo.domain.NutriVO;
import com.javateam.foodCrawlingDemo.repository.NutriRepository;
import com.javateam.foodCrawlingDemo.service.RealFoodMakerFinder;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class NutriPresentTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	RealFoodMakerFinder realFoodMakerFinder;
	
	@Autowired
	NutriRepository nutriRepository;
	
	@Autowired
	ServletContext application;
	
	@BeforeEach
	public void setUp() {
	
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}	
	
	@Test
	public void test() {
		
		String contextPath = application.getContextPath();
		
		try {
			// 검색 경향성
			// 식약처 집계) 컵라면 종류 => 큰컵 짜장불닭볶음면, 사리곰탕컵, 컵 삼양라면, 컵 삼양라면 등
			// "컵" 이라는 단어가 앞/뒤로 불규칙적으로수록됨. 심지어는 "스낵면 Cup" 처럼 영어 표기도 있음.
			
			
			String productName = "롯데)미니비엔나70g";
			// String productName = "속초홍게라면";
			// String productName = "마동석라면";
			// String productName = "동원)양반참치죽";			
			// String productName = "롯데)빅팜";			
			// String productName = "농심)앵그리짜파구리컵";
			// String productName = "롯데)키스틱55g";
			// String productName = "농심)신라면소컵";
			
			String makerName = realFoodMakerFinder.extractPureMakerName(productName);
			
			// productName = realFoodMakerFinder.extractPureProductName(productName);
			
			String searchProductName = realFoodMakerFinder.extractPureProductNameByKoalaNLP(productName);
			
			log.info("makerName : {}", makerName);
			log.info("productName : {}", productName);
			log.info("searchProductName : {}", searchProductName);			
			
			String url = contextPath + "/foodRest?productName="+searchProductName;
			
			if (makerName.equals("없음") == false || makerName.equals("") == false) {				
				url += "&makerName="+makerName;
			}
			
			// 데이터 미검색시 : 
			// {"I2790":{"total_count":"0","RESULT":{"MSG":"해당하는 데이터가 없습니다.","CODE":"INFO-200"}}}
			// {"I2790":{"total_count":"0","RESULT":{"MSG":"파일타입 값이 누락 혹은 유효하지 않습니다. 요청인자 중 TYPE을 확인하십시오.","CODE":"ERROR-301"}}}
			// {"I2790":{"total_count":"","RESULT":{"MSG":"유효 호출건수를 이미 초과하셨습니다.","CODE":"INFO-300"}}}
			
			ResultActions resultActions 
					= mockMvc.perform(get(url)
			   					.accept(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk())
								.andDo(print());
			
			
			String contents = resultActions.andReturn().getResponse().getContentAsString();
			
			boolean result = contents.contains("해당하는 데이터가 없습니다.") || 
						  	 contents.contains("파일타입 값이 누락 혹은 유효하지 않습니다. 요청인자 중 TYPE을 확인하십시오.") ||
						  	 contents.contains("유효 호출건수를 이미 초과하셨습니다.");
					
			// 결과가 없을 경우
			log.info("결과 : " + result);
			
			// 검색된 식품 결과 예시 : 
			// {"I2790":{"total_count":"1","row":[{"NUTR_CONT8":"0.5","NUTR_CONT9":"0","NUTR_CONT4":"2.6","NUTR_CONT5":"0","NUTR_CONT6":"465","NUM":"1","NUTR_CONT7":"10","NUTR_CONT1":"165","NUTR_CONT2":"29","SUB_REF_NAME":"식약처(\u002719)","NUTR_CONT3":"7","RESEARCH_YEAR":"2019","MAKER_NAME":"㈜동원에프앤비","GROUP_NAME":"즉석식품류","SERVING_SIZE":"287.5","SERVING_UNIT":"104","SAMPLING_REGION_NAME":"전국(대표)","SAMPLING_MONTH_CD":"AVG","SAMPLING_MONTH_NAME":"평균","DESC_KOR":"양반참치죽","SAMPLING_REGION_CD":"ZZ","FOOD_CD":"P025824"}],"RESULT":{"MSG":"정상처리되었습니다.","CODE":"INFO-000"}}}
			
			JSONObject jsonObject = new JSONObject(contents);
			jsonObject = new JSONObject(jsonObject.get("I2790").toString());
			
			// 미검색시 : 예외처리 : org.json.JSONException: JSONObject["row"] not found.
			try {
				JSONArray jsonArray = new JSONArray(jsonObject.get("row").toString());
				jsonObject = new JSONObject(jsonArray.get(0).toString());
				
				NutriVO nutriVO = new NutriVO(jsonObject);
				
				// 실제 이름 저장시 원래의 DB table(nutri_tbl) 이름 그대로 저장				
				nutriVO.setFoodName(productName);
				nutriVO.setFoodSearchName(searchProductName); // 검색된 단어
							
				log.info("NutriVO : " + nutriVO);				
				// nutriRepository.save(nutriVO); // DB table 저장
				
			} catch (Exception e) {
				
				log.error("예외발생 : " + e);
			}
			
		} catch (Exception e) {
			log.error("예외처리 : " + e);
			e.printStackTrace();
		}
	}
}