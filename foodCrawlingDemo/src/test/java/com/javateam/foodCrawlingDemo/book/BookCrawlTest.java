package com.javateam.foodCrawlingDemo.book;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class BookCrawlTest {
	
	@Test
	public void Test() throws IOException {
		
		String bookSite = "http://www.yes24.com/24/Category/Display/001001019001";
		Document doc = Jsoup.connect(bookSite).get();
		
		log.info("타이틀 : " + doc.select("title").text());
		
		log.info("카테고리 도서수 : " + doc.select("li div.goods_info div.goods_name a[href]").size());
		log.info("카테고리 첫째 도서 번호 : " + doc.select("li div.goods_info div.goods_name a[href]").attr("href").split("/")[3]);
		// log.info("카테고리 첫째 도서 제목 : " + doc.select("li div.goods_info div.goods_name a[href]").get(0).text());
		
		log.info("-------------------------------");
		
		int len = doc.select("li div.goods_info div.goods_name a[href]").size();
		
		for (int i=0; i<len; i+=2) {
			log.info("카테고리 {} 도서 번호 : {}", i, doc.select("li div.goods_info div.goods_name a[href]").get(i).attr("href").split("/")[3]);
		} //
	} //

}