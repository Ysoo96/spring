package com.javateam.foodCrawlingDemo.food2;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class NutriJSONSearchTest {

	@Test
	public void test() {

		log.info("식약처 영양정보 DB화");

		// 파일명 :
		// simple-JSON 활용 : https://dzone.com/articles/how-can-we-read-a-json-file-in-java

		// 파일 경로/이름
		// D:\lsh\프로젝트\영양관련자료_정부\전국통합식품영양성분정보(가공식품)표준데이터.json

		// 필드 성분들(설명) :
		//
		// 식품코드	식품명	데이터구분코드	데이터구분명	식품기원코드	식품기원명
		// 식품대분류코드	식품대분류명	대표식품코드	대표식품명	식품중분류코드	식품중분류명
		// 식품소분류코드	식품소분류명	식품세분류코드	식품세분류명	영양성분함량기준량
		// 에너지(kcal)	수분(g)	단백질(g)	지방(g)	회분(g)	탄수화물(g)	당류(g)
		// 식이섬유(g)	칼슘(mg)	철(mg)	인(mg)	칼륨(mg)	나트륨(mg)	비타민 A(μg RAE)
		// 레티놀(μg)	베타카로틴(μg)	티아민(mg)	리보플라빈(mg)	니아신(mg)	비타민 C(mg)
		// 비타민 D(μg)	콜레스테롤(mg)	포화지방산(g)	트랜스지방산(g)	출처코드	출처명
		// 1회 섭취참고량	제조사명	식품중량	수입업체명	유통업체명	수입여부	원산지국코드
		// 원산지국명	데이터생성방법코드	데이터생성방법명	데이터생성일자	데이터기준일자	제공기관코드	제공기관명

		// 파일 구조 예시
		/*
		 {"fields":[{"id":"식품코드"},{"id":"식품명"},{"id":"데이터구분코드"},{"id":"데이터구분명"},{"id":"식품기원코드"},
		 {"id":"식품기원명"},{"id":"식품대분류코드"},{"id":"식품대분류명"},{"id":"대표식품코드"},{"id":"대표식품명"},{"id":"식품중분류코드"},
		 {"id":"식품중분류명"},{"id":"식품소분류코드"},{"id":"식품소분류명"},{"id":"식품세분류코드"},{"id":"식품세분류명"},{"id":"영양성분함량기준량"},
		 {"id":"에너지(kcal)"},{"id":"수분(g)"},{"id":"단백질(g)"},{"id":"지방(g)"},{"id":"회분(g)"},{"id":"탄수화물(g)"},
		 {"id":"당류(g)"},{"id":"식이섬유(g)"},{"id":"칼슘(mg)"},{"id":"철(mg)"},{"id":"인(mg)"},{"id":"칼륨(mg)"},
		 {"id":"나트륨(mg)"},{"id":"비타민 A(μg RAE)"},{"id":"레티놀(μg)"},{"id":"베타카로틴(μg)"},{"id":"티아민(mg)"},
		 {"id":"리보플라빈(mg)"},{"id":"니아신(mg)"},{"id":"비타민 C(mg)"},{"id":"비타민 D(μg)"},{"id":"콜레스테롤(mg)"},
		 {"id":"포화지방산(g)"},{"id":"트랜스지방산(g)"},{"id":"출처코드"},{"id":"출처명"},{"id":"1회 섭취참고량"},{"id":"제조사명"},
		 {"id":"식품중량"},{"id":"수입업체명"},{"id":"유통업체명"},{"id":"수입여부"},{"id":"원산지국코드"},{"id":"원산지국명"},
		 {"id":"데이터생성방법코드"},{"id":"데이터생성방법명"},{"id":"데이터생성일자"},{"id":"데이터기준일자"},{"id":"제공기관코드"},{"id":"제공기관명"}],

		 "records":[{"식품코드":"P109-302030200-2176","식품명":"과·채주스_프룻밀토마토","데이터구분코드":"P","데이터구분명":"가공식품",
		 "식품기원코드":"1","식품기원명":"가공식품","식품대분류코드":"09","식품대분류명":"음료류","대표식품코드":"09302","대표식품명":"과·채주스",
		 "식품중분류코드":"0900003","식품중분류명":"과일·채소류음료","식품소분류코드":"090000302","식품소분류명":"과·채주스","식품세분류코드":"00",
		 "식품세분류명":"해당없음","영양성분함량기준량":"100ml","에너지(kcal)":"43","수분(g)":"","단백질(g)":"0.47","지방(g)":"0","회분(g)":"",
		 "탄수화물(g)":"10","당류(g)":"8","식이섬유(g)":"","칼슘(mg)":"","철(mg)":"","인(mg)":"","칼륨(mg)":"","나트륨(mg)":"17",
		 "비타민 A(μg RAE)":"","레티놀(μg)":"","베타카로틴(μg)":"","티아민(mg)":"","리보플라빈(mg)":"","니아신(mg)":"","비타민 C(mg)":"",
		 "비타민 D(μg)":"","콜레스테롤(mg)":"0","포화지방산(g)":"0","트랜스지방산(g)":"0","출처코드":"3","출처명":"식품의약품안전처",
		 "1회 섭취참고량":"200ml","제조사명":"(주)웰팜","식품중량":"150ml","수입업체명":"해당없음","유통업체명":"해당없음","수입여부":"N",
		 "원산지국코드":"036","원산지국명":"해당없음","데이터생성방법코드":"2","데이터생성방법명":"수집","데이터생성일자":"2021-06-30",
		 "데이터기준일자":"2024-02-23","제공기관코드":"1471000","제공기관명":"식품의약품안전처"},

		 ....(후략)...
		 */

		// https://dzone.com/articles/how-can-we-read-a-json-file-in-java
		String filename = "./bigdata/전국통합식품영양성분정보(가공식품)표준데이터.json";

		JSONObject json = new JSONObject();

		try {

			Object obj = new JSONParser().parse(new FileReader(filename));
			json = (JSONObject)obj;

			// ex) "식품코드" 필드 가져오기
			JSONArray jsonArr = (JSONArray) json.get("records");

			// 전체 레코드 크기
			int recordsLen = jsonArr.size();

			log.info("전체 식품 영양 정보 크기 : " + recordsLen); // 50000

			JSONObject jsonOne = (JSONObject) jsonArr.get(0);
			String foodCode = (String)jsonOne.get("식품코드"); // ex) P109-302030200-2176

			String foodName = (String)jsonOne.get("식품명");

			log.info("식품코드 : " + foodCode);
			log.info("식품명 : " + foodName);

			////////////////////////////////////////////////////////////

			// 식품명으로 정보 조회하기
			// 식품명 예시) 과·채주스_프룻밀토마토 => 프룻밀토마토 (토큰화)

			String searchWord = "신라면";

			int count = 0;

			for (int i=0; i<recordsLen; i++) {

				jsonOne = (JSONObject) jsonArr.get(i);

				String srcWord = jsonOne.get("식품명").toString().split("\\_")[1].trim();

				if (srcWord.contains(searchWord)) {

					log.info("검색된 식품명 : {}, 식품코드 : {}", srcWord, jsonOne.get("식품코드"));
					count++;
				}

			} // for

			log.info("검색 결과 수 : " + count);

		} catch (IOException | ParseException e) {
			log.error("JSON 파일 읽기 오류 : " + e);
			e.printStackTrace();
		}


	} //

}
