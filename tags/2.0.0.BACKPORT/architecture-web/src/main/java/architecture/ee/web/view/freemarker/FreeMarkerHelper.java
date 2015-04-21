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
package architecture.ee.web.view.freemarker;

import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class FreeMarkerHelper {

	public FreeMarkerHelper() {
		// TODO 자동 생성된 생성자 스텁
	}
	
	/**
	 * @param args
	 */
	public static void populateStatics(BeansWrapper wrapper, Map<String, Object> model){
		
		try {
			TemplateHashModel enumModels = wrapper.getEnumModels();
			try {

			} catch (Exception e) {
				
			}
			model.put("enums", wrapper.getEnumModels());
		} catch (UnsupportedOperationException e) {
		}

		TemplateHashModel staticModels = wrapper.getStaticModels();
		try {
			model.put("TextUtils",						staticModels.get("architecture.common.util.TextUtils"));
			model.put("StringUtils",						staticModels.get("architecture.common.util.StringUtils"));
			model.put("L10NUtils",						staticModels.get("architecture.common.util.L10NUtils"));
			model.put("LocaleUtils",					staticModels.get("architecture.ee.web.util.LocaleUtils"));
			model.put("HtmlUtils",						staticModels.get("architecture.ee.web.util.HtmlUtils"));
			model.put("ServletUtils",					staticModels.get("architecture.ee.web.util.ServletUtils"));
			model.put("ParamUtils",					staticModels.get("architecture.ee.web.util.ParamUtils"));
			model.put("WebSiteUtils",					staticModels.get("architecture.ee.web.util.WebSiteUtils"));
			model.put("CompanyUtils",				staticModels.get("architecture.user.util.CompanyUtils"));
			model.put("SecurityHelper",				staticModels.get("architecture.common.user.SecurityHelper"));
			model.put("ApplicationHelper",			staticModels.get("architecture.ee.util.ApplicationHelper"));							
			model.put("WebApplicationHelper",	staticModels.get("architecture.ee.web.util.WebApplicationHelper"));
		} catch (TemplateModelException e) {
			
		}	
		
		model.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
		
	}
}
