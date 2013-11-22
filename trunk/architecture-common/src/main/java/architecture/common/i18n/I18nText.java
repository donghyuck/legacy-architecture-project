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
package architecture.common.i18n;

import architecture.common.model.DateModelObject;

public interface I18nText  extends  DateModelObject {

	public abstract String getName();
	
	public abstract void setName(String name);
	
	/**
	 * @return textId
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 */
	public abstract void setTextId(long textId);
	
	/**
	 * @return localeCode
	 */
	public abstract String getLocaleCode();
	
	/**
	 * @param  localeCode
	 */
	public abstract void setLocaleCode(String localeCode);
	
	/**
	 * 
	 * @return text
	 */
	public abstract String getText();
	
	/**
	 * 
	 * @param  text
	 */
	public abstract void setText(String text);

	public abstract String getResourceBundleKey();
	
	public abstract String getCategoryName();
	
	public abstract  void setCategoryName(String categoryName);
		
}
