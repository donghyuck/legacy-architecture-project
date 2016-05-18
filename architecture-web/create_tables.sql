
		

		-- =================================================  
		--  ADDRESS
		-- =================================================	
		CREATE TABLE V2_ADDRESS (
		
			ADDRESS_ID						INTEGER NOT NULL,
			OBJECT_TYPE						INTEGER NOT NULL,
			OBJECT_ID							INTEGER NOT NULL,
			
			COMPANY_ID						INTEGER NOT NULL,	 		
			USER_ID								INTEGER NOT NULL,	 	
			
			NAME								VARCHAR2(100),	 	
			STREET1								VARCHAR2(255),
			STREET2								VARCHAR2(255),
			STREET3								VARCHAR2(255),
			CITY									VARCHAR2(255),
			ZIP									VARCHAR2(255),
			REGION_ID							INTEGER NOT NULL,	 	
			COUNTRY_ID						INTEGER NOT NULL,			
			
			MAILING_ADDRESS				NUMBER(1, 0)  DEFAULT 0, 
			PRIMARY_ADDRESS				NUMBER(1, 0)  DEFAULT 0, 
			
			CREATION_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE					DATE DEFAULT  SYSDATE NOT NULL,					
			CONSTRAINT V2_CONTENT_PK PRIMARY KEY (ADDRESS_ID)
		);	
		-- =================================================  
		--  CONTENT
		-- =================================================	
		CREATE TABLE V2_CONTENT (
			CONTENT_ID						INTEGER NOT NULL,
			CONTENT_TYPE					VARCHAR2(255) NOT NULL,
			--NAME								VARCHAR2(255),
		--	OBJECT_TYPE						INTEGER NOT NULL,
		--	OBJECT_ID							INTEGER NOT NULL,
			SUBJECT								VARCHAR2(255),
			SUMMARY							SUMMARY(4000),
			BODY								CLOB,
			CREATOR							INTEGER,
			LASTMODIFIER					INTEGER,			
			CREATION_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE					DATE DEFAULT  SYSDATE NOT NULL,			
			CONSTRAINT V2_CONTENT_PK PRIMARY KEY (CONTENT_ID)
		);	
		CREATE INDEX V2_CONTENT_IDX   ON V2_CONTENT (CREATOR);
		CREATE INDEX V2_CONTENT_IDX2 ON V2_CONTENT (LASTMODIFIER);		
		CREATE INDEX V2_CONTENT_IDX2 ON V2_CONTENT (NAME);	
		CREATE INDEX V2_CONTENT_IDX2 ON V2_CONTENT (CONTENT_TYPE);	

		CREATE TABLE V2_CONTENT_PROPERTY (
		  CONTENT_ID                     INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_CONTENT_PROPERTY_PK PRIMARY KEY (CONTENT_ID, PROPERTY_NAME)
		);	
		
		-- =================================================  
		--  COMMENT
		-- =================================================		
		CREATE TABLE V2_COMMENT (
			COMMENT_ID						INTEGER NOT NULL,
			PARENT_COMMENT_ID			INTEGER NOT NULL,
			OBJECT_TYPE						INTEGER NOT NULL,
			OBJECT_ID							INTEGER NOT NULL,	 					
			PARENT_OBJECT_TYPE			INTEGER NOT NULL,
			PARENT_OBJECT_ID				INTEGER NOT NULL,	 		
			USER_ID								INTEGER NOT NULL,	 		
			NAME								VARCHAR2(100),
			EMAIL								VARCHAR2(100),
			URL									VARCHAR2(255),
			IP										VARCHAR2(15),
			BODY								VARCHAR2(4000) NOT NULL,
			STATUS								NUMBER(1, 0)  DEFAULT 0, 
			CREATION_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE					DATE DEFAULT  SYSDATE NOT NULL,			
			CONSTRAINT V2_COMMENT_PK PRIMARY KEY (COMMENT_ID)
		);
		CREATE INDEX V2_COMMENT_IDX   ON V2_COMMENT (CREATION_DATE);
		CREATE INDEX V2_COMMENT_IDX2 ON V2_COMMENT (MODIFIED_DATE);
		CREATE INDEX V2_COMMENT_IDX3 ON V2_COMMENT( OBJECT_TYPE , OBJECT_ID) ;	
		CREATE INDEX V2_COMMENT_IDX4 ON V2_COMMENT( PARENT_OBJECT_TYPE , PARENT_OBJECT_ID) ;	
		CREATE INDEX V2_COMMENT_IDX5 ON V2_COMMENT (USER_ID);
		
		
		-- =================================================  
		--  CONTENT 	> TEMPLATE
		-- =================================================		
		CREATE TABLE V2_TEMPLATE (			
			OBJECT_TYPE							INTEGER NOT NULL,
			OBJECT_ID								INTEGER NOT NULL,	 			
			TEMPLATE_ID							INTEGER NOT NULL,
			TEMPLATE_TYPE						VARCHAR2(255) NOT NULL,
			TITLE										VARCHAR2(255) NOT NULL,
			LOCATION								VARCHAR2(255) NOT NULL,
			CREATOR								INTEGER,
			LASTMODIFIER						INTEGER,
			CREATION_DATE						DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE						DATE DEFAULT  SYSDATE NOT NULL,		
			CONSTRAINT V2_TEMPLATE_PK PRIMARY KEY (TEMPLATE_ID)
		);
		
		CREATE INDEX V2_TEMPLATE_TITLE_IDX ON V2_TEMPLATE (TITLE);
		
		CREATE INDEX V2_TEMPLATE_IDX2 ON V2_TEMPLATE( OBJECT_TYPE , OBJECT_ID) ;	
		
		CREATE TABLE V2_TEMPLATE_BODY (			
			BODY_TEMPLATE_ID					INTEGER NOT NULL,	 			
			BODY									CLOB,
			CONSTRAINT V2_TEMPLATE_BODY_PK PRIMARY KEY (BODY_TEMPLATE_ID)
		);		
		
		CREATE TABLE V2_TEMPLATE_PROPERTY (
		  TEMPLATE_ID                     INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_TEMPLATE_PROPERTY_PK PRIMARY KEY (TEMPLATE_ID, PROPERTY_NAME)
		);	

		-- =================================================  
		--  ANNOUNCE 	
		-- =================================================		
		CREATE TABLE V2_ANNOUNCE (
			ANNOUNCE_ID				INTEGER NOT NULL,
			OBJECT_TYPE						INTEGER NOT NULL,
			OBJECT_ID							INTEGER NOT NULL,	 		
			USER_ID								INTEGER NOT NULL,	 		
			SUBJECT								VARCHAR2(255) NOT NULL,
			BODY								VARCHAR2(255) NOT NULL,
			START_DATE						DATE DEFAULT  SYSDATE NOT NULL,
			END_DATE							DATE DEFAULT  SYSDATE NOT NULL,
			STATUS								NUMBER(1, 0)  DEFAULT 0, 
			CREATION_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			CONSTRAINT V2_ANNOUNCE_PK PRIMARY KEY (ANNOUNCE_ID)
		);
		
		CREATE INDEX V2_ANNOUNCE_IDX1 ON V2_USER (OBJECT_TYPE, OBJECT_ID);
		CREATE INDEX V2_ANNOUNCE_IDX2 ON V2_USER (START_DATE);
		CREATE INDEX V2_ANNOUNCE_IDX3 ON V2_USER (END_DATE);
		CREATE INDEX V2_ANNOUNCE_IDX4 ON V2_USER (USER_ID);
		
		CREATE TABLE V2_ANNOUNCE_PROPERTY (
		  ANNOUNCE_ID                     INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_ANNOUNCE_PROPERTY_PK PRIMARY KEY (ANNOUNCE_ID, PROPERTY_NAME)
		);	
		COMMENT ON TABLE      "V2_ANNOUNCE_PROPERTY"  IS '공지 프로퍼티 테이블';
		COMMENT ON COLUMN "V2_ANNOUNCE_PROPERTY"."ANNOUNCE_ID" IS '공지 ID'; 
		COMMENT ON COLUMN "V2_ANNOUNCE_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름'; 
		COMMENT ON COLUMN "V2_ANNOUNCE_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값'; 			
		
		-- =================================================  
		--  SOCIAL 	
		-- =================================================		
		CREATE TABLE V2_SOCIAL_PROVIDER (
			PROVIDER_ID				INTEGER NOT NULL,
			PROVIDER_NAME			VARCHAR2(255) NOT NULL,
			API_KEY						VARCHAR2(255) NOT NULL,
			API_SECRET					VARCHAR2(255) NOT NULL,
			CREATION_DATE			DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE			DATE DEFAULT  SYSDATE NOT NULL,			
			CONSTRAINT V2_SOCIAL_PROVIDER_PK PRIMARY KEY (PROVIDER_ID)
	    );
		CREATE UNIQUE INDEX V2_SOCIAL_PROVIDER_IDX ON V2_USER (PROVIDER_NAME);
		
		-- V2_SOCIAL_ACCOUNT to V2_SOCIAL_OAUTO_TOKEN
		CREATE TABLE V2_SOCIAL_OAUTO_TOKEN (
			OAUTO_TOKEN_ID					INTEGER NOT NULL,
			OBJECT_TYPE							INTEGER NOT NULL,
			OBJECT_ID								INTEGER NOT NULL,	 
			SOCIAL_PROVIDER					VARCHAR2(255) NOT NULL,
			ACCESS_TOKEN						VARCHAR2(255) NOT NULL,
			ACCESS_SECRET						VARCHAR2(255) NOT NULL,					
			ACCESS_TOKEN_EXPIRED			NUMBER(1, 0)  DEFAULT 0, 
			SIGNED									NUMBER(1, 0)  DEFAULT 0, 
			CREATION_DATE						DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE						DATE DEFAULT  SYSDATE NOT NULL,		
			CONSTRAINT V2_SOCIAL_ACCOUNT_PK PRIMARY KEY (ACCOUNT_ID)
	    );	    		
		
		-- =================================================  
		--  V2_OAUTH_TOKEN 	
		-- =================================================		
		CREATE TABLE V2_SOCIAL_PROVIDER (
			PROVIDER_ID				INTEGER NOT NULL,
			PROVIDER_NAME			VARCHAR2(255) NOT NULL,
			API_KEY						VARCHAR2(255) NOT NULL,
			API_SECRET					VARCHAR2(255) NOT NULL,
			CREATION_DATE			DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE			DATE DEFAULT  SYSDATE NOT NULL,			
			CONSTRAINT V2_SOCIAL_PROVIDER_PK PRIMARY KEY (PROVIDER_ID)
	    );
		CREATE UNIQUE INDEX V2_SOCIAL_PROVIDER_IDX ON V2_USER (PROVIDER_NAME);
		
		CREATE TABLE V2_SOCIAL_ACCOUNT (
			ACCOUNT_ID							INTEGER NOT NULL,
			OBJECT_TYPE							INTEGER NOT NULL,
			OBJECT_ID								INTEGER NOT NULL,	 
			SOCIAL_PROVIDER					VARCHAR2(255) NOT NULL,
			ACCESS_TOKEN						VARCHAR2(255) NOT NULL,
			ACCESS_SECRET						VARCHAR2(255) ,
			USERNAME                             VARCHAR2(255) NOT NULL,
			ACCESS_TOKEN_EXPIRED			NUMBER(1, 0)  DEFAULT 0, 
			SIGNED									NUMBER(1, 0)  DEFAULT 0, 
			CREATION_DATE						DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE						DATE DEFAULT  SYSDATE NOT NULL,		
			CONSTRAINT V2_SOCIAL_ACCOUNT_PK PRIMARY KEY (ACCOUNT_ID)
	    );	    
	    CREATE INDEX V2_SOCIAL_ACCOUNT_IDX ON V2_SOCIAL_ACCOUNT( OBJECT_TYPE ) ;	
	    CREATE INDEX V2_SOCIAL_ACCOUNT_IDX2 ON V2_SOCIAL_ACCOUNT( OBJECT_TYPE , OBJECT_ID) ;	
	    CREATE INDEX V2_SOCIAL_ACCOUNT_IDX3 ON V2_SOCIAL_ACCOUNT( OBJECT_TYPE , USERNAME) ;	
	    
	    COMMENT ON TABLE      "V2_SOCIAL_ACCOUNT"  IS '쇼셜 계정 정보 테이블';
	    COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."ACCOUNT_ID" IS '쇼셜계정정보 아이디'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."OBJECT_TYPE" IS '관련 데이터 유형'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."OBJECT_ID" IS '관련 데이터 ID'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."SOCIAL_PROVIDER" IS '서비스 제공자 명'; 
		--COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."SOCIAL_ACCOUNT_ID" IS '계정 ID'; 
		--COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."SOCIAL_ACCOUNT_NAME" IS '계정 NAMe'; 
		--COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."SOCIAL_ACCOUNT_USERNAME" IS '계정 USERNAME'; 
		--COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."SOCIAL_ACCOUNT_URL" IS '계정 URL'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."ACCESS_TOKEN" IS '인증된 TOKEN 값'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."ACCESS_SECRET" IS '인증된 SECRET 값'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."CREATION_DATE" IS '생성일'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT"."MODIFIED_DATE" IS '수정일'; 		
		
		CREATE TABLE V2_SOCIAL_ACCOUNT_PROPERTY (
		  ACCOUNT_ID                     INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_SOCIAL_ACCOUNT_PROPERTY_PK PRIMARY KEY (ACCOUNT_ID, PROPERTY_NAME)
		);	
		COMMENT ON TABLE      "V2_SOCIAL_ACCOUNT_PROPERTY"  IS '쇼셜 계정 정보 프로퍼티 테이블';
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT_PROPERTY"."ACCOUNT_ID" IS '쇼셜계정정보  ID'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름'; 
		COMMENT ON COLUMN "V2_SOCIAL_ACCOUNT_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값'; 				
		-- =================================================  
		--  MENU	
		-- =================================================		
		CREATE TABLE V2_MENU (
	    	MENU_ID			          INTEGER NOT NULL,
	   		--COMPANY_ID               INTEGER NOT NULL,
		   	NAME                         VARCHAR2(255) NOT NULL,
		   	TITLE                           VARCHAR2(255) NOT NULL,
		   	LOCATION                   VARCHAR2(255),
		    MENU_ENABLED           NUMBER(1, 0)  DEFAULT 1,
			CREATION_DATE            DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE            DATE DEFAULT  SYSDATE NOT NULL,	
	    	CONSTRAINT V2_MENU_PK PRIMARY KEY (MENU_ID)	    
	    );		
		COMMENT ON TABLE      "V2_MENU"  IS '메뉴 테이블';
		COMMENT ON COLUMN "V2_MENU"."MENU_ID" IS '메뉴 ID'; 
		--COMMENT ON COLUMN "V2_MENU"."COMPANY_ID" IS '회사 아이디'; 
		COMMENT ON COLUMN "V2_MENU"."NAME" IS '이름'; 		
		COMMENT ON COLUMN "V2_MENU"."TITLE" IS '타이틀 명'; 
		COMMENT ON COLUMN "V2_MENU"."LOCATION" IS 'LOCATION 값'; 				
		COMMENT ON COLUMN "V2_MENU"."MENU_ENABLED" IS '사용여부'; 
		COMMENT ON COLUMN "V2_MENU"."CREATION_DATE" IS '생성을'; 		
		COMMENT ON COLUMN "V2_MENU"."MODIFIED_DATE" IS '수정일'; 
		
		CREATE TABLE V2_MENU_PROPERTY (
		  MENU_ID                     INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_MENU_PROPERTY_PK PRIMARY KEY (MENU_ID, PROPERTY_NAME)
		);	
		COMMENT ON TABLE      "V2_MENU_PROPERTY"  IS '메뉴 프로퍼티 테이블';
		COMMENT ON COLUMN "V2_MENU_PROPERTY"."MENU_ID" IS '메뉴 ID'; 
		COMMENT ON COLUMN "V2_MENU_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름'; 
		COMMENT ON COLUMN "V2_MENU_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값'; 		

		CREATE TABLE V2_MENU_XML (
			  MENU_ID                    INTEGER NOT NULL,
			  MENU_DATA               CLOB,
			  CONSTRAINT V2_MENU_XML_PK PRIMARY KEY (MENU_ID)
		);
		
		CREATE TABLE V2_MENU_DATA (
			  MENU_ID                    INTEGER NOT NULL,
			  MENU_DATA               BLOB,
			  CONSTRAINT V2_MENU_DATA_PK PRIMARY KEY (MENU_ID)
		);
		
		-- =================================================  
		--  ATTACHEMENT 	
		-- =================================================		
		CREATE TABLE V2_ATTACHMENT (
	    		ATTACHMENT_ID			  INTEGER NOT NULL,
			  	OBJECT_TYPE                INTEGER NOT NULL,
			  	OBJECT_ID                   INTEGER NOT NULL,	    		
			   	CONTENT_TYPE             VARCHAR2(255)  NOT NULL,			  
			   	FILE_NAME                   VARCHAR2(255)   NOT NULL,
			   	FILE_SIZE                      INTEGER   NOT NULL,
			   	USER_ID								INTEGER NOT NULL,	 	
			   	CREATION_DATE            DATE DEFAULT  SYSDATE NOT NULL,
			   	MODIFIED_DATE            DATE DEFAULT  SYSDATE NOT NULL,	
	    	   CONSTRAINT V2_ATTACHMENT_PK PRIMARY KEY (ATTACHMENT_ID)	    
	    );
	    
	    
	    -- alter table V2_ATTACHMENT add(USER_ID INTEGER NOT NULL DEFAULT 0);	
	    
	    CREATE INDEX V2_ATTACHMENT_OBJ_IDX ON V2_ATTACHMENT( OBJECT_TYPE, OBJECT_ID ) ;	
		COMMENT ON TABLE "V2_ATTACHMENT"  IS '첨부파일 테이블';
		COMMENT ON COLUMN "V2_ATTACHMENT"."ATTACHMENT_ID" IS 'ID'; 
		COMMENT ON COLUMN "V2_ATTACHMENT"."OBJECT_TYPE" IS '첨부파일과 연관된 모델 유형'; 
        COMMENT ON COLUMN "V2_ATTACHMENT"."OBJECT_ID" IS '첨부파일과 연관된 모델 ID';
		COMMENT ON COLUMN "V2_ATTACHMENT"."FILE_NAME" IS '첨부파일 이름'; 
        COMMENT ON COLUMN "V2_ATTACHMENT"."FILE_SIZE" IS '첨부파일 크기';        
		COMMENT ON COLUMN "V2_ATTACHMENT"."CONTENT_TYPE" IS 'CONTENT TYPE 값'; 
		COMMENT ON COLUMN "V2_ATTACHMENT"."CREATION_DATE" IS '생성일'; 
        COMMENT ON COLUMN "V2_ATTACHMENT"."MODIFIED_DATE" IS '수정일';

        CREATE TABLE V2_ATTACHMENT_PROPERTY (
		  ATTACHMENT_ID               INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_ATTACHMENT_PROPERTY_PK PRIMARY KEY (ATTACHMENT_ID, PROPERTY_NAME)
		);	
		COMMENT ON TABLE      "V2_ATTACHMENT_PROPERTY"  IS '첨부파일 프로퍼티 테이블';
		COMMENT ON COLUMN "V2_ATTACHMENT_PROPERTY"."ATTACHMENT_ID" IS '첨부파일 ID'; 
		COMMENT ON COLUMN "V2_ATTACHMENT_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름'; 
		COMMENT ON COLUMN "V2_ATTACHMENT_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값'; 		

		CREATE TABLE V2_ATTACHMENT_DATA (
			  ATTACHMENT_ID                    INTEGER NOT NULL,
			  ATTACHMENT_DATA               BLOB,
			  CONSTRAINT V2_ATTACHMENT_DATA_PK PRIMARY KEY (ATTACHMENT_ID)255
		);		        
		
		COMMENT ON TABLE "V2_ATTACHMENT_DATA"  IS '첨부파일 데이터 테이블';
		COMMENT ON COLUMN "V2_ATTACHMENT_DATA"."ATTACHMENT_ID" IS 'ID'; 
		COMMENT ON COLUMN "V2_ATTACHMENT_DATA"."ATTACHMENT_DATA" IS '첨부파일 데이터'; 		
		
	    -- =================================================  
		--  IMAGE	
		-- =================================================		
		
		CREATE TABLE V2_IMAGE (
			  IMAGE_ID                    INTEGER NOT NULL,
			  OBJECT_TYPE                INTEGER NOT NULL,
			  OBJECT_ID                   INTEGER NOT NULL,
			  FILE_NAME                   VARCHAR2(255)   NOT NULL,
			  FILE_SIZE                INTEGER   NOT NULL,
			  CONTENT_TYPE             VARCHAR2(50)  NOT NULL,			  
			  USER_ID				   INTEGER NOT NULL,	 	
			  CREATION_DATE            DATE DEFAULT  SYSDATE NOT NULL,
			  MODIFIED_DATE            DATE DEFAULT  SYSDATE NOT NULL,	
			  CONSTRAINT V2_IMAGE_PK PRIMARY KEY (IMAGE_ID)
		);		        
		
		
		CREATE INDEX V2_IMAGE_OBJ_IDX ON V2_IMAGE( OBJECT_TYPE, OBJECT_ID ) ;	
		COMMENT ON TABLE "V2_IMAGE"  IS '이미지 테이블';
		COMMENT ON COLUMN "V2_IMAGE"."IMAGE_ID" IS 'ID'; 
		COMMENT ON COLUMN "V2_IMAGE"."OBJECT_TYPE" IS '이미지와 연관된 모델 유형'; 
        COMMENT ON COLUMN "V2_IMAGE"."OBJECT_ID" IS '이미지와 연관된 모델 ID';
		COMMENT ON COLUMN "V2_IMAGE"."FILE_NAME" IS '이미지 파일 이름'; 
        COMMENT ON COLUMN "V2_IMAGE"."FILE_SIZE" IS '이미지 파일 크기';        
		COMMENT ON COLUMN "V2_IMAGE"."CONTENT_TYPE" IS 'CONTENT TYPE 값'; 
		COMMENT ON COLUMN "V2_IMAGE"."CREATION_DATE" IS '생성일'; 
        COMMENT ON COLUMN "V2_IMAGE"."MODIFIED_DATE" IS '수정일';
        
        CREATE TABLE V2_IMAGE_DATA (
			  IMAGE_ID                    INTEGER NOT NULL,
			  IMAGE_DATA               BLOB,
			  CONSTRAINT V2_IMAGE_DATA_PK PRIMARY KEY (IMAGE_ID)
		);		        
		
		COMMENT ON TABLE "V2_IMAGE_DATA"  IS '이미지 데이터 테이블';
		COMMENT ON COLUMN "V2_IMAGE_DATA"."IMAGE_ID" IS 'ID'; 
		COMMENT ON COLUMN "V2_IMAGE_DATA"."IMAGE_DATA" IS '이미지 데이터'; 		
		
		
		CREATE TABLE V2_IMAGE_STREAMS ( 
			EXTERNAL_ID	VARCHAR2(255)   NOT NULL, 
			IMAGE_ID		INTEGER NOT NULL,
			PUBLIC_SHARED			NUMBER(1, 0)  DEFAULT 1,
			CREATOR					INTEGER,
			CREATION_DATE			DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE			DATE DEFAULT  SYSDATE NOT NULL,
			CONSTRAINT V2_IMAGE_STREAMS_PK PRIMARY KEY (EXTERNAL_ID)
		);
		
		CREATE INDEX V2_IMAGE_STREAMS_IDX ON V2_IMAGE_STREAMS( EXTERNAL_ID, PUBLIC_SHARED ) ;	
		 		 		
		
		CREATE TABLE V2_IMAGE_LINK ( 
			LINK_ID						VARCHAR2(255)	NOT NULL, 
			IMAGE_ID					INTEGER NOT NULL,			
			PUBLIC_SHARED			NUMBER(1, 0)  DEFAULT 1,
			CONSTRAINT V2_IMAGE_LINK_PK PRIMARY KEY (LINK_ID)
		); 		
		CREATE UNIQUE INDEX V2_IMAGE_LINK_IDX ON V2_IMAGE_LINK (IMAGE_ID);
		
		COMMENT ON TABLE "V2_IMAGE_LINK"  IS '이미지 링크 테이블';
		COMMENT ON COLUMN "V2_IMAGE_LINK"."LINK_ID" IS '링크 아이디'; 
		COMMENT ON COLUMN "V2_IMAGE_LINK"."IMAGE_ID" IS '이미지 아이디'; 	
		
		
		
		CREATE TABLE V2_PROFILE_IMAGE (
			  PROFILE_IMAGE_ID         INTEGER NOT NULL,
			  USER_ID                       INTEGER NOT NULL,
			  PRIMARY_IMAGE           NUMBER(1, 0)  DEFAULT 1,
			  FILE_NAME                   VARCHAR2(255)   NOT NULL,
			  FILE_SIZE                      INTEGER   NOT NULL,
			  CONTENT_TYPE             VARCHAR2(50)  NOT NULL,			  
			  CREATION_DATE            DATE DEFAULT  SYSDATE NOT NULL,
			  MODIFIED_DATE            DATE DEFAULT  SYSDATE NOT NULL,	
			  CONSTRAINT V2_PROFILE_IMAGE_PK PRIMARY KEY (PROFILE_IMAGE_ID)
		);		        
		
		
		CREATE INDEX V2_PROFILE_IMAGE_IDX ON V2_IMAGE( USER_ID ) ;	
		COMMENT ON TABLE "V2_PROFILE_IMAGE"  IS '이미지 테이블';
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."PROFILE_IMAGE_ID" IS 'ID'; 
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."USER_ID" IS '사용자  ID';
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."FILE_NAME" IS '이미지 파일 이름'; 
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."FILE_SIZE" IS '이미지 파일 크기';        
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."CONTENT_TYPE" IS 'CONTENT TYPE 값'; 
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."CREATION_DATE" IS '생성일'; 
		COMMENT ON COLUMN "V2_PROFILE_IMAGE"."MODIFIED_DATE" IS '수정일';
        
		CREATE TABLE V2_PROFILE_IMAGE_DATA (
			PROFILE_IMAGE_ID         INTEGER NOT NULL,
			PROFILE_IMAGE_DATA               BLOB,
			CONSTRAINT V2_PROFILE_IMAGE_DATA_PK PRIMARY KEY (PROFILE_IMAGE_ID)
		);		        
		
		COMMENT ON TABLE "V2_PROFILE_IMAGE_DATA"  IS '프로파일 이미지 데이터 테이블';
		COMMENT ON COLUMN "V2_PROFILE_IMAGE_DATA"."PROFILE_IMAGE_ID" IS 'ID'; 
		COMMENT ON COLUMN "V2_PROFILE_IMAGE_DATA"."PROFILE_IMAGE_DATA" IS '이미지 데이터'; 		
		

		CREATE TABLE V2_LOGO (
			  LOGO_ID                    INTEGER NOT NULL,
			  OBJECT_TYPE                INTEGER NOT NULL,
			  OBJECT_ID                   INTEGER NOT NULL,
			  PRIMARY_LOGO           NUMBER(1, 0)  DEFAULT 1,
			  FILE_NAME                   VARCHAR2(255)   NOT NULL,
			  FILE_SIZE                      INTEGER   NOT NULL,
			  CONTENT_TYPE             VARCHAR2(50)  NOT NULL,			  
			  CREATION_DATE            DATE DEFAULT  SYSDATE NOT NULL,
			  MODIFIED_DATE            DATE DEFAULT  SYSDATE NOT NULL,	
			  CONSTRAINT V2_IMAGE_PK PRIMARY KEY (IMAGE_ID)
		);			
		
		CREATE INDEX V2_LOGO_IDX ON V2_LOGO( OBJECT_TYPE, OBJECT_ID ) ;	
		CREATE INDEX V2_LOGO_IDX2 ON V2_LOGO( OBJECT_TYPE, OBJECT_ID, PRIMARY_LOGO ) ;	
		
		CREATE TABLE V2_LOGO_DATA (
			LOGO_ID			INTEGER NOT NULL,
			LOGO_DATA		BLOB,
			CONSTRAINT V2_LOGO_DATA_PK PRIMARY KEY (LOGO_ID)
		);	
				
		-- =================================================  
		--  ALBUM
		-- =================================================	
		CREATE TABLE V2_ALBUM (		
			ALBUM_ID						INTEGER NOT NULL,
			USER_ID							INTEGER NOT NULL,	 	
			NAME							VARCHAR2(1000),	 	
			DESCRIPTION						VARCHAR2(4000),
			SHARED							NUMBER(1, 0)  DEFAULT 0, 
			COLLABORATE						NUMBER(1, 0)  DEFAULT 0, 
			CREATION_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE					DATE DEFAULT  SYSDATE NOT NULL,					
			CONSTRAINT V2_ALBUM_PK PRIMARY KEY (ALBUM_ID)
		);	
		
		CREATE INDEX V2_ALBUM_IDX1 ON V2_ALBUM (USER_ID) ;

		CREATE TABLE V2_ALBUM_PROPERTY (
		  ALBUM_ID               INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_ALBUM_PROPERTY_PK PRIMARY KEY (ALBUM_ID, PROPERTY_NAME)
		);	
		
		COMMENT ON TABLE  "V2_ALBUM_PROPERTY"  IS '앨범 프로퍼티 테이블';
		COMMENT ON COLUMN "V2_ALBUM_PROPERTY"."ALBUM_ID" IS '앨범 ID'; 
		COMMENT ON COLUMN "V2_ALBUM_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름'; 
		COMMENT ON COLUMN "V2_ALBUM_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값'; 	

		
		CREATE TABLE "V2_ALBUM_IMAGE" 
	   	(	"ALBUM_ID" 		INTEGER NOT NULL,
			"IMAGE_ID" 		INTEGER NOT NULL,
			"IMAGE_LINK" 	VARCHAR2(500), 
			"CREATION_DATE" DATE DEFAULT SYSDATE, 
			"MODIFIED_DATE" DATE DEFAULT SYSDATE,
			CONSTRAINT V2_ALBUM_IMAGE_PK PRIMARY KEY (ALBUM_ID, IMAGE_ID)
	  	) ;

	  	
	  	-- =================================================  
		--  WEBSITE
		-- =================================================	
