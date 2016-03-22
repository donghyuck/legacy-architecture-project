package architecture.ee.test;

import java.io.File;
import java.lang.reflect.Method;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ClassUtils;

import architecture.ee.util.ApplicationHelper;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;

public class GroovyTest {

    // @Test
    public void testLoadGroovyEngine() {
	try {

	    String uri = "C:/Users/donghyuck/workspace/architecture-ee/src/test/resources";
	    // "file:/C:/Users/donghyuck/workspace/architecture-example-default/WebContent/WEB-INF/groovy/"
	    // ;
	    org.apache.commons.vfs2.FileObject fo = architecture.common.util.vfs.VFSUtils.resolveFile(uri);
	    //
	    /*
	     * GroovyScriptEngine engine = new GroovyScriptEngine( new String[]{
	     * "C:/Users/donghyuck/workspace/architecture-ee/src/test/resources"
	     * } ); Binding binding = new Binding();
	     * binding.setVariable("input", "안녕"); engine.run( "hello.groovy",
	     * binding ); System.out.println(binding.getVariable("output"));
	     * engine.getGroovyClassLoader();
	     * 
	     */
	    // engine.getGroovyClassLoader().

	    // Class groovyClass =
	    // engine.getGroovyClassLoader().loadClass("groovy.GroovyCharSetTest");

	    GroovyCodeSource source = new GroovyCodeSource(new File(
		    "C:/Users/donghyuck/workspace/architecture-ee/src/test/resources/groovy/GroovyCharSetTest.groovy"));

	    groovy.util.GroovyScriptEngine engine = new groovy.util.GroovyScriptEngine(
		    new java.net.URL[] { fo.getURL() });

	    CompilerConfiguration config = CompilerConfiguration.DEFAULT;
	    config.setSourceEncoding("UTF-8");
	    config.setRecompileGroovySource(true);
	    config.setDebug(true);
	    engine.setConfig(config);

	    GroovyClassLoader groovyClassLoader = new GroovyClassLoader(ClassUtils.getDefaultClassLoader(), config);
	    groovyClassLoader.addURL(fo.getURL());
	    // .addClasspath(uri);

	    ClassLoader cl = (ClassLoader) groovyClassLoader;
	    for (int i = 0; i < 3; i++) {
		Thread.currentThread().sleep(5000);
		try {
		    /**
		     * Class groovyClass =
		     * engine.getGroovyClassLoader().parseClass( source, false);
		     */
		    Class groovyClass = groovyClassLoader.loadClass("groovy.GroovyCharSetTest", true, true);
		    // Class groovyClass =
		    // groovyClassLoader.loadClass("groovy.GroovyCharSetTest");

		    // engine.getGroovyClassLoader().setShouldRecompile(true);

		    System.out.println(engine.getGroovyClassLoader().isShouldRecompile());

		    GroovyObject go = (GroovyObject) groovyClass.newInstance();
		    go.invokeMethod("getCharSet", null);
		} catch (Exception e) {
		}

	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    // @Test
    public void testGroovyClassLoader() {
	try {
	    String uri = "C:/Users/donghyuck/workspace/architecture-example-default/WebContent/WEB-INF/groovy";
	    org.apache.commons.vfs2.FileObject fo = architecture.common.util.vfs.VFSUtils.resolveFile(uri);

	    CompilerConfiguration config = CompilerConfiguration.DEFAULT;
	    config.setSourceEncoding("UTF-8");
	    config.setRecompileGroovySource(true);
	    config.setDebug(true);

	    GroovyClassLoader groovyClassLoader = new GroovyClassLoader(ClassUtils.getDefaultClassLoader(), config);
	    // groovyClassLoader.addURL(fo.getURL());
	    groovyClassLoader.addClasspath(fo.getURL().getFile());

	    for (int i = 0; i < 30; i++) {
		Thread.currentThread().sleep(5000);
		try {
		    Class groovyClass = groovyClassLoader.loadClass("unitofwork.SqlQueryTest", true, false);
		    GroovyObject go = (GroovyObject) groovyClass.newInstance();
		    go.invokeMethod("getTableNames", "");
		} catch (Exception e) {
		    // TODO 자동 생성된 catch 블록
		    e.printStackTrace();
		}
	    }
	} catch (Exception e) {
	    // TODO 자동 생성된 catch 블록
	    e.printStackTrace();
	}

    }

    // @Test
    public void testLoadGroovyEngine2() {
	try {
	    // C:/Users/donghyuck/workspace/architecture-example-default/WebContent/WEB-INF/groovy
	    GroovyScriptEngine engine = new GroovyScriptEngine(
		    new String[] { "C:/Users/donghyuck/workspace/architecture-ee/src/test/resources/groovy" });
	    /*
	     * FileObject fo = VFSUtils.resolveFile(
	     * "res://groovy.GroovyCharSetTest" ); URL url = fo.getURL();
	     */

	    Class groovyClass = engine.getGroovyClassLoader().loadClass("groovy.GroovyCharSetTest");

	    SpringTransactionAnnotationParser parser = new SpringTransactionAnnotationParser();
	    Object obj = null;
	    Method[] methods = groovyClass.getMethods();
	    for (Method method : methods) {

		if (method.isAnnotationPresent(Transactional.class)) {
		    final String methodName = method.getName();
		    final GroovyObject go = (GroovyObject) groovyClass.newInstance();
		    Transactional ann = method.getAnnotation(Transactional.class);
		    TransactionAttribute transactionAttribute = parser.parseTransactionAnnotation(ann);
		    PlatformTransactionManager transactionManager = ApplicationHelper
			    .getComponent(PlatformTransactionManager.class);
		    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager,
			    transactionAttribute);
		    obj = transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
			    return go.invokeMethod(methodName, null);
			}
		    });
		}
	    }

	    // MethodInvoker inv = new MethodInvoker();

	    // inv.setTargetObject(test);
	    // inv.setTargetMethod("getCharSet");
	    System.out.println(obj);

	    /*
	     * Map map = (Map)go.invokeMethod("getCharSet", null);
	     * 
	     * Iterator iterator = map.keySet().iterator(); while
	     * (iterator.hasNext()) { String key = (String) iterator.next(); }
	     */

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }
}
