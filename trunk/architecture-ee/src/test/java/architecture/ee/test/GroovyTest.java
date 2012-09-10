package architecture.ee.test;

import groovy.util.GroovyScriptEngine;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.junit.Test;
import org.springframework.util.MethodInvoker;

import architecture.common.util.vfs.VFSUtils;

public class GroovyTest {

	
	@Test
	public void testLoadGroovyEngine(){
		try {
			
/*			GroovyScriptEngine engine = new GroovyScriptEngine( new String[]{ "C:/Users/donghyuck/workspace/architecture-ee/src/test/resources" } );
			Binding binding = new Binding();
			binding.setVariable("input", "안녕");
			engine.run( "hello.groovy", binding );
			System.out.println(binding.getVariable("output"));
			engine.getGroovyClassLoader();
			
			
			engine.getGroovyClassLoader();*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testLoadGroovyEngine2(){
		try {
			
			GroovyScriptEngine engine = new GroovyScriptEngine( new String[]{ "C:/Users/donghyuck/workspace/architecture-ee/src/test/resources/groovy" } );
/*			FileObject fo = VFSUtils.resolveFile( "res://groovy.GroovyCharSetTest" );
			URL url = fo.getURL();*/
			
			Class classFile = engine.getGroovyClassLoader().loadClass("groovy.GroovyCharSetTest");
			
			System.out.println("-1--");
			
			Object test = classFile.newInstance();
			
			System.out.println("-2--");
						
			MethodInvoker inv = new MethodInvoker();
			
			inv.setTargetObject(test);
			inv.setTargetMethod("getCharSet");	
			 
			
/*			System.out.println("-3--");
			inv.prepare();
			System.out.println("-4--");
			 Map map = (Map)inv.invoke();
			 
			 System.out.println("-5--");
			 
             Iterator iterator = map.keySet().iterator();
             while (iterator.hasNext()) {
                     String key = (String) iterator.next();
             }*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
