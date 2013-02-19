

		
		<!-- 1. 프로퍼티 -->
		CREATE TABLE V2_PROPERTY (
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_PROPERTY_PK PRIMARY KEY (PROPERTY_NAME)
		); 		
			
		COMMENT ON COLUMN V2_PROPERTY.PROPERTY_NAME    IS '프로퍼티 이름';                      
		COMMENT ON COLUMN V2_PROPERTY.PROPERTY_VALUE   IS '프로퍼티 값';  
		
		
		<!-- 2. 로케일에 따른 프로퍼티 -->
		CREATE TABLE V2_LOCALIZED_PROPERTY (
		  LOCALE_CODE            VARCHAR2(100)   NOT NULL,
		  PROPERTY_NAME          VARCHAR2(100)   NOT NULL,
		  PROPERTY_VALUE         VARCHAR2(1024)  NOT NULL,
		  CONSTRAINT V2_LOCALIZED_PROPERTY_PK PRIMARY KEY (LOCALE_CODE, PROPERTY_NAME)
		);
		
		COMMENT ON COLUMN V2_LOCALIZED_PROPERTY.LOCALE_CODE      IS '로케일 코드';                          
		COMMENT ON COLUMN V2_LOCALIZED_PROPERTY.PROPERTY_NAME    IS '프로퍼티 이름';                      
		COMMENT ON COLUMN V2_LOCALIZED_PROPERTY.PROPERTY_VALUE   IS '프로퍼티 값';                                

		
		<!-- 3. 아이디 관리를 위한 시퀀스  -->
		CREATE TABLE V2_SEQUENCER (
		    SEQUENCER_ID           INTEGER NOT NULL,
		    NAME                   VARCHAR2(200) NOT NULL,
		    VALUE                  INTEGER NOT NULL,
		    CONSTRAINT V2_SEQUENCER_PK PRIMARY KEY (SEQUENCER_ID)
		); 				 
		
		CREATE UNIQUE INDEX V2_SEQUENCER_NAME_IDX ON V2_SEQUENCER (NAME);		
       
		COMMENT ON COLUMN V2_SEQUENCER.SEQUENCER_ID      IS '시퀀스 아이디';                          
		COMMENT ON COLUMN V2_SEQUENCER.NAME              IS '시퀀스 이름';                      
		COMMENT ON COLUMN V2_SEQUENCER.VALUE             IS '시퀀스 값';    
		
		<!-- 4.I18N 텍스트 -->
		CREATE TABLE V2_I18N_TEXT (
		     TEXT_ID                   INTEGER NOT NULL,
		     NAME                      VARCHAR2(200)   NOT NULL,	     
		     TEXT                        VARCHAR2(2000)  NOT NULL,	
             CATEGORY_NAME     VARCHAR2(100)   NOT NULL,    
             LOCALE_CODE          VARCHAR2(100)   NOT NULL,
			 CREATION_DATE       DATE DEFAULT SYSDATE NOT NULL ,			 
		     MODIFIED_DATE       DATE DEFAULT SYSDATE NOT NULL ,
		     CONSTRAINT V2_I18N_TEXT_PK PRIMARY KEY (TEXT_ID)
		);				
		CREATE INDEX V2_I18N_TEXT_LOCALE_IDX ON V2_I18N_TEXT (LOCALE_CODE) ;
		CREATE UNIQUE INDEX V2_I18N_TEXT_NAME_IDX ON V2_I18N_TEXT (NAME, CATEGORY_NAME);	
		
		COMMENT ON TABLE "V2_I18N_TEXT"  IS 'I18N 지원을 위한 테이블';
		COMMENT ON COLUMN "V2_I18N_TEXT"."TEXT_ID" IS '국제화 텍스트 ID 값';	
		COMMENT ON COLUMN "V2_I18N_TEXT"."NAME" IS '국제화 텍스트 키 값';	
		COMMENT ON COLUMN "V2_I18N_TEXT"."TEXT" IS '국제화 텍스트 값';	
		COMMENT ON COLUMN "V2_I18N_TEXT"."CATEGORY_NAME" IS '카테고리 이름';	
		COMMENT ON COLUMN "V2_I18N_TEXT"."LOCALE_CODE" IS '로케일 코드 값';			
		COMMENT ON COLUMN "V2_I18N_TEXT"."CREATION_DATE" IS '생성일';	
		COMMENT ON COLUMN "V2_I18N_TEXT"."MODIFIED_DATE" IS '수정일';			
		
        