SELECT *
  FROM MEMBER_TBL
 WHERE ID = 'abcd1111';

-- 회원 가입시

-- 아이디 존재(중복) 여부
SELECT COUNT(*)
  FROM MEMBER_TBL
 WHERE ID = 'abcd1111';
 
-- 이메일 존재(중복) 여부
SELECT COUNT(*)
  FROM MEMBER_TBL
 WHERE EMAIIL = 'abcd1111@abcd.com';
 
-- 연락처(휴대폰) 존재(중복) 여부
SELECT COUNT(*)
  FROM MEMBER_TBL
 WHERE MOBILE = '010-1111-3333';
 
-- 회원 정보 조회
SELECT *
  FROM MEMBER_TBL
 WHERE ID = 'abcd1111';

-------------------------------------------

-- 회원 정보 수정시 메일 중복 점검
-- 자신의 기준 메일과는 중복 허용하면서, 다른 메일과는 중복 불허

-- 기존 메일 재사용
SELECT COUNT(*)
  FROM ( SELECT EMAIL
           FROM MEMBER_TBL
          WHERE ID != 'abcd1111' )
 WHERE EMAIL = 'abcd1111@abcd.com';
 
-- 신규 메일 사용 : 다른 회원과 메일 정보 중복
SELECT COUNT(*)
  FROM ( SELECT EMAIL
           FROM MEMBER_TBL
          WHERE ID != 'abcd1111' )
 WHERE EMAIL = 'mbc_23@abcd.com';

-- 신규 메일 사용 : 다른 회원과 메일 정보 중복 X
SELECT COUNT(*)
  FROM ( SELECT EMAIL
           FROM MEMBER_TBL
          WHERE ID != 'abcd1111' )
 WHERE EMAIL = 'mbcmbc@abcd.com';
 
-- 회원 정보 수정시 연락처(휴대폰) 중복 점검
-- 자신의 기존 연락처(휴대폰)와는 중복 허용하면서, 다른 회원 연락처(휴대폰)와는 중복 불허

-- 기존 연락처(휴대폰) 재사용
SELECT COUNT(*)
  FROM ( SELECT MOBILE
           FROM MEMBER_TBL
          WHERE ID != 'abcd1111' )
 WHERE MOBILE = '010-1111-3333';
 
-- 신규 연락처(휴대폰) 사용 : 다른 회원과 연락처(휴대폰) 정보 중복
SELECT COUNT(*)
  FROM ( SELECT MOBILE
           FROM MEMBER_TBL
          WHERE ID != 'abcd1111' )
 WHERE MOBILE = '010-1234-1023';

-- 신규 연락처(휴대폰) 사용 : 다른 회원과 연락처(휴대폰) 정보 중복 X
SELECT COUNT(*)
  FROM ( SELECT MOBILE
           FROM MEMBER_TBL
          WHERE ID != 'abcd1111' )
 WHERE MOBILE = '010-2222-3434';