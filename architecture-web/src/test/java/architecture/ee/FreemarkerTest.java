/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerTest {

	public FreemarkerTest() {

	}

	@Test
	public void testTemplate() throws IOException, TemplateException{
		StringWriter out = new StringWriter();
		Template t = getConfiguration().getTemplate("test.ftl");
		t.process(getModel(), out);
		
		System.out.print(out.toString());
	}
	
	private Configuration getConfiguration() throws IOException {
		Configuration cfg = new Configuration();
		//cfg.setDirectoryForTemplateLoading(new File());
		cfg.setClassForTemplateLoading(this.getClass(), "/");		
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);  

		return cfg;		
	}
	
	private Map getModel(){
		Map root = new HashMap();		
		root.put("msg", "hello");
		return root;
	}
}
