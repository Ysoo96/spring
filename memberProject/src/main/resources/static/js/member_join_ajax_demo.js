
window.onload = () => {

    // 전송 버튼 이벤트 처리
    let submitBtn = document.getElementById("submit_btn");    
    // let frm = document.getElementById("frm");

    submitBtn.onclick = function(e) {
	// frm.onsubmit = function(e) {		
	
        let frm = document.getElementById("frm");
        
        // 주의) form의 기본 전송 방식 MIME이 "x-www-form-urlencoded"이기 때문에 
        // 백단(backend)에서의 요청에 따른 전통적인 form 전송(submit) 처리시 
        // @RequestBody로 수신할 경우, 실질적인 인자 전송에는 문제가 없지만 아래와 같은 경고 메시지 발생.  
        // 
        // Warning(경고) : Resolved [org.springframework.web.HttpMediaTypeNotSupportedException: 
        // Content-Type 'application/x-www-form-urlencoded;charset=UTF-8' is not supported]
        let formData = new FormData(frm);               
        
        // 참고) fromEntries 
        // : https://developer.mozilla.org/ko/docs/Web/JavaScript/Reference/Global_Objects/Object/fromEntries
        // JSON.stringify(Object.fromEntries(formData)),
        
        let json = JSON.stringify(Object.fromEntries(formData))
        console.log("json (formdata) : ", json)
        
        // Javascript XHR 전송
        // 참고) XHR : https://developer.mozilla.org/ko/docs/Web/API/XMLHttpRequest
        // 참고) XHR send() : https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/send
        /*
        const xhr = new XMLHttpRequest();
		xhr.open("POST", "/memberProject/member/joinProcAjax", true);
		xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		
		xhr.onreadystatechange = () => {
		  
			if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
			    console.log("전송 성공");
			}
			
		};
		
		xhr.send(json);
		*/
        
        // 참고) 일반 form 전송시 기본 Content Type
        // application/x-www-form-urlencoded;charset=ISO-8859-1 
        
        axios.post('/memberProject/member/joinProcAjax', 
			       //  JSON.stringify(Object.fromEntries(formData)),
        		   formData,
        		   { headers: { 'content-type': 'application/json;charset=UTF-8' } },
        		 )
			  .then(function (response) {
				  
			     console.log("응답 : " + response.data);
			     
			     if (response.data == true) {
					 
					 alert("회원가입에 성공하였습니다. 로그인 페이지로 이동합니다.");
					 location.href = "/memberProject/login";
					 
				 } else {
					 
					 alert("회원가입에 실패하였습니다. 회원가입을 다시 시도하십시오.");
				 }
			    
			  })
			  .catch(function (error) {
			    console.log("회원가입 에러 처리 : " + error);
			  });
               
    } // 

} // window.onload ...