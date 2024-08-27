package com.javateam.foodCrawlingDemo.food;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.FoodVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FoodCrawlTest {

	FoodVO foodVO;

	@Test
	public void test() {

		String foodSiteId = "91391";

		// ex) 농사로 -> 가물치곰탕(음식)
		String foodSite = "https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmDtl.ps?menuId=PS03933&"
						+ "pageIndex=1&pageSize=10&pageUnit=10&"
						+ "cntntsNo="
						+ foodSiteId
						+"&"
						+ "schType=A&schText=&"
						+ "schTrditfdNm=&"
						+ "schTrditfdNm2=&"
						+ "sidoCode=&"
						+ "food_type_ctg01=&"
						+ "food_type_ctg02=&"
						+ "food_type_ctg03=&"
						+ "food_type_ctg04=&"
						+ "fd_mt_ctg01=&"
						+ "fd_mt_ctg02=&"
						+ "ck_ry_ctg01=&"
						+ "ck_ry_ctg02=&"
						+ "ck_ry_ctg03=&"
						+ "tema_ctg01=";

		Document doc;
		foodVO = new FoodVO();

		try {
			doc = Jsoup.connect(foodSite).get();

			Elements foodInfo = doc.select("div.txtlist ul li span");

			// 타이틀 => 식품명
			String foodName = doc.select("title").text().split(" | ")[0].trim();
			log.info("타이틀(식품명) : " + foodName);
			foodVO.setFoodName(foodName);

			// 식품 이미지
			// https://www.nongsaro.go.kr/cms_contents/789/91391_MF_BIMG_01.jpg
			// <img src="/cms_contents/789/91391_MF_BIMG_01.jpg" alt="가루장국(가리장국)" data-pop="91391_MF_BIMG_01.jpg">

			String foodImg = doc.select("div.img-wrap img").attr("src");
			log.info("식품 이미지 : " + foodImg);

			// 이미지 원 주소
			String imgURL = "https://www.nongsaro.go.kr/" + foodImg;

			// 이미지 확장자
			String saveImgFileNameExt = foodImg.substring(foodImg.lastIndexOf('.') + 1);

			// 이미지 저장(다운로드)
			String path = "D:/student/lsh/works/spring/foodCrawlingDemo/upload_image/"; // 이미지 저장 경로
			String saveImgFilename = UUID.randomUUID().toString() + "." + saveImgFileNameExt;

			log.info("path : " + path);
			log.info("saveImgFilename : " + saveImgFilename);

			InputStream in = new URL(imgURL).openStream();
			Files.copy(in, Paths.get(path + saveImgFilename), StandardCopyOption.REPLACE_EXISTING);

			foodVO.setFoodImg(saveImgFilename);

			// 식품 유형
			String foodType = foodInfo.get(0).text().trim();
			log.info("식품 유형 : " + foodType);
			foodVO.setFoodType(foodType);

			// 조리법 : 이 부분만 태그가 td가 들어감(publisher 작성 오타)
			// ex) <td>가열하여 익히는 음식 > 물을 이용한 음식 > 끓이는 음식</span>
			String recipe = doc.select("div.txtlist ul li:eq(1)").text().replace("조리법 ", "");
			log.info("조리법 : " + recipe);
			foodVO.setRecipe(recipe);

			// 식재료
			String ingredient = foodInfo.get(1).text().trim();
			log.info("식재료 : " + ingredient);
			foodVO.setIngredient(ingredient);

			// 부재료
			String minorIngredient = foodInfo.get(2).text().trim();
			log.info("부재료 : " + minorIngredient);
			foodVO.setMinorIngredient(minorIngredient);

			// 조리방법
			String cookingInstruction = foodInfo.get(3).text().trim();
			log.info("조리방법 : " + cookingInstruction);
			foodVO.setCookingInstruction(cookingInstruction);

			// 조리시연자
			String cook = foodInfo.get(4).text().trim();
			log.info("조리시연자 : " + cook);
			foodVO.setCook(cook);

			// 참고사항
			String reference = foodInfo.get(5).text().trim();
			log.info("참고사항 : " + reference);
			foodVO.setReference(reference);

			// 출처
			String cookingOrigin = foodInfo.get(6).text().trim();
			log.info("출처 : " + cookingOrigin);
			foodVO.setCookingOrigin(cookingOrigin);

			// 정보제공자
			String informationProvider = foodInfo.get(7).text().trim();
			log.info("정보 제공자 : " + informationProvider);
			foodVO.setInformationProvider(informationProvider);

			log.info("음식정보 : " + foodVO);

			// 음식정보 : FoodVO(id=0, foodName=가물치곰탕,
			// foodType=부식류 > 국 및 탕류 > 곰국 및 탕 > 어패류,
			// recipe=가열하여 익히는 음식 > 물을 이용한 음식 > 끓이는 음식,
			// ingredient=가물치 1마리, 찹쌀 330g(2컵), 미나리
			// 50g, 대파 35g(1뿌리), 생강 20g(5쪽), 대추 20g(10개), 인삼 2뿌리, 다진 마늘 5큰술, 다진 생강 3큰술, 깨소금
			// 3큰술, 물 적량, 소금 적량,
			// minorIngredient=,
			// cookingInstruction=1. 가물치는 손질하여 물을 붓고 3시간
			// 정도 약한 불에서 푹 곤다. 2. 찹쌀은 깨끗이 씻어 30분 정도 물에 불린다. 3. 미나리는 다듬어 5cm길이로 썰고, 대파는
			// 어슷썬다(0.3cm) 4. 생강은 얇게 편 썰고(0.3cm), 대추와 인삼은 깨끗이 씻어 둔다. 5. 1을 체에 밭쳐 찌꺼지는 버리고 받은
			// 국물에 찹쌀, 생강, 대추, 인삼을 넣고 끓인다. 6. 찹쌀이 어느 정도 익으면 미나리, 어슷썬 대파, 다진 마늘, 다진 생강, 깨소금,
			// 소금을 넣어 한소끔 끓인다.,
			// cook=,
			// reference=·예로부터 이뇨제로 작용한다 하여 결석 환자에게 특효제로서 작용하였고
			// 보양제로서 널리 각광을 받고 있다. ·탕을 먹고 나서 계피를 이용하여 만든 수정과를 마시면 뒷맛이 개운해 진다.,
			// cookingOrigin=농촌진흥청 농촌영양개선연수원(현 농촌자원개발연구소), 한국의 향토음식, 1994,
			// informationProvider=)

		} catch (IOException e) {
			log.error("접속 불능");
			e.printStackTrace();
		}

	} //

} //