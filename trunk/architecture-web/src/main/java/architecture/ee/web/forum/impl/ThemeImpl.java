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
package architecture.ee.web.forum.impl;

import java.io.Serializable;

import architecture.common.model.ModelObjectType;
import architecture.common.model.support.EntityModelObjectSupport;
import architecture.ee.web.theme.Theme;

public class ThemeImpl extends EntityModelObjectSupport  implements Theme {
	
	private Long themeId = -1L;
	
	private String styleName ;
	
	private String templateName;
	
	/**
	 * @return themeId
	 */
	public Long getThemeId() {
		return themeId;
	}
	/**
	 * @param themeId 설정할 themeId
	 */
	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}
	/**
	 * @return styleName
	 */
	public String getStyleName() {
		return styleName;
	}
	/**
	 * @param styleName 설정할 styleName
	 */
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	/**
	 * @return templateName
	 */
	public String getTemplateName() {
		return templateName;
	}
	/**
	 * @param templateName 설정할 templateName
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public Serializable getPrimaryKeyObject() {
		return themeId;
	}
	
	public ModelObjectType getModelObjectType() {
		return ModelObjectType.FORUM_THEME;
	}
	
	public int getCachedSize() {
		return 0;
	}

}
