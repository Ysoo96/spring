// 에러 메시징 함수
// 기능 : 필드별로 폼 점검 시행 후 에러 메시징(패널)
//        개별 필드 체크 플래그에 리턴
// 1) 함수명 : isCheckFldValid
// 2) 인자 :
// 필드 (아이디) 변수(fld), 필드 기정값(initVal),
// 필드별 정규표현식(유효성 점검 기준) (regex)
// 필드별 에러 패널(errPnl), 필드별 에러 메시지(errMsg)
// 3) 리턴 : fldCheckFlag : boolean (true/false) : 유효/무효
// 4) 용례(usage) :  
// idCheckFlag = isCheckFldValid(idFld, 
//                        /^[a-zA-Z]{1}\w{7,19}$/,
//                        idFldErrPnl,
//                        "",     
//                        "에러 메시지")
function isCheckFldValid(fld, regex, initVal, errPnl, errMsg) {

    // 리턴값 : 에러 점검 플래그
    let fldCheckFlag = false;

    // 체크 대상 필드 값 확인
    console.log(`체크 대상 필드 값 : ${fld.value}`);

    // 폼 유효성 점검(test)
    console.log(`점검 여부 : ${regex.test(fld.value)}`);

    if (regex.test(fld.value) == false) {

        errPnl.style.height = "50px"; 
        errPnl.innerHTML = errMsg; 

        // 기존 필드 데이터 초기화
        // fld.value = "";
        fld.value = initVal;
        fld.focus(); // 재입력 준비     
        
        fldCheckFlag = false;

    } else { // 정상

        // 에러 패널 초기화
        errPnl.style.height = "0"; 
        errPnl.innerHTML = "";

        fldCheckFlag = true;
    } // if

    return fldCheckFlag;
} //

////////////////////////////////////////////////////////////////////////////////////

