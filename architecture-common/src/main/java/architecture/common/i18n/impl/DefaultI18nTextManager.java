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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.i18n.I18nResourceBundle;
import architecture.common.i18n.I18nText;
import architecture.common.i18n.I18nTextManager;
import architecture.common.i18n.dao.I18nTextDao;
import architecture.common.util.LocaleUtils;
import architecture.common.util.StringUtils;

public class DefaultI18nTextManager implements I18nTextManager {

    private Log log = LogFactory.getLog(getClass());
    private I18nTextDao i18nTextDao;
    private Map<Locale, ResourceBundle> resourceBundles;

    public I18nTextDao getI18nTextDao() {
	return i18nTextDao;
    }

    public void setI18nTextDao(I18nTextDao i18nTextDao) {
	this.i18nTextDao = i18nTextDao;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveTexts(List<I18nText> list) {

	List<I18nText> textsToCreate = new ArrayList<I18nText>();
	List<I18nText> textsToUpdate = new ArrayList<I18nText>();
	List<I18nText> textsToDelete = new ArrayList<I18nText>();

	for (I18nText text : list) {
	    if (StringUtils.isEmpty(text.getText())) {
		if (text.getTextId() != -1L)
		    textsToDelete.add(text);
	    } else if (text.getTextId() == -1L) {
		textsToCreate.add(text);
	    } else {
		textsToUpdate.add(text);
	    }
	}

	i18nTextDao.createTexts(textsToCreate);
	i18nTextDao.updateTexts(textsToUpdate);
	i18nTextDao.deleteTexts(textsToDelete);
	reloadResourceBundes();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveTexts(List<I18nText> list, String categoryName) {
	for (I18nText text : list) {
	    if (text.getTextId() == -1L) {
		text.setCategoryName(categoryName);
	    }
	}
	saveTexts(list);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void deleteTexts(List<I18nText> list) {
	i18nTextDao.deleteTexts(list);
    }

    public I18nText getText(long textId) {
	return i18nTextDao.getText(textId);
    }

    public List<I18nText> getTexts() {
	return i18nTextDao.getTexts();
    }

    public List<I18nText> getTexts(String categoryName, String localeCode) {
	return i18nTextDao.getTexts(categoryName, localeCode);
    }

    public Map<Locale, ResourceBundle> getResourceBundles() {
	return resourceBundles;
    }

    protected void reloadResourceBundes() {
	reloadResourceBundes(true);
    }

    protected void reloadResourceBundes(boolean sendClusterTask) {
	loadResourceBundles();
    }

    protected void loadResourceBundles() {

	log.debug("loading resource bundles from database. ");

	List<I18nText> texts = i18nTextDao.getTexts();
	Map<Locale, List<String[]>> allKeyValues = new HashMap<Locale, List<String[]>>();

	for (I18nText text : texts) {
	    List<String[]> keyValues = allKeyValues.get(LocaleUtils.localeCodeToLocale(text.getLocaleCode()));
	    // 만일 keyValues 가 널이라면 값이 없는 배열 값으로 생성한다.
	    if (keyValues == null) {
		keyValues = new ArrayList<String[]>();
		allKeyValues.put(LocaleUtils.localeCodeToLocale(text.getLocaleCode()), keyValues);
	    }
	    keyValues.add(new String[] { text.getResourceBundleKey(), text.getText() });
	}

	HashMap<Locale, ResourceBundle> newResourceBundles = new HashMap<Locale, ResourceBundle>();
	for (Locale locale : allKeyValues.keySet()) {
	    log.debug(locale + " : " + allKeyValues);
	    newResourceBundles.put(locale, new I18nResourceBundle(locale, allKeyValues.get(locale)));
	}
	this.resourceBundles = newResourceBundles;
    }

    public void refresh() {
	reloadResourceBundes();
    }

}