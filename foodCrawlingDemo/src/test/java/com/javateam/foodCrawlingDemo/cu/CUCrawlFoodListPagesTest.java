package com.javateam.foodCrawlingDemo.cu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import com.javateam.foodCrawlingDemo.domain.CUVO;
import com.javateam.foodCrawlingDemo.util.WebDriverUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CUCrawlFoodListPagesTest {

	@Autowired
	WebDriverUtil webDriverUtil;
						
	@Test
	public void test() {

		WebDriver driver = webDriverUtil.getChromeDriver();
		
		log.info("driver : " + driver.getCurrentUrl());
		
		List<WebElement> webElementList = new ArrayList<>();
		
		String url = "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=1";
		String query = "div[class='prod_wrap'] div[class='prod_img']"; 
		
		try {
			
			if (!ObjectUtils.isEmpty(driver)) {
				
				driver.get(url);    
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));     
				webElementList = driver.findElements(By.cssSelector(query));
				
				// log.info("text : " + webElementList.size()); // 40
				// log.info("text : " + webElementList.get(0).getAttribute("onclick")); // view(20319)
				
				List<String> idList = new ArrayList<>();
				
				for (int i=0; i<webElementList.size(); i++) {
					
					String id = webElementList.get(i).getAttribute("onclick").substring(5, 10);
					log.info("id : {}", id);
					idList.add(id);
				} // for
				 
				log.info("1");
			} // if
			
			log.info("2");
		
		} catch (Exception e) {
			log.error("예외 : " + e);
		}
		
		log.info("3");
		
		webDriverUtil.close(driver);
		
		log.info("4");
		webDriverUtil.quit(driver);
		
		log.info("5");
		
	} //

}
