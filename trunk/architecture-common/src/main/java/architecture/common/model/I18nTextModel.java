/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.common.model;

import architecture.common.i18n.I18nText;

public interface I18nTextModel extends ModelObject<I18nText> {
	
	
	public abstract String getResourceBundleKey();
	
	/**
	 * @return textId
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 */
	public abstract void setTextId(long textId);
	
	public abstract int getObjectType();
	
	public abstract void setObjectType(int objectType);
	
	public abstract long getObjectId();
	
	public abstract void setObjectId(long objectId);
	
	public abstract int getObjectAttribute();
	
	public abstract void setObjectAttribute(int objectAttribute);
	
	
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

}
