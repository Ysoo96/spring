-- 회원정보 삽입(생성)
INSERT INTO 
       MEMBER_TBL     
       (
 	     	ID
		  , PW
		  , NAME
		  , GENDER
		  , EMAIL
		  , MOBILE
		  , PHONE
		  , ZIP
		  , ROAD_ADDRESS
		  , JIBUN_ADDRESS
		  , DETAIL_ADDRESS
		  , BIRTHDAY
		  , JOINDATE
	   )
       VALUES
       (
           'abcd1111',
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
           SYSDATE
       )
;

-- 회원정보 존재 여부
SELECT COUNT(*) FROM MEMBER_TBL WHERE ID = 'abcd1111';

-- 회원정보 수정(갱신)
UPDATE
 	   MEMBER_TBL
   SET PW = '#Abcd1234'
 	 , EMAIL = 'java@djkangnam.com'
 	 , MOBILE = '010-1234-5757'
 	 , PHONE = '02-1234-5678'
 	 , ZIP = '08290'
 	 , ROAD_ADDRESS = '서울특별시 관악구 남부순환로 1587 (신림동)'
 	 , JIBUN_ADDRESS = '서울특별시 관악구 신림동 1433-120번지'
 	 , DETAIL_ADDRESS = 'MBC 아카데미 신림점 별관 8층'
 WHERE ID = 'abcd1111'
 ;
