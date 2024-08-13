package com.javateam.memberProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.memberProject.service.MemberService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("member")
@Slf4j
public class MemberRestController {
	@Autowired
	MemberService memberService;

	@GetMapping("/hasFld/{fld}/{val}")
	public ResponseEntity<Boolean> hasFld(@Parameter(name = "fld", required = true) @PathVariable("fld") String fld,
			@Parameter(name = "val", required = true) @PathVariable("val") String val) {
		ResponseEntity<Boolean> resEntity = null;

		try {
			boolean result = memberService.hasMemberByFld(fld, val);

			if (result == true) {
				// 중복 아이디가 있음 : http 상태 코드 200 => 사용불가
				resEntity = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				// 중복 아이디가 없음 : http 상태 코드 204 (No Content) => 사용가
				resEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("MemberService.hasFld 에러 : " + e);
			// http 상태 코드 : 500 (서버 에러)
			resEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resEntity;
	}

	@GetMapping("/hasFldForUpdate/{id}/{fld}/{val}")
	public ResponseEntity<Boolean> hasFldForUpdate(
			@Parameter(name = "id", required = true) @PathVariable("id") String id,
			@Parameter(name = "fld", required = true) @PathVariable("fld") String fld,
			@Parameter(name = "val", required = true) @PathVariable("val") String val) {
		ResponseEntity<Boolean> resEntity = null;
		
		try {
			boolean result = memberService.hasMemberForUpdate(id, fld, val);
			
			// 중복 이메일/휴대폰 있음 : http 상태 코드 200 => 사용 불가
			if (result == true) {
				resEntity = new ResponseEntity<>(result, HttpStatus.OK);

			// 중복 이메일/휴대폰 없음 : http 상태 코드 204 => 사용가
			} else {
				resEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("MemberRestController.hasFldForUpdate 에러 : " + e);
		}
		return resEntity;
	}
}
