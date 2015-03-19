package architecture.ee.test;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import architecture.user.permission.Permissions;

public class SecurityTest {

	private static Log log = LogFactory.getLog("SecurityTest");
	/*
	public static final void main(String [] args){
		AdminHelper.getCache("userCache");
		log.debug("======================================");
		int i = 0 ;
		for(String name : AdminHelper.getComponentNames())
			log.debug( "[" + (i++) + "] " + name);		
		log.debug("======================================");
		
		
		//for( User user : AdminHelper.getUserManager().getApplicationUsers()){
		//	log.debug(user);
		//}
		
	}
	
	
	
	public void testGetUserManager(){		
		
		AdminHelper.getCache("USER");
		log.debug("======================================");
		for( User user : ApplicationHelper.getUserManager().getApplicationUsers()){
			log.debug(user);			
			
			List<Group> groups = ApplicationHelper.getGroupManager().getUserGroups(user);			
			log.debug(groups);
			
			List<Role> roles = ApplicationHelper.getRoleManager().getFinalUserRoles(user.getUserId());
			log.debug(roles);
		}
		
		
	}
	

	public void testPasswordEncoder(){

		//77d15fa93b2a3274c76126a3a10abd4d2823308a7018f75998918f7a64642a9a
		//6bbdf2b028e0a58dcadc17227bc2e8f7d53a8b50aaf5dd08655dbc5fd4681991
		String rawPass = "1234qwer";
		MessageDigestPasswordEncoder encoder = (MessageDigestPasswordEncoder) Bootstrap.getBootstrapComponent(PasswordEncoder.class);
		log.debug("encoding:" + encoder.getAlgorithm());
		//log.debug("1:" + encoder.encodePassword(rawPass, "inkium"));
		log.debug("2:" + encoder.encodePassword(rawPass, "andang"));
		//log.debug("3:" + encoder.encodePassword(rawPass, "tristan"));
		log.debug("valid :"+
		encoder.isPasswordValid("77d15fa93b2a3274c76126a3a10abd4d2823308a7018f75998918f7a64642a9a", rawPass, "andang")
		);
	}
	*/
	@Test
	public void testPasswordEncoder (){
		MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
		
		System.out.println(
				encoder.encodePassword("inkium01", null)
		);
		System.out.println(
				encoder.encodePassword("weham1", null)
		);		
	}
	
	@Test
	public void testPermissions(){
		
		Permissions perms1 = new Permissions(1L);
		Permissions perms2 = new Permissions(2L);
		Permissions perms3 = new Permissions(4L);
		Permissions perms4 = new Permissions(8L);
		Permissions perms5 = new Permissions(16L);
		Permissions perms6 = new Permissions(32L);
		Permissions perms7 = new Permissions(64L);
		Permissions perms8 = new Permissions(128L);
		Permissions perms9 = new Permissions(256L);
		
		//long i = 0x0000000000000000000000000000000010000000000000000000000000000001;
		
		//System.out.println(">>" + i );
		
		Permissions perms = new Permissions(0x000000000000000L);
		perms.set(perms1.value(), true);
		perms.set(perms2.value(), true);
		perms.set(perms3.value(), true);
		perms.set(perms6.value(), true);
		//perms.set(perms7.value(), true);
		perms.set(perms8.value(), true);
		perms.set(perms9.value(), true);
		
		log.debug(perms.hasPermission(perms4.value()));
		log.debug(perms.hasPermission(perms5.value()));
		log.debug(perms.hasPermission(perms7.value()));
		
		System.out.println(perms.value());
		
	}
}
