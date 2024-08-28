package com.javateam.foodCrawlingDemo.food2;

public class ProductExerciseTest {

	public static void main(String[] args) {

//		String str = "ab)cd";
//		String words[] = str.split("\\)");
//		
//		System.out.println(words.length);
		
		String productName = "신라면큰사발컵";
		System.out.println(productName.endsWith("컵"));
		
		// String productName2 = "동원)동원참치150g";
		// String productName2 = "CJ)대두유500ml";
		String productName2 = "ASMR꾸덕쫀득크림면";
		
		// String regexStr = productName2.replaceAll("[\\d+ | g]", "");
		// System.out.println("regexStr : " + productName2.replaceAll("[\\d+ | g | ml]", ""));
		
		String foodName = "HEYROO구운계란득템10입";
		String words[] = foodName.split("\\)");		
		String realProductName = words.length == 1 ? words[0] : words[1];
		realProductName = realProductName.replaceAll("[HEYROO | \\d+ | g | T | ml]", "");
		
		String makerName = words.length == 1 ? "없음" : words[0];
		makerName = words[0].startsWith("HEYROO") ? "HEYROO" : makerName;
		
		System.out.println("realProductName : " + realProductName);
		System.out.println("makerName : " + makerName);
		
	}

}
