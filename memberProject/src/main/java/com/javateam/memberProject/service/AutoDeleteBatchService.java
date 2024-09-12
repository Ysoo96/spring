package com.javateam.memberProject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.memberProject.domain.BoardVO;
import com.javateam.memberProject.domain.UploadFile;
import com.javateam.memberProject.repository.BoardDAO;
import com.javateam.memberProject.repository.FileDAO;
import com.javateam.memberProject.repository.FileDeleteMyBatisDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableScheduling
// 참고) Enable Scheduling Annotations
// cron 일괄(batch) 처리 적용 위해서 반드시 표기
// : https://docs.spring.io/spring-framework/reference/integration/scheduling.html#scheduling-enable-annotation-support
@Slf4j
public class AutoDeleteBatchService {

	@Autowired
	FileDeleteMyBatisDAO fileDeleteMyBatisDAO;

	@Autowired
	BoardService boardService;;

	@Autowired
	ImageService imageService;

	@Autowired
	FileService fileService;

	@Value("${imageUpload.path}") // 이미지 저장 경로
	String uploadPath;

	// cron 표현식 :
	// www.classum.com/main/space/126296/community/462
	// https://ko.wikipedia.org/wiki/Cron

	// @Scheduled annotation
	// : https://docs.spring.io/spring-framework/reference/integration/scheduling.html#scheduling-annotation-support-scheduled

	// 매일 5분 단위로 불필요한 이미지 저장소 최적화.
	// 실제 운영할 경우는 이용자들이 거의 없는 유휴시간대(취침시간대 가령, 새벽 시간)에 구동하도록
	// 정기점검 및 일괄 파일삭제 시간을 지정하면 네트워크 트래픽 문제를 해소할 수 있음.
	//
	// 이미지 DB table(UPLOAD_FILE_TBL) 현황(실제 게시판에 등록된 이미지)에 없는
	// 이미지들(garbage images)을 일괄(batch) 선별 삭제 : batch processing

	// @Scheduled(cron="0 0 4 * * *") // 매일 오전(심야:유휴시간) 04:00:00에 적용
	// @Scheduled(cron="50 54 16 4 9 *") // 9월 4일 오후 16:54:50에 적용
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	// @Scheduled(cron="0 0/1 * * * ?") // 5분 단위 반복 실행
	public void deleteGarbageImagesAuto() {

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

		// List<BoardVO> defaultBoardList = (List<BoardVO>) boardDAO.findAll(Sort.by("boardNum")); // 전체 데이터 조회
		List<BoardVO> defaultBoardList = boardService.findAll(); // 전체 데이터 조회

		log.info("defaultBoardList 크기 : {}", defaultBoardList.size());

		List<Integer> defaultImgList = new ArrayList<>();

		// 게시글 레코드들에서  board_content 필드가 보유하고 있는 등록 이미지 리스트를 확보
		for (BoardVO boardVO : defaultBoardList) {
			defaultImgList.addAll(imageService.getImageList(boardVO.getBoardContent(), "/board/image/"));
		}

		log.info("defaultImgList : {}", defaultImgList.size());

		// 여기서 구한 아이디(UPLOAD_FILE_TBL.ID)를 제외한 모든 아이디는 가비지 이미지(공식 등록되지 않은 이미지)이므로 모두 삭제
		defaultImgList.forEach(x -> { log.info("기존 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		// 해당 upload_file_tbl의 이미지 아이디(PK:기본키) 이외의 레코드(정식 미등록 이미지)는 삭제
		List<UploadFile> realUpdateFileList = fileService.findAll();

		// UPLOAD_FILE_TBL에서 지워야될 이미지 아이디 목록 조회
		List<Integer> deleteImageIdList = new ArrayList<>();
		deleteImageIdList.addAll(realUpdateFileList.stream()
													.map(x -> x.getId())
													.filter(x -> !defaultImgList.contains(x))
													.toList());

		deleteImageIdList.forEach(x -> { log.info("삭제할 DB UPLOAD_FILE_TBL 테이블 이미지 아이디(ID) : " + x); });

		// fileDAO.deleteAllById(deleteImageIdList);
		// JPA 결함으로 인해 MyBatis 메서드 활용
		for (int id : deleteImageIdList) {
			fileDeleteMyBatisDAO.deleteFile(id);
			log.info("id = {} 레코드 삭제", id);
		}

		log.info("삭제 후");

		// 가비지 이미지 DB table 레코드 삭제 이후 실제 보유 현황 조회
		realUpdateFileList = fileService.findAll();

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

	} //

}