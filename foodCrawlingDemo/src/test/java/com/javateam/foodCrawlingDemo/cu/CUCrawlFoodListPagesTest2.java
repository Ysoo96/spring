package com.javateam.foodCrawlingDemo.cu;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CUCrawlFoodListPagesTest2 {

	@Test
	public void test() {

		// AJAX feed : 1 ~ 6 페이지(pageIndex)까지 조회됨
		String url = "https://cu.bgfretail.com/product/productAjax.do?pageIndex=1&listtType=1";
		Document doc;
		
		try {
			
			doc = Jsoup.connect(url).get();
			Elements products= doc.select("ul li[class='prod_list'] div[class='prod_img']");
			
			// log.info("html : " + products.get(0).html());
			log.info("페이지당 상품의 갯수 : "+ products.size()); // 40
			
			// 상품 아이디
			for (int i=0; i<products.size(); i++) {
				
				String id = products.get(i).attr("onclick").split("[()]")[1];
				log.info("상품 아이디 : {}", id);
			} // for
			
			
		} catch (IOException e) {
			log.error("예외 : " + e);
			e.printStackTrace();
		}
		
	} //

}