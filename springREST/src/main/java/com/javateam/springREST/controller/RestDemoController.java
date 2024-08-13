package com.javateam.springREST.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.springREST.domain.TestVO;

import lombok.extern.log4j.Log4j2;
//import lombok.extern.slf4j.Slf4j;

@Controller
//@RestController
//@Slf4j
@Log4j2
public class RestDemoController {
	
	@RequestMapping(value = "/jsonFeed", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String jsonFeed(@RequestParam("id") String id) throws JsonProcessingException {

		log.info("jsonFeed");

		String json = new ObjectMapper().writeValueAsString(new TestVO(id, "홍길동", "서울 강남"));

		return json;
	} //

	@RequestMapping(value = "/jsonFeed3", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<TestVO> jsonFeed3(@RequestParam("id") String id) {

		log.info("jsonFeed3");

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		ResponseEntity<TestVO> re = null;
		TestVO testVO = this.getTestVO(id);
		// TestVO testVO = null; // null 상황 연출

		try {

			if (testVO != null) {
				re = new ResponseEntity<TestVO>(testVO, HttpStatus.OK); // 200 OK
			} else {
				re = new ResponseEntity<TestVO>(testVO, HttpStatus.NO_CONTENT); // 204 error
			}

		} catch (Exception e) {
			log.error("JSON 생성 오류 ");
			re = new ResponseEntity<TestVO>(testVO, HttpStatus.INTERNAL_SERVER_ERROR); // 500 error
		}

		return re;
	} //

	// @GetMapping(value = "/jsonFeed4/id/{id}", produces = "application/json; charset=UTF-8")
	@GetMapping(value = "/jsonFeed4/id/{id}", produces = "text/html; charset=UTF-8")
	public ResponseEntity<String> boardDetailFeed(@PathVariable("id") String id) {
		
		log.info("jsonFeed4");

		// HTTP Header 정보 setting
		HttpHeaders responseHeaders = new HttpHeaders();
		// responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		TestVO testVO = this.getTestVO(id);

		try {
			
			json = mapper.writeValueAsString(testVO);
			
			log.info("json : {}", json);
			
		} catch (JsonProcessingException e) {
			log.error("json exception !");
			e.printStackTrace();
		}

		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
	} //
	
	
	// @GetMapping(value = "/jsonFeed5/id/{id}", produces = "application/json; charset=UTF-8")
	@GetMapping(value = "/jsonFeed5/id/{id}")
	// public ResponseEntity<String> boardDetailFeed2(@PathVariable("id") String id) { // 1)
	public ResponseEntity<TestVO> boardDetailFeed2(@PathVariable("id") String id) { // 2)
		
		log.info("jsonFeed5");

		// HTTP Header 정보 setting
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		TestVO testVO = this.getTestVO(id);

		log.info("testVO : {}", testVO);

		// return new ResponseEntity<String>(testVO.toString(), responseHeaders, HttpStatus.OK); // 1) TestVO(id=abcd, name=홍길동, address=서울 강남)
		return new ResponseEntity<TestVO>(testVO, responseHeaders, HttpStatus.OK); // 2) {"id":"java","name":"홍길동","address":"서울 강남"}
	} //

	private TestVO getTestVO(String id) {
		return new TestVO(id, "홍길동", "서울 강남");
	}
	
	@PostMapping(value="/demo", produces = "application/json; charset=UTF-8")	
	@ResponseBody
	public ResponseEntity<TestVO> demo(@RequestBody TestVO testVO) {
		
		log.info("demo");

		// HTTP Header 정보 setting
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		return new ResponseEntity<>(testVO, responseHeaders, HttpStatus.OK);
	} //

}