package com.javateam.foodCrawlingDemo.food;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.FoodVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FoodCrawlFoodIdTest {

	@Test
	public void test() {

		// 음식 목록 화면
		// 첫 페이지 :
		// https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmLst.ps?menuId=PS03933
		// 갤러리 페이지 :
		// https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmLst.ps?menuId=PS03933&pageIndex=1&pageSize=10&pageUnit=10&schType=A&schText=&schTrditfdNm=&schTrditfdNm2=&sidoCode=&food_type_ctg01=&food_type_ctg02=&food_type_ctg03=&food_type_ctg04=&fd_mt_ctg01=&fd_mt_ctg02=&ck_ry_ctg01=&ck_ry_ctg02=&ck_ry_ctg03=&tema_ctg01=
		String foodGallerySite = "https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmLst.ps?menuId=PS03933&pageIndex=1&pageSize=10"
							   + "&pageUnit=10&schType=A&schText=&schTrditfdNm=&schTrditfdNm2=&sidoCode=&food_type_ctg01=&"
							   + "food_type_ctg02=&food_type_ctg03=&food_type_ctg04=&fd_mt_ctg01=&fd_mt_ctg02=&ck_ry_ctg01=&ck_ry_ctg02=&ck_ry_ctg03=&tema_ctg01=";

		// 음식 고유 번호 추출
		Document doc;
		
		// 상품 고유 번호(Primary Key)
		List<String> productIdList = new ArrayList<>();
		
		try {
			doc = Jsoup.connect(foodGallerySite).get();

			Elements foodListInfo = doc.select("div[class='data_list fSite'] ul li");

			// 갤러리 음식 갯수
			int foodLen = foodListInfo.size();

			log.info("갤러리 페이지 음식 갯수 : " + foodLen); // ex) 10

			// 음식 아이디 : 91511
			
			// <a href="#" onclick="fncDtl('91511');return false;"><img
			// src="/ps/img/common/anvil_img.jpg" alt="이미지 준비중입니다." ></a> </span>
			
			for (int i=0; i<foodLen; i++) {
			
				String foodId = foodListInfo.get(i) // <div class="inBox" >
											.select("span a") // <span class="pic"><a>
											.attr("onclick")  // onclick="fncDtl('91511');return false;"
											.substring("fncDtl('".length(), 
													   "fncDtl('".length() + 5)
											.trim(); // select("span[class='pic'] a[onclick]").text();
				
				log.info("foodId : " + foodId);
				
				productIdList.add(foodId);
			}
			
			// 상품번호 출력
			productIdList.forEach(x -> { log.info("상품번호 : " + x);});
 
		} catch (IOException e) {
			log.error("접속 불능");
			e.printStackTrace();
		}

	} //

}
