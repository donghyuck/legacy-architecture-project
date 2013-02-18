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
package architecture.common.i18n.object.dao;

import java.util.List;
import java.util.Locale;

import architecture.common.i18n.object.I18nText;

public interface I18nTextDao {
	
    public abstract void createTexts(List<I18nText> list);

    public abstract void updateTexts(List<I18nText> list);

    public abstract void deleteTexts(List<I18nText> list);

    public abstract I18nText getText(long textId);
    
    public abstract List<I18nText> getTexts();
    
    public abstract List<I18nText> getTexts(Locale locale);
    
    public abstract List<I18nText> getTexts(int objectType);

    public abstract List<I18nText> getTexts(int objectType, long objectId);

    public abstract List<I18nText> getTexts(int objectType, String locale);
    
}
