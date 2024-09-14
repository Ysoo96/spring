package com.javateam.memberProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.memberProject.repository.BoardDAO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeleteFileTest {

	@Autowired
	FileService fileService;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Rollback(false)
	@Test
	void testDeleteById() {

		log.info("삭제 전");

		fileService.deleteById(35);

		log.info("삭제 후");
	}

}
