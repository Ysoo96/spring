package com.javateam.memberProject.service;

import java.util.List;

import com.javateam.memberProject.domain.UploadFile;

public interface FileService {

	List<UploadFile> findAll();

	void deleteAllById(List<Integer> deleteImageIdList);

	void deleteById(int boardNum);

}
