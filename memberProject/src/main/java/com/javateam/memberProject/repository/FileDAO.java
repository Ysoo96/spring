package com.javateam.memberProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javateam.memberProject.domain.UploadFile;

public interface FileDAO extends JpaRepository<UploadFile, Integer> {

	public UploadFile findOneByFileName(String fileName);

	public UploadFile findOneById(int id);

	public void deleteById(int id);

}