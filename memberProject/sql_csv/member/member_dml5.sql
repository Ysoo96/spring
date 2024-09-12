-- 검색 

-- "신림"이라는 검색어를 입력하여 회원정보를 검색 : 페이징 적용

-- 검색에 따른 레코드 수
SELECT COUNT(*) 
  FROM MEMBER_TBL
 WHERE ROAD_ADDRESS LIKE '%신림%'
    OR JIBUN_ADDRESS LIKE '%신림%'
    OR DETAIL_ADDRESS LIKE '%신림%';
    
-- 참고) LISTAGG 함수 
-- : https://docs.oracle.com/cd/E11882_01/server.112/e41084/functions089.htm#CJABDFBD
-- 
SELECT DISTINCT m3.* 
       , ( SELECT LISTAGG(r2.ROLE, ',') 
                    WITHIN GROUP (ORDER BY m2.ID) 
             FROM MEMBER_TBL m2, USER_ROLES r2
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"           
  FROM MEMBER_TBL m3, USER_ROLES r3;


SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND ROAD_ADDRESS LIKE '%신림%'
                      OR JIBUN_ADDRESS LIKE '%신림%'
		              OR DETAIL_ADDRESS LIKE '%신림%'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
 -- 그외의 정보 검색
 /*
 검색 대상 : 검색 방법
 주소, 이름, 이메일, 휴대폰 : 유사 검색(방법)
 아이디, 성별 : 동일 검색
 이메일, 휴대폰 : 동일 검색
 */
 
 -- 이름 : 
 -- "훈"이라는 말이 포함된 이름 검색
 -- 레코드 수
 SELECT COUNT(*)
 FROM MEMBER_TBL
 WHERE NAME LIKE '%훈%';
 
 -- 페이징
 SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND NAME LIKE '%훈%'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
---------------------------------------------------------------------
 
 -- 아이디 검색 : 동일 검색
 -- 레코드 수
 SELECT COUNT(*)
 FROM MEMBER_TBL
 WHERE ID = 'abcd1111';
 
 -- 페이징
 SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND ID = 'abcd1111'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
 -- 회원등급(role) 미포함
 SELECT *
 FROM MEMBER_TBL
 WHERE ID = 'abcd1111';
 
 -- 회원등급(role) 포함
 SELECT DISTINCT m3.*
        , ( SELECT LISTAGG(r2.ROLE, ',')
                    WITHIN GROUP (ORDER BY m2.ID)
            FROM MEMBER_TBL m2, USER_ROLES r2
            WHERE r2.USERNAME = m2.ID
            AND r2.USERNAME = m3.ID ) AS "ROLE"
FROM MEMBER_TBL m3, USER_ROLES r3
WHERE m3.ID = r3.USERNAME
AND ID = 'abcd1111';
 
 ---------------------------------------------------------------------
 
-- 성별 검색 : 동일 검색
 
-- '남성'만 검색
-- 레코드 수
 SELECT COUNT(*)
 FROM MEMBER_TBL
 WHERE GENDER = 'm';
 
-- 페이징
 SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND GENDER = 'm'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
-- '여성'만 검색
-- 레코드 수
 SELECT COUNT(*)
 FROM MEMBER_TBL
 WHERE GENDER = 'f';
 
-- 페이징
 SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND GENDER = 'f'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
 ---------------------------------------------------------------------
 
 -- 이메일 검색 : 동일 검색
 -- 이메일이  mbc_1@abcd.com 인 회원정보 검색
 -- 레코드 수
 SELECT COUNT(*)
 FROM MEMBER_TBL
 WHERE EMAIL = 'mbc_1@abcd.com';
 
-- 회원등급(role) 미포함
SELECT * 
  FROM MEMBER_TBL 
 WHERE EMAIL = 'mbc_1@abcd.com';


-- 회원등급(role) 포함
SELECT DISTINCT m3.* 
       , ( SELECT LISTAGG(r2.ROLE, ',') 
                    WITHIN GROUP (ORDER BY m2.ID) 
             FROM MEMBER_TBL m2, USER_ROLES r2  
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"           
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND EMAIL = 'mbc_1@abcd.com';
 
 ---------------------------------------------------------------------
 
-- 휴대폰 : 동일/유사 검색
-- 휴대폰 번호가  010-1234-1023 인 회원정보 검색
-- 유사 검색 : "08" 번호가 포함된 휴대폰 정보로 회원정보 검색

-- 레코드 수 = 1
SELECT COUNT(*) 
  FROM MEMBER_TBL
 WHERE MOBILE = '010-1234-1023';

-- 회원등급(role) 미포함
SELECT * 
  FROM MEMBER_TBL 
 WHERE MOBILE = '010-1234-1023';
 
-- 유사 검색 : "08" 번호가 포함된 휴대폰 정보로 회원정보 검색
SELECT COUNT(*)
FROM MEMBER_TBL
WHERE MOBILE LIKE '%08%';

-- 회원등급(role) 포함
SELECT DISTINCT m3.* 
       , ( SELECT LISTAGG(r2.ROLE, ',') 
                    WITHIN GROUP (ORDER BY m2.ID) 
             FROM MEMBER_TBL m2, USER_ROLES r2  
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"           
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND MOBILE = '010-1234-1023';
   
-- 유사 검색
-- 유사 검색 : "08" 번호가 포함된 휴대폰 정보로 회원정보 검색
-- 페이징
 SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND MOBILE LIKE '%08%'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
---------------------------------------------------------------------
 
-- 연락처(일반 전화) : 동일/유사 검색
-- 일반 전화번호가 02-860-1234 인 회원정보 검색
-- 유사 검색 : "77"이 포함된 일반전화 번호로 회원정보 검색

-- 일반 전화번호 데이터의 다양화를 위한 PL/SQL 실행하여 
-- 일반 전화번호를 다양화할 수 있습니다.

-- 레코드 수
SELECT COUNT(*) 
  FROM MEMBER_TBL
 WHERE PHONE = '02-860-1234';
 
SELECT COUNT(*)
  FROM MEMBER_TBL
 WHERE PHONE LIKE '%77%';

-- 회원등급(role) 미포함
SELECT * 
  FROM MEMBER_TBL 
 WHERE PHONE = '02-860-1234';

-- 회원등급(role) 포함
-- 유사 검색 : "77"이 포함된 일반전화 번호로 회원정보 검색
-- 페이징 
 SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND PHONE LIKE '%77%'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT DISTINCT m3.* 
                         , ( SELECT LISTAGG(r2.ROLE, ',') 
                                      WITHIN GROUP (ORDER BY m2.ID) 
                               FROM MEMBER_TBL m2, USER_ROLES r2  
                              WHERE r2.USERNAME = m2.ID
                                AND r2.USERNAME = m3.ID ) AS "ROLE"           
                    FROM MEMBER_TBL m3, USER_ROLES r3
                   WHERE m3.ID = r3.USERNAME
			         AND PHONE = '02-860-1234'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
SELECT DISTINCT m3.* 
       , ( SELECT LISTAGG(r2.ROLE, ',') 
                    WITHIN GROUP (ORDER BY m2.ID) 
             FROM MEMBER_TBL m2, USER_ROLES r2  
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"           
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND PHONE = '02-860-1234';