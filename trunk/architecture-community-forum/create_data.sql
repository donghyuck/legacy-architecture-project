
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (61,'BOARD',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (62,'THREAD',1);



INSERT INTO V2_BOARD
    (BOARD_ID, 
    OBJECT_TYPE, 
    OBJECT_ID, 
    BOARD_NAME, 
    BOARD_DESC, 
    COMMENT_YN, 
    FILE_YN, 
    ANONY_YN, 
    USE_YN, 
    CREATION_DATE, 
    CREATE_ID, 
    THREAD_SIZE,
    TOTAL_CNT
    )
    VALUES
    (1,
     30,
     1,
     '인키움 뉴스게시판입니다.',
     '인키움 뉴스게시판입니다.',
     1,
     1,
     0,
     1,
     SYSDATE,
     1,
     10,
     1
     )
     ;