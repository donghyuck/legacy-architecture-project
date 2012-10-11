	
       drop table V2_USER cascade constraints PURGE ;
       drop table V2_USER_PROPERTY cascade constraints PURGE ;
 
        -- User data table.
        CREATE TABLE V2_USER (
		  USER_ID                INTEGER NOT NULL,
		  USERNAME               VARCHAR2(100) NOT NULL,
		  PASSWORD_HASH          VARCHAR2(64)  NOT NULL,
		  NAME                   VARCHAR2(100),		  
		  NAME_VISIBLE           NUMBER(1, 0)  DEFAULT 1 ,
		  FIRST_NAME             VARCHAR2(100),		  
		  LAST_NAME              VARCHAR2(100),		
		  EMAIL                  VARCHAR2(100) NOT NULL,
		  EMAIL_VISIBLE          NUMBER(1, 0)  DEFAULT 1,
		  LAST_LOGINED_IN       DATE DEFAULT  SYSDATE NOT NULL,
		  LAST_PROFILE_UPDATE  DATE DEFAULT  SYSDATE NOT NULL,
		  USER_ENABLED           NUMBER(1, 0)  DEFAULT 1, 
		  VISIBLE                NUMBER(1, 0)  DEFAULT 1, 
		  IS_EXTERNAL            NUMBER(1, 0)  DEFAULT 0, 
		  FEDERATED              NUMBER(1, 0)  DEFAULT 0,
		  STATUS                 NUMBER(1, 0)  DEFAULT 0,
		  CREATION_DATE          DATE DEFAULT  SYSDATE NOT NULL,
		  MODIFIED_DATE          DATE DEFAULT  SYSDATE NOT NULL,		    
		  CONSTRAINT V2_USER_PK PRIMARY KEY (USER_ID)
		);		
		
		CREATE UNIQUE INDEX V2_USER_USERNAME_IDX ON V2_USER (USERNAME);
	    CREATE INDEX V2_USER_DATE_IDX ON V2_USER(CREATION_DATE) ;		
	    CREATE INDEX V2_USER_EXTERNAL_IDX ON V2_USER(VISIBLE, USER_ENABLED, IS_EXTERNAL) ;		
				    
		COMMENT ON TABLE "V2_USER"  IS '사용자 정보';
		COMMENT ON COLUMN "V2_USER"."USER_ID" IS '사용자 ID'; 
        COMMENT ON COLUMN "V2_USER"."USERNAME" IS '로그인 아이디';		
		COMMENT ON COLUMN "V2_USER"."PASSWORD_HASH" IS '암호화된 패스워드';	
		COMMENT ON COLUMN "V2_USER"."NAME" IS '전체이름';		
		COMMENT ON COLUMN "V2_USER"."NAME_VISIBLE" IS '이름 공개 여부';		
		COMMENT ON COLUMN "V2_USER"."FIRST_NAME" IS '이름';		
		COMMENT ON COLUMN "V2_USER"."LAST_NAME" IS '성';		
		COMMENT ON COLUMN "V2_USER"."EMAIL" IS '메일주소';		
		COMMENT ON COLUMN "V2_USER"."EMAIL_VISIBLE" IS '메일공개여부';		
		COMMENT ON COLUMN "V2_USER"."LAST_LOGINED_IN" IS '마지막로그인일자';		
		COMMENT ON COLUMN "V2_USER"."LAST_PROFILE_UPDATE" IS '마지막프로파일 수정일자';		
		COMMENT ON COLUMN "V2_USER"."USER_ENABLED" IS '계정 사용여부';		
		COMMENT ON COLUMN "V2_USER"."IS_EXTERNAL" IS '외부 계정 여부';		
		COMMENT ON COLUMN "V2_USER"."STATUS" IS '계정 상태';		
		COMMENT ON COLUMN "V2_USER"."CREATION_DATE" IS '생성일자';		
		COMMENT ON COLUMN "V2_USER"."MODIFIED_DATE" IS '수정일자';			
	      
		-- User properties data table.		        
        CREATE TABLE V2_USER_PROPERTY (
		  USER_ID                INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_USER_PROPERTY_PK PRIMARY KEY (USER_ID, PROPERTY_NAME)
		);		
				    
		COMMENT ON TABLE "V2_USER_PROPERTY"  IS '사용자 프로퍼티 정보';
		COMMENT ON COLUMN "V2_USER_PROPERTY"."USER_ID" IS '사용자 ID'; 
        COMMENT ON COLUMN "V2_USER_PROPERTY"."PROPERTY_NAME" IS '프로퍼티 이름';		
		COMMENT ON COLUMN "V2_USER_PROPERTY"."PROPERTY_VALUE" IS '프로퍼티 값';		
		
		
		
		
		
		
		
		
		
		
		
		
		
		-- Group data table.
        CREATE TABLE V2_GROUPS (
		  GROUP_ID                INTEGER NOT NULL,
		  NAME                    VARCHAR2(100)   NOT NULL,
		  DESCRIPTION             VARCHAR2(1000)  NOT NULL,
		  CREATION_DATE           DATE DEFAULT  SYSDATE NOT NULL,
		  MODIFIED_DATE           DATE DEFAULT  SYSDATE NOT NULL,	
		  CONSTRAINT V2_GROUPS_PK PRIMARY KEY (GROUP_ID)
		);
		
		CREATE INDEX V2_GROUPS_DATE_IDX ON V2_GROUPS(CREATION_DATE) ;		
		CREATE INDEX V2_GROUPS_NAME_IDX ON V2_GROUPS(NAME);			
		
		CREATE TABLE V2_GROUP_MEMBERS (
		  GROUP_ID                INTEGER NOT NULL,
		  USER_ID                 INTEGER NOT NULL,
		  ADMINISTRATOR           NUMBER(1, 0)  DEFAULT 0,
		  CREATION_DATE           DATE DEFAULT  SYSDATE NOT NULL,
		 --  MODIFIED_DATE           DATE DEFAULT  SYSDATE NOT NULL,	
		  CONSTRAINT V2_GROUP_MEMBERS_PK PRIMARY KEY (GROUP_ID, USER_ID, ADMINISTRATOR)
		);
		
		CREATE INDEX V2_GROUP_MEMBERS_USER_IDX ON V2_GROUPS(USER_ID) ASC;		
		
		CREATE TABLE V2_GROUP_PROPERTY (
		  GROUP_ID               INTEGER NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_GROUP_PROPERTY_PK PRIMARY KEY (GROUP_ID, PROPERTY_NAME)
		);	
		
		-- Profile field data table.
		CREATE TABLE V2_PROFILE_FIELD (
		    FIELD_ID            INTEGER NOT NULL,
		    FIELD_TYPE          INTEGER NOT NULL,
		    NAME                VARCHAR2(200) NOT NULL,		
		    IDX                 INTEGER NOT NULL,
		    REG_IDX             INTEGER NOT NULL,
		    IS_DEFAULT          NUMBER(1, 0)  DEFAULT 1, 
		    IS_EDITABLE         NUMBER(1, 0)  DEFAULT 1, 
		    IS_FILTERABLE       NUMBER(1, 0)  DEFAULT 1, 
		    IS_LIST             NUMBER(1, 0)  DEFAULT 0, 
		    IS_REQUIRED         NUMBER(1, 0)  DEFAULT 1, 
		    IS_SEARCHABLE       NUMBER(1, 0)  DEFAULT 1, 
		    IS_VISIBLE          NUMBER(1, 0)  DEFAULT 1, 
		    EXTERNAL_MANAGED    NUMBER(1, 0)  DEFAULT 0, 
		    EXTERNAL_MAPPING    VARCHAR2(2048),
		    CONSTRAINT V2_PROFILE_FIELD_PK PRIMARY KEY (FIELD_ID)
		);
		
		CREATE UNIQUE INDEX V2_PROFILE_FIELD_NAME_IDX ON V2_PROFILE_FIELD (NAME);
		
		-- Profile field option data table.
		CREATE TABLE V2_PROFILE_FIELD_OPT (
		    DEFAULT_OPTION      NUMBER(1, 0)  DEFAULT 1, 
		    FIELD_ID            INTEGER NOT NULL,
		    FIELD_VALUE         VARCHAR2(255) NOT NULL,	   
		    IDX                 INTEGER NOT NULL
		);
		
		ALTER TABLE V2_PROFILE_FIELD_OPT ADD CONSTRAINT V2_PROFILE_FIELD_OPT_FK FOREIGN KEY (FIELD_ID) 
        REFERENCES V2_PROFILE_FIELD (FIELD_ID) DEFERRABLE INITIALLY DEFERRED;
		
		-- User profile data table.
		CREATE TABLE V2_USER_PROFILE (
		    USER_ID             INTEGER NOT NULL,
		    FIELD_ID            INTEGER NOT NULL,
		    PRIMARY_VALUE       NUMBER(1, 0)  DEFAULT 0,   
		    VALUE               VARCHAR2(3500) NOT NULL	   
		);
		ALTER TABLE V2_USER_PROFILE ADD CONSTRAINT V2_USER_PROFILE_FK FOREIGN KEY (FIELD_ID) 
        REFERENCES V2_PROFILE_FIELD (FIELD_ID) DEFERRABLE INITIALLY DEFERRED;        
        CREATE INDEX V2_PROFILE_FIELD_IDX ON V2_USER_PROFILE(USER_ID, FIELD_ID) ;		
		
		
		<!-- Role data table -->
		 
		 CREATE TABLE V2_ROLES (
		  ROLE_ID                INTEGER NOT NULL,
		  NAME                    VARCHAR2(100)   NOT NULL,
		  DESCRIPTION             VARCHAR2(1000)  NOT NULL,
		  CREATION_DATE           DATE DEFAULT  SYSDATE NOT NULL,
		  MODIFIED_DATE           DATE DEFAULT  SYSDATE NOT NULL,	
		  CONSTRAINT V2_ROLES_PK PRIMARY KEY (ROLE_ID)
		);
		
		CREATE TABLE V2_USER_ROLES (
		  USER_ID                 INTEGER NOT NULL,
		  ROLE_ID                 INTEGER NOT NULL,
		  CONSTRAINT V2_USER_ROLES_PK PRIMARY KEY (USER_ID, ROLE_ID)
		);

		CREATE TABLE V2_GROUP_ROLES (
		  GROUP_ID                INTEGER NOT NULL,
		  ROLE_ID                 INTEGER NOT NULL,
		  CONSTRAINT V2_GROUP_ROLES_PK PRIMARY KEY (GROUP_ID, ROLE_ID)
		);
				
		<!-- Stores permissions with it's permission mask" -->		
		CREATE TABLE V2_PERMISSION_MASK (
		    NAME                 VARCHAR2(255) NOT NULL,
		    MASK                 INTEGER NOT NULL,
		    CONSTRAINT V2_PERMISSION_MASK_PK PRIMARY KEY (NAME)
		);		
				
		-- CREATE UNIQUE INDEX V2_PERMISSION_MASK_IDX ON V2_PERMISSION_MASK (NAME)
       

       <!-- DYNAMIC --> 
              
       CREATE TABLE V2_WEB_RESOURCE (
             RESOURCE_ID    INTEGER NOT NULL,
             LOCATION       VARCHAR2(255) NOT NULL,
             CONTENT_TYPE   INTEGER DEFAULT 1 NOT NULL,
		     CREATION_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     MODIFIED_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     CONSTRAINT V2_WEB_RESOURCE_PK PRIMARY KEY (RESOURCE_ID)
	   );	     
		     
	   CREATE TABLE V2_DYNAMIC_OBJECT_SOURCE (
	         OBJECT_ID     INTEGER NOT NULL,
	         OBJECT_TYPE   INTEGER NOT NULL,
	         LOCATION       VARCHAR2(255) NOT NULL,
		     CREATION_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     MODIFIED_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     CONSTRAINT V2_DYNAMIC_OBJECT_SOURCE_PK PRIMARY KEY (OBJECT_ID)	         
	   );	     
              
        -- =================================================  
		-- I18N  	
		-- =================================================
		
		CREATE TABLE V2_I18N_LOCALE (
			 LOCALE_ID      INTEGER NOT NULL,
			 LANGUAGE       VARCHAR2(100),
			 COUNTRY        VARCHAR2(100),
			 VARIANT        VARCHAR2(100),
			 ENCODING       VARCHAR2(100),
		     CREATION_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     MODIFIED_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     CONSTRAINT V2_I18N_LOCALE_PK PRIMARY KEY (LOCALE_ID)
    	);
				
		INSERT INTO V2_I18N_LOCALE ( LOCALE_ID ,LANGUAGE, COUNTRY, VARIANT, ENCODING ) VALUES (1, 'ko', 'KR', NULL, 'UTF-8');

		CREATE TABLE V2_I18N_LOCALIZER (
			 LOCALIZER_ID      INTEGER NOT NULL,
			 LOCALE_ID         INTEGER NOT NULL,
			 NAME              VARCHAR2(255) NOT NULL,
			 DESCRIPTION       VARCHAR2(1000),
		     CREATION_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     MODIFIED_DATE  DATE DEFAULT SYSDATE NOT NULL,
		     
		     CONSTRAINT V2_I18N_LOCALIZER_PK PRIMARY KEY (LOCALIZER_ID)
    	);		
			
    	CREATE INDEX V2_I18N_LOCALIZER_IDX ON V2_I18N_LOCALIZER(NAME, LOCALE_ID) ;
    					
		CREATE TABLE V2_I18N_TEXT (
		     LOCALIZER_ID           INTEGER NOT NULL,
		     TEXT_KEY               VARCHAR2(200)   NOT NULL,		    
		     TEXT                   VARCHAR2(2000)  NOT NULL,	
			 CREATION_DATE          DATE DEFAULT SYSDATE NOT NULL ,
		     MODIFIED_DATE          DATE DEFAULT SYSDATE NOT NULL ,
		     CONSTRAINT V2_I18N_TEXT_PK PRIMARY KEY (LOCALIZER_ID, TEXT_KEY)
		);
		
		
		-- provides internationalized text for other tables.		

				
		CREATE TABLE V2_ZIPCODE (
		    ZIPCODE_ID             INTEGER NOT NULL,
		    ZIPCODE                CHAR(7),
		    SIDO                   VARCHAR2(50), 
		    GUGUN                  VARCHAR2(100),  
		    DONG                   VARCHAR2(200),
		    BUNJI		           VARCHAR2(100),
		    CONSTRAINT V2_ZIPCODE_PK PRIMARY KEY (ZIPCODE_ID)		    
		);
				
	
		create table V2_I18N_COUNTRY (
		    COUNTRY_ID         INTEGER NOT NULL,
		    NAME               VARCHAR2(300) NULL,
		    A2                 VARCHAR2(300) NULL,
		    A3                 VARCHAR2(300) NULL,
		    NUM                VARCHAR2(300) NULL,
		    DIAL               VARCHAR2(300) NULL,     
		    ENABLED            NUMBER(1, 0) DEFAULT 1,
		    CONSTRAINT V2_I18N_COUNTRY_PK PRIMARY KEY (COUNTRY_ID)
		);
      			
		CREATE TABLE V2_I18N_REGION (
		    REGION_ID              INTEGER NOT NULL,
			COUNTRY_ID             INTEGER,
			REGION_CODE            VARCHAR2(300) NULL,
			NAME                   VARCHAR2(300) NULL,
			ENABLED                NUMBER(1, 0)  DEFAULT 1,
			CONSTRAINT V2_I18N_REGION_PK PRIMARY KEY (REGION_ID)
		);
		
        <!-- PLUGIN -->         
        CREATE TABLE V2_PLUGIN_DATA (
            PLUGIN_ID             INTEGER NOT NULL,
            NAME                  VARCHAR2(200) NOT NULL,
            CREATION_DATE         DATE DEFAULT SYSDATE NOT NULL,
            MODIFIED_DATE         DATE DEFAULT SYSDATE NOT NULL,
            DATA                  BLOB,
            CONSTRAINT V2_PLUGIN_DATA_PK PRIMARY KEY (PLUGIN_ID)
        );     
        
        CREATE UNIQUE INDEX V2_PLUGIN_DATA_NAME_IDX ON V2_PLUGIN_DATA (NAME);
        
         <!-- UPGRADE MANGER -->
        CREATE TABLE V2_PRODUCT_VERSION (
            NAME                  VARCHAR(50) NOT NULL,
            VERSION               INTEGER ,
            CONSTRAINT V2_PRODUCT_VERSION_PK PRIMARY KEY (NAME)
        );
        