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
package architecture.common.i18n.impl;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.i18n.I18nText;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.DateModelObjectSupport;
import architecture.common.util.I18nTextUtils;

public class I18nTextImpl extends DateModelObjectSupport implements I18nText {

    private long textId = -1L;
    private String name;
    private String text;
    private String categoryName;
    private String localeCode;

    public long getTextId() {
	return textId;
    }

    public void setTextId(long textId) {
	this.textId = textId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

    public String getLocaleCode() {
	return localeCode;
    }

    public void setLocaleCode(String localeCode) {
	this.localeCode = localeCode;
    }

    public Serializable getPrimaryKeyObject() {
	return getTextId();
    }

    public int getModelObjectType() {
	return ModelTypeFactory.getTypeIdFromCode(".I18N_TEXT");
    }

    public int getCachedSize() {
	return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName()) + CacheSizes.sizeOfString(this.categoryName)
		+ CacheSizes.sizeOfString(getLocaleCode()) + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
    }

    public String getResourceBundleKey() {
	return I18nTextUtils.generateResourceBundleKey(categoryName, name);
    }

}
