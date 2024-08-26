package com.javateam.springJPAValidatorDemo.domain;

import java.util.Date; 

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MemberValid {
	
	// @Id //  // 폼점검과 직접 연관없고 VO 기능으로 사용시 유효
	@Size(min=8, 
		  max=20, 
		  message="아이디는 8~20자입니다")
	private String id;

	@NotNull 
	@Pattern(regexp="(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,20}", 
			 message="패쓰워드는 특수문자, 숫자, 영문자 대소문자를 섞어서 8~20자로 입력합니다")
	private String pw;
	
	@NotNull // (message="필수 사항은 반드시 입력하십시오")
	@Size(min=2, 
		  max=50, 
		  message="회원 이름은 2자 이상입니다")
	private String name;
	
	@NotEmpty  // 주의사항) @Email은 반드시 @NotEmpty와 같이 사용하십시오. 메시징이 안될 수 있습니다.
	@Email(message="잘못된 이메일 형식입니다") // 이메일 성분의 "@"은 점검하지만 "."을 점검하지 않기 때문에 제대로된 메일 점검이 수행되지 않습니다.
	@Pattern(regexp="[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}", 
	 		 message="잘못된 이메일 형식입니다")
	private String email;
	
	@Past(message="생일은 금일을 포함하여 이전 날짜가 입력되어야 합니다")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	// @Temporal(TemporalType.DATE)  // 폼점검과 직접 연관없고 VO 기능으로 사용시 유효
	@NotNull
	private Date birthday;
	
}