package com.javateam.foodCrawlingDemo.food3;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
public class NutriCollectTest1 {

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

		CUVO cuVO = null;
		cuVO = new CUVO();

		String foodName = "백종원 더블까스 김밥";

		String productName = realFoodMakerFinder.extractPureProductNameByKoalaNLP(foodName);
		String makerName = realFoodMakerFinder.extractPureMakerName(foodName);
		makerName = realFoodMakerFinder.findRealMaker(productName, makerName);

		log.info("{} ==> {}", productName, makerName);

		//////////////////////////////////////////////////////////////////////////


		// 새로운 영양소 검색 사이트 활용
		// https://www.fatsecret.kr/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search
		// String url = contextPath + "/https://www.fatsecret.kr/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search?q=";

		/* 원(original) 결과물 예시(HTML)
		 *
		 <table class="generic">
			<tr>
				<td class="leftCell">
					<div class="leftCellContent">
						<div class="searchBoxPanel">
							<div class="title" style="padding-bottom:10px;">식품&#160;검색</div>
							<form id="searchForm" style="margin:0px" method="get" action="/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search">
								<div style="float:left">
									<input type="text" id="ctl12_ByFood" class="searchInput" id="exp" name="q" value=" 백종원한판도시락" onkeyup="onSearchInputKeyUp(event)" onkeydown="onSearchInputKeyDown(event)" onclick="onSearchInputKeyUp(event)" autocomplete="off" maxlength="50"/>
								</div>
								<div style="float:right">
									<a class="button" href="#" onclick="document.getElementById('searchForm').submit(); return false;">
										<span class="left">&nbsp;</span>
										<span class="middle" style="padding:0px 10px"><img src="https://m.ftscrt.com/static/images/foodadd/But_icon_Search_2.png" width="20px" height="20px"/>&nbsp;검색하기</span>
										<span class="right">&nbsp;</span>
									</a>
								</div>
								<div style="clear:both"></div>


							</form>
							<img id="fqs_autosuggest_arrow" src="https://m.ftscrt.com/static/images/foodadd/FA_SearchBox_Arrow_blue_2.png" width="17" height="10" style="position:absolute;padding-left:20px;display:none" />
							<table id="fqs_autosuggest" cellspacing="0" cellpadding="0" style="width:100%;background-color:#fff;margin-bottom:2px;margin-top:9px;display:none">

								...(중략)...

							<table class="generic searchResult">

									<tr>
										<td class="borderBottom">
											<a class="prominent" href="/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/cu/%EB%B0%B1%EC%A2%85%EC%9B%90-%ED%95%9C%ED%8C%90-%EB%8F%84%EC%8B%9C%EB%9D%BD/1%EC%9D%B8%EB%B6%84">백종원 한판 도시락</a>&nbsp;&nbsp;<a class="brand" href="/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/cu">(CU)</a>
											<div class="smallText greyText greyLink">
												1인분  (435g)당 - 칼로리: 843kcal | 지방: 35.00g | 탄수화물: 100.00g | 단백질: 32.00g

									...(후략)...


		 */

		Document doc;

		String url = "https://www.fatsecret.kr/%EC%B9%BC%EB%A1%9C%EB%A6%AC-%EC%98%81%EC%96%91%EC%86%8C/search?q="+productName;

