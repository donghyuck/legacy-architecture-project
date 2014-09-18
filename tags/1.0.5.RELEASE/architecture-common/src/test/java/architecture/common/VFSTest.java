package architecture.common;

import org.apache.commons.vfs2.FileObject;
import org.junit.Test;

import architecture.common.util.vfs.VFSUtils;

public class VFSTest {

	
	@Test
	public void testResolveFile(){
		FileObject fo = VFSUtils.resolveFile("file:///C:/TOOLS");
		try {
			System.out.println( fo.exists() ) ;
			System.out.println( VFSUtils.isFile(fo) );
		} catch (Exception e) {
		}
	}
}
