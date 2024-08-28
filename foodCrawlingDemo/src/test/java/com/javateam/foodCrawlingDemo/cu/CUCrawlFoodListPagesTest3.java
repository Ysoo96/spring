package com.javateam.foodCrawlingDemo.cu;

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

import com.javateam.foodCrawlingDemo.domain.CUVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CUCrawlFoodListPagesTest3 {

	@Test
	public void test() {

		// 개별 상품 페이지 : 정적 페이지
		// https://cu.bgfretail.com/product/view.do?category=product&gdIdx=19732

		String path = "D:/student/lsh/works/spring_ex/foodCrawlingDemo/upload_image/"; // 이미지 저장 경로
		String url = "https://cu.bgfretail.com/product/view.do?category=product&gdIdx=19732";
		Document doc;

		try {
			doc = Jsoup.connect(url).get();
			Elements product = doc.select("div[class='prodDetail']");

			// 상품명
			// <div class="prodDetail-e">
			// <p class="tit">주)후추네매콤쭈꾸미삼겹</p>
			String productName = product.select("div[class='prodDetail-e'] p[class='tit']").html();

			log.info("상품명 : " + productName); // 주)후추네매콤쭈꾸미삼겹

			// 상품 이미지
			// <div class="prodDetail-w">
			// <img src="//tqklhszfkvzk6518638.cdn.ntruss.com/product/8809383959255.jpg" alt="8809383959255.jpg" />

			// 이미지 원 주소
			String productImgURL = "https:"+product.select("div[class='prodDetail-w'] img").attr("src").trim();

			log.info("productImgURL : " + productImgURL);

			// 이미지 다운로드
			// 이미지 확장자
			String saveImgFileNameExt = productImgURL.substring(productImgURL.lastIndexOf('.') + 1);

			// String path = "D:/student/lsh/works/spring_ex/foodCrawlingDemo/upload_image/";
			String productImg = UUID.randomUUID().toString() + "." + saveImgFileNameExt;

			log.info("path : " + path);
			log.info("productImg : " + productImg);

			InputStream in = new URL(productImgURL).openStream();
			Files.copy(in, Paths.get(path + productImg), StandardCopyOption.REPLACE_EXISTING);

			log.info("상품 이미지 : " + productImg);

			// 가격
			/*
			<div class="prodDetail-e">

				<p class="tit">주)후추네매콤쭈꾸미삼겹</p>
				<ul class="prodTag">

				</ul>
				<div class="prodInfo">
					<dl>
						<dt>가격</dt>
						<dd class="prodPrice">
							<p><span>1,300</span>원</p>
				String productImg = "https://"+product.select("div[class='prodDetail-w'] img").attr("src");
			*/

			int productPrice = Integer.parseInt(product.select("div[class='prodDetail-e'] dd[class='prodPrice'] span").html().replace(",", ""));

			log.info("상품 가격 : " + productPrice);

			// 상품 상세 설명
			/*
				<div class="prodDetail-e">
					<p class="tit">주)후추네매콤쭈꾸미삼겹</p>
					<ul class="prodTag">

					</ul>
					<div class="prodInfo">

						...(중략)...

							<dt>상품 설명</dt>
							<dd>
								<ul class="prodExplain">
									<li>냉동삼겹살 맛집 후추네 제휴 상품으로 삼겹살과 쭈꾸미 그리고 매운소스의 조화를 이룬 삼각김밥</li>
								</ul>
							</dd>
			*/
			String productDetail = product.select("div[class='prodDetail-e'] ul[class='prodExplain'] li").html();
			// 냉동삼겹살 맛집 후추네 제휴 상품으로 삼겹살과 쭈꾸미 그리고 매운소스의 조화를 이룬 삼각김밥

			log.info("상품 상세 설명 : " + productDetail);

			// 상품 분류(태그) : food_type
			/*
			<div class="prodDetail-e">

				...(중략)...

					<ul class="prodTag" id="taglist">

							<li onclick="search(0);" style="cursor:pointer;">삼각김밥</li>

							<li onclick="search(1);" style="cursor:pointer;"> 간편식사</li>

			*/
			// String temp = product.select("div[class='prodDetail-e'] ul[class='prodTag']").html();
			// log.info("temp : " + temp);

			String productType2 = product.select("div[class='prodDetail-e'] ul[class='prodTag'] li").get(0).html(); // food_type2 (소분류)
			// 삼각김밥

			String productType1 = product.select("div[class='prodDetail-e'] ul[class='prodTag'] li").get(1).html(); // food_type2 (대분류)
			// 간편식사

			log.info("상품 분류2(소분류) : " + productType2);
			log.info("상품 분류1(대분류) : " + productType1);

			CUVO cuVO = CUVO.builder()
						    .foodName(productName)
						    .foodImg(productImg)
						    .foodDetail(productDetail)
						    .foodType1(productType1)
						    .foodType2(productType1)
						    .foodPrice(productPrice)
						    .build();

			log.info("상품 정보 : " + cuVO);

		} catch (IOException e) {
			e.printStackTrace();
		}

	} //

}