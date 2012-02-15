package architecture.ee.web;

import java.util.Properties;

import org.junit.Test;

import architecture.common.util.ClassUtils;
import architecture.ee.web.model.impl.ThemeModelImpl;
import architecture.ee.web.theme.Theme;
import architecture.ee.web.theme.ThemeXStreamFactory;

import com.thoughtworks.xstream.XStream;


public class ThemeTest {

	@Test
	public void testToXml(){
		
		XStream xstream = ThemeXStreamFactory.getThemeXStream();
		ThemeModelImpl impl = new ThemeModelImpl("custom", "새마을 금고 테마", new Properties());
		System.out.println(xstream.toXML(impl));
		
		
	}

	@Test
	public void testToTheme(){
		
		XStream xstream = ThemeXStreamFactory.getThemeXStream();
		Theme theme = (Theme)xstream.fromXML(ClassUtils.getResourceAsStream("theme.xml"));
		
		System.out.println(theme.getName());
		
		
	}
}
