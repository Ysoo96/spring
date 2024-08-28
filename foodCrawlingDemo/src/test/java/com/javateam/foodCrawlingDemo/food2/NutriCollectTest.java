package com.javateam.foodCrawlingDemo.food2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.foodCrawlingDemo.domain.CUVO;
import com.javateam.foodCrawlingDemo.domain.NutriVO;
import com.javateam.foodCrawlingDemo.repository.CURepository;
import com.javateam.foodCrawlingDemo.repository.NutriRepository;
import com.javateam.foodCrawlingDemo.service.RealFoodMakerFinder;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class NutriCollectTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	RealFoodMakerFinder realFoodMakerFinder;
	
	@Autowired
	CURepository cuRepository;
	
	@Autowired
	NutriRepository nutriRepository;
	
	@Autowired
	ServletContext application;
	
	@BeforeEach
	public void setUp() {
	
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}	
			
	// CU 식품 테이블 식품들 중에서 식약처에 영양정보가 있는 식품이 몇가지 인지 테스트 
	@Test
	public void test() throws Exception {
		
		String contextPath = application.getContextPath();
		
		List<CUVO> list = (List<CUVO>)cuRepository.findAll();
		
		log.info("리스트 크기 : " + list.size());
		
		int count = 0;		
		int limit = list.size();
		
		CUVO cuVO = null;
		
		for (int i=0; i<limit; i++) {
			
			cuVO = new CUVO();
			cuVO = list.get(i);
		
			// String productName = realFoodMakerFinder.extractPureProductName(cuVO.getFoodName()); // "빅"만 추출 (버그)
			String productName = realFoodMakerFinder.extractPureProductNameByKoalaNLP(cuVO.getFoodName()); // "빅팜" 추출 (위 문제 해결)
			String makerName = realFoodMakerFinder.extractPureMakerName(cuVO.getFoodName());
			makerName = realFoodMakerFinder.findRealMaker(productName, makerName);
			
			log.info("{} ==> {}", productName, makerName);
			
			String url = contextPath + "/foodRest?productName="+productName;
			
			if (makerName.equals("없음") == false || makerName.equals("") == false) {				
				url += "&makerName="+makerName;
			}
			
			ResultActions resultActions 
					= mockMvc.perform(get(url)
			   					.accept(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk())
								.andDo(print());
			
			// 결과가 없을 경우
			// 데이터 미검색시 : Body = {"I2790":{"total_count":"0","RESULT":{"MSG":"해당하는 데이터가 없습니다.","CODE":"INFO-200"}}}
			
			// !!!!!! 검색 정지될 경우 !!!!!!
			// {"I2790":{"total_count":"0","RESULT":{"MSG":"파일타입 값이 누락 혹은 유효하지 않습니다. 요청인자 중 TYPE을 확인하십시오.","CODE":"ERROR-301"}}}
			
			// JSON 예외 발생
			// {"I2790":{"total_count":"","RESULT":{"MSG":"유효 호출건수를 이미 초과하셨습니다.","CODE":"INFO-300"}}}
			
			String contents = resultActions.andReturn().getResponse().getContentAsString();
			
			boolean result = contents.contains("해당하는 데이터가 없습니다.") || 
						  	 contents.contains("파일타입 값이 누락 혹은 유효하지 않습니다. 요청인자 중 TYPE을 확인하십시오.") ||
						  	 contents.contains("유효 호출건수를 이미 초과하셨습니다.");
			
			log.info("결과 : {}, id = {}", result, cuVO.getId());
									
			if (result == false) { // 검색이 되었을 경우 카운터 증가

				// json 객체 처리
				JSONObject jsonObject = new JSONObject(contents);
				jsonObject = new JSONObject(jsonObject.get("I2790").toString());
				
				JSONArray jsonArray = new JSONArray(jsonObject.get("row").toString());
				
				jsonObject = new JSONObject(jsonArray.get(0).toString());
				
				NutriVO nutriVO = new NutriVO(jsonObject);
				
				// 실제 이름 저장시 원래의 DB table(nutri_tbl) 이름 그대로 저장				
				nutriVO.setFoodName(cuVO.getFoodName());
				nutriVO.setFoodSearchName(productName); // 검색된 단어
				nutriVO.setFoodId((int)cuVO.getId());
				
				log.info("NutriVO : " + nutriVO);
				
				// nutriRepository.save(nutriVO); // DB table 저장	
				
				count++;
			}
			
			log.info("count : " +count);
		} // for
		
		log.info("CU 식품 중에서 실제 식약처 영양 정보 조회가 가능한 식품 수 : " + count);
	} //

}