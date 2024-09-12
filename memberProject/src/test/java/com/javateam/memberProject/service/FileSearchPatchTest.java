package com.javateam.memberProject.service;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.memberProject.domain.BoardVO;
import com.javateam.memberProject.domain.UploadFile;
import com.javateam.memberProject.repository.BoardDAO;
import com.javateam.memberProject.repository.FileDAO;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@DataJpaTest
//@ExtendWith(SpringExtension.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class FileSearchPatchTest {

	@Autowired
	FileDAO fileDAO;

	@Value("${imageUpload.path}")
	String imgPath;

	ImageService imgSvc;

	@Autowired
	BoardDAO boardDAO;

	@Value("${imageUpload.path}")
	String uploadPath;

	// 개선사항) 실제로 이미지를 summernote에서 업로드할 경우 upload_file_tbl에 반영되기 때문에
	// submit(전송)을 하지 않았어도 가비지(garbage) 파일이 잔류하는 경우가 발생합니다.
	// 이럴 경우는 submit한 시점에서 그때 Controller 단계에서 저장하게 할 수도 있고(howto-1),
	// 그렇지 않고 기존의 로직을 그대로 활용할 경우는 board_tbl.board_content 필드에 들어간 그림 목록과 비교/대조하여
	// 가비지 삭제 목록을 생성하여 일괄 삭제할 수 있습니다(howto-2).
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Rollback(false)
	@Test
	public void test() {

		imgSvc = new ImageService(imgPath);

		// 이미지 파일 지역 저장소의 파일명 현황 확보
		log.info("파일 지역 저장소 경로 : {}", uploadPath);

		// 하위 디렉토리(폴더)들
		List<File> subDirectories = new ArrayList<>();

		// 하위 경로들 확보
		try (Stream<Path> uploadPathStream = Files.list(Paths.get(uploadPath))) {

			subDirectories = uploadPathStream.map(Path::toFile)
									   .filter(File::isDirectory)
									   .toList();

			subDirectories.forEach(x -> { log.info("하위 경로들 : {}", x.toString()); });

		} catch (IOException e) {
			log.error("파일 경로 탐색 오류");
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

				subFiles.forEach(x -> { log.info("하위 경로의 파일들 : {}", x.toString()); });

			} catch (IOException e) {
				log.error("파일 경로 탐색 오류");
				e.printStackTrace();
			}

		} // for

		log.info("-------------------------------------------------------");


		// DB table(UPLOAD_FILE_TBL) 이미지(UUID로 명명된 이미지 파일명) 적재

		// 개선사항) 실제로 이미지를 summernote에서 업로드할 경우 upload_file_tbl에 반영되기 때문에
		// submit(전송)을 하지 않았어도 가비지(garbage) 파일이 잔류하는 경우가 발생합니다.
		// 이럴 경우는 submit한 시점에서 그때 Controller 단계에서 저장하게 할 수도 있고(howto-1),
		// 그렇지 않고 기존의 로직을 그대로 활용할 경우는 board_tbl.board_content 필드에 들어간 그림 목록과 비교/대조하여
		// 가비지 삭제 목록을 생성하여 일괄 삭제할 수 있습니다(howto-2).

		// 여기서는 howto-2를 활용하였습니다.

		// submit 하기 직전 기존 게시글 내용에서 보유하고 있는 이미지 파일들을 확보

		// List<UploadFile> realUpdateFileList = fileDAO.findAll(); // before

		List<BoardVO> defaultBoardList = (List<BoardVO>) boardDAO.findAll(Sort.by("boardNum")); // 전체 데이터 조회

		log.info("defaultBoardList 크기 : {}", defaultBoardList.size());

		List<Integer> defaultImgList = new ArrayList<>();

		// 게시글 레코드들에서  board_content 필드가 보유하고 있는 등록 이미지 리스트를 확보
		for (BoardVO boardVO : defaultBoardList) {
			defaultImgList.addAll(imgSvc.getImageList(boardVO.getBoardContent(), "/board/image/"));
		}

		log.info("defaultImgList : {}", defaultImgList.size());

		// 여기서 구한 아이디(UPLOAD_FILE_TBL.ID)를 제외한 모든 아이디는 가비지 이미지(공식 등록되지 않은 이미지)이므로 모두 삭제
		defaultImgList.forEach(x -> { log.info("기존 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		// 해당 upload_file_tbl의 이미지 아이디(PK:기본키) 이외의 레코드(정식 미등록 이미지)는 삭제
		List<UploadFile> realUpdateFileList = fileDAO.findAll();

		// UPLOAD_FILE_TBL에서 지워야될 이미지 아이디 목록 조회
		List<Integer> deleteImageIdList = new ArrayList<>();
		deleteImageIdList.addAll(realUpdateFileList.stream()
													.map(x -> x.getId())
													.filter(x -> !defaultImgList.contains(x))
													.toList());

		deleteImageIdList.forEach(x -> { log.info("삭제할 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		fileDAO.deleteAllById(deleteImageIdList);

		log.info("삭제 후");

		// 가비지 이미지 DB table 레코드 삭제 이후 실제 보유 현황 조회
		realUpdateFileList = fileDAO.findAll();

		realUpdateFileList.forEach(x -> { log.info("삭제 후 기존 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		List<String> realUpdateFilenameList = realUpdateFileList.stream().map(UploadFile::getFilePath).toList();

		// 실제 DB table(UPLOAD_FILE_TBL)에 등록된 이미지 파일 리스트
		realUpdateFilenameList.forEach(x -> { log.info("실제 등록된 파일들 : {}", x); });

		// 이미지 파일 지역 저장소 파일 현황과 실제 DB table(UPLOAD_FILE_TBL)에 등록된 이미지 파일 현황의 차이
		// : 일괄 삭제할 파일들

		List<String> subFileList = new ArrayList<>();

		// 파일 경로 구분자를 변경 : ex) "\" => "/"
		subFileList.addAll(subFiles.stream().map(x -> x.toString().replace("\\", "/")).toList());

		log.info("하위 경로들에 따른 저장 이미지 파일수 : {}", subFileList.size());
		log.info("실제 DB table에 등록된 이미지 파일수 : {}", realUpdateFilenameList.size());

		// subFileList.forEach(x -> { log.info("하위 경로 저장 파일들 : {}", x); });

		// 차집합 개념 적용
		// : 실제 DB table에 등록된 이미지 파일 리스트 - 하위 경로들에 따른 저장 이미지 파일 리스트

		subFileList.removeAll(realUpdateFilenameList);

		log.info("실제 삭제할 파일(garbage image file) 수 : {}", subFileList.size());

		if (subFileList.size() > 0) {

			subFileList.forEach(x ->  { log.info("실제 삭제할 파일들 : {}", x); } );

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
