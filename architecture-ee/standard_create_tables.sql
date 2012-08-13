

		
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
		
		
        