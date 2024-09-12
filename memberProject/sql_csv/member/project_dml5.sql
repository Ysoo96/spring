-- 검색 
    
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
 
 -- 그외의 정보 검색
 /*
 검색 대상 : 검색 방법
 주소, 이름, 이메일, 휴대폰 : 유사 검색(방법)
 아이디, 성별 : 동일 검색
 이메일, 휴대폰 : 동일 검색
 */
 
 -- 이름 : 
 -- "홍"이라는 말이 포함된 이름 검색
 -- 레코드 수
 SELECT COUNT(*)
 FROM MEMBER_TBL
 WHERE NAME LIKE '%홍%';
 
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
			         AND NAME LIKE '%홍%'
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
 
-- 휴대폰 : 동일/유사 검색
-- 휴대폰 번호가  010-1111-1111 인 회원정보 검색
-- 유사 검색 : "11" 번호가 포함된 휴대폰 정보로 회원정보 검색

-- 레코드 수 = 1
SELECT COUNT(*) 
  FROM MEMBER_TBL
 WHERE MOBILE = '010-1111-1111';

-- 회원등급(role) 미포함
SELECT * 
  FROM MEMBER_TBL 
 WHERE MOBILE = '010-1111-1111';
 
-- 유사 검색 : "08" 번호가 포함된 휴대폰 정보로 회원정보 검색
SELECT COUNT(*)
FROM MEMBER_TBL
WHERE MOBILE LIKE '%11%';

-- 회원등급(role) 포함
SELECT DISTINCT m3.* 
       , ( SELECT LISTAGG(r2.ROLE, ',') 
                    WITHIN GROUP (ORDER BY m2.ID) 
             FROM MEMBER_TBL m2, USER_ROLES r2  
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"           
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND MOBILE = '010-1111-1111';
   
-- 유사 검색
-- 유사 검색 : "11" 번호가 포함된 휴대폰 정보로 회원정보 검색
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
			         AND MOBILE LIKE '%11%'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;