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
package architecture.common.i18n;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Set;

public class I18nResourceBundle extends ListResourceBundle {

    /**
     */
    private Object contents[][];
    /**
     */
    private Locale locale;
    private String baseName;
    public final Set<String> keySet;

    public I18nResourceBundle(String baseName, Locale locale, List<String[]> keyValues) {

	this.baseName = baseName;
	this.locale = locale;
	this.contents = new Object[keyValues.size()][2];
	Set<String> tempKeySet = new HashSet<String>();
	int index = 0;
	for (String[] keyValue : keyValues) {
	    this.contents[index][0] = keyValue[0];
	    tempKeySet.add(keyValue[0]);
	    this.contents[index][1] = keyValue[1];
	    index++;
	}
	this.keySet = Collections.unmodifiableSet(tempKeySet);

    }

    public String getBaseName() {
	return baseName;
    }

    public I18nResourceBundle(Locale locale, List<String[]> keyValues) {
	this.locale = locale;
	this.contents = new Object[keyValues.size()][2];
	Set<String> tempKeySet = new HashSet<String>();
	int index = 0;
	for (String[] keyValue : keyValues) {
	    this.contents[index][0] = keyValue[0];
	    tempKeySet.add(keyValue[0]);
	    this.contents[index][1] = keyValue[1];
	    index++;
	}
	this.keySet = Collections.unmodifiableSet(tempKeySet);
    }

    /**
     * @return
     */
    public Object[][] getContents() {
	return contents;
    }

    /**
     * @return
     */
    public Locale getLocale() {
	return locale;
    }
}
