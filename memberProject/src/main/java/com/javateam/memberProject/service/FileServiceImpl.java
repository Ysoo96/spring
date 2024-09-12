package com.javateam.memberProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.memberProject.domain.UploadFile;
import com.javateam.memberProject.repository.FileDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	@Autowired
	FileDAO fileDAO;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteAllById(List<Integer> deleteImageIdList) {

		log.info("이미지 파일 레코드 삭제");

		try {

			fileDAO.deleteAllById(deleteImageIdList);

		} catch (Exception e) {
			log.error("삭제 실패");
			e.printStackTrace();
		}

	}

	@Transactional(readOnly = true)
	@Override
	public List<UploadFile> findAll() {
		return fileDAO.findAll();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void deleteById(int boardNum) {

		fileDAO.deleteById(boardNum);
	}

}