		try {

			doc = Jsoup.connect(url).get();
			// log.info("---- url : " + doc.html());

			Elements products = doc.select("table[class='generic searchResult'] td.borderBottom a");

			String productDetailURL = products.get(0).attr("href");

			//////////////////////////////////////////////////////////////////////////////////////////

			log.info("productDetailURL : {}", productDetailURL);

			doc = Jsoup.connect("https://www.fatsecret.kr/" + productDetailURL).get();

			/*
			 <div class="nutrition_facts international">
			    <div class="heading black">영양 정보</div>
			    <div class="divider thin"></div>
			    <div class="serving_size black serving_size_label">서빙 사이즈</div>
			    <div class="serving_size black serving_size_value">1인분  (435 g)</div>
			    <div class="divider thick"></div>
			    <div class="nutrient header black per_serve right">서브 당</div>
			    <div class="divider medium"></div>
			    <div class="nutrient black left">열량</div>
			    <div class="nutrient black right tRight">3527 kJ</div>
			    <div style="clear:both"></div>
			    <div class="nutrient black left"></div>
			    <div class="nutrient right tRight">843 kcal</div>
			    <div class="divider thin"></div>
			    <div class="nutrient black left">탄수화물</div>
			    <div class="nutrient black right tRight">100.00g</div>

			        <div style="clear:both"></div>
			        <div class="nutrient sub left">설탕당</div>
			        <div class="nutrient right tRight">8.00g</div>

			    <div class="divider thin"></div>
			    <div class="nutrient black left">단백질</div>
			    <div class="nutrient black right tRight">32.00g</div>
			    <div class="divider thin"></div>
			    <div class="nutrient black left">지방</div>
			    <div class="nutrient black right tRight">35.00g</div>

			        <div style="clear:both"></div>
			        <div class="nutrient sub left">포화지방</div>
			        <div class="nutrient right tRight">10.000g</div>

			        <div style="clear:both"></div>
			        <div class="nutrient sub left">트랜스 지방</div>
			        <div class="nutrient right tRight">0.000g</div>

			        <div class="divider thin"></div>
			        <div class="nutrient left">콜레스테롤</div>
			        <div class="nutrient right tRight">124mg</div>

			        <div class="divider thin"></div>
			        <div class="nutrient left">나트륨</div>
			        <div class="nutrient right tRight">987mg</div>

			    <div class="divider medium"></div>
			</div>
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

			// 열량1    열량2     탄수화물  설탕당(당류)  단백질  지방    포화지방  트랜스지방   콜레스테롤  나트륨
			// 3527 kJ 843 kcal 100.00g 8.00g      32.00g 35.00g 10.000g 0.000g     124mg     987mg
			// 단위 정보 제거
			log.info("정보 : " + doc.select("div[class='nutrition_facts international'] div[class$='right tRight']").text());

			String nutrConts[] = doc.select("div[class='nutrition_facts international'] div[class$='right tRight']").text()
									.replaceAll("[a-zA-Z]","") // 단위 정보 제거
									.replaceAll("  ", " ")
									.split("\\s");

			float nutrCont1 = Float.parseFloat(nutrConts[1].trim()); // 열량
			float nutrCont2 = Float.parseFloat(nutrConts[2].trim()); // 탄수화물
			float nutrCont3 = Float.parseFloat(nutrConts[4].trim()); // 단백질
			float nutrCont4 = Float.parseFloat(nutrConts[5].trim()); // 지방
			float nutrCont5 = Float.parseFloat(nutrConts[3].trim()); // 당류
			float nutrCont6 = Float.parseFloat(nutrConts[9].trim()); // 나트륨
			float nutrCont7 = Float.parseFloat(nutrConts[8].trim()); // 콜레스테롤
			float nutrCont8 = Float.parseFloat(nutrConts[6].trim()); // 포화지방산
			float nutrCont9 = Float.parseFloat(nutrConts[7].trim()); // 트랜스지방

			log.info("열량(nutrCont1) : {}", nutrCont1);
			log.info("탄수화물(nutrCont2) : {}", nutrCont2);
			log.info("단백질(nutrCont3) : {}", nutrCont3);
			log.info("지방(nutrCont4) : {}", nutrCont4);
			log.info("당류(nutrCont5) : {}", nutrCont5);
			log.info("나트륨(nutrCont6) : {}", nutrCont6);
			log.info("콜레스테롤(nutrCont7) : {}", nutrCont7);
			log.info("포화지방산(nutrCont8) : {}", nutrCont8);
			log.info("트랜스지방(nutrCont9) : {}", nutrCont9);



		} catch (Exception e) {

			log.error("크롤링 예외 " + e);
		}


		if (makerName.equals("없음") == false || makerName.equals("") == false) {
			url += "&makerName="+makerName;
		}


		// 결과가 없을 경우
		//

		// !!!!!! 검색 정지될 경우 !!!!!!
		//

		// JSON 예외 발생
		//

		/*

			String contents = resultActions.andReturn().getResponse().getContentAsString();

//			boolean result = contents.contains("해당하는 데이터가 없습니다.") ||
//						  	 contents.contains("파일타입 값이 누락 혹은 유효하지 않습니다. 요청인자 중 TYPE을 확인하십시오.") ||
//						  	 contents.contains("유효 호출건수를 이미 초과하셨습니다.");

			boolean result = false;

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

		*/
	} //

}