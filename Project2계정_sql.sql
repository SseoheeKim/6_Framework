
-- 관리자 계정 ---------------------------------------------------------

-- 계정 삭제
DROP USER 계정명 [CASCADE];

ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;

-- 수업용 프로젝트 계정 생성
CREATE USER project IDENTIFIED BY project1234;

-- 권한부여
GRANT CONNECT, RESOURCE, CREATE VIEW TO project;

-- 객체 생성 공간 할당
ALTER USER project DEFAULT TABLESPACE SYSTEM QUOTA UNLIMITED ON SYSTEM;


-------------------------------------------------------------------------
-- 수업용 프로젝트 계정


CREATE TABLE "BOARD" (
	"BOARD_NO"	NUMBER		NOT NULL,
	"BOARD_TITLE"	VARCHAR2(150)		NOT NULL,
	"BOARD_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"B_CREATE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"B_UPDATE_DATE"	DATE		NULL,
	"READ_COUNT"	NUMBER	DEFAULT 0	NOT NULL,
	"BOARD_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_CODE"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD"."BOARD_NO" IS '게시글 번호(SEQ_BOARD_NO)';

COMMENT ON COLUMN "BOARD"."BOARD_TITLE" IS '게시글 제목';

COMMENT ON COLUMN "BOARD"."BOARD_CONTENT" IS '게시글 내용';

COMMENT ON COLUMN "BOARD"."B_CREATE_DATE" IS '게시글 작성일';

COMMENT ON COLUMN "BOARD"."B_UPDATE_DATE" IS '마지막 수정일(수정시 UPDATE)';

COMMENT ON COLUMN "BOARD"."READ_COUNT" IS '조회수';

COMMENT ON COLUMN "BOARD"."BOARD_DEL_FL" IS '게시글 삭제여부(Y,N)';

COMMENT ON COLUMN "BOARD"."MEMBER_NO" IS '작성자 회원 번호';

COMMENT ON COLUMN "BOARD"."BOARD_CODE" IS '게시판 코드 번호';

CREATE TABLE "BOARD_IMG" (
	"IMG_NO"	NUMBER		NOT NULL,
	"IMG_PATH"	VARCHAR2(300)		NOT NULL,
	"IMG_RENAME"	VARCHAR2(30)		NOT NULL,
	"IMG_ORIGINAL"	VARCHAR2(300)		NOT NULL,
	"IMG_ORDER"	NUMBER		NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD_IMG"."IMG_NO" IS '이미지 번호(SEQ_IMG_NO)';

COMMENT ON COLUMN "BOARD_IMG"."IMG_PATH" IS '이미지 저장 폴더 경로';

COMMENT ON COLUMN "BOARD_IMG"."IMG_RENAME" IS '변경된 이미지 파일 이름';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORIGINAL" IS '원본 이미지 파일 이름';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORDER" IS '이미지 파일 순서';

COMMENT ON COLUMN "BOARD_IMG"."BOARD_NO" IS '이미지가 첨부된 게시글 번호';

CREATE TABLE "BOARD_LIKE" (
	"BOARD_NO"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD_LIKE"."BOARD_NO" IS '게시글 번호';

COMMENT ON COLUMN "BOARD_LIKE"."MEMBER_NO" IS '좋아요 누른 회원번호';

CREATE TABLE "COMMENT" (
	"COMMENT_NO"	NUMBER		NOT NULL,
	"COMMENT_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"C_CREATE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"COMMENT_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL,
	"PARENT_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "COMMENT"."COMMENT_NO" IS '댓글번호(SEQ_COMMENT_NO)';

COMMENT ON COLUMN "COMMENT"."COMMENT_CONTENT" IS '댓글 내용';

COMMENT ON COLUMN "COMMENT"."C_CREATE_DATE" IS '댓글 작성일';

COMMENT ON COLUMN "COMMENT"."COMMENT_DEL_FL" IS '댓글 삭제 여부(Y/N)';

COMMENT ON COLUMN "COMMENT"."MEMBER_NO" IS '댓글 작성한 회원 번호';

COMMENT ON COLUMN "COMMENT"."BOARD_NO" IS '댓글이 달린 게시글 번호';

COMMENT ON COLUMN "COMMENT"."PARENT_NO" IS '부모 댓글 번호';

CREATE TABLE "BOARD_TYPE" (
	"BOARD_CODE"	NUMBER		NOT NULL,
	"BOARD_NAME"	VARCHAR2(300)		NOT NULL
);

COMMENT ON COLUMN "BOARD_TYPE"."BOARD_CODE" IS '게시판 종류별 코드번호(SEQ_BOARD_CODE)';

COMMENT ON COLUMN "BOARD_TYPE"."BOARD_NAME" IS '게시판 이름';

CREATE TABLE "MEMBER" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"MEMBER_EMAIL"	VARCHAR2(50)		NOT NULL,
	"MEMBER_PW"	VARCHAR2(100)		NOT NULL,
	"MEMBER_NICKNAME"	VARCHAR2(30)		NOT NULL,
	"MEMBER_TEL"	CHAR(11)		NOT NULL,
	"MEMBER_ADDRESS"	VARCHAR2(300)		NULL,
	"PROFILE_IMG"	VARCHAR2(300)		NULL,
	"ENROLL_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"AUTHORITY"	NUMBER	DEFAULT 1	NOT NULL
);

COMMENT ON COLUMN "MEMBER"."MEMBER_NO" IS '회원 번호(SEQ_MEMBER_NO)';

COMMENT ON COLUMN "MEMBER"."MEMBER_EMAIL" IS '회원 이메일(아이디로 사용)';

COMMENT ON COLUMN "MEMBER"."MEMBER_PW" IS '회원 비밀번호(암호화 진행)';

COMMENT ON COLUMN "MEMBER"."MEMBER_NICKNAME" IS '회원 닉네임(중복불가)';

COMMENT ON COLUMN "MEMBER"."MEMBER_TEL" IS '휴대폰 번호( - 생략)';

COMMENT ON COLUMN "MEMBER"."MEMBER_ADDRESS" IS '회원주소';

COMMENT ON COLUMN "MEMBER"."PROFILE_IMG" IS '프로필 이미지 경로';

COMMENT ON COLUMN "MEMBER"."ENROLL_DATE" IS '회원가입일';

COMMENT ON COLUMN "MEMBER"."MEMBER_DEL_FL" IS '탈퇴 여부(Y,N)';

COMMENT ON COLUMN "MEMBER"."AUTHORITY" IS '회원 권한(1: 일반 /  2:관리자)';


ALTER TABLE "BOARD" ADD CONSTRAINT "PK_BOARD" PRIMARY KEY (
	"BOARD_NO"
);

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "PK_BOARD_IMG" PRIMARY KEY (
	"IMG_NO"
);

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "PK_BOARD_LIKE" PRIMARY KEY (
	"BOARD_NO",
	"MEMBER_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "PK_COMMENT" PRIMARY KEY (
	"COMMENT_NO"
);

ALTER TABLE "BOARD_TYPE" ADD CONSTRAINT "PK_BOARD_TYPE" PRIMARY KEY (
	"BOARD_CODE"
);

ALTER TABLE "MEMBER" ADD CONSTRAINT "PK_MEMBER" PRIMARY KEY (
	"MEMBER_NO"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "FK_BOARD_TYPE_TO_BOARD_1" FOREIGN KEY (
	"BOARD_CODE"
)
REFERENCES "BOARD_TYPE" (
	"BOARD_CODE"
);

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "FK_BOARD_TO_BOARD_IMG_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_BOARD_TO_BOARD_LIKE_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);


ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_LIKE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_MEMBER_TO_COMMENT_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_BOARD_TO_COMMENT_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_COMMENT_TO_COMMENT_1" FOREIGN KEY (
	"PARENT_NO"
)
REFERENCES "COMMENT" (
	"COMMENT_NO"
);


SELECT * FROM MEMBER;
SELECT * FROM BOARD;
SELECT * FROM BOARD_TYPE;
SELECT * FROM BOARD_IMG;
SELECT * FROM BOARD_LIKE;
SELECT * FROM "COMMENT";


-- 시퀀스 생성
CREATE SEQUENCE SEQ_MEMBER_NO NOCACHE; -- 회원 번호
CREATE SEQUENCE SEQ_BOARD_NO NOCACHE; -- 게시글 번호
CREATE SEQUENCE SEQ_IMG_NO NOCACHE; -- 게시글 이미지 번호
CREATE SEQUENCE SEQ_COMMENT_NO NOCACHE; -- 댓글 번호
CREATE SEQUENCE SEQ_BOARD_CODE NOCACHE; -- 게시판 종류 코드 번호

-- 게시판 종류 추가
INSERT INTO BOARD_TYPE VALUES(SEQ_BOARD_CODE.NEXTVAL, '공지사항');
INSERT INTO BOARD_TYPE VALUES(SEQ_BOARD_CODE.NEXTVAL, '자유 게시판');
INSERT INTO BOARD_TYPE VALUES(SEQ_BOARD_CODE.NEXTVAL, '질문 게시판');
INSERT INTO BOARD_TYPE VALUES(SEQ_BOARD_CODE.NEXTVAL, '테스트 게시판');


COMMIT;

-- 게시판 종류 조회
SELECT * FROM BOARD_TYPE ORDER BY 1;




-- 회원 샘플 데이터 삽입INSERT INTO MEMBER VALUES (SEQ_MEMBER_NO.NEXTVAL, 'user01@kh.or.kr', 'pass01!', '유저일','01012345678', '04540,, 서울시 중구 남대문로 120,, 2층',
						DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO MEMBER VALUES (SEQ_MEMBER_NO.NEXTVAL, 'user02@kh.or.kr', 'pass02!', '유저이','01087654321', '04540,, 서울시 중구 남대문로 120,, 3층',
						DEFAULT, DEFAULT, DEFAULT, DEFAULT);

SELECT * FROM MEMBER; 
COMMIT;				
					
-- 로그인 SQL
SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_TEL, MEMBER_ADDRESS, 
	PROFILE_IMG, AUTHORITY, 
	TO_CHAR( ENROLL_DATE, 'YYYY"년" MM"월" DD"일" HH24"시" MI"분" SS"초"' ) AS ENROLL_DATE 
FROM MEMBER
WHERE MEMBER_DEL_FL = 'N' AND MEMBER_EMAIL = ? AND MEMBER_PW = ?;



-- 회원정보 수정
UPDATE MEMBER SET MEMBER_NICKNAME = ?, MEMBER_TEL = ?, MEMBER_ADDRESS =? WHERE SEQ_MEMBER_NO = ?



-- 암호화 된 비밀번호로 변경
UPDATE MEMBER SET MEMBER_PW = '$2a$10$2MXViDoUAv1zfvxVKAArR.qwe6SLazU7OIOFcqqnrc29S3IgnkKha';



-- bycrypt 암호화 적용 시 로그인 sql
SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_PW, MEMBER_NICKNAME, 
	MEMBER_TEL, MEMBER_ADDRESS, PROFILE_IMG, AUTHORITY, 
	TO_CHAR( ENROLL_DATE, 'YYYY"년" MM"월" DD"일" HH24"시" MI"분" SS"초"' ) AS ENROLL_DATE 
FROM MEMBER
WHERE MEMBER_DEL_FL = 'N' 
AND MEMBER_EMAIL = 'user04@kh.or.kr' ;


-- 탈퇴하지 않은 회원 중 이메일이 같은 회원 수 조회
SELECT * FROM MEMBER;
SELECT COUNT(*) FROM "MEMBER"
WHERE MEMBER_EMAIL = 'user01@kh.or.kr'
AND MEMBER_DEL_FL = 'N';

-- 탈퇴하지 않은 회원 중 닉네임이 같은 회원 수 조회
SELECT * FROM MEMBER;

SELECT COUNT (*) FROM "MEMBER"
WHERE MEMBER_NICKNAME = '유저일'
AND MEMBER_DEL_FL = 'N';


-- 입력된 이메일로 조회했을 때
SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_ADDRESS, 
TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"' ) ENROLL_DATE, MEMBER_DEL_FL 
FROM "MEMBER"
WHERE MEMBER_EMAIL = 'user08@kh.or.kr'
AND ROWNUM = 1;


-- 10초마다 회원목록조회
SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_DEL_FL
FROM MEMBER 
ORDER BY MEMBER_NO;

-- COMMENT 테이블에 PARENT_NO 컬럼 NULL 허용
ALTER TABLE "COMMENT"
MODIFY PARENT_NO NULL;


-- BOARD 테이블 샘플 데이터 삽입(PL/SQL)
BEGIN
	FOR I IN 1..2000 LOOP
		INSERT INTO BOARD 
		VALUES(SEQ_BOARD_NO.NEXTVAL,
			   SEQ_BOARD_NO.CURRVAL || '번째 게시글',
			   SEQ_BOARD_NO.CURRVAL || '번째 게시글입니다.<br>안녕하세요.',
			   DEFAULT, DEFAULT, DEFAULT, DEFAULT, 15,
			   CEIL(DBMS_RANDOM.VALUE(0,4)) );
	END LOOP;
END;
/

COMMIT;
SELECT * FROM BOARD;
-- BOARD 테이블 데이터삽입 완료
SELECT COUNT(*) FROM BOARD WHERE BOARD_CODE=1; --532
SELECT COUNT(*) FROM BOARD WHERE BOARD_CODE=2; --516
SELECT COUNT(*) FROM BOARD WHERE BOARD_CODE=3; --472
SELECT COUNT(*) FROM BOARD WHERE BOARD_CODE=4; --481



-- COMMENT 테이블 샘플 데이터 삽입(PL/SQL)
BEGIN
   FOR I IN 1..2000 LOOP
      INSERT INTO "COMMENT" 
      VALUES(SEQ_COMMENT_NO.NEXTVAL, 
             SEQ_COMMENT_NO.CURRVAL || '번째 댓글',
             DEFAULT, DEFAULT, 15,
             CEIL(DBMS_RANDOM.VALUE(0,2000)), NULL);
   END LOOP;
END;
/

SELECT * FROM "COMMENT";
COMMIT;
	


-- BOARD_IMG 테이블 샘플 데이터 삽입
SELECT * FROM BOARD_IMG;

SELECT * FROM BOARD WHERE BOARD_CODE = 1 ORDER BY 1 DESC;

--1998, 1990, 1988, 1984
INSERT INTO BOARD_IMG
VALUES(SEQ_IMG_NO.NEXTVAL, 'resources/images/board/',
'20221116105843_00001.gif', '1.gif', 0, 1998);

INSERT INTO BOARD_IMG
VALUES(SEQ_IMG_NO.NEXTVAL, 'resources/images/board/',
'20221116105843_00001.gif', '1.gif', 0, 1990);

INSERT INTO BOARD_IMG
VALUES(SEQ_IMG_NO.NEXTVAL, 'resources/images/board/',
'20221116105843_00001.gif', '1.gif', 0, 1988);

INSERT INTO BOARD_IMG
VALUES(SEQ_IMG_NO.NEXTVAL, 'resources/images/board/',
'20221116105843_00001.gif', '1.gif', 0, 1984);

COMMIT;



-- BOARD_LIKE 테이블 샘플 데이터 삽입
INSERT INTO BOARD_LIKE VALUES(1998, 15);
INSERT INTO BOARD_LIKE VALUES(1990, 15);

SELECT * FROM BOARD_LIKE;
COMMIT;


-- 게시글 목록 조회
-- 게시글 번호, 제목, 작성자닉넴, 조회수, 작성일 + 댓글수, 좋아요 수, 썸네일
SELECT BOARD_NO, BOARD_TITLE, MEMBER_NICKNAME, READ_COUNT, 
	CASE  
      WHEN SYSDATE - B_CREATE_DATE < 1/24/60
      THEN FLOOR( (SYSDATE - B_CREATE_DATE) * 24 * 60 * 60 ) || '초 전'
      WHEN SYSDATE - B_CREATE_DATE < 1/24
      THEN FLOOR( (SYSDATE - B_CREATE_DATE) * 24 * 60) || '분 전'
      WHEN SYSDATE - B_CREATE_DATE < 1
      THEN FLOOR( (SYSDATE - B_CREATE_DATE) * 24) || '시간 전'
      ELSE TO_CHAR(B_CREATE_DATE, 'YYYY-MM-DD')
   END B_CREATE_DATE, 
	(SELECT COUNT(*) FROM "COMMENT" C WHERE C.BOARD_NO = B.BOARD_NO) COMMENT_COUNT,
	(SELECT COUNT(*) FROM BOARD_LIKE L WHERE L.BOARD_NO = B.BOARD_NO) LIKE_COUNT,
	(SELECT IMG_PATH || IMG_RENAME FROM BOARD_IMG I WHERE IMG_ORDER = 0 AND I.BOARD_NO = B.BOARD_NO) THUMBNAIL
FROM BOARD B
JOIN MEMBER USING(MEMBER_NO)
WHERE B.BOARD_CODE = 1
AND BOARD_DEL_FL = 'N'
ORDER BY BOARD_NO DESC;

SELECT IMG_PATH || IMG_RENAME FROM BOARD_IMG I WHERE IMG_ORDER = 0 AND I.BOARD_NO = 1998
COMMIT;

SELECT * FROM BOARD_IMG bi ;
-- 댓글 수 
SELECT COUNT(*) FROM "COMMENT"
WHERE BOARD_NO = 1990; 

-- 좋아요 수
SELECT COUNT(*) FROM BOARD_LIKE
WHERE BOARD_NO = 1990;

-- 썸네일 이미지 조회 (IMG_PATH + IMG_RENAME)
SELECT IMG_PATH || IMG_RENAME FROM BOARD_IMG I 
WHERE IMG_ORDER = 0
AND BOARD_NO = 1998;



-- 게시글 수 조회
SELECT COUNT(*) FROM BOARD b WHERE BOARD_CODE = 2 AND BOARD_DEL_FL = 'N';







