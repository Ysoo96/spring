package com.javateam.foodCrawlingDemo.food;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.FoodVO;
import com.javateam.foodCrawlingDemo.repository.FoodRepository;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.geometry.Positions;

@SpringBootTest
@Slf4j
public class FoodCrawlFoodListPagesTest3 {

	@Autowired
	private FoodRepository foodRepo;

	@Test
	public void test() {


		for (int j=0; j<10; j++) { // 10개의 페이지
		// for (int j=2; j<5; j++) { // 특정 범위의 페이지

			// 음식 목록 화면
			// 첫 페이지 :
			// https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmLst.ps?menuId=PS03933
			// 갤러리 페이지 :
			// https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmLst.ps?menuId=PS03933&pageIndex=1&pageSize=10&pageUnit=10&schType=A&schText=&schTrditfdNm=&schTrditfdNm2=&sidoCode=&food_type_ctg01=&food_type_ctg02=&food_type_ctg03=&food_type_ctg04=&fd_mt_ctg01=&fd_mt_ctg02=&ck_ry_ctg01=&ck_ry_ctg02=&ck_ry_ctg03=&tema_ctg01=

			// j+1 페이지
			String foodGallerySite = "https://www.nongsaro.go.kr/portal/ps/psr/psrc/fdNmLst.ps?menuId=PS03933&"
								   + "pageIndex="
								   + (j+1)
								   + "&pageSize=10"
								   + "&pageUnit=10&schType=A&schText=&schTrditfdNm=&schTrditfdNm2=&sidoCode=&food_type_ctg01=&"
								   + "food_type_ctg02=&food_type_ctg03=&food_type_ctg04=&fd_mt_ctg01=&fd_mt_ctg02=&ck_ry_ctg01=&ck_ry_ctg02=&ck_ry_ctg03=&tema_ctg01=";

			// 음식 고유 번호 추출
			Document doc;

			// 식품 고유 번호(Primary Key)
			List<String> foodIdList = new ArrayList<>();

			// 식품(식품) 리스트
			List<FoodVO> foodList = new ArrayList<>();

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

					foodIdList.add(foodId);
				}

				// 식품번호 출력
				foodIdList.forEach(x -> { log.info("식품번호 : " + x);});

			} catch (IOException e) {
				log.error("접속 불능");
				e.printStackTrace();
			}


			//////////////////////////////////////////////////////////////////////////////////////////////////

			FoodVO foodVO = null;

