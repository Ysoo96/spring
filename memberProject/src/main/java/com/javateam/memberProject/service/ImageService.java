package com.javateam.memberProject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

//import com.javateam.memberProject.dao.FileDAO;
import com.javateam.memberProject.domain.UploadFile;
import com.javateam.memberProject.util.FileUploadUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

    private final Path rootLocation;

    // application.properties 파일 경로 정보
    // ex) imageUpload.path=D:/student/lsh/works/spring/memberProject/upload/image/
    @Autowired
    public ImageService(@Value("${imageUpload.path}") String uploadPath) {

    	log.info("PATH :: " + uploadPath);
        this.rootLocation = Paths.get(uploadPath);
    }

    @Autowired
    ImageStoreService imageStoreService;

    /**
     * 모든 파일 읽어오기
     *
     * @return 파일들
     */
    public Stream<Integer> loadAll() {

        List<UploadFile> files = imageStoreService.findAll();
        return files.stream().map(file -> file.getId());
    }

    /**
     * 파일 읽어오기
     *
     * @param fileId 파일 ID
     * @return 업로드 파일 객체
     */
    public UploadFile load(int fileId) {

    	return imageStoreService.findOneById(fileId);
    }

    /**
     * 파일 자원(resource) 로딩
     *
     * @param fileName 파일명
     * @return 파일 자원(resource)
     * @throws Exception
     */
    public Resource loadAsResource(String fileName) throws Exception {

    	try {

            if (fileName.toCharArray()[0] == '/') {
                fileName = fileName.substring(1);
            }

            Path file = loadPath(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("파일을 읽을 수 없습니다 : " + fileName);
            }

        } catch (Exception e) {
            throw new Exception("파일을 읽을 수 없습니다 : " + fileName);
        } //

    }

    /**
     * 경로 읽어오기
     *
     * @param fileName 파일명
     * @return 파일 경로
     */
    private Path loadPath(String fileName) {
        return rootLocation.resolve(fileName);
    }

    /**
     * 업로드 파일 저장
     *
     * @param file 업로드 파일
     * @return 업로드 파일 객체
     * @throws Exception
     */
    public UploadFile store(MultipartFile file) throws Exception {

        try {
            if (file.isEmpty()) {
                throw new Exception("업로드 파일이 비어 있어서 저장에 실패하였습니다 : " + file.getOriginalFilename());
            }

            String saveFileName = FileUploadUtil.fileSave(rootLocation.toString(), file);

            if (saveFileName.toCharArray()[0] == '/') {
                saveFileName = saveFileName.substring(1);
            }

            Resource resource = loadAsResource(saveFileName);

            // 업로드 파일 객체 구성
            UploadFile saveFile = new UploadFile();
            saveFile.setSaveFileName(saveFileName);
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setContentType(file.getContentType());

            log.info("Root 경로 : {}", rootLocation);

            String tempPath = rootLocation.toString()
            		.replace(File.separatorChar, '/') + File.separator + saveFileName;
            saveFile.setFilePath(tempPath.replace("\\", "/"));

            log.info("이미지 파일 저장경로 : {}", saveFile.getFilePath());

            saveFile.setFileSize(resource.contentLength());
            saveFile.setRegDate(new Date());
            saveFile = imageStoreService.save(saveFile); // 저장

            return saveFile;

        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }

    } //

	/**
	 * 게시글내의 삽입 이미지 리스트 조회
	 *
	 * @param str 게시글 내용(board_content)
	 * @param imgUploadPath 이미지 경로
	 * @return
	 */
	// imgUploadPath = /board/image/
	public List<Integer> getImageList(String str, String imgUploadPath) {

		log.info("BoardService.getImageList");
		List<Integer> imgList = new ArrayList<>(); // upload_file_tbl 테이블의 PK(기본키)

		if (str.contains(imgUploadPath) == false) { // 이미지 미포함

			log.info("이미지가 전혀 포함되어 있지 않습니다.");

		} else {

			// 포함된 전체 이미지 수 : 이 한계량 만큼 검색  => 카운터에 반영
			int imgLen = StringUtils.countOccurrencesOf(str, imgUploadPath);

			log.info("imgLen : " + imgLen);

			// 이미지 검색 카운터 설정 : 이미지 검색할 횟수
			int count = 0;

			int initPos = str.indexOf(imgUploadPath);
			log.info("첫 발견 위치 : " + initPos);

			// 추출된 문자열 : 반복문에서 사용
			String subStr = str;

			while (count < imgLen) {

				initPos = subStr.indexOf(imgUploadPath);

				// 이미지 파일만 추출 (첫번째)
				// "/board/image/".length()
				initPos += imgUploadPath.length();
				log.info("이미지 파일 시작 위치 : " + initPos);

				// 추출된 문자열
				// ex) 41 (.../board/image/41" : upload_file_tbl 테이블의 삽입 이미지 PK(기본키))
				subStr = subStr.substring(initPos);

				log.info("subStr : " + subStr);

				// 첫번째 " (큰 따옴표) 위치 검색하여 순수한 숫자(PK)만 추출
				int quotMarkPos = subStr.indexOf("\"");

				// 이미지 파일 끝 검색하여 이미지 파일명/확장자 추출
				// 이미지 끝 검색 : 검색어(" )
				int imgFileNum = Integer.parseInt(subStr.substring(0, quotMarkPos));

				log.info("이미지 파일 테이블 PK(기본기) : " + imgFileNum);

				count++; // 이미지 추출되었으므로 카운터 증가

				imgList.add(imgFileNum); // 리스트에 추가

				log.info("----------------------------------------");

			} //  while

		} // if

		return imgList;
	}

}