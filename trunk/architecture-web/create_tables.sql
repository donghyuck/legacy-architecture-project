
	    -- =================================================  
		--  IMAGE	
		-- =================================================		
		CREATE TABLE V2_IMAGE (
			  IMAGE_ID                    INTEGER NOT NULL,
			  OBJECT_TYPE                INTEGER NOT NULL,
			  OBJECT_ID                   INTEGER NOT NULL,
			  FILE_NAME                   VARCHAR2(255)   NOT NULL,
			  FILE_SIZE                      INTEGER   NOT NULL,
			  CONTENT_TYPE             VARCHAR2(50)  NOT NULL,			  
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
		