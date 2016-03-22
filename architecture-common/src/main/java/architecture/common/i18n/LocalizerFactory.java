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

import java.util.Locale;
import java.util.MissingResourceException;

import architecture.common.util.ImplFactory;

/**
 * @author DongHyuck, Son
 */
public class LocalizerFactory {

    public static interface Implementation {

	/**
	 * 
	 * @param location
	 *            리소스 파일 경로
	 * @param locale
	 *            로케일
	 * @param classloader
	 * @return
	 * @throws MissingResourceException
	 */
	public Localizer getLocalizer(String location, Locale locale, ClassLoader classloader)
		throws MissingResourceException;

	/**
	 * 
	 * @param location
	 *            리소스 파일 경로
	 * @param locale
	 *            로케일
	 * @return
	 * @throws MissingResourceException
	 */
	public Localizer getLocalizer(String location, Locale locale) throws MissingResourceException;

	/**
	 * 
	 * @param location
	 *            리소스 파일 경로
	 * @return
	 * @throws MissingResourceException
	 */
	public Localizer getLocalizer(String location) throws MissingResourceException;

    }

    private static Implementation impl = null;

    static {
	impl = (Implementation) ImplFactory.loadImplFromKey(LocalizerFactory.Implementation.class);
    }

    public static Localizer getLocalizer(String location) throws MissingResourceException {
	return impl.getLocalizer(location, Locale.getDefault());
    }

    public static Localizer getLocalizer(String location, Locale locale) throws MissingResourceException {
	return impl.getLocalizer(location, locale);
    }

    public static Localizer getLocalizer(String location, Locale locale, ClassLoader classloader)
	    throws MissingResourceException {
	return impl.getLocalizer(location, locale, classloader);
    }
}
