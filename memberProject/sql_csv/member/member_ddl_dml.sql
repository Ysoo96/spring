CREATE TABLE MEMBER_TBL (
ID VARCHAR2(20) PRIMARY KEY,
PW VARCHAR2(60) NOT NULL, -- 패스워드 암호화 위한 변경 : PW VARCHAR2(60) NOT NULL, 
NAME VARCHAR2(100) NOT NULL,
EMAIL VARCHAR2(50) NOT NULL UNIQUE,
MOBILE VARCHAR2(13) NOT NULL UNIQUE,
BIRTHDAY DATE,
JOINDATE DATE DEFAULT CURRENT_DATE
);
 
COMMENT ON COLUMN MEMBER_TBL.ID IS '아이디';
COMMENT ON COLUMN MEMBER_TBL.PW IS '비밀번호';
COMMENT ON COLUMN MEMBER_TBL.NAME IS '이름';
COMMENT ON COLUMN MEMBER_TBL.EMAIL IS '이메일';
COMMENT ON COLUMN MEMBER_TBL.MOBILE IS '휴대폰';
COMMENT ON COLUMN MEMBER_TBL.BIRTHDAY IS '생년월일';
COMMENT ON COLUMN MEMBER_TBL.JOINDATE IS '가입일';
 
DROP TABLE member_tbl;

----------------------------------------------------------

CREATE TABLE member_tbl (
id varchar2(20) CONSTRAINT member_tbl_id_pk PRIMARY KEY,
pw varchar2(20) CONSTRAINT member_tbl_pw_nn NOT NULL,
name varchar2(100) CONSTRAINT member_tbl_name_nn NOT NULL,
email varchar2(50) CONSTRAINT member_tbl_email_nn NOT NULL,
mobile varchar2(13) CONSTRAINT member_tbl_mobile_nn NOT NULL,
birthday DATE,
joindate DATE DEFAULT current_date,
CONSTRAINT member_tbl_email_u UNIQUE(email),
CONSTRAINT member_tbl_mobile_u UNIQUE(mobile)
);

COMMENT ON COLUMN MEMBER_TBL.ID IS '아이디';
COMMENT ON COLUMN MEMBER_TBL.PW IS '비밀번호';
COMMENT ON COLUMN MEMBER_TBL.NAME IS '이름';
COMMENT ON COLUMN MEMBER_TBL.EMAIL IS '이메일';
COMMENT ON COLUMN MEMBER_TBL.MOBILE IS '휴대폰';
COMMENT ON COLUMN MEMBER_TBL.BIRTHDAY IS '생년월일';
COMMENT ON COLUMN MEMBER_TBL.JOINDATE IS '가입일';
 
DROP TABLE member_tbl;

------------------------------------------------------------------

CREATE TABLE member_tbl (
id varchar2(20),
pw varchar2(20) CONSTRAINT member_tbl_pw_nn NOT NULL,
name varchar2(100) CONSTRAINT member_tbl_name_nn NOT NULL,
email varchar2(50) CONSTRAINT member_tbl_email_nn NOT NULL,
mobile varchar2(13) CONSTRAINT member_tbl_mobile_nn NOT NULL,
birthday DATE,
joindate DATE DEFAULT current_date,
CONSTRAINT member_tbl_id_pk PRIMARY KEY(id),
CONSTRAINT member_tbl_email_u UNIQUE(email),
CONSTRAINT member_tbl_mobile_u UNIQUE(mobile)
);
 
COMMENT ON COLUMN MEMBER_TBL.ID IS '아이디';
COMMENT ON COLUMN MEMBER_TBL.PW IS '비밀번호';
COMMENT ON COLUMN MEMBER_TBL.NAME IS '이름';
COMMENT ON COLUMN MEMBER_TBL.EMAIL IS '이메일';
COMMENT ON COLUMN MEMBER_TBL.MOBILE IS '휴대폰';
COMMENT ON COLUMN MEMBER_TBL.BIRTHDAY IS '생년월일';
COMMENT ON COLUMN MEMBER_TBL.JOINDATE IS '가입일';

DROP TABLE member_tbl;

-------------------------------------------------------------

CREATE TABLE member_tbl (
id varchar(20),
pw nvarchar2(20),
name nvarchar2(100),
email nvarchar2(50),
mobile nvarchar2(13),
birthday DATE,
joindate DATE DEFAULT current_date
);
  
ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_id_pk PRIMARY KEY(id);

ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_email_u UNIQUE(email);
 
ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_mobile_u UNIQUE(mobile);
 
ALTER TABLE member_tbl
MODIFY (pw CONSTRAINT member_tbl_pw_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (name CONSTRAINT member_tbl_name_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (email CONSTRAINT member_tbl_email_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (mobile CONSTRAINT member_tbl_mobile_nn NOT NULL);

-- 참고 제약조건(constraint) 삭제 예시

ALTER TABLE member_tbl DROP CONSTRAINT MEMBER_TBL_EMAIL_U;

ALTER TABLE member_tbl DROP CONSTRAINT MEMBER_TBL_MOBILE_U;

ALTER TABLE member_tbl DROP CONSTRAINT MEMBER_TBL_ID_PK; 

DROP TABLE member_tbl;


-------------------------------------------------------------

/* 참고) 성별 제약조건은 아래와 같이 작성할 수도 있습니다. */
ALTER TABLE member_tbl ADD constraint member_tbl_gender_ck check (gender = 'm' OR gender = 'f');

--------------------------------------------------------------

-------------------------------------------------------------------------------

SELECT TO_DATE('July 8, 2024, 2:10 P.M.',
               'Month dd, YYYY, HH:MI P.M.',
               'NLS_DATE_LANGUAGE = American')
FROM DUAL;

-------------------------------------------------------------------------------


INSERT INTO member_tbl VALUES
('abcd1111',
 '#Abcd1234',
 '홍길동',
 'm',
 'abcd1111@abcd.com',
 '010-1111-3333',
 '02-1111-2222',
 '08290',
 '서울특별시 관악구 신림로 340',
 '서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰',
 '6층 MBC 아카데미',
 '2000-01-01',
  sysdate);
  
  
INSERT INTO member_tbl VALUES
('abcd2222',
 '#Abcd1234',
 '류관순',
 'f',
 'abcd2222@efgh.com',
 '010-2222-7777',
 '02-1111-2222',
 '08290',
 '서울특별시 관악구 신림로 340',
 '서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰',
 '6층 MBC 컴퓨터 아카데미',
 '2000-01-01',
 TO_DATE('August 4, 2023, 2:00 P.M.',
        'Month dd, YYYY, HH:MI P.M.',
        'NLS_DATE_LANGUAGE = American'));

 UPDATE member_tbl SET
 pw='#Abcd1234',
 email='java@djkangnam.com',
 zip='08290',
 jibun_address='서울특별시 관악구 신림동 1433-120번지',
 detail_address='MBC 아카데미 신림점 별관 8층'
 WHERE id='abcd1111';
 
 SELECT * FROM member_tbl;
 
 -- 개별 회원정보 조회
 SELECT * FROM member_tbl
 WHERE id='abcd1111';
 
 DELETE member_tbl;
 
 DELETE member_tbl
 WHERE id='abcd1111';

