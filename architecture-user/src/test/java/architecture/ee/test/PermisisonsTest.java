package architecture.ee.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import architecture.user.permission.DefaultPermissionsManager;
import architecture.user.permission.Permissions;

public class PermisisonsTest {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Test
	public void testPermissions (){
		
		System.out.println(
				"SYSTEM_ADMINISTRATION :" + DefaultPermissionsManager.toDbFormat(Permissions.SYSTEM_ADMINISTRATION)
		);	
		System.out.println(
				"HOSTED_ADMINISTRATION :" + DefaultPermissionsManager.toDbFormat(Permissions.HOSTED_ADMINISTRATION)
		);		
		System.out.println(
				"USER_ADMINISTRATION :" + DefaultPermissionsManager.toDbFormat(Permissions.USER_ADMINISTRATION)
		);		
		System.out.println(
				"GROUP_ADMINISTRATION :" + DefaultPermissionsManager.toDbFormat(Permissions.GROUP_ADMINISTRATION)
		);			
		System.out.println(
				"READ_DOCUMENT :" + DefaultPermissionsManager.toDbFormat(Permissions.READ_DOCUMENT)
		);		
		
		System.out.println(
				"WEBSITE_ADMIN 2 :" + DefaultPermissionsManager.fromDbFormat(9)
		);		
		
		System.out.println( "user:" + (Permissions.READ_DOCUMENT & 512L) );
	}
	
	@Test 
	public void testPermissionsLevel(){
		Permissions system = new Permissions( Permissions.SYSTEM_ADMINISTRATION );
		Permissions hosted = new Permissions( Permissions.HOSTED_ADMINISTRATION );
		Permissions group = new Permissions( Permissions.GROUP_ADMINISTRATION );
		Permissions user = new Permissions( Permissions.USER_ADMINISTRATION );
		
		System.out.println( "system:" + system.hasPermission(system.value()));
		System.out.println( "hosted:" + system.hasPermission(hosted.value()));
		System.out.println( "group:"+ system.hasPermission(group.value()));
		System.out.println( "user:" + system.hasPermission(user.value()));
	
		
		System.out.println("web admin perms");
		System.out.println( "system:" + system.hasPermission(Permissions.WEBSITE_ADMIN));
		System.out.println( "hosted:" + system.hasPermission(Permissions.WEBSITE_ADMIN));
		System.out.println( "group:"+ system.hasPermission(Permissions.WEBSITE_ADMIN));
		System.out.println( "user:" + system.hasPermission(Permissions.WEBSITE_ADMIN));		
		
		Permissions admin = new Permissions( Permissions.WEBSITE_ADMIN );
		System.out.println( "admin:" + system.hasPermission(Permissions.CREATE_ANNOUNCEMENT));		
		
	}
}
