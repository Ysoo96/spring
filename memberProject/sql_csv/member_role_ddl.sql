-- 스프링 시큐리티 적용을 위해 패쓰워드 필드 길이 조정
-- ALTER TABLE MEMBER_TBL MODIFY PW VARCHAR2(60);


CREATE TABLE USER_ROLES (
    USER_ROLE_ID  NUMBER(11)  NOT NULL,
    USERNAME      VARCHAR(20) NOT NULL,
    ROLE          VARCHAR(45) NOT NULL,
    PRIMARY KEY (USER_ROLE_ID),
    CONSTRAINT FK_USERNAME FOREIGN KEY (USERNAME) 
    REFERENCES MEMBER_TBL (ID)
);
 
CREATE SEQUENCE USER_ROLES_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999
    NOCYCLE; 
    
-- ROLE 더미(dummy) 정보 생성

DECLARE
BEGIN
 
    FOR i IN 1..100 LOOP
    
        INSERT INTO 
               USER_ROLES VALUES
               (
                    USER_ROLES_SEQ.NEXTVAL
                    , 'mbc_' || (1000+I)        
                    , 'ROLE_USER'
                 );

     END LOOP;
 
    COMMIT;    
END;
/    

-- 기타 롤(role) 정보가 없는 개별 아이디에 대한 롤 정보 추가
INSERT INTO 
	   USER_ROLES
	   VALUES
	   (
	       USER_ROLES_SEQ.NEXTVAL
	       , 'abcd1111'       
	       , 'ROLE_USER'
	   );
                 
-- 롤(role) 테이블의 회원 아이디(username) 및 롤(role) 확인    
-- 참고로 Spring Security에서는 롤(role)은 1:n 관계로써 1인당 다수의 롤을 보유할 수 있습니다.
-- 추후 자체 규정을 확립하여 1:1 관계로 규정하여 프로그래밍할 수도 있습니다.
SELECT USERNAME
       , ROLE
  FROM USER_ROLES 
 ORDER BY USERNAME; 