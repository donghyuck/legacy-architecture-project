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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public interface I18nTextManager {

    public abstract void saveTexts(List<I18nText> list);

    public abstract void saveTexts(List<I18nText> list, String categoryName);

    public abstract void deleteTexts(List<I18nText> list);

    public abstract I18nText getText(long id);

    public abstract List<I18nText> getTexts();

    public abstract List<I18nText> getTexts(String categoryName, String localeCode);

    public abstract Map<Locale, ResourceBundle> getResourceBundles();

    public abstract void refresh();
}
