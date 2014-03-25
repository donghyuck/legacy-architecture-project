--------------------------------------------------------
--  DDL for Table V2_ANNOUNCE
--------------------------------------------------------

  CREATE TABLE "V2_ANNOUNCE" 
   (	"ANNOUNCE_ID" NUMBER(*,0), 
	"OBJECT_TYPE" NUMBER(*,0), 
	"OBJECT_ID" NUMBER(*,0), 
	"USER_ID" NUMBER(*,0), 
	"SUBJECT" VARCHAR2(255 BYTE), 
	"BODY" VARCHAR2(4000 BYTE), 
	"START_DATE" TIMESTAMP (6) DEFAULT SYSTIMESTAMP, 
	"END_DATE" DATE DEFAULT SYSDATE, 
	"STATUS" NUMBER(1,0) DEFAULT 0, 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_ANNOUNCE_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_ANNOUNCE_PROPERTY" 
   (	"ANNOUNCE_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_ANNOUNCE_PROPERTY"."ANNOUNCE_ID" IS '공지 ID';
 
   COMMENT ON COLUMN "V2_ANNOUNCE_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_ANNOUNCE_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_ANNOUNCE_PROPERTY"  IS '공지 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_ATTACHMENT
--------------------------------------------------------

  CREATE TABLE "V2_ATTACHMENT" 
   (	"ATTACHMENT_ID" NUMBER(*,0), 
	"OBJECT_TYPE" NUMBER(*,0), 
	"OBJECT_ID" NUMBER(*,0), 
	"CONTENT_TYPE" VARCHAR2(50 BYTE), 
	"FILE_NAME" VARCHAR2(255 BYTE), 
	"FILE_SIZE" NUMBER(*,0), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_ATTACHMENT"."ATTACHMENT_ID" IS 'ID';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."OBJECT_TYPE" IS '첨부파일과 연관된 모델 유형';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."OBJECT_ID" IS '첨부파일과 연관된 모델 ID';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."CONTENT_TYPE" IS 'CONTENT TYPE 값';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."FILE_NAME" IS '첨부파일 이름';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."FILE_SIZE" IS '첨부파일 크기';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."CREATION_DATE" IS '생성일';
 
   COMMENT ON COLUMN "V2_ATTACHMENT"."MODIFIED_DATE" IS '수정일';
 
   COMMENT ON TABLE "V2_ATTACHMENT"  IS '첨부파일 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_ATTACHMENT_DATA
--------------------------------------------------------

  CREATE TABLE "V2_ATTACHMENT_DATA" 
   (	"ATTACHMENT_ID" NUMBER(*,0), 
	"ATTACHMENT_DATA" BLOB
   ) ;
 

   COMMENT ON COLUMN "V2_ATTACHMENT_DATA"."ATTACHMENT_ID" IS 'ID';
 
   COMMENT ON COLUMN "V2_ATTACHMENT_DATA"."ATTACHMENT_DATA" IS '첨부파일 데이터';
 
   COMMENT ON TABLE "V2_ATTACHMENT_DATA"  IS '첨부파일 데이터 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_ATTACHMENT_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_ATTACHMENT_PROPERTY" 
   (	"ATTACHMENT_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_ATTACHMENT_PROPERTY"."ATTACHMENT_ID" IS '첨부파일 ID';
 
   COMMENT ON COLUMN "V2_ATTACHMENT_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_ATTACHMENT_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_ATTACHMENT_PROPERTY"  IS '첨부파일 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_COMPANY
--------------------------------------------------------

  CREATE TABLE "V2_COMPANY" 
   (	"COMPANY_ID" NUMBER(*,0), 
	"DISPLAY_NAME" VARCHAR2(255 BYTE), 
	"NAME" VARCHAR2(100 BYTE), 
	"DESCRIPTION" VARCHAR2(1000 BYTE), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE, 
	"DOMAIN_NAME" VARCHAR2(100 CHAR)
   ) ;
 

   COMMENT ON COLUMN "V2_COMPANY"."COMPANY_ID" IS '회사 ID';
 
   COMMENT ON COLUMN "V2_COMPANY"."DISPLAY_NAME" IS '출력시 보여줄 회사 이름';
 
   COMMENT ON COLUMN "V2_COMPANY"."NAME" IS '회사 이름';
 
   COMMENT ON COLUMN "V2_COMPANY"."DESCRIPTION" IS '설명';
 
   COMMENT ON COLUMN "V2_COMPANY"."CREATION_DATE" IS '생성일자';
 
   COMMENT ON COLUMN "V2_COMPANY"."MODIFIED_DATE" IS '수정일자';
 
   COMMENT ON COLUMN "V2_COMPANY"."DOMAIN_NAME" IS '도메인 이름';
 
   COMMENT ON TABLE "V2_COMPANY"  IS '회사 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_COMPANY_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_COMPANY_PROPERTY" 
   (	"COMPANY_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_COMPANY_PROPERTY"."COMPANY_ID" IS '회사 ID';
 
   COMMENT ON COLUMN "V2_COMPANY_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_COMPANY_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_COMPANY_PROPERTY"  IS '회사 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_CONTENT
--------------------------------------------------------

  CREATE TABLE "V2_CONTENT" 
   (	"CONTENT_ID" NUMBER(*,0), 
	"CONTENT_TYPE" VARCHAR2(255 BYTE), 
	"SUMMARY" VARCHAR2(4000 BYTE), 
	"SUBJECT" VARCHAR2(255 BYTE), 
	"BODY" CLOB, 
	"CREATOR" NUMBER(*,0), 
	"LASTMODIFIER" NUMBER(*,0), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_CONTENT_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_CONTENT_PROPERTY" 
   (	"CONTENT_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_GROUP
--------------------------------------------------------

  CREATE TABLE "V2_GROUP" 
   (	"GROUP_ID" NUMBER(*,0), 
	"COMPANY_ID" NUMBER(*,0), 
	"NAME" VARCHAR2(100 BYTE), 
	"DISPLAY_NAME" VARCHAR2(255 BYTE), 
	"DESCRIPTION" VARCHAR2(1000 BYTE), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_GROUP"."GROUP_ID" IS '그룹 ID';
 
   COMMENT ON COLUMN "V2_GROUP"."COMPANY_ID" IS '회사 ID';
 
   COMMENT ON COLUMN "V2_GROUP"."NAME" IS '그룹 이름';
 
   COMMENT ON COLUMN "V2_GROUP"."DESCRIPTION" IS '설명';
 
   COMMENT ON COLUMN "V2_GROUP"."CREATION_DATE" IS '생성일자';
 
   COMMENT ON COLUMN "V2_GROUP"."MODIFIED_DATE" IS '수정일자';
 
   COMMENT ON TABLE "V2_GROUP"  IS '그룹 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_GROUP_MEMBERS
--------------------------------------------------------

  CREATE TABLE "V2_GROUP_MEMBERS" 
   (	"GROUP_ID" NUMBER(*,0), 
	"USER_ID" NUMBER(*,0), 
	"ADMINISTRATOR" NUMBER(1,0) DEFAULT 0, 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_GROUP_MEMBERS"."GROUP_ID" IS '그룹 ID';
 
   COMMENT ON COLUMN "V2_GROUP_MEMBERS"."USER_ID" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_GROUP_MEMBERS"."ADMINISTRATOR" IS '관리자 구분';
 
   COMMENT ON COLUMN "V2_GROUP_MEMBERS"."CREATION_DATE" IS '생성일자';
 
   COMMENT ON COLUMN "V2_GROUP_MEMBERS"."MODIFIED_DATE" IS '수정일자';
 
   COMMENT ON TABLE "V2_GROUP_MEMBERS"  IS '그룹 멤버 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_GROUP_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_GROUP_PROPERTY" 
   (	"GROUP_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_GROUP_PROPERTY"."GROUP_ID" IS '그룹 ID';
 
   COMMENT ON COLUMN "V2_GROUP_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_GROUP_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_GROUP_PROPERTY"  IS '그룹 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_GROUP_ROLES
--------------------------------------------------------

  CREATE TABLE "V2_GROUP_ROLES" 
   (	"GROUP_ID" NUMBER(*,0), 
	"ROLE_ID" NUMBER(*,0)
   ) ;
 

   COMMENT ON COLUMN "V2_GROUP_ROLES"."GROUP_ID" IS '그룹 ID';
 
   COMMENT ON COLUMN "V2_GROUP_ROLES"."ROLE_ID" IS '롤 ID';
 
   COMMENT ON TABLE "V2_GROUP_ROLES"  IS '그룹 롤 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_I18N_TEXT
--------------------------------------------------------

  CREATE TABLE "V2_I18N_TEXT" 
   (	"TEXT_ID" NUMBER(*,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"TEXT" VARCHAR2(2000 BYTE), 
	"CATEGORY_NAME" VARCHAR2(100 BYTE), 
	"LOCALE_CODE" VARCHAR2(100 BYTE), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_I18N_TEXT"."TEXT_ID" IS '국제화 텍스트 ID 값';
 
   COMMENT ON COLUMN "V2_I18N_TEXT"."NAME" IS '국제화 텍스트 키 값';
 
   COMMENT ON COLUMN "V2_I18N_TEXT"."TEXT" IS '국제화 텍스트 값';
 
   COMMENT ON COLUMN "V2_I18N_TEXT"."CATEGORY_NAME" IS '카테고리 이름';
 
   COMMENT ON COLUMN "V2_I18N_TEXT"."LOCALE_CODE" IS '로케일 코드 값';
 
   COMMENT ON COLUMN "V2_I18N_TEXT"."CREATION_DATE" IS '생성일';
 
   COMMENT ON COLUMN "V2_I18N_TEXT"."MODIFIED_DATE" IS '수정일';
 
   COMMENT ON TABLE "V2_I18N_TEXT"  IS 'I18N 지원을 위한 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_IMAGE
--------------------------------------------------------

  CREATE TABLE "V2_IMAGE" 
   (	
   	"IMAGE_ID" NUMBER(*,0), 
	"OBJECT_TYPE" NUMBER(*,0), 
	"OBJECT_ID" NUMBER(*,0), 
	"FILE_NAME" VARCHAR2(255 BYTE), 
	"FILE_SIZE" NUMBER(*,0), 
	"CONTENT_TYPE" VARCHAR2(50 BYTE), 
	"CREATION_DATE" TIMESTAMP  DEFAULT SYSTIMESTAMP , 
	"MODIFIED_DATE" TIMESTAMP  DEFAULT SYSTIMESTAMP 
   ) ;
 

   COMMENT ON COLUMN "V2_IMAGE"."IMAGE_ID" IS 'ID';
 
   COMMENT ON COLUMN "V2_IMAGE"."OBJECT_TYPE" IS '이미지와 연관된 모델 유형';
 
   COMMENT ON COLUMN "V2_IMAGE"."OBJECT_ID" IS '이미지와 연관된 모델 ID';
 
   COMMENT ON COLUMN "V2_IMAGE"."FILE_NAME" IS '이미지 파일 이름';
 
   COMMENT ON COLUMN "V2_IMAGE"."FILE_SIZE" IS '이미지 파일 크기';
 
   COMMENT ON COLUMN "V2_IMAGE"."CONTENT_TYPE" IS 'CONTENT TYPE 값';
 
   COMMENT ON COLUMN "V2_IMAGE"."CREATION_DATE" IS '생성일';
 
   COMMENT ON COLUMN "V2_IMAGE"."MODIFIED_DATE" IS '수정일';
 
   COMMENT ON TABLE "V2_IMAGE"  IS '이미지 테이블';
/

  CREATE TABLE "V2_IMAGE_PROPERTY" 
   (	"IMAGE_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_IMAGE_PROPERTY"."IMAGE_ID" IS '이미지 ID';
 
   COMMENT ON COLUMN "V2_IMAGE_PROPERTY"."PROPERTY_NAME" IS '이미지 프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_IMAGE_PROPERTY"."PROPERTY_VALUE" IS '이미지 프로퍼티 값';
 
   COMMENT ON TABLE "V2_IMAGE_PROPERTY"  IS '이미지 프로퍼티 테이블';
/

--------------------------------------------------------
--  DDL for Table V2_IMAGE_DATA
--------------------------------------------------------

  CREATE TABLE "V2_IMAGE_DATA" 
   (	"IMAGE_ID" NUMBER(*,0), 
	"IMAGE_DATA" BLOB
   ) ;
 

   COMMENT ON COLUMN "V2_IMAGE_DATA"."IMAGE_ID" IS 'ID';
 
   COMMENT ON COLUMN "V2_IMAGE_DATA"."IMAGE_DATA" IS '이미지 데이터';
 
   COMMENT ON TABLE "V2_IMAGE_DATA"  IS '이미지 데이터 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_LOCALIZED_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_LOCALIZED_PROPERTY" 
   (	"LOCALE_CODE" VARCHAR2(100 BYTE), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_LOCALIZED_PROPERTY"."LOCALE_CODE" IS '로케일 코드';
 
   COMMENT ON COLUMN "V2_LOCALIZED_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_LOCALIZED_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_LOCALIZED_PROPERTY"  IS '로케일에 따른 응용프로그램 프로퍼티 지원을 위한 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_MENU
--------------------------------------------------------

  CREATE TABLE "V2_MENU" 
   (	"MENU_ID" NUMBER(*,0), 
	"NAME" VARCHAR2(255 BYTE), 
	"TITLE" VARCHAR2(255 BYTE), 
	"LOCATION" VARCHAR2(255 BYTE), 
	"MENU_ENABLED" NUMBER(1,0) DEFAULT 1, 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_MENU"."MENU_ID" IS '메뉴 ID';
 
   COMMENT ON COLUMN "V2_MENU"."NAME" IS '이름';
 
   COMMENT ON COLUMN "V2_MENU"."TITLE" IS '타이틀 명';
 
   COMMENT ON COLUMN "V2_MENU"."LOCATION" IS 'LOCATION 값';
 
   COMMENT ON COLUMN "V2_MENU"."MENU_ENABLED" IS '사용여부';
 
   COMMENT ON COLUMN "V2_MENU"."CREATION_DATE" IS '생성을';
 
   COMMENT ON COLUMN "V2_MENU"."MODIFIED_DATE" IS '수정일';
 
   COMMENT ON TABLE "V2_MENU"  IS '메뉴 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_MENU_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_MENU_PROPERTY" 
   (	"MENU_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_MENU_PROPERTY"."MENU_ID" IS '메뉴 ID';
 
   COMMENT ON COLUMN "V2_MENU_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_MENU_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_MENU_PROPERTY"  IS '메뉴 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_MENU_XML
--------------------------------------------------------

  CREATE TABLE "V2_MENU_XML" 
   (	"MENU_ID" NUMBER(*,0), 
	"MENU_DATA" CLOB
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_PROPERTY" 
   (	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_PROPERTY"  IS '응용프로그램 프로퍼티 지원을 위한 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_ROLE
--------------------------------------------------------

  CREATE TABLE "V2_ROLE" 
   (	"ROLE_ID" NUMBER(*,0), 
	"NAME" VARCHAR2(100 BYTE), 
	"DESCRIPTION" VARCHAR2(1000 BYTE), 
	"MASK" NUMBER(*,0), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_ROLE"."ROLE_ID" IS '롤 ID';
 
   COMMENT ON COLUMN "V2_ROLE"."NAME" IS '롤 이름';
 
   COMMENT ON COLUMN "V2_ROLE"."DESCRIPTION" IS '설명';
 
   COMMENT ON COLUMN "V2_ROLE"."MASK" IS '마스크 값';
 
   COMMENT ON COLUMN "V2_ROLE"."CREATION_DATE" IS '생성일자';
 
   COMMENT ON COLUMN "V2_ROLE"."MODIFIED_DATE" IS '수정일자';
 
   COMMENT ON TABLE "V2_ROLE"  IS '롤 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_SEQUENCER
--------------------------------------------------------

  CREATE TABLE "V2_SEQUENCER" 
   (	"SEQUENCER_ID" NUMBER(*,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"VALUE" NUMBER(*,0)
   ) ;
 

   COMMENT ON COLUMN "V2_SEQUENCER"."SEQUENCER_ID" IS '시퀀스 아이디';
 
   COMMENT ON COLUMN "V2_SEQUENCER"."NAME" IS '시퀀스 이름';
 
   COMMENT ON COLUMN "V2_SEQUENCER"."VALUE" IS '시퀀스 값';
 
   COMMENT ON TABLE "V2_SEQUENCER"  IS '시퀀스 지원을 위한 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_SOCIAL_ACCOUNT
--------------------------------------------------------

  CREATE TABLE "V2_SOCIAL_ACCOUNT" 
   (	"ACCOUNT_ID" NUMBER(*,0), 
	"OBJECT_TYPE" NUMBER(*,0), 
	"OBJECT_ID" NUMBER(*,0), 
	"SOCIAL_PROVIDER" VARCHAR2(255 BYTE), 
	"ACCESS_TOKEN" VARCHAR2(255 BYTE), 
	"ACCESS_SECRET" VARCHAR2(255 BYTE), 
	"SIGNED" NUMBER(1,0) DEFAULT 0, 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE, 
	"USERNAME" VARCHAR2(255 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."ACCOUNT_ID" IS '쇼셜계정정보 아이디';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."OBJECT_TYPE" IS '관련 데이터 유형';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."OBJECT_ID" IS '관련 데이터 ID';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."SOCIAL_PROVIDER" IS '서비스 제공자 명';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."ACCESS_TOKEN" IS '인증된 TOKEN 값';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."ACCESS_SECRET" IS '인증된 SECRET 값';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."CREATION_DATE" IS '생성일';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."MODIFIED_DATE" IS '수정일';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."USERNAME" IS '쇼셜 미디어에 등록된 ID 값';
 
   COMMENT ON TABLE "V2_SOCIAL_ACCOUNT"  IS '쇼셜 계정 정보 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_SOCIAL_ACCOUNT_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_SOCIAL_ACCOUNT_PROPERTY" 
   (	"ACCOUNT_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT_PROPERTY"."ACCOUNT_ID" IS '쇼셜계정정보  ID';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_SOCIAL_ACCOUNT_PROPERTY"  IS '쇼셜 계정 정보 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_TEMPLATE
--------------------------------------------------------

  CREATE TABLE "V2_TEMPLATE" 
   (	"OBJECT_TYPE" NUMBER(*,0), 
	"OBJECT_ID" NUMBER(*,0), 
	"TEMPLATE_ID" NUMBER(*,0), 
	"TEMPLATE_TYPE" VARCHAR2(255 BYTE), 
	"TITLE" VARCHAR2(255 BYTE), 
	"LOCATION" VARCHAR2(255 BYTE), 
	"CREATOR" NUMBER(*,0), 
	"LASTMODIFIER" NUMBER(*,0), 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_TEMPLATE_BODY
--------------------------------------------------------

  CREATE TABLE "V2_TEMPLATE_BODY" 
   (	"BODY_TEMPLATE_ID" NUMBER(*,0), 
	"BODY" CLOB
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_TEMPLATE_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_TEMPLATE_PROPERTY" 
   (	"TEMPLATE_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
/
--------------------------------------------------------
--  DDL for Table V2_USER
--------------------------------------------------------

  CREATE TABLE "V2_USER" 
   (	"COMPANY_ID" NUMBER(*,0), 
	"USER_ID" NUMBER(*,0), 
	"USERNAME" VARCHAR2(100 BYTE), 
	"PASSWORD_HASH" VARCHAR2(64 BYTE), 
	"NAME" VARCHAR2(100 BYTE), 
	"NAME_VISIBLE" NUMBER(1,0) DEFAULT 1, 
	"FIRST_NAME" VARCHAR2(100 BYTE), 
	"LAST_NAME" VARCHAR2(100 BYTE), 
	"EMAIL" VARCHAR2(100 BYTE), 
	"EMAIL_VISIBLE" NUMBER(1,0) DEFAULT 1, 
	"LAST_LOGINED_IN" DATE DEFAULT SYSDATE, 
	"LAST_PROFILE_UPDATE" DATE DEFAULT SYSDATE, 
	"USER_ENABLED" NUMBER(1,0) DEFAULT 1, 
	"VISIBLE" NUMBER(1,0) DEFAULT 1, 
	"IS_EXTERNAL" NUMBER(1,0) DEFAULT 0, 
	"FEDERATED" NUMBER(1,0) DEFAULT 0, 
	"STATUS" NUMBER(1,0) DEFAULT 0, 
	"CREATION_DATE" DATE DEFAULT SYSDATE, 
	"MODIFIED_DATE" DATE DEFAULT SYSDATE
   ) ;
 

   COMMENT ON COLUMN "V2_USER"."USER_ID" IS 'ID';
 
   COMMENT ON COLUMN "V2_USER"."USERNAME" IS '로그인 아이디';
 
   COMMENT ON COLUMN "V2_USER"."PASSWORD_HASH" IS '암호화된 패스워드';
 
   COMMENT ON COLUMN "V2_USER"."NAME" IS '전체 이름';
 
   COMMENT ON COLUMN "V2_USER"."NAME_VISIBLE" IS '이름 공개 여부';
 
   COMMENT ON COLUMN "V2_USER"."FIRST_NAME" IS '이름';
 
   COMMENT ON COLUMN "V2_USER"."LAST_NAME" IS '성';
 
   COMMENT ON COLUMN "V2_USER"."EMAIL" IS '메일주소';
 
   COMMENT ON COLUMN "V2_USER"."EMAIL_VISIBLE" IS '메일주소 공개여부';
 
   COMMENT ON COLUMN "V2_USER"."LAST_LOGINED_IN" IS '마지막 로그인 일자';
 
   COMMENT ON COLUMN "V2_USER"."LAST_PROFILE_UPDATE" IS '마지막 프로파일 갱신 일자';
 
   COMMENT ON COLUMN "V2_USER"."USER_ENABLED" IS '계정 사용여부';
 
   COMMENT ON COLUMN "V2_USER"."VISIBLE" IS '계정 공개 여부';
 
   COMMENT ON COLUMN "V2_USER"."IS_EXTERNAL" IS '외부 시스템에서 생성된 계정 여부';
 
   COMMENT ON COLUMN "V2_USER"."FEDERATED" IS '외부 계정과 연합 여부';
 
   COMMENT ON COLUMN "V2_USER"."STATUS" IS '계정 상태';
 
   COMMENT ON COLUMN "V2_USER"."CREATION_DATE" IS '생성일자';
 
   COMMENT ON COLUMN "V2_USER"."MODIFIED_DATE" IS '수정일자';
 
   COMMENT ON TABLE "V2_USER"  IS '애플리케이션 사용자 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_USER_PROPERTY
--------------------------------------------------------

  CREATE TABLE "V2_USER_PROPERTY" 
   (	"USER_ID" NUMBER(*,0), 
	"PROPERTY_NAME" VARCHAR2(100 BYTE), 
	"PROPERTY_VALUE" VARCHAR2(1024 BYTE)
   ) ;
 

   COMMENT ON COLUMN "V2_USER_PROPERTY"."USER_ID" IS '사용자 ID';
 
   COMMENT ON COLUMN "V2_USER_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';
 
   COMMENT ON COLUMN "V2_USER_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';
 
   COMMENT ON TABLE "V2_USER_PROPERTY"  IS '사용자 프로퍼티 테이블';
/
--------------------------------------------------------
--  DDL for Table V2_USER_ROLES
--------------------------------------------------------

  CREATE TABLE "V2_USER_ROLES" 
   (	"USER_ID" NUMBER(*,0), 
	"ROLE_ID" NUMBER(*,0)
   ) ;
 

   COMMENT ON COLUMN "V2_USER_ROLES"."USER_ID" IS '그룹 ID';
 
   COMMENT ON COLUMN "V2_USER_ROLES"."ROLE_ID" IS '롤 ID';
 
   COMMENT ON TABLE "V2_USER_ROLES"  IS '사용자 롤 테이블';
/
--------------------------------------------------------
--  DDL for Index V2_ANNOUNCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_ANNOUNCE_PK" ON "V2_ANNOUNCE" ("ANNOUNCE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_ANNOUNCE_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_ANNOUNCE_PROPERTY_PK" ON "V2_ANNOUNCE_PROPERTY" ("ANNOUNCE_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_ATTACHMENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_ATTACHMENT_PK" ON "V2_ATTACHMENT" ("ATTACHMENT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_ATTACHMENT_OBJ_IDX
--------------------------------------------------------

  CREATE INDEX "V2_ATTACHMENT_OBJ_IDX" ON "V2_ATTACHMENT" ("OBJECT_TYPE", "OBJECT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_ATTACHMENT_DATA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_ATTACHMENT_DATA_PK" ON "V2_ATTACHMENT_DATA" ("ATTACHMENT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_ATTACHMENT_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_ATTACHMENT_PROPERTY_PK" ON "V2_ATTACHMENT_PROPERTY" ("ATTACHMENT_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_COMPANY_DNAME_IDX
--------------------------------------------------------

  CREATE INDEX "V2_COMPANY_DNAME_IDX" ON "V2_COMPANY" ("DOMAIN_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_COMPANY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_COMPANY_PK" ON "V2_COMPANY" ("COMPANY_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_COMPANY_DATE_IDX
--------------------------------------------------------

  CREATE INDEX "V2_COMPANY_DATE_IDX" ON "V2_COMPANY" ("CREATION_DATE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_COMPANY_NAME_IDX
--------------------------------------------------------

  CREATE INDEX "V2_COMPANY_NAME_IDX" ON "V2_COMPANY" ("NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_COMPANY_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_COMPANY_PROPERTY_PK" ON "V2_COMPANY_PROPERTY" ("COMPANY_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_CONTENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_CONTENT_PK" ON "V2_CONTENT" ("CONTENT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_CONTENT_IDX
--------------------------------------------------------

  CREATE INDEX "V2_CONTENT_IDX" ON "V2_CONTENT" ("CREATOR") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_CONTENT_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_CONTENT_IDX2" ON "V2_CONTENT" ("LASTMODIFIER") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_CONTENT_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_CONTENT_PROPERTY_PK" ON "V2_CONTENT_PROPERTY" ("CONTENT_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_GROUP_PK" ON "V2_GROUP" ("GROUP_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_COMPANY_IDX
--------------------------------------------------------

  CREATE INDEX "V2_GROUP_COMPANY_IDX" ON "V2_GROUP" ("COMPANY_ID", "GROUP_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_NAME_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_GROUP_NAME_IDX2" ON "V2_GROUP" ("COMPANY_ID", "NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_DATE_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_GROUP_DATE_IDX2" ON "V2_GROUP" ("COMPANY_ID", "CREATION_DATE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_MEMBERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_GROUP_MEMBERS_PK" ON "V2_GROUP_MEMBERS" ("GROUP_ID", "USER_ID", "ADMINISTRATOR") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_GROUP_PROPERTY_PK" ON "V2_GROUP_PROPERTY" ("GROUP_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_GROUP_ROLES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_GROUP_ROLES_PK" ON "V2_GROUP_ROLES" ("GROUP_ID", "ROLE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_I18N_TEXT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_I18N_TEXT_PK" ON "V2_I18N_TEXT" ("TEXT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_I18N_TEXT_LOCALE_IDX
--------------------------------------------------------

  CREATE INDEX "V2_I18N_TEXT_LOCALE_IDX" ON "V2_I18N_TEXT" ("LOCALE_CODE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_I18N_TEXT_NAME_IDX
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_I18N_TEXT_NAME_IDX" ON "V2_I18N_TEXT" ("NAME", "CATEGORY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_IMAGE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_IMAGE_PK" ON "V2_IMAGE" ("IMAGE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_IMAGE_OBJ_IDX
--------------------------------------------------------

  CREATE INDEX "V2_IMAGE_OBJ_IDX" ON "V2_IMAGE" ("OBJECT_TYPE", "OBJECT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_IMAGE_DATA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_IMAGE_DATA_PK" ON "V2_IMAGE_DATA" ("IMAGE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_LOCALIZED_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_LOCALIZED_PROPERTY_PK" ON "V2_LOCALIZED_PROPERTY" ("LOCALE_CODE", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_MENU_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_MENU_PK" ON "V2_MENU" ("MENU_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_MENU_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_MENU_PROPERTY_PK" ON "V2_MENU_PROPERTY" ("MENU_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_MENU_XML_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_MENU_XML_PK" ON "V2_MENU_XML" ("MENU_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_PROPERTY_PK" ON "V2_PROPERTY" ("PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_ROLE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_ROLE_PK" ON "V2_ROLE" ("ROLE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SEQUENCER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_SEQUENCER_PK" ON "V2_SEQUENCER" ("SEQUENCER_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SEQUENCER_NAME_IDX
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_SEQUENCER_NAME_IDX" ON "V2_SEQUENCER" ("NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SOCIAL_ACCOUNT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_SOCIAL_ACCOUNT_PK" ON "V2_SOCIAL_ACCOUNT" ("ACCOUNT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SOCIAL_ACCOUNT_IDX
--------------------------------------------------------

  CREATE INDEX "V2_SOCIAL_ACCOUNT_IDX" ON "V2_SOCIAL_ACCOUNT" ("OBJECT_TYPE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SOCIAL_ACCOUNT_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_SOCIAL_ACCOUNT_IDX2" ON "V2_SOCIAL_ACCOUNT" ("OBJECT_TYPE", "OBJECT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SOCIAL_ACCOUNT_IDX3
--------------------------------------------------------

  CREATE INDEX "V2_SOCIAL_ACCOUNT_IDX3" ON "V2_SOCIAL_ACCOUNT" ("OBJECT_TYPE", "USERNAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_SOCIAL_ACCOUNT_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_SOCIAL_ACCOUNT_PROPERTY_PK" ON "V2_SOCIAL_ACCOUNT_PROPERTY" ("ACCOUNT_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_TEMPLATE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_TEMPLATE_PK" ON "V2_TEMPLATE" ("TEMPLATE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_TEMPLATE_TITLE_IDX
--------------------------------------------------------

  CREATE INDEX "V2_TEMPLATE_TITLE_IDX" ON "V2_TEMPLATE" ("TITLE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_TEMPLATE_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_TEMPLATE_IDX2" ON "V2_TEMPLATE" ("OBJECT_TYPE", "OBJECT_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_TEMPLATE_BODY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_TEMPLATE_BODY_PK" ON "V2_TEMPLATE_BODY" ("BODY_TEMPLATE_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_TEMPLATE_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_TEMPLATE_PROPERTY_PK" ON "V2_TEMPLATE_PROPERTY" ("TEMPLATE_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_USERNAME_IDX
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_USER_USERNAME_IDX" ON "V2_USER" ("USERNAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_DATE_IDX
--------------------------------------------------------

  CREATE INDEX "V2_USER_DATE_IDX" ON "V2_USER" ("CREATION_DATE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_EXTERNAL_IDX
--------------------------------------------------------

  CREATE INDEX "V2_USER_EXTERNAL_IDX" ON "V2_USER" ("VISIBLE", "USER_ENABLED", "IS_EXTERNAL") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_COMPANY_IDX
--------------------------------------------------------

  CREATE INDEX "V2_USER_COMPANY_IDX" ON "V2_USER" ("COMPANY_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_DATE_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_USER_DATE_IDX2" ON "V2_USER" ("COMPANY_ID", "CREATION_DATE") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_EXTERNAL_IDX2
--------------------------------------------------------

  CREATE INDEX "V2_USER_EXTERNAL_IDX2" ON "V2_USER" ("COMPANY_ID", "VISIBLE", "USER_ENABLED", "IS_EXTERNAL") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_USER_PK" ON "V2_USER" ("USER_ID") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_USER_PROPERTY_PK" ON "V2_USER_PROPERTY" ("USER_ID", "PROPERTY_NAME") 
  ;
/
--------------------------------------------------------
--  DDL for Index V2_USER_ROLES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "V2_USER_ROLES_PK" ON "V2_USER_ROLES" ("USER_ID", "ROLE_ID") 
  ;
/
--------------------------------------------------------
--  Constraints for Table V2_ANNOUNCE
--------------------------------------------------------

  ALTER TABLE "V2_ANNOUNCE" MODIFY ("ANNOUNCE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("OBJECT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("OBJECT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("USER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("SUBJECT" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("BODY" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("START_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("END_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE" ADD CONSTRAINT "V2_ANNOUNCE_PK" PRIMARY KEY ("ANNOUNCE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_ANNOUNCE_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_ANNOUNCE_PROPERTY" MODIFY ("ANNOUNCE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ANNOUNCE_PROPERTY" ADD CONSTRAINT "V2_ANNOUNCE_PROPERTY_PK" PRIMARY KEY ("ANNOUNCE_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_ATTACHMENT
--------------------------------------------------------

  ALTER TABLE "V2_ATTACHMENT" MODIFY ("ATTACHMENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("OBJECT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("OBJECT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("CONTENT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("FILE_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("FILE_SIZE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT" ADD CONSTRAINT "V2_ATTACHMENT_PK" PRIMARY KEY ("ATTACHMENT_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_ATTACHMENT_DATA
--------------------------------------------------------

  ALTER TABLE "V2_ATTACHMENT_DATA" MODIFY ("ATTACHMENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT_DATA" ADD CONSTRAINT "V2_ATTACHMENT_DATA_PK" PRIMARY KEY ("ATTACHMENT_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_ATTACHMENT_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_ATTACHMENT_PROPERTY" MODIFY ("ATTACHMENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ATTACHMENT_PROPERTY" ADD CONSTRAINT "V2_ATTACHMENT_PROPERTY_PK" PRIMARY KEY ("ATTACHMENT_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_COMPANY
--------------------------------------------------------

  ALTER TABLE "V2_COMPANY" MODIFY ("COMPANY_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY" MODIFY ("DESCRIPTION" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY" ADD CONSTRAINT "V2_COMPANY_PK" PRIMARY KEY ("COMPANY_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_COMPANY_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_COMPANY_PROPERTY" MODIFY ("COMPANY_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_COMPANY_PROPERTY" ADD CONSTRAINT "V2_COMPANY_PROPERTY_PK" PRIMARY KEY ("COMPANY_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_CONTENT
--------------------------------------------------------

  ALTER TABLE "V2_CONTENT" MODIFY ("CONTENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT" MODIFY ("CONTENT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT" MODIFY ("SUBJECT" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT" ADD CONSTRAINT "V2_CONTENT_PK" PRIMARY KEY ("CONTENT_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_CONTENT_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_CONTENT_PROPERTY" MODIFY ("CONTENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_CONTENT_PROPERTY" ADD CONSTRAINT "V2_CONTENT_PROPERTY_PK" PRIMARY KEY ("CONTENT_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_GROUP
--------------------------------------------------------

  ALTER TABLE "V2_GROUP" MODIFY ("GROUP_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" MODIFY ("COMPANY_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" MODIFY ("DESCRIPTION" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP" ADD CONSTRAINT "V2_GROUP_PK" PRIMARY KEY ("GROUP_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_GROUP_MEMBERS
--------------------------------------------------------

  ALTER TABLE "V2_GROUP_MEMBERS" MODIFY ("GROUP_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_MEMBERS" MODIFY ("USER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_MEMBERS" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_MEMBERS" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_MEMBERS" ADD CONSTRAINT "V2_GROUP_MEMBERS_PK" PRIMARY KEY ("GROUP_ID", "USER_ID", "ADMINISTRATOR") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_GROUP_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_GROUP_PROPERTY" MODIFY ("GROUP_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_PROPERTY" ADD CONSTRAINT "V2_GROUP_PROPERTY_PK" PRIMARY KEY ("GROUP_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_GROUP_ROLES
--------------------------------------------------------

  ALTER TABLE "V2_GROUP_ROLES" MODIFY ("GROUP_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_ROLES" MODIFY ("ROLE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_GROUP_ROLES" ADD CONSTRAINT "V2_GROUP_ROLES_PK" PRIMARY KEY ("GROUP_ID", "ROLE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_I18N_TEXT
--------------------------------------------------------

  ALTER TABLE "V2_I18N_TEXT" MODIFY ("TEXT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" MODIFY ("TEXT" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" MODIFY ("CATEGORY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" MODIFY ("LOCALE_CODE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_I18N_TEXT" ADD CONSTRAINT "V2_I18N_TEXT_PK" PRIMARY KEY ("TEXT_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_IMAGE
--------------------------------------------------------

  ALTER TABLE "V2_IMAGE" MODIFY ("IMAGE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("OBJECT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("OBJECT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("FILE_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("FILE_SIZE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("CONTENT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE" ADD CONSTRAINT "V2_IMAGE_PK" PRIMARY KEY ("IMAGE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_IMAGE_DATA
--------------------------------------------------------

  ALTER TABLE "V2_IMAGE_DATA" MODIFY ("IMAGE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_IMAGE_DATA" ADD CONSTRAINT "V2_IMAGE_DATA_PK" PRIMARY KEY ("IMAGE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_LOCALIZED_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_LOCALIZED_PROPERTY" MODIFY ("LOCALE_CODE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_LOCALIZED_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_LOCALIZED_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_LOCALIZED_PROPERTY" ADD CONSTRAINT "V2_LOCALIZED_PROPERTY_PK" PRIMARY KEY ("LOCALE_CODE", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_MENU
--------------------------------------------------------

  ALTER TABLE "V2_MENU" MODIFY ("MENU_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU" MODIFY ("TITLE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU" ADD CONSTRAINT "V2_MENU_PK" PRIMARY KEY ("MENU_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_MENU_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_MENU_PROPERTY" MODIFY ("MENU_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU_PROPERTY" ADD CONSTRAINT "V2_MENU_PROPERTY_PK" PRIMARY KEY ("MENU_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_MENU_XML
--------------------------------------------------------

  ALTER TABLE "V2_MENU_XML" MODIFY ("MENU_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_MENU_XML" ADD CONSTRAINT "V2_MENU_XML_PK" PRIMARY KEY ("MENU_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_PROPERTY" ADD CONSTRAINT "V2_PROPERTY_PK" PRIMARY KEY ("PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_ROLE
--------------------------------------------------------

  ALTER TABLE "V2_ROLE" MODIFY ("ROLE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ROLE" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ROLE" MODIFY ("DESCRIPTION" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ROLE" MODIFY ("MASK" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ROLE" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ROLE" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_ROLE" ADD CONSTRAINT "V2_ROLE_PK" PRIMARY KEY ("ROLE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_SEQUENCER
--------------------------------------------------------

  ALTER TABLE "V2_SEQUENCER" MODIFY ("SEQUENCER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SEQUENCER" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SEQUENCER" MODIFY ("VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SEQUENCER" ADD CONSTRAINT "V2_SEQUENCER_PK" PRIMARY KEY ("SEQUENCER_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_SOCIAL_ACCOUNT
--------------------------------------------------------

  ALTER TABLE "V2_SOCIAL_ACCOUNT" MODIFY ("ACCOUNT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT" MODIFY ("OBJECT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT" MODIFY ("OBJECT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT" MODIFY ("SOCIAL_PROVIDER" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT" ADD CONSTRAINT "V2_SOCIAL_ACCOUNT_PK" PRIMARY KEY ("ACCOUNT_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_SOCIAL_ACCOUNT_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_SOCIAL_ACCOUNT_PROPERTY" MODIFY ("ACCOUNT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_SOCIAL_ACCOUNT_PROPERTY" ADD CONSTRAINT "V2_SOCIAL_ACCOUNT_PROPERTY_PK" PRIMARY KEY ("ACCOUNT_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_TEMPLATE
--------------------------------------------------------

  ALTER TABLE "V2_TEMPLATE" MODIFY ("OBJECT_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("OBJECT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("TEMPLATE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("TEMPLATE_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("TITLE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("LOCATION" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE" ADD CONSTRAINT "V2_TEMPLATE_PK" PRIMARY KEY ("TEMPLATE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_TEMPLATE_BODY
--------------------------------------------------------

  ALTER TABLE "V2_TEMPLATE_BODY" MODIFY ("BODY_TEMPLATE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE_BODY" ADD CONSTRAINT "V2_TEMPLATE_BODY_PK" PRIMARY KEY ("BODY_TEMPLATE_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_TEMPLATE_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_TEMPLATE_PROPERTY" MODIFY ("TEMPLATE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_TEMPLATE_PROPERTY" ADD CONSTRAINT "V2_TEMPLATE_PROPERTY_PK" PRIMARY KEY ("TEMPLATE_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_USER
--------------------------------------------------------

  ALTER TABLE "V2_USER" MODIFY ("COMPANY_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("USER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("USERNAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("PASSWORD_HASH" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("EMAIL" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("LAST_LOGINED_IN" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("LAST_PROFILE_UPDATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("CREATION_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER" ADD CONSTRAINT "V2_USER_PK" PRIMARY KEY ("USER_ID") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_USER_PROPERTY
--------------------------------------------------------

  ALTER TABLE "V2_USER_PROPERTY" MODIFY ("USER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER_PROPERTY" MODIFY ("PROPERTY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER_PROPERTY" MODIFY ("PROPERTY_VALUE" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER_PROPERTY" ADD CONSTRAINT "V2_USER_PROPERTY_PK" PRIMARY KEY ("USER_ID", "PROPERTY_NAME") ENABLE;
/
--------------------------------------------------------
--  Constraints for Table V2_USER_ROLES
--------------------------------------------------------

  ALTER TABLE "V2_USER_ROLES" MODIFY ("USER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER_ROLES" MODIFY ("ROLE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "V2_USER_ROLES" ADD CONSTRAINT "V2_USER_ROLES_PK" PRIMARY KEY ("USER_ID", "ROLE_ID") ENABLE;
/

Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (19,'SOCIAL_NETWORK',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (22,'ANNOUNCE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (1,'COMPANY',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (2,'USER',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (3,'GROUP',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (4,'ROLE',4);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (16,'IMAGE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (17,'ATTACHMENT',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (18,'MENU',2);

Insert into V2_COMPANY (COMPANY_ID,DISPLAY_NAME,NAME,DESCRIPTION,DOMAIN_NAME) values (1, '인키움', 'INKIUM','주식회사 인키움', null);

Insert into V2_USER (COMPANY_ID,USER_ID,USERNAME,PASSWORD_HASH,NAME,NAME_VISIBLE,FIRST_NAME,LAST_NAME,EMAIL,EMAIL_VISIBLE,USER_ENABLED,VISIBLE,IS_EXTERNAL,FEDERATED,STATUS) 
values (1, 1, 'system','b524ac69d3f8516d3f90679d983cc797b91ddd2daa85f97696722e3e4701275b','운영자',1,null,null,'system@demo.com',1,1,1,0,0,0);

Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (1,'ROLE_USER','사용자 ROLE',0);

Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (2,'ROLE_ADMIN','관리자 ROLE',0);

Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (3,'ROLE_SYSTEM','시스템 ROLE',0);

insert into V2_USER_ROLES ( USER_ID,  ROLE_ID) values ( 1, 2);

insert into V2_USER_ROLES ( USER_ID,  ROLE_ID) values ( 1, 3);

Insert into V2_MENU (MENU_ID,NAME,TITLE,LOCATION,MENU_ENABLED) values (1,'PUBLIC_MENU','디폴트 메뉴',null, 1 );

/**
<?xml version="1.0" encoding="UTF-8" ?>
<MenuConfig>
  <Menus>
    <Menu  name="USER_MENU"  title="사용자 메뉴">
        <Item  name="MENU_1"  title="회사소개" page="#">
            <Item  name="MENU_1_1"  title="기업소개" page="/about.do" />
            <Item  name="MENU_1_2"  title="공지 &amp; 이벤트" page="/events.do" />
            <Item  name="MENU_1_3"  title="고객" page="/customers.do" />
            <Item  name="MENU_1_4"  title="뉴스" page="/press.do" />
            <Item  name="MENU_1_5"  title="오시는길" page="/contact.do" />
        </Item>
        <!--	
        <Item  name="MENU_2"  title="사업소개" toolTip="Shows customized menu displays." page="/menutest2.jsp">
            <Item  name="MENU_2_1"  title="서비스" page="/menutest1.jsp">
                <Item  name="MENU_2_1_1"  title="소프트웨어 개발" page="/menutest1.jsp"/>
                <Item  name="MENU_2_1_2"  title="인력파견" page="/menutest1.jsp"/>
            </Item>
            <Item  name="MENU_2_2"  title="솔루션" page="/menutest1.jsp">
                <Item  name="MENU_2_2_1"  title="역량진단 솔루션" page="/menutest1.jsp"/>
                <Item  name="MENU_2_2_2"  title="인적자원개발 솔루션" page="/menutest1.jsp"/>
            </Item>
        </Item>
        -->
    </Menu>
    <Menu  name="SYSTEM_MENU"  title="시스템메뉴">
        <Item  name="MENU_1"  title="기본운영관리">
            <Item  name="MENU_1_1"  title="회사관리" description="회사관리를 위한 기능을 제공합니다." page="/secure/main-company.do" />
            <Item  name="MENU_1_2"  title="사이트관리" description="회사에 관련된 모든 정보를 관리합니다." page="/secure/main-site.do" />
            <Item  name="MENU_1_3"  title="그룹관리" description="그룹관리를 위한 기능을 제공합니다." page="/secure/main-group.do" />
            <Item  name="MENU_1_4"  title="사용자관리" description="사용자 관리를 위한 기능을 제공합니다." page="/secure/main-user.do" />
            <Item  name="MENU_1_5"  title="시스템 정보" description="시스템에 대한 기본 정보를  제공합니다." page="/secure/system-info.do" />
        </Item>
    </Menu>
  </Menus>
</MenuConfig>

*/


