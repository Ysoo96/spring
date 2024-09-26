// 09.26 회원 아이디조회

window.onload = () => {
	
	let sendEmail = document.getElementById("sendEmail"); // 아이디 조회버튼
	let name = document.getElementById("name"); // 이름 입력 필드
	let email = document.getElementById("email"); // 이메일 입력 필드
	
	sendEmail.onclick = function(e) {
		console.log("아이디 조회 클릭");
		
		nameCheckFlag = isCheckFldValid(nameFld,                                 
		                            /^[가-힣]{1,2}[가-힣]{1,48}$/,
		                            nameFld.value,
		                            nameFldErrPnl,
		                            nameErrMsg);
		
		emailCheckFlag = isCheckFldValid(emailFld,
                    /^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/,
                    emailFld.value,
                    emailFldErrPnl,
                    emailErrMsg);
                    
		if (test="${check == 1}") {
			name.innerText = "";
			email.innerText = "";
			
			alert("일치하는 회원정보가 존재하지 않습니다.");
		}
		
	} // sendEmail.onclick
	
} // onload