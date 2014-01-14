package architecture.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.util.I18nTextUtils;

public class I18nTextText {

	@Test
	public void testGenerateResourceBundleKey(){
		String str = I18nTextUtils.generateResourceBundleKey(ModelTypeFactory.getTypeIdFromCode("USER"), -1L, -1);
		System.out.println(str);
		System.out.println(
				I18nTextUtils.generateResourceBundleKey(ModelTypeFactory.getTypeIdFromCode("USER"), 3469, 1)
		);
		System.out.println(
				I18nTextUtils.generateResourceBundleKey(ModelTypeFactory.getTypeIdFromCode("USER"), 3469, 20)
		);
	}
	
	@Test
	public void testListMap(){
		
		Map<String, Map> map = new HashMap<String, Map>();
		Map map2 = new HashMap();


		map.put("1", map2);
		
		System.out.println(map2);
		
		map2.put("a", "b");
		map.get("1").put("b", "c");
		System.out.println(map.get("1"));
		
	}
	
	@Test
	public void testLocale(){
	    for ( Locale locale : Locale.getAvailableLocales() )	{
	    	
	    	System.out.println(   locale.getLanguage()  );
	    	
	    }
	}
	
}
