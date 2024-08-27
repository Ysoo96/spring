package com.javateam.foodCrawlingDemo.cu;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class DownloadTest {

	public static void main(String[] args) {

		String imgURL = "https://tqklhszfkvzk6518638.cdn.ntruss.com/product/8809895797871.jpg";

		// 이미지 확장자
		String saveImgFileNameExt = imgURL.substring(imgURL.lastIndexOf('.') + 1);

		String path = "D:/student/lsh/works/spring/foodCrawlingDemo/upload_image/"; // 이미지 저장 경로
		String saveImgFilename = UUID.randomUUID().toString() + "." + saveImgFileNameExt;

		InputStream in;

		try {

			in = new URL(imgURL).openStream();
			Files.copy(in, Paths.get(path + saveImgFilename), StandardCopyOption.REPLACE_EXISTING);

		} catch (MalformedURLException e) {
			System.out.println("예외1 : " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("예외2 : " + e);
			e.printStackTrace();
		}

	}

}
