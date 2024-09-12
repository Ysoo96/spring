-- 참고로 JPA의 테이블 자동 생성 기능을 사용하지 않고, 수동으로 테이블을 먼저 생성하는 방향으로 전개하겠습니다.

-- 게시글 삽입 이미지 파일명 저장 테이블

-- 테이블 PK 시퀀스
CREATE SEQUENCE image_upload_file_seq
START WITH 1
INCREMENT BY 1
MAXVALUE 99999
NOCYCLE;
​
-- ex)
-- 참고) windows 파일 최대 경로 길이 제한 : 260 char
-- https://learn.microsoft.com/ko-kr/windows/win32/fileio/naming-a-file?redirectedfrom=MSDN

-- save_filename : 2024/09/03/02f59711d9744c139aac3c5adc1d738d.jpg
-- 참고) 파일 저장 경로는 개인 설정에 따라 변경할 수 있습니다.
-- file_path : D:/student/lsh/works/spring/memberProject/upload/image/2024/09/03/02f59711d9744c139aac3c5adc1d738d.jpg

-- 참고) content-type 중에서 MIME type 최대 사이즈 : 127 char
-- https://datatracker.ietf.org/doc/html/rfc4288#section-4.2

-- 실질적인 MIME type : JPEG(JPG)/GIF/PNG 정도만 들어가도록 설정되어 있으므로 (MeaUtl.java 참조)
-- 실질적 MIME type 최대 크기 : 10 char
-- ex) image/jpeg, image/gif, image/png

-- file_size : 추후 업로드 쿼터 한계 용량에 따라 조절 가능

CREATE TABLE UPLOAD_FILE_TBL (
	ID NUMBER(10,0) PRIMARY KEY,
	FILENAME NVARCHAR2(300) NOT NULL,
	SAVE_FILENAME NVARCHAR2(500) NOT NULL,
	FILE_PATH NVARCHAR2(500) NOT NULL,
	CONTENT_TYPE NVARCHAR2(10),
	FILE_SIZE NUMBER(20,0) DEFAULT 0,
	REGDATE DATE DEFAULT SYSDATE
);