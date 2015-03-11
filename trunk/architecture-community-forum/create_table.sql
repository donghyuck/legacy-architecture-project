
CREATE TABLE V2_BOARD
(
	BOARD_ID             NUMBER NOT NULL ,
    OBJECT_TYPE          NUMBER NOT NULL ,
	OBJECT_ID            NUMBER NOT NULL, 
	BOARD_NAME           VARCHAR2(200) NOT NULL ,
	BOARD_DESC           VARCHAR2(1000) NULL ,
	COMMENT_YN           NUMBER(1) NOT NULL ,
	FILE_YN              NUMBER(1) NOT NULL ,
	ANONY_YN             NUMBER(1) NOT NULL ,
	USE_YN               NUMBER(1) NOT NULL ,
	CREATION_DATE        DATE NOT NULL ,
	CREATE_ID            NUMBER NOT NULL ,
	MODIFIED_DATE        DATE NULL ,
	MODIFY_ID            NUMBER NULL ,
	LAST_THREAD_DATE     DATE NULL ,
	THREAD_SIZE          NUMBER(3) NULL ,
	TOTAL_CNT            NUMBER NULL 
);



CREATE UNIQUE INDEX V2_BOARD_IDX ON V2_BOARD
(BOARD_ID   ASC);



ALTER TABLE V2_BOARD
	ADD CONSTRAINT  V2_BOARD_IDX PRIMARY KEY (BOARD_ID);


CREATE TABLE V2_COMMENT
(
	THREAD_ID            NUMBER NOT NULL ,
	COMMENT_ID           CHAR(18) NOT NULL ,
	COMMENT_CONTENT      VARCHAR2(1000) NOT NULL ,
	PASSWD               VARCHAR2(20) NULL ,
	CREATE_ID            NUMBER NULL ,
	CREATE_NAME          VARCHAR2(100) NULL ,
	CREATION_DATE        DATE NOT NULL ,
	MODIFY_ID            NUMBER NULL ,
	MODIFIED_DATE          DATE NULL ,
	DEL_YN               NUMBER(1) NOT NULL 
);



CREATE UNIQUE INDEX V2_COMMENT_IDX ON V2_COMMENT
(THREAD_ID   ASC,COMMENT_ID   ASC);



ALTER TABLE V2_COMMENT
	ADD CONSTRAINT  V2_COMMENT_IDX PRIMARY KEY (THREAD_ID,COMMENT_ID);



CREATE TABLE V2_THREAD
(
	BOARD_ID             NUMBER NOT NULL ,
	THREAD_ID            NUMBER NOT NULL ,
	THREAD_TITLE         VARCHAR2(1000) NOT NULL ,
	THREAD_CONTENT       CLOB NULL ,
	PASSWD               VARCHAR2(20) NULL ,
	ATTACHMENT_ID        NUMBER NULL ,
	VIEW_CNT             NUMBER NOT NULL ,
	CREATE_ID            NUMBER NULL ,
	CREATE_NAME          VARCHAR2(100) NULL ,
	CREATION_DATE        DATE NOT NULL ,
	MODIFY_ID            NUMBER NULL ,
	MODIFIED_DATE        DATE NULL ,
	PARENT_THREAD_ID     NUMBER NULL ,
	REPLY_DEPTH          NUMBER(2) NULL ,
	DEL_YN               NUMBER(1) NOT NULL ,
	COMMENT_CNT          NUMBER NOT NULL 
);



CREATE UNIQUE INDEX V2_THREAD_IDX ON V2_THREAD
(THREAD_ID   ASC);



ALTER TABLE V2_THREAD
	ADD CONSTRAINT  V2_THREAD_IDX PRIMARY KEY (THREAD_ID);



ALTER TABLE V2_COMMENT
	ADD (CONSTRAINT R_2 FOREIGN KEY (THREAD_ID) REFERENCES V2_THREAD (THREAD_ID));



ALTER TABLE V2_THREAD
	ADD (CONSTRAINT R_1 FOREIGN KEY (BOARD_ID) REFERENCES V2_BOARD (BOARD_ID));




CREATE TABLE V2_THREAD_PROPERTY
(
    THREAD_ID     NUMBER NOT NULL,
    PROPERTY_NAME   VARCHAR2(100) NOT NULL,
    PROPERTY_VALUE  VARCHAR2(1024) NOT NULL
)
;

ALTER TABLE V2_THREAD_PROPERTY
ADD CONSTRAINT V2_THREAD_PROPERTY PRIMARY KEY (THREAD_ID,PROPERTY_NAME);

COMMENT ON TABLE V2_THREAD_PROPERTY IS '공지 게시글 테이블';
COMMENT ON COLUMN V2_THREAD_PROPERTY.THREAD_ID IS '게시글 ID';
COMMENT ON COLUMN V2_THREAD_PROPERTY.PROPERTY_NAME IS '프로퍼티 이름';
COMMENT ON COLUMN V2_THREAD_PROPERTY.PROPERTY_VALUE IS '프로퍼티 값';


CREATE TABLE V2_BOARD_PROPERTY
(
    BOARD_ID     NUMBER NOT NULL,
    PROPERTY_NAME   VARCHAR2(100) NOT NULL,
    PROPERTY_VALUE  VARCHAR2(1024) NOT NULL
)
;

ALTER TABLE V2_BOARD_PROPERTY
ADD CONSTRAINT V2_BOARD_PROPERTY PRIMARY KEY (BOARD_ID,PROPERTY_NAME);

COMMENT ON TABLE V2_BOARD_PROPERTY IS '공지 게시판 테이블';
COMMENT ON COLUMN V2_BOARD_PROPERTY.BOARD_ID IS '게시판 ID';
COMMENT ON COLUMN V2_BOARD_PROPERTY.PROPERTY_NAME IS '프로퍼티 이름';
COMMENT ON COLUMN V2_BOARD_PROPERTY.PROPERTY_VALUE IS '프로퍼티 값';