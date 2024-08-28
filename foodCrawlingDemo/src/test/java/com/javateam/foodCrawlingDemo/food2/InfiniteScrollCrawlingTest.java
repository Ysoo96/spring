package com.javateam.foodCrawlingDemo.food2;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import com.javateam.foodCrawlingDemo.domain.InfiniteFoodVO;
import com.javateam.foodCrawlingDemo.service.InfiniteFoodService;
import com.javateam.foodCrawlingDemo.util.WebDriverUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class InfiniteScrollCrawlingTest {

	@Autowired
	WebDriverUtil webDriverUtil;

	@Autowired
	InfiniteFoodService infiniteFoodService;

	@Test
	public void test() {

		WebDriver driver = webDriverUtil.getChromeDriver();
		log.info("driver : " + driver.getCurrentUrl());

		List<String> foodLinkList = new ArrayList<>();

		try {

			if (!ObjectUtils.isEmpty(driver)) {

				// 무한 스크롤 대비 : "우리의 식탁" 레시피(음식 조리법)
				String url = "https://wtable.co.kr/recipes";
				driver.get(url);


				WebElement actions = driver.findElement(By.cssSelector("body"));

				String selectors = "div[class^='RecipeListstyle__RecipeList'] a[href]";
				List<WebElement> webElementList = new ArrayList<>();

				for (int i=0; i<2; i++) {

					webElementList = driver.findElements(By.cssSelector(selectors));

					for (WebElement element : webElementList) {

						String link = element.getAttribute("href");
						log.info("개별 음식 조리법(레시피) 사이트 링크 : " + link);

						InfiniteFoodVO ifVO = new InfiniteFoodVO();
						ifVO.setFoodSite(link);
						infiniteFoodService.save(ifVO);

						foodLinkList.add(link);

						actions.sendKeys(Keys.END);
						Thread.sleep(2000); //2초 대기시간(delay term)

					} // for element

				} // for i

				log.info("링크 크기 :" + webElementList.size());

				log.info("---------------------------------------------------------------------");
				log.info("음식 리스트 크기 : " + foodLinkList.size());

				foodLinkList.forEach(x -> { log.info("{}", x); });

			} // if


		} catch (Exception e) {
			log.error("예외 : " + e);
		}

		webDriverUtil.close(driver);
		webDriverUtil.quit(driver);

	} //

}
