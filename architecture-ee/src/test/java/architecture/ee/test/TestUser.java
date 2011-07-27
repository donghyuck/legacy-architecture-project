package architecture.ee.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.AdminService;
import architecture.ee.g11n.I18nText;
import architecture.ee.g11n.dao.I18nTextDao;
import architecture.ee.model.UserModel.Status;
import architecture.ee.model.internal.ProfileFieldModelImpl;
import architecture.ee.model.internal.ProfileFieldValueModelImpl;
import architecture.ee.user.User;
import architecture.ee.user.UserTemplate;
import architecture.ee.user.dao.ProfileFieldDao;
import architecture.ee.user.dao.UserDao;
import architecture.ee.user.dao.UserProfileDao;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldValue;
import architecture.ee.util.AdminHelper;


public class TestUser {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void test(){
		
		
		
	}
	
	//@Test
	public void testBoot() {		
		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml,databaseSubsystemContext.xml,daoSubsystemContext.xml"
		);
				
		servletContext.addInitParameter("RUNTIME_APPLICATION_HOME", "C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default");
		
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log.debug(admin.getState());
			admin.start();
		}	
		/*
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	//@Test
	public void getUserDao(){
		
		UserDao dao = AdminHelper.getComponent(UserDao.class);
		int size = dao.getTotalUserCount();
		log.debug("getTotalUserCount=" + size);
		if(size == 0){
			
			UserTemplate template = new UserTemplate();
			template.setUsername("andang");
			template.setName("안당");		
			template.setPasswordHash("1111");
			template.setEmail("andang311@inkium.com");
			Date now = new Date();
			template.setCreationDate(now);
			template.setModificationDate(now);
			template.setStatus(Status.registered)
			;
			template.setLastLoggedIn(now);
			template.setLastProfileUpdate(now);
			
			template.setProperties(new HashMap());
			dao.create(template);
		}
		
    
		log.debug(new Date(-1L));
	
	}
	
	//@Test
	public void findUserByUsername(){
		UserDao dao = AdminHelper.getComponent(UserDao.class);
		User user = dao.getUserByUsername("andang");
		
		
		
		log.debug(user);		
	}

	//@Test
	public void testProfileField(){
		Locale locale = AdminHelper.getLocale();
		I18nTextDao textDao = AdminHelper.getComponent(I18nTextDao.class);
		ProfileFieldDao dao = AdminHelper.getComponent(ProfileFieldDao.class);
		List<ProfileField> list = dao.getProfileFields();
		for(ProfileField f : list){			
			f = dao.getProfileField(f.getFieldId());			
			log.debug("profiel :" + f );			
			String displayName = f.getDisplayName(locale.toString());			
			log.debug(" ================================= " + StringUtils.isEmpty(displayName) );
			
			if( StringUtils.isEmpty(displayName)){
				if( "Company".equals( f.getName())){
					displayName = "회사";
				} else if( "Position".equals( f.getName())){
					displayName = "직위";
				} else if( "Homepage".equals( f.getName())){
					displayName = "홈페이지";
				} else if( "Telephone".equals( f.getName())){
					displayName = "전화";
				}
				log.debug(f.getName() +" ================================= " + displayName);
				f.setDisplayName(locale.toString(), displayName );
			}
			
			for( I18nText t : (List<I18nText>)f.getProfileFieldText()){
				t.setObjectType(500);
				t.setObjectId(f.getFieldId());
				//Date now = new Date(System.currentTimeMillis());
				//t.setCreationDate(now);
				//t.setModifiedDate(now);				
			}
			textDao.createTexts(f.getProfileFieldText());
		}
        		
		if(list.size() == 0){
			ProfileFieldModelImpl field = new ProfileFieldModelImpl();
			field.setDefaultField(true);
			field.setEditable(true);
			field.setExternallyManaged(false);
			field.setFieldTypeId(ProfileField.Type.TEXT.getId());
			field.setFilterable(false);
			field.setName("Position");
			field.setListValues(false);
			field.setRequired(false);
			field.setSearchable(false);
			field.setVisible(true);		
			dao.createProfileField((ProfileField)field);
		}
	}
	
	//@Test
	public void getUserProfile(){
		
		Locale locale = AdminHelper.getLocale();
		UserDao dao1 = AdminHelper.getComponent(UserDao.class);
		UserProfileDao dao2 = AdminHelper.getComponent(UserProfileDao.class);
		ProfileFieldDao dao3 = AdminHelper.getComponent(ProfileFieldDao.class);
				
		User user = dao1.getUserByUsername("andang");
		
		log.debug("locale=" + locale.toString() );
		
		
		List<ProfileField> list = dao3.getProfileFields();
		List<ProfileFieldValue> values = new ArrayList<ProfileFieldValue>();
		for(ProfileField f : list ){
			
			
			f = dao3.getProfileField(f.getFieldId());
			log.debug(f.getName() + ": visible=" + f.isVisible() + ", editable=" + f.isEditable() );
					
			if(f.isVisible() && f.isEditable()){
				
				ProfileFieldValue v = new ProfileFieldValueModelImpl(f.getFieldId(), f.getFieldTypeId());
				v.setList(false);
				
				if( "Company".equals( f.getName())){
					v.setValue("(주)인키움");
				} else if( "Position".equals( f.getName())){
					v.setValue("수석");
				} else if( "Homepage".equals( f.getName())){
					v.setValue("www.inkium.com");
				} else if( "Telephone".equals( f.getName())){
					v.setValue("02-2081-1071");
				}	
				values.add(v);
			}
		}
		
		log.debug(values);
		
		dao2.setProfile(user, values);
		
		Map<Long, ProfileFieldValue> profile = dao2.getProfile(user);		
		
		
		log.debug(profile);
		
	}
}
