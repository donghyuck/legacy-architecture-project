package architecture.common;

import org.junit.Test;

import architecture.common.util.PlatformHelper;

public class PlatformTest {

	@Test
	public void testOSInfo() {		
		System.out.println( PlatformHelper.getName() );
		System.out.println( PlatformHelper.getVersionNumber() );
		System.out.println( PlatformHelper.currentPlatform() );
	}

}
