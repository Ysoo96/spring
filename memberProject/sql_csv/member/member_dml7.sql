-- 아래와 같이 다수의 회원 롤을 구성하기 위해 
-- 관리자(ROLE_ADMIN) 롤을 의도적으로 추가할 수 있습니다.
INSERT INTO 
	   USER_ROLES 
	   VALUES 
	   (
	  	   USER_ROLES_SEQ.NEXTVAL 
	  	   , 'abcd1111' 
	  	   , 'ROLE_ADMIN'
  	   );
  	  
-- 다수의 롤(role) 생성 조회  	  
SELECT * 
  FROM USER_ROLES 
 WHERE USERNAME = 'abcd1111';  	

-- 회원 롤(들) 삭제
DELETE
  FROM USER_ROLES
 WHERE USERNAME = 'abcd1111';

-- 회원정보 삭제
DELETE
  FROM MEMBER_TBL
 WHERE ID = 'abcd1111';