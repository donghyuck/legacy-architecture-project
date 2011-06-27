package architecture.ee.test;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.AdminService;
import architecture.ee.model.internal.GroupModelImpl;
import architecture.ee.security.dao.RoleDao;
import architecture.ee.user.Group;
import architecture.ee.user.User;
import architecture.ee.user.dao.GroupDao;
import architecture.ee.user.dao.UserDao;
import architecture.ee.util.AdminHelper;


public class TestGroup {
private Log log = LogFactory.getLog(getClass());
@Test
public void test(){
	
	
	
}
	//@Test
	public void boot() {		
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
	public void createGroup(){
		
		GroupDao gDao = (GroupDao)AdminHelper.getComponent(GroupDao.class);		
		try{
			gDao.getGroupByName("Administrators", true);
		}catch(EmptyResultDataAccessException e){
			GroupModelImpl model = new GroupModelImpl();
			model.setName("Administrators");
			model.setDescription("시스템 관리자 그룹");
			gDao.createGroup(model);			
		}
	}
	
	//@Test
	public void addGroupMember(){
		
		UserDao uDao = AdminHelper.getComponent(UserDao.class);		
		GroupDao gDao = AdminHelper.getComponent(GroupDao.class);	
		
		Group g = gDao.getGroupByName("Administrators", true);
		
		User u = uDao.getUserByUsername("andang");		
		
		if(!g.isMember(u))
			gDao.addMember(g.getGroupId(), u.getUserId());
		
	}

	//@Test
	public void addGroupAdministrator(){		
		UserDao uDao = AdminHelper.getComponent(UserDao.class);		
		GroupDao gDao = AdminHelper.getComponent(GroupDao.class);			
		Group g = gDao.getGroupByName("Administrators", true);		
		User u = uDao.getUserByUsername("andang");				
		if(!g.isAdministrator(u))
			gDao.addAdministrator(g.getGroupId(), u.getUserId());
		
	}

	//@Test
	public void addGroupRoles(){		
		
		UserDao uDao = AdminHelper.getComponent(UserDao.class);		
		GroupDao gDao = AdminHelper.getComponent(GroupDao.class);			
		RoleDao rDao = AdminHelper.getComponent(RoleDao.class);			
		
		User u = uDao.getUserByUsername("andang");	
		List<Long> groups = gDao.getUserGroupIds(u.getUserId());
		for(Long groupId : groups){
			log.debug("****groupId=" + groupId );
			rDao.getGroupRoleIds(groupId);
			
		}
		
		List<Long> roles = rDao.getUserRoleIds(u.getUserId());		
	
	}
	
	//@Test
	public void getUserMembership(){		
		UserDao uDao = AdminHelper.getComponent(UserDao.class);		
		GroupDao gDao = AdminHelper.getComponent(GroupDao.class);			
		
		User u = uDao.getUserByUsername("andang");	
		List<Long> l = gDao.getUserGroupIds(u.getPrimaryKey());
		for(Long groupId : l){
			Group g = gDao.getGroupById(groupId);
			log.debug("group:" + g.getName());
		}
		
		gDao.deleteUserMemberships(u.getUserId());
		
	}
}
