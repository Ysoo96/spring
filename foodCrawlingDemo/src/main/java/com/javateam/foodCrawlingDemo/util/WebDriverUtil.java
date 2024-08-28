package com.javateam.foodCrawlingDemo.util;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebDriverUtil {

	// 셀레니엄 웹드라이버 크롬(Chrome) 버전 다운로드 경로
	//
	// : https://developer.chrome.com/docs/chromedriver/downloads?hl=ko
	// : https://googlechromelabs.github.io/chrome-for-testing/
	//
	// 직접 다운로드(128.0.6613.85 버전) : https://storage.googleapis.com/chrome-for-testing-public/128.0.6613.85/win64/chromedriver-win64.zip

	// @Value("${diver.chrome.driver_path}")
	private String WEB_DRIVER_PATH = "D:\\student\\lsh\\works\\spring_ex\\foodCrawlingDemo\\src\\main\\resources\\webdriver\\chromedriver.exe"; // WebDriver 경로

	public WebDriver getChromeDriver() {

		log.info("------------- getChormeDriver -----------------");

		log.info("WEB_DRIVER_PATH : ", WEB_DRIVER_PATH);

		if (ObjectUtils.isEmpty(System.getProperty("webdriver.chrome.driver"))) {
			System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);
		} // webDriver

		// webDriver 옵션 설정
		ChromeOptions chromeOptions = new ChromeOptions();

		chromeOptions.addArguments("--headless=new");
		chromeOptions.addArguments("--lang=ko");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--disable-gpu");
		chromeOptions.setCapability("ignoreProtectedModeSettings", true);

		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

		log.info("----- getChormeDriver end ---------------");

		return driver;
	}

//	@Value("${diver.chrome.driver_path}")
//	public void initDirver(String path) {
//		WEB_DRIVER_PATH = path;
//	}

	public void quit(WebDriver driver) {

		if (!ObjectUtils.isEmpty(driver)) {
			driver.quit();
		}
	}

	public void close(WebDriver driver) {

		if (!ObjectUtils.isEmpty(driver)) {
			driver.close();
		}
	}

}