window.onload = () => {

    // 각 필드들의 에러 점검 여부 (플래그(flag) 변수)
    let idCheckFlag = false;

	// 아이디 중복 점검 플래그
	let idDuplicatedCheckFlag = false;  

    // 패쓰워드 점검 플래그 
    let pwCheckFlag = false;
    
    // 패스워드 & 패스워드 확인 점검 플래그
    let pw1CheckFlag = false;
    let pw2CheckFlag = false;

    // 이름 점검 플래그
    let nameCheckFlag = false;

    // 이메일 점검 플래그
    let emailCheckFlag = false;

	// 이메일 중복 점검 플래그
	let emailDuplicatedCheckFlag = false;

    // 연락처 점검 플래그
    let mobileCheckFlag = false;

	// 연락처휴대폰 중복 점검 플래그
	let mobileDuplicatedCheckFlag = false;

    // 생일 점검 플래그
    let birthdayCheckFlag = false;

    // 아이디 필드 폼 점검(form validation)
    // 아이디 필드 인식
    let idFld = document.getElementById("id");

    // 아이디 에러 패널 인식
    let idFldErrPnl = document.getElementById("id_fld_err_pnl");

    // 비밀번호 필드 인식
    let pwFld = document.getElementById("pw");
    
    // 비밀번호 & 비밀번호 확인
    let pw1Fld = document.getElementById("pw1");
    let pw2Fld = document.getElementById("pw2");

    // 비밀번호 에러 패널 인식
    let pwFldErrPnl = document.getElementById("pw_fld_err_pnl");

    // 이름 필드 인식
    let nameFld = document.getElementById("name");

    // 이름 에러 패널 인식
    let nameFldErrPnl = document.getElementById("name_fld_err_pnl");

    // 이메일 필드 인식
    let emailFld = document.getElementById("email");

    // 이메일 필드 에러 패널 인식
    let emailFldErrPnl = document.getElementById("email_fld_err_pnl");

    // 휴대폰 필드 인식
    let mobileFld = document.getElementById("mobile");

    // 휴대폰 필드 에러 패널 인식
    let mobileFldErrPnl = document.getElementById("mobile_fld_err_pnl");

    // 생일 필드 인식
    let birthdayFld = document.getElementById("birthday");

    // 생일 필드 에러 패널 인식
    let birthdayFldErrPnl = document.getElementById("birthday_fld_err_pnl");
    
    ////////////////////////////////////////////////////////////////////////
    
    // 에러 메시지
    let idErrMsg = "회원 아이디는 8~20사이의 영문으로 시작하여 영문 대소문자 및 숫자로 작성하십시오";
	
	let pwErrMsg = "회원 비밀번호는 영문 대소문자, 숫자, 특수문자 1자 이상 포함하여 8~20자로 작성하십시오";
	
	let nameErrMsg = "회원 이름은 한글 이름만 허용됩니다";
	
	let emailErrMsg = "회원 이메일을 작성하십시오";
	
	let mobileErrMsg = "회원 휴대폰을 작성하십시오";
	
	let birthdayErrMsg = "회원 생일을 작성하십시오";
	    
	////////////////////////////////////////////////////////////////////////
	
	// 초기화 : 날짜 한계(max) 금일 초기화 
	// ex) 2024-08-23 formatting
	// 달력 컨퍼넌트에서의 날짜 제한
	birthdayFld.max = new Date().toISOString().substring(0, 10);

    ////////////////////////////////////////////////////////////////////////

    // 아이디 필드 유효성 및 중복 점검
    idFld.onkeyup = () => {

        console.log(`아이디 중복 점검 : ${idFld.value}`);
		
		idCheckFlag = isCheckFldValid(idFld, 
                        /^[a-zA-Z]{1}\w{7,19}$/,
                        idFld.value,
                        idFldErrPnl,
                        idErrMsg);

		if (idCheckFlag == true) {
			
			console.log("아이디 유효성 점검 성공");
		 
			axios.get(`/memberProject/member/hasFld/ID/${idFld.value}`)
				 .then(function(response) {
					
					// console.log("서버 응답 : " + JSON.stringify(response));
					
					idDuplicatedCheckFlag = response.data;
					
					console.log("response.data : ", response.data);
					// console.log("response.data : ", typeof(response.data));
	
					let idDupErrMsg = idDuplicatedCheckFlag == true ? "중복되는 아이디가 존재합니다" : "사용가능한 아이디입니다"				   
					console.log(idDupErrMsg);
					
					// 메시지 반복 출력 방지 : 출력할 메시지 있으면 출력
					if (idDuplicatedCheckFlag == true) {
					
						alert(idDupErrMsg);						
						idFld.value = "";
					}					
						
				 })
				 .catch(function(err) {
					console.error("아이디 중복 점검 중 서버 에러가 발견되었습니다");
					//idDuplicatedCheckFlag = false;
				 });
			
		} // if	
		            
    } // idFld.onkeyup ... 

    //////////////////////////////////////           

    // 패쓰워드 필드 입력 후 이벤트 처리 : keyup
    /*
    pwFld.onkeyup = (e) => {

        console.log("패쓰워드 필드 keyup")
        pwCheckFlag = isCheckFldValid(pwFld,                                 
                        /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
                        pwFld.value,
                        pwFldErrPnl,
						pwErrMsg);
    } //
    */
   
	pw1Fld.onkeyup = (e) => {
	
	    console.log("비밀번호1 필드 keyup")
	    pw1CheckFlag = isCheckFldValid(pw1Fld,                                 
	                    /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
	                    pw1Fld.value,
	                    pwFldErrPnl,
						pwErrMsg);
    } //
    
    pw2Fld.onkeyup = (e) => {
	
	    console.log("비밀번호2 필드 keyup")
	    pw2CheckFlag = isCheckFldValid(pw2Fld,                                 
	                    /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
	                    pw2Fld.value,
	                    pwFldErrPnl,
						pwErrMsg);
    } //
    
    // pw1, pw2 동등비교
    pw2Fld.onblur = (e) => {
	
	    console.log("비밀번호2 필드 blur");
	    
	    if (pw1CheckFlag == true && pw2CheckFlag == true) {
			console.log("비밀번호 동등여부 : ", pw1Fld.value == pw2Fld.value);
			pwCheckFlag = pw1Fld.value == pw2Fld.value ? true : false;
		} else {
			console.log("비밀번호 불일치합니다");
			pwCheckFlag = false;
		}
		
		console.log("pwCheckFlag : ", pwCheckFlag);
	    
    } //

    // 이름 필드 입력 후 이벤트 처리 : blur
    nameFld.onblur = (e) => {

        console.log("이름 필드 blur")
        // 이름 필드 유효성 점검(validation)
        // 기준)
        /*
            1) 한글"만" : 3~50자 (성함 사이 띄워쓰기 포함)
            2) 성/함(이름) : ex) 홍길동, 남궁민수
            3) regex(정규표현식) : /^[가-힣]{1,2}[가-힣]{1,48}$/
            4) 메시징 : 회원 이름은 한글 이름만 허용됩니다.
        */
        nameCheckFlag = isCheckFldValid(nameFld,                                 
                        /^[가-힣]{1,2}[가-힣]{1,48}$/,
                        "",
                        nameFldErrPnl,
                        nameErrMsg);
    } //     

    // 이름 필드 입력 후 이벤트 처리 : keyup
    nameFld.onkeyup = (e) => {

        console.log("이름 필드 blur")
        nameCheckFlag = isCheckFldValid(nameFld,                                 
                        /^[가-힣]{1,2}[가-힣]{1,48}$/,
                        nameFld.value,
                        nameFldErrPnl,
                        nameErrMsg);
    } //    

    
    ///////////////////////////////////////////////////////////////////////

    // 이메일 필드 유효성 및 중복 점검
    emailFld.onkeyup = (e) => {

        console.log("이메일 필드 onkeyup")
        // 이메일 필드 유효성 점검(validation)
        // 기준)
        /*
            1) "@", "." 포함여부 점검
            2) regex(정규표현식) : /^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/
            3) 메시징 : 회원 이메일을 작성하십시오
        */
		emailCheckFlag = isCheckFldValid(emailFld,
                        /^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/,
                        emailFld.value,
                        emailFldErrPnl,
                        emailErrMsg);
		 
		if (emailCheckFlag == true) {
			
			console.log("이메일 유효성 점검 성공");
			
			axios.get(`/memberProject/member/hasFld/EMAIL/${emailFld.value}`)
				 .then(function(response) {
					
					emailDuplicatedCheckFlag = response.data;
					console.log("response.data : ", response.data);
	
					let emailDupErrMsg = emailDuplicatedCheckFlag == true ? "중복되는 이메일이 존재합니다" : "사용가능한 이메일입니다"				   
					console.log(emailDupErrMsg)
					
					// 메시지 반복 출력 방지 : 출력할 메시지 있으면 출력
					if (emailDuplicatedCheckFlag == true) {
					
						alert(emailDupErrMsg);
						emailFld.value = "";
					}
					
				 })
				 .catch(function(err) {
					console.error("이메일 중복 점검 중 서버 에러가 발견되었습니다");
					//emailDuplicatedCheckFlag = false;
				 });
		} // if
    } //     

    // 휴대폰 필드 유효성 및 중복 점검
    mobileFld.onkeyup = (e) => {

        console.log("연락처(휴대폰) 필드 onkeyup")
        // 연락처 필드 유효성 점검(validation)
        // 기준)
        /*
            1) 휴대폰 입력 예시 : ex) 010-1234-5678
            2) regex(정규표현식) : /^010-\d{4}-\d{4}$/
            3) 메시징 : 회원 연락처(휴대폰)를 작성하십시오
        */
		mobileCheckFlag = isCheckFldValid(mobileFld,
                        /^010-\d{4}-\d{4}$/,
                        mobileFld.value,
                        mobileFldErrPnl,
                        mobileErrMsg)
		
		if (mobileCheckFlag == true) {
			
			console.log("연락처(휴대폰) 유효성 점검 성공");
			 
			axios.get(`/memberProject/member/hasFld/MOBILE/${mobileFld.value}`)
				 .then(function(response) {
					
					mobileDuplicatedCheckFlag = response.data;
					console.log("response.data : ", response.data);
	
					let mobileDupErrMsg = mobileDuplicatedCheckFlag == true ? "중복되는 연락처(휴대폰)가 존재합니다" : "사용가능한 연락처(휴대폰)입니다"			   
					console.log(mobileDupErrMsg);
					
					// 메시지 반복 출력 방지 : 출력할 메시지 있으면 출력
					if (mobileDuplicatedCheckFlag == true) {

						alert(mobileDupErrMsg);
						mobileFld.value = "";
					}	
						
				 })
				 .catch(function(err) {
					console.error("연락처(휴대폰) 중복 점검 중 서버 에러가 발견되었습니다");
					//mobileDuplicatedCheckFlag = false;				
				 });
		
		} // if
		
    } //

    /////////////////////////////////////////////////////////////////

    // 생일 필드 입력 후 이벤트 처리 : onkeyup
    // birthdayFld.onkeyup = (e) => {
	birthdayFld.onblur = (e) => {

        // console.log("생일 필드 onkeyup")
        console.log("생일 필드 onblur")

        // 생일 필드 유효성 점검(validation)
        // 기준)
        /*
            1) 생일 입력 예시 : ex) 2000-01-01
            2) regex(정규표현식) : /^([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))$/
            3) 메시징 : 회원 생일을 작성하십시오
        */
       birthdayCheckFlag = isCheckFldValid(birthdayFld,
                        /^([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))$/,
                        birthdayFld.value,
                        birthdayFldErrPnl,
                        birthdayErrMsg);
                        
       // 추가) 생일이 금일보다 이전 날짜인지 점검
       if (birthdayCheckFlag == true) {
		   
		   let now = Date.now();
		   let parsedBith = Date.parse(birthdayFld.value);
		   
		   console.log('금일 날짜 : ' + now);
		   console.log('생일 : ' + parsedBith);
		   console.log("생일을 적정성 여부(과거 일자 입력) : " + (parsedBith <= now));
		   
		   if (parsedBith > now) {
			   
			   alter("생일은 금일 날짜 이전이어야 합니다.");
			   birthdayCheckFlag = false;
			   
			   birthdayFld.value = "";
			   birthdayFld.focus();
		   }
	   } // 생일이 금일보다 이전 날짜인지 점검
	   
    } //
    
    /////////////////////////////////////////////////////////////////
    // 09.23 인증번호 발송
    let checkEmail = document.getElementById("checkEmail");
    
    checkEmail.onclick = function(e) {
		
		axios.get(`/memberProject/member/makeEmailCheckAuth/${checkEmail.value}`)
			 .then(function(response) {
				
				// console.log("서버 응답 : " + JSON.stringify(response));
				
				idDuplicatedCheckFlag = response.data;
				
				console.log("response.data : ", response.data);
				// console.log("response.data : ", typeof(response.data));
								
					
			 })
			 .catch(function(err) {
				console.error("아이디 중복 점검 중 서버 에러가 발견되었습니다");
			 });
	}

    /////////////////////////////////////////////////////////////////
 
    // 전송 버튼 이벤트 처리
    let frm = document.getElementById("frm");

    frm.onsubmit = function(e) {
	
		alert("회원가입 폼점검");	
	
        console.log("\n######## 회원가입폼 전체점검 ###############################\n");

        console.log(`아이디 점검 플래그(idCheckFlag) : ${idCheckFlag}`);
        console.log(`비밀번호1 점검 플래그(pw1CheckFlag) : ${pw1CheckFlag}`);
        console.log(`비밀번호2 점검 플래그(pw2CheckFlag) : ${pw2CheckFlag}`);
        console.log(`비밀번호 점검 플래그(pwCheckFlag) : ${pwCheckFlag}`);
        console.log(`이름 점검 플래그(nameCheckFlag) : ${nameCheckFlag}`);          

        // 이메일 및 연락처 점검 플래그
        console.log(`이메일 점검 플래그(emailCheckFlag) : ${emailCheckFlag}`);
        console.log(`휴대폰 점검 플래그(mobileCheckFlag) : ${mobileCheckFlag}`);

        // 생일 필드 점검 플래그
        console.log(`생일 점검 플래그(birthdayCheckFlag) : ${birthdayCheckFlag}`);

		// 아이디/이메일/휴대폰 중복 점검 플래그
		// 주의) 이 플래그들은 false 이어야 중복되지 않는 값을 의미합니다.  
		console.log(`아이디 중복 점검 플래그(idDuplicatedCheckFlag) : ${idDuplicatedCheckFlag}`);
		console.log(`이메일 중복 점검 플래그(emailDuplicatedCheckFlag) : ${emailDuplicatedCheckFlag}`);
		console.log(`휴대폰 중복 점검 플래그(mobileDuplicatedCheckFlag) : ${mobileDuplicatedCheckFlag}`);
		
		console.log("\n######################################################\n\n");
		
		alert("전체 폼점검 플래그 확인");
				
        // 모든 플래그 참(true) : 논리곱(&&)
		// 집전화 필드
		// 아이디/이메일/휴대폰 중복 점검 필드 추가
		// 주의) 아이디/이메일/휴대폰 중복 점검 필드는 false(중복 안됨)이어야 정상값입니다.
        if (idCheckFlag == true &&
            pwCheckFlag == true &&
            nameCheckFlag == true &&
            emailCheckFlag == true &&
            mobileCheckFlag == true &&
            birthdayCheckFlag == true &&
			idDuplicatedCheckFlag == false &&
			emailDuplicatedCheckFlag == false &&
			mobileDuplicatedCheckFlag == false)
        {
            alert("회원가입 정보 전송");
            
        } else {
	
            // TODO
			alert("폼 점검 오류");
            console.log("폼 점검 오류");
	
            // 필드들을 종합적으로 일일이 점검할 필요가 있기 때문에 
            // if ~ else if문은 사용하지 않고 개별 if문을 사용하도록 하겠습니다.

            // 아이디 필드 재점검                    
            if (idCheckFlag == false) {

                idCheckFlag = isCheckFldValid(idFld, 
                            /^[a-zA-Z]{1}\w{7,19}$/,
                            idFld.value,
                            idFldErrPnl,
                            idErrMsg);
            } //

            // 비밀번호 필드 재점검
            if (pw1CheckFlag == false) {

                pw1CheckFlag = isCheckFldValid(pw1Fld,                                 
                            /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
                            "",
                            pwFldErrPnl,
                            pwErrMsg);
            } //
            
            if (pw2CheckFlag == false) {

                pw2CheckFlag = isCheckFldValid(pw2Fld,                                 
                            /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
                            "",
                            pwFldErrPnl,
                            pwErrMsg);
            } //  
            
            if (pw1CheckFlag == true && pw2CheckFlag == true) {
				console.log("비밀번호 동등여부 : ", pw1Fld.value == pw2Fld.value);
				pwCheckFlag = pw1Fld.value == pw2Fld.value ? true : false;
			} else {
				console.log("비밀번호 불일치합니다");
				pwCheckFlag = false;
			}
			

            // 이름 필드 재점검
            if (nameCheckFlag == false) {

                nameCheckFlag = isCheckFldValid(nameFld,                                 
		                            /^[가-힣]{1,2}[가-힣]{1,48}$/,
		                            nameFld.value,
		                            nameFldErrPnl,
		                            nameErrMsg);

            } //   

            // 이메일 필드 재검검
            if (emailCheckFlag == false) {
                
                emailCheckFlag = isCheckFldValid(emailFld,
	                                /^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/,
	                                emailFld.value,
	                                emailFldErrPnl,
	                                emailErrMsg);

            } //    

            // 연락처 필드 재점검
            if (mobileCheckFlag == false) {

                mobileCheckFlag = isCheckFldValid(mobileFld,
			                        /^010-\d{4}-\d{4}$/,
			                        mobileFld.value,
			                        mobileFldErrPnl,
			                        mobileErrMsg);
            } //

            // 생일 필드 재점검
            if (birthdayCheckFlag == false) {

                birthdayCheckFlag = isCheckFldValid(birthdayFld,
                                    /^([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))$/,
                                    birthdayFld.value,
                                    birthdayFldErrPnl,
                                    birthdayErrMsg);
                                   
            ////////////////////////////////////////////
                
           // 추가) 생일이 금일보다 이전 날짜인지 점검
           if (birthdayCheckFlag == true) {
			   
			   let now = Date.now();
			   let parsedBith = Date.parse(birthdayFld.value);
			   
			   console.log('금일 날짜 : ' + now);
			   console.log('생일 : ' + parsedBith);		    
			   console.log("생일을 적정성 여부(과거 일자 입력) : " + (parsedBith <= now));
			   
			   if (parsedBith > now) {
				   alert("생일은 금일 날짜 이전이어야 합니다.");
				   birthdayCheckFlag = false;
				   
				   birthdayFld.value = "";
				   birthdayFld.focus();
				   }
				   
			  } // 생일이 금일보다 이전 날짜인지 점검
			} //

			// 아이디/이메일/휴대폰 중복 재점검에 따른 최종 메시징			
			if (idDuplicatedCheckFlag == true) {
				alert("중복되는 아이디가 존재합니다");
			}
			
			if (emailDuplicatedCheckFlag == true) {
				alert("중복되는 이메일이 존재합니다");
			}
			
			if (mobileDuplicatedCheckFlag == true) {
				alert("중복되는 휴대폰이 존재합니다");
			}
			
		    // 전송 방지
		    e.preventDefault();			
        } //

    } // frm.onsubmit ...

} // window.onload ...