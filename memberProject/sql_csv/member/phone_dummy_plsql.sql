-- 회원 전화번호를 다양화하는 서비스

-- 서울시 지역번호 국번 관련 근거 : https://namu.wiki/w/%EA%B5%AD%EB%B2%88/%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C

-- 대상 지역 및 번호 : 관악/구로/금천
-- 818 : 구로구 가리봉동 전역, 구로동 중 경인선 이남지역, 금천구 가산동 전역, 독산동 북부, 관악구 신림동 서부
-- 890 : 금천구, 구로구(구로동 중 경인선 이북지역, 신도림동 제외), 광명시, 동작구 신대방동 서부, 영등포구 신길동, 대림동, 도림동 전역, 영등포구 중 경부선 이남지역[97]

-- 3397 : 구로구 구로동(1호선 이북지역 제외), 가리봉동 전역, 금천구 전역, 관악구 신림동 서부, 광명시 하안동, 소하동, 일직동[123]
-- 3285 : 관악구 봉천동 전역, 신림동 동부
-- 3281~3283 : 관악구 신림동 서부, 구로구 구로동(경인선 이북지역 제외), 가리봉동, 금천구 가산동 전역, 독산동 북부
-- 3284, 3289 : 동작구 신대방동 서부, 영등포구 대림동, 도림동, 신길동 전역, 영등포동 중 경부선 이남지역

SET SERVEROUTPUT ON;

DECLARE
	TYPE local_numbers_type IS VARRAY(9) OF VARCHAR(4);
	local_numbers local_numbers_type;	
	v_phone VARCHAR(13);
	v_phone1 CHAR(2);
	v_phone2 VARCHAR(4);
	v_phone3 VARCHAR(4);	
	temp_num NUMBER(1);
BEGIN
	
	local_numbers := local_numbers_type('818', '890', '3397', '3285', '3281', '3282', '3283', '3284', '3289');
	
	FOR i in 1..100 LOOP
		
		v_phone1 := '02';
		
		temp_num := round(DBMS_RANDOM.VALUE(1,9) ,0);
		v_phone2 := local_numbers(temp_num);
		
		SELECT LPAD(TO_CHAR(round(DBMS_RANDOM.VALUE(0,9999),0)), 4, '0') INTO v_phone3 FROM DUAL;
		
		v_phone := v_phone1 || '-' || v_phone2 || '-' || v_phone3;
		
		DBMS_OUTPUT.put_line(v_phone);
        
        UPDATE MEMBER_TBL SET PHONE = v_phone
        WHERE ID = 'mbc_' || (1000+i);
		
	END LOOP;
    
END;
/