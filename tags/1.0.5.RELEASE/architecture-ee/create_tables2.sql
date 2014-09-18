CREATE TABLE V2_ZIPCODE (
		    ZIPCODE_ID             INTEGER NOT NULL,
		    ZIPCODE                CHAR(7),
		    SIDO                   VARCHAR2(50), 
		    GUGUN                  VARCHAR2(100),  
		    DONG                   VARCHAR2(200),
		    BUNJI		           VARCHAR2(100),
		    CONSTRAINT V2_ZIPCODE_PK PRIMARY KEY (ZIPCODE_ID)		    
		);
		

CREATE TABLE V2_ZIPCODE_STREET (
	ZIPCODE              VARCHAR2(6)  NOT NULL,     
	ZIPSEQ               INTEGER      DEFAULT NULL, 
	SIDO                 VARCHAR2(21) DEFAULT NULL, 
	GUGUN                VARCHAR2(25) DEFAULT NULL, 
	EUPMYUN              VARCHAR2(12) DEFAULT NULL, 
	STREETCODE           VARCHAR2(12) DEFAULT NULL, 
	STREET               VARCHAR2(30) DEFAULT NULL, 
	ISUNDER              CHAR(1)      DEFAULT NULL, 
	BUILDINGNUM1         INTEGER      DEFAULT NULL, 
	BUILDINGNUM2         INTEGER      DEFAULT NULL, 
	BUILDING             VARCHAR2(60) DEFAULT NULL, 
	DONGCODE             VARCHAR2(10) DEFAULT NULL, 
	DONG                 VARCHAR2(16) DEFAULT NULL, 
	BLDGCODE             VARCHAR2(26) NOT NULL,     
	CONSTRAINT V2_ZIPCODE_STREET_PK PRIMARY KEY (BLDGCODE)
);
                                
COMMENT ON COLUMN V2_ZIPCODE_STREET.ZIPCODE      IS '우편번호';                          
COMMENT ON COLUMN V2_ZIPCODE_STREET.ZIPSEQ       IS '우편일련번호';                      
COMMENT ON COLUMN V2_ZIPCODE_STREET.SIDO         IS '시도';                              
COMMENT ON COLUMN V2_ZIPCODE_STREET.GUGUN        IS '시군구';                            
COMMENT ON COLUMN V2_ZIPCODE_STREET.EUPMYUN      IS '읍면';                              
COMMENT ON COLUMN V2_ZIPCODE_STREET.STREETCODE   IS '도로명코드';                        
COMMENT ON COLUMN V2_ZIPCODE_STREET.STREET       IS '도로명';                            
COMMENT ON COLUMN V2_ZIPCODE_STREET.ISUNDER      IS '지하여부(1,0)';                     
COMMENT ON COLUMN V2_ZIPCODE_STREET.BUILDINGNUM1 IS '건물번호본번';                      
COMMENT ON COLUMN V2_ZIPCODE_STREET.BUILDINGNUM2 IS '건물번호부번';                      
COMMENT ON COLUMN V2_ZIPCODE_STREET.BUILDING     IS '다량배달처명(건물명)';              
COMMENT ON COLUMN V2_ZIPCODE_STREET.DONGCODE     IS '법정동코드';                        
COMMENT ON COLUMN V2_ZIPCODE_STREET.DONG         IS '법정동명';                          
COMMENT ON COLUMN V2_ZIPCODE_STREET.BLDGCODE     IS '건물관리번호';                      
  

CREATE UNIQUE INDEX V2_ZIPCODE_STREET_ZIPCODE_IDX ON V2_ZIPCODE_STREET (ZIPCODE);