			// 개별 식품 정보 추출
			for (int i=0; i<foodIdList.size(); i++) {

				String foodSiteId = foodIdList.get(i);

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

				///////////////////////////////////////////////////////////

				Document doc2 = null;
				foodVO = new FoodVO();

				try {
					doc2 = Jsoup.connect(foodSite).get();

					Elements foodInfo = doc2.select("div.txtlist ul li span");

					// 타이틀 => 식품명
					String foodName = doc2.select("title").text().split(" | ")[0].trim();
					log.info("타이틀(식품명) : " + foodName);
					foodVO.setFoodName(foodName);

					// 식품 이미지
					// https://www.nongsaro.go.kr/cms_contents/789/91391_MF_BIMG_01.jpg
					// <img src="/cms_contents/789/91391_MF_BIMG_01.jpg" alt="가루장국(가리장국)" data-pop="91391_MF_BIMG_01.jpg">

					String foodImg = doc2.select("div.img-wrap img").attr("src");
					log.info("식품 이미지 : " + foodImg);

					// 이미지 원 주소
					String imgURL = "https://www.nongsaro.go.kr/" + foodImg;

					/////////////////////////////////////////////////////////////////////////////////////////
					//
					// 추가 기능 :
					// 식품 이미지가 아예 없어서 공(blank) 이미지를
					// 사용하는 경우(이미지가 없는 경우)는 "blank_image.jpg"로 대체
					String saveImgFilename;

					log.info("foodImg : " + foodImg);

					if (foodImg.contains("anvil_img.jpg") == true) {

						saveImgFilename = "blank_image.png";

					} else { // 식품 이미지가 있는 경우

						// 이미지 확장자
						String saveImgFileNameExt = foodImg.substring(foodImg.lastIndexOf('.') + 1);

						/////////////////////////////////////////////////////////////////////////////////////////

						// 이미지 저장(다운로드)
						String path = "D:/student/lsh/works/spring/foodCrawlingDemo/upload_image/"; // 이미지 저장 경로
						saveImgFilename = UUID.randomUUID().toString() + "." + saveImgFileNameExt;

						log.info("path : " + path);
						log.info("saveImgFilename : " + saveImgFilename);

						InputStream in = new URL(imgURL).openStream();
						Files.copy(in, Paths.get(path + saveImgFilename), StandardCopyOption.REPLACE_EXISTING);

						/////////////////////////////////////////////////////////////////////////////////////////

						// 썸네일 파일명 생성
						// 썸네일(thumbnail) path : webp/gif를 제외한 파일들은 PNG 형식으로 저장
						// ex) D:/lsh/works/spring_food1/foodCrawlingDemo/upload_image/thumb

						String thumbPath = path + "thumb/";
						String thumbPathFilename = "thumb_" + saveImgFilename.split("\\.")[0] + ".png";

						// 썸네일 저장
						// 썸네일 파일 저장 시작
						// https://github.com/coobird/thumbnailator
						// 100*100 크기의 썸네일 작성

						File thumbnail = new File(thumbPathFilename);
//						File outFilename = new File(path + saveImgFilename);

						///////////////////////////////////////////////////////////////////

						// 슬라이드용 이미지 저장
						// 썸네일 파일 저장 시작
						// https://github.com/coobird/thumbnailator
						// 250*350 크기의 썸네일 작성

						String slidePath = path + "slide/";
						String slidePathFilename = "slide_" + saveImgFilename.split("\\.")[0] + ".png";

						File slideImage = new File(slidePathFilename);
//						File outFilename = new File(path + saveImgFilename);

						try {

							// 갤러리용 썸네일 이미지
							Thumbnails.of(path + saveImgFilename)
									  .size(100, 100)
									  // .keepAspectRatio(true)
									  .outputFormat("png")
									  .toFile(thumbPath + thumbnail);

							/////////////////////////////////////////////////////////////////////////////////////

							// 슬라이드용 이미지
							Thumbnails.of(path + saveImgFilename)
									  // .forceSize(300, 400) // 강제 사이즈 규격화 : 이럴 경우 image aspect 비율(ratio) 깨질 수 있음(이미지 변형)
									  .size(300,400)
									  .addFilter(new Canvas(300, 400, Positions.CENTER, false, Color.WHITE)) // 흰색 여백처리
									  // .size(250, 350)
									  .keepAspectRatio(true) // 이미지 애스팩트 비율(원래 이미지 가로/세로 할당 비율) 주의) forceSize와 동시 사용시 에러 유발 !
									  .outputFormat("png")
									  .toFile(slidePath + slideImage);

							/*
							// Thumbnailator 미사용시 썸네일 저장

							BufferedImage originalImage = ImageIO.read(new File(path + saveImgFilename));

							BufferedImage thumbImage = new BufferedImage(85, 100, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
							Graphics2D graphic = thumbImage.createGraphics();
							Image image = originalImage.getScaledInstance(85, 100, Image.SCALE_SMOOTH);

							graphic.drawImage(image, 0, 0, 85, 100, null);
							graphic.dispose(); // 리소스를 모두 해제

							ImageIO.write(thumbImage, "png", new File(thumbPath + thumbnail));

							*/

						} catch (IOException e) {

							log.error("썸네일 저장 오류 : " + e);
							e.printStackTrace();
						}

						log.info("썸네일 저장-마침");
						// }
						// 썸네일 파일 저장 끝

					} // if (foodImg.equals("anvil_img.jpg") == true) {

					/////////////////////////////////////////////////////////////////////////////////////////

					foodVO.setFoodImg(saveImgFilename);

					// 식품 유형
					String foodType = foodInfo.get(0).text().trim();
					log.info("식품 유형 : " + foodType);
					foodVO.setFoodType(foodType);

					// 조리법 : 이 부분만 태그가 td가 들어감(publisher 작성 오타)
					// ex) <td>가열하여 익히는 음식 > 물을 이용한 음식 > 끓이는 음식</span>
					String recipe = doc2.select("div.txtlist ul li:eq(1)").text().replace("조리법 ", "");
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

					log.info("------------------------------------------------------------");

					///////////////////////////////////////////////////////////////////////////

					// DB 저장
					foodRepo.save(foodVO);

				} catch (IOException e) {
					log.error("접속 불능");
					e.printStackTrace();
				}

			} // for

			// 식품 리스트 정보 출력
			foodList.forEach(x -> { log.info("식품 : " + x);});

		} // 10개의 페이지

	} //

}
