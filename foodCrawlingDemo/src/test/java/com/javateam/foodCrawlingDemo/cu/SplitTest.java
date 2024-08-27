package com.javateam.foodCrawlingDemo.cu;

public class SplitTest {

	public static void main(String[] args) {

		String str = "view(17339);";
		String tokens[] = str.split("[()]");
		
		System.out.println(tokens[1]);
//		for (String t : tokens) {
//			System.out.println(t);
//		}
	} //

}
