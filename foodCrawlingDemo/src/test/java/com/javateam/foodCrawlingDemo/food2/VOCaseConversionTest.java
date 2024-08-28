package com.javateam.foodCrawlingDemo.food2;

import org.apache.commons.text.CaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.base.CaseFormat;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class VOCaseConversionTest {
	
	@Test
	public void test() {
		
		// API : https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/base/Converter.html
		String str = "abcd_efg";
		String toStr = CaseUtils.toCamelCase(str, false, '_');
		log.info("toStr : " + toStr); // abcdEfg
		
		String toStr2 = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
		log.info("toStr2 : " + toStr2); // AbcdEfg
	} //

}
