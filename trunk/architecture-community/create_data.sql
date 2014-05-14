
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (1,'COMPANY',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (2,'USER',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (3,'GROUP',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (4,'ROLE',7);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (16,'IMAGE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (17,'ATTACHMENT',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (18,'MENU',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (19,'SOCIAL_NETWORK',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (22,'ANNOUNCE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (23,'PROFILE_IMAGE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (30,'WEBSITE',2);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (31,'PAGE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (32,'PAGE_BODY',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (33,'LOGO_IMAGE',1);

Insert into V2_COMPANY (COMPANY_ID,DISPLAY_NAME,NAME,DESCRIPTION,DOMAIN_NAME) values (1, '메르디앙', 'meridian', '또다른 인생을 위하여..', null);

Insert into V2_USER (COMPANY_ID,USER_ID,USERNAME,PASSWORD_HASH,NAME,NAME_VISIBLE,FIRST_NAME,LAST_NAME,EMAIL,EMAIL_VISIBLE,USER_ENABLED,VISIBLE,IS_EXTERNAL,FEDERATED,STATUS) 
values (1, 1, 'system','b524ac69d3f8516d3f90679d983cc797b91ddd2daa85f97696722e3e4701275b','운영자',1,null,null,'system@demo.com',1,1,1,0,0,0);

Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (1,'ROLE_USER','사용자 ROLE',0);
Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (2,'ROLE_SYSTEM','시스템 ROLE',0);
Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (3,'ROLE_ADMIN','관리자 ROLE',0);
Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (4,'ROLE_GROUP_ADMIN','시스템 ROLE',0);
Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (5,'ROLE_USER_ADMIN','시스템 ROLE',0);
Insert into V2_ROLE (ROLE_ID,NAME,DESCRIPTION,MASK) values (6,'ROLE_SITE_ADMIN','시스템 ROLE',0);


insert into V2_USER_ROLES ( USER_ID,  ROLE_ID) values ( 1, 2);
insert into V2_USER_ROLES ( USER_ID,  ROLE_ID) values ( 1, 3);

Insert into V2_MENU (MENU_ID,NAME,TITLE,LOCATION,MENU_ENABLED) values (1,'PUBLIC_MENU','디폴트 메뉴',null, 1 );

Insert into V2_WEBSITE (WEBSITE_ID,NAME,DESCRIPTION,DISPLAY_NAME,URL,PUBLIC_SHARED,ENABLED,COMPANY_ID,USER_ID) 
values (1,'MAIN_WEBSITE','인키움 홈페이지','인키움','222.122.63.147',1,1,1,1);


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