package com.javateam.foodCrawlingDemo.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.javateam.foodCrawlingDemo.domain.CUVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CUCrawlService {

	/**
	 * 카테고리별 음식(상품) 크롤링
	 *
	 *  - 메인 카테고리(searchMainCategory) (현재)
	 *  : 간편식사=10, 즉석조리=20, 과자류=30, 아이스크림=40, 식품=50, 음료=60, (생활용품=70)
	 *
	 *  - 각 카테고리별 총페이지 수 (현재)
	 *  : 간편식사=6, 즉석조리=3, 과자류=45, 아이스크림=9, 식품=67, 음료=27, (생활용품=70)
	 *
	 *  ex) 간편식사 카테고리
	 *	https://cu.bgfretail.com/product/productAjax.do?pageIndex=1&listtType=2&searchMainCategory=10
	 *
 	 * @param categoryTitle
	 * @param endPage
	 * @return
	 */
	public List<CUVO> crawlProductList(String categoryTitle, int endPage) {

		String path = "D:/student/lsh/works/spring_ex/foodCrawlingDemo/upload_image/"; // 이미지 저장 경로
		List<CUVO> cuProductList = new ArrayList<>();

		int searchMainCategory = categoryTitle.equals("간편식사") ? 10 :
								 categoryTitle.equals("즉석조리") ? 20 :
								 categoryTitle.equals("과자류") ?  30 :
								 categoryTitle.equals("아이스크림") ? 40 :
								 categoryTitle.equals("식품") ? 50 :
								 categoryTitle.equals("음료")	 ? 60 : 70;

		// AJAX feed : 1 ~ 6 페이지(pageIndex)까지 조회됨
		// String url = "https://cu.bgfretail.com/product/productAjax.do?pageIndex=1&listtType=2&searchMainCategory=10";
		String url;
		Document doc;

		for (int count = 1; count <= endPage; count++) {

			url = "https://cu.bgfretail.com/product/productAjax.do?pageIndex="+count
				+ "&listtType=1"
				+ "&searchMainCategory="+searchMainCategory;

			try {

				doc = Jsoup.connect(url).get();
				Elements products = doc.select("ul li[class='prod_list'] div[class='prod_img']");

				// log.info("html : " + products.get(0).html());
				log.info("페이지당 상품의 갯수 : " + products.size()); // 40 => 페이지당 상품 한계수

				// 상품 아이디
				for (int i = 0; i < products.size(); i++) {

					String id = products.get(i).attr("onclick").split("[()]")[1];
					log.info("상품 아이디 : {}", id);

					//////////////////////////////////////////////////////////////////////////////////////

					// 개별 상품 페이지 : 정적 페이지
					// https://cu.bgfretail.com/product/view.do?category=product&gdIdx=19732

					String productURL = "https://cu.bgfretail.com/product/view.do?category=product&gdIdx=" + id;

					try {
						doc = Jsoup.connect(productURL).get();
						Elements product = doc.select("div[class='prodDetail']");

						// 상품명
						// <div class="prodDetail-e">
						// <p class="tit">주)후추네매콤쭈꾸미삼겹</p>
						String productName = product.select("div[class='prodDetail-e'] p[class='tit']").html();

						log.info("상품명 : " + productName); // 주)후추네매콤쭈꾸미삼겹

						// 상품 이미지
						// 이미지 원 주소
						String productImgURL = "https:"
								+ product.select("div[class='prodDetail-w'] img").attr("src").trim();

						log.info("productImgURL : " + productImgURL);

						// 이미지 다운로드
						// 이미지 확장자
						String saveImgFileNameExt = productImgURL.substring(productImgURL.lastIndexOf('.') + 1);

						// String path = "D:/lsh/works/spring_food1/foodCrawlingDemo/upload_image/";
						String productImg = UUID.randomUUID().toString() + "." + saveImgFileNameExt;

						log.info("path : " + path);
						log.info("productImg : " + productImg);

						try {

							InputStream in = new URL(productImgURL).openStream();
							Files.copy(in, Paths.get(path + productImg), StandardCopyOption.REPLACE_EXISTING);

						} catch (Exception e) {

							log.error("그림 파일 저장 에러 : " + e);
							productImg = "없음";
						} //

						log.info("상품 이미지 : " + productImg);

						// 가격
						int productPrice = Integer.parseInt(product
								.select("div[class='prodDetail-e'] dd[class='prodPrice'] span").html().replace(",", ""));

						log.info("상품 가격 : " + productPrice);

						// 상품 상세 설명
						String productDetail = product.select("div[class='prodDetail-e'] ul[class='prodExplain'] li")
								.html();
						// 냉동삼겹살 맛집 후추네 제휴 상품으로 삼겹살과 쭈꾸미 그리고 매운소스의 조화를 이룬 삼각김밥

						log.info("상품 상세 설명 : " + productDetail);

						// 상품 분류(태그) : food_type
						// String temp = product.select("div[class='prodDetail-e'] ul[class='prodTag']").html();
						// log.info("temp : " + temp);

						// food_type2 (소분류) : 태그(Tag)
						String productType2 = "";
						Elements productTags = product.select("div[class='prodDetail-e'] ul[class='prodTag'] li");
						int tagLen = productTags.size();

						if (tagLen > 0) {

							for (int k=0; k<tagLen; k++) {

								productType2 += product.select("div[class='prodDetail-e'] ul[class='prodTag'] li").get(k).html();

								if (k < tagLen-1) {
									productType2 += ",";
								}
							} // for

						}

						log.info("상품 분류(소분류, 태그) : " + productType2);

						CUVO cuVO = CUVO.builder()
										.foodName(productName)
										.foodImg(productImg)
										.foodDetail(productDetail)
										.foodType1(categoryTitle) // categoryTitle
										.foodType2(productType2) // 소분류(태그)
										.foodPrice(productPrice)
										.build();

						log.info("상품 정보 : " + cuVO);

						cuProductList.add(cuVO); // 개별 상품 리스트에 추가

					} catch (IOException e) {
						e.printStackTrace();
					}

					//////////////////////////////////////////////////////////////////////////////////////

				} // for

			} catch (IOException e) {
				log.error("예외 : " + e);
				e.printStackTrace();
			} //

		} // for

		return cuProductList;

	} // 메서드 끝

}