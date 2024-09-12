package com.javateam.memberProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.memberProject.domain.UploadFile;
import com.javateam.memberProject.repository.FileDAO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class FileSearchTest {

	@Autowired
	FileDAO fileDAO;
	
	@Value("${imageUpload.path}")
	String uploadPath;
	
	// case-1) 게시글 글쓰기시 업로드된 이미지들이 실제 등록 전송(submit) 하지 않았을 경우,
	// 이미지 저장소에 가비지(garbage) 상태로 남아있을 수 있는데, 이를 일괄적으로 삭제하는 테스트
	@Test
	public void test() {
		
		// 이미지 파일 지역 저장소의 파일명 현황 확보
		log.info("파일 지역 저장소 경로 : {}", uploadPath);
		
		// 하위 디렉토리(폴더)들
		List<File> subDirectories = new ArrayList<>();
		
		// 하위 경로들 확보
		try (Stream<Path> uploadPathStream = Files.list(Paths.get(uploadPath))) {
			
			subDirectories = uploadPathStream.map(Path::toFile)
											 .filter(File::isDirectory)
											 .toList();
			
			subDirectories.forEach(x -> {log.info("하위 경로들 : {}", x.toString()); });
			
		} catch (IOException e) {
			log.info("파일 경로 탐색 오류");
			e.printStackTrace();
		}
		
		log.info("---------------------------------------------------");
		
		List<File> subFiles = new ArrayList<>();
		
		for (File subDir : subDirectories) {
			
			// 하위 경로들에 따른 저장 이미지 파일이름들 확보
			try (Stream<Path> uploadFileStream = Files.list(Paths.get(subDir.getPath()))) {
				
				subFiles.addAll(uploadFileStream.map(Path::toFile)
												.filter(File::isFile)
												.toList());
				
				subFiles.forEach(x -> {log.info("하위 경로의 파일들 : {}", x.toString()); });
				
			} catch (IOException e) {
				log.error("파일 경로 탐색 오류");
				e.printStackTrace();
			}
		} // for
		
		log.info("---------------------------------------------------");
		
		// DB table(UPLOAD_FILE_TBL) 이미지(UUID로 명명된 이미지 파일명) 적재
		List<UploadFile> realUpdateFileList = fileDAO.findAll();
		
		List<String> realUpdateFilenameList = realUpdateFileList.stream().map(UploadFile::getFilePath).toList();
		
		// 실제 DB table(UPLOAD_FILE_TBL)에 등록된 이미지 파일 리스트
		realUpdateFilenameList.forEach(x -> {log.info("실제 등록된 파일들 : {}", x); });
		
		// 이미지 파일 지역 저장소 파일 현황과 실제 DB table(UPLOAD_FILE_TBL)에 등록된 이미지 파일 현황의 차이
		// : 일괄 삭제할 파일들
		
		List<String> subFileList = new ArrayList<>();
		
		// 파일 경로 구분자를 변경 : ex) "\" => "/"
		subFileList.addAll(subFileList.stream().map(x -> x.toString().replace("\\", "/")).toList());
		
		log.info("하위 경로들에 따른 저장 이미지 파일수 : {}", subFileList.size());
		log.info("실제 DB table에 등록된 이미지 파일수 : {}", realUpdateFilenameList.size());
		
		// subFileList.forEach(x -> {log.info("하위 경로 저장 파일들 : {}", x); });
		
		// 차집합 개념 적용
		// : 실제 DB table에 등록된 이미지 파일 리스트 - 하위 경로들에 따른 저장 이미지 파일 리스트
		
		subFileList.removeAll(realUpdateFilenameList);
		
		log.info("실제 삭제할 파일(garbage image file) 수 : {}", subFileList.size());
		
		if (subFileList.size() > 0) {
			
			subFileList.forEach(x -> {log.info("실제 삭제할 파일들 : {}", x); });
			
			// 파일들 일괄 삭제
			for (String subFile : subFileList) {

				try {
					Files.delete(Paths.get(subFile));
				} catch (IOException e) {
					log.error("가비지(garbage) 이미지 파일 삭제 오류");
					e.printStackTrace();
				}
			}
		} else {
			log.info("삭제할 파일들이 없습니다.");
		}
	}

}
