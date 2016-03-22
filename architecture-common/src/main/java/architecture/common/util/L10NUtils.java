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
package architecture.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import architecture.common.i18n.Localizer;
import architecture.common.i18n.LocalizerFactory;

public class L10NUtils {

    private static Log log = LogFactory.getLog(L10NUtils.class);

    private static final L10NUtils SINGLETON = new L10NUtils(true);

    public static final String I18N_XML_RESOURCE = "META-INF/i18n.xml";

    public static final String I18N_CUSTOM_XML_RESOURCE = "i18n_custom.xml";

    private List<LocalizerKey> localizers = new ArrayList<LocalizerKey>();

    private L10NUtils(boolean load) {
	// 1. 모든 jar 파일에 포함된 META-INF/i18n.xml 에서 오류 코드에 따른 properties 파일 정보를
	// 가져온다.
	try {
	    if (load) {
		loadProps(I18N_XML_RESOURCE, true);
	    }
	} catch (IOException e) {
	    log.debug((new StringBuilder()).append("Unable to access I18n file, " + I18N_XML_RESOURCE + ": ")
		    .append(e.toString()).toString());
	}

	// 1. 모든 jar 에 포함된 i18n_custom.xml 에서 오류 코드에 따른 properties 파일 정보를 가져온다.
	try {
	    if (load) {
		loadProps(I18N_CUSTOM_XML_RESOURCE, true);
	    }
	} catch (IOException e) {
	    log.debug(
		    ((new StringBuilder()).append("Unable to access I18n user file, " + I18N_CUSTOM_XML_RESOURCE + ": ")
			    .append(e.toString()).toString()));
	}
    }

    public static L10NUtils getInstance() {
	return SINGLETON;
    }

    public static String format(String id, Object... args) {
	return MessageFormat.format(getMessage(id), args);
    }

    public static String format(String id, Locale locale, Object... args) {
	return MessageFormat.format(getMessage(id, locale), args);
    }

    public static String format(String id, Locale locale, ClassLoader cl, Object... args) {
	return MessageFormat.format(getMessage(id, locale, cl), args);
    }

    public static String codeToString(int code) {
	return Localizer.decimalformat.format(code);
    }

    public static String getMessage(String id) {
	return getLocalizer(id).getMessage(id);
    }

    public static String getMessage(String id, Locale locale) {
	return getLocalizer(id, locale).getMessage(id);
    }

    public static String getMessage(String id, Locale locale, ClassLoader cl) {
	return getLocalizer(id, locale, cl).getMessage(id);
    }

    private void loadProps(String resource, boolean breakOnError) throws IOException {

	HashSet<URL> hashset = new HashSet<URL>();
	if (log.isDebugEnabled()) {
	    log.debug((new StringBuilder()).append("Searching ").append(resource).toString());
	}
	Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(resource);
	if (urls != null) {
	    URL url;
	    for (; urls.hasMoreElements(); hashset.add(url)) {
		url = urls.nextElement();
		if (log.isDebugEnabled())
		    log.debug((new StringBuilder()).append("Adding ").append(url).toString());
	    }
	}

	for (URL url : hashset) {
	    if (log.isDebugEnabled())
		log.debug((new StringBuilder()).append("Loading from ").append(url).toString());
	    InputStream is = null;
	    try {
		is = url.openStream();
		InputSource input = new InputSource(is);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XMLReader xmlreader = factory.newSAXParser().getXMLReader();
		I18nParsingHandler handler = new I18nParsingHandler();
		xmlreader.setContentHandler(handler);
		xmlreader.setDTDHandler(handler);
		xmlreader.setEntityResolver(handler);
		xmlreader.setErrorHandler(handler);
		xmlreader.parse(input);
		localizers.addAll(handler.localizers);
	    } catch (IOException e) {
		if (log.isDebugEnabled())
		    log.debug((new StringBuilder()).append("Skipping ").append(url).toString());
		if (breakOnError)
		    throw e;
	    } catch (Exception e) {
		log.error(e);
	    } finally {
		if (is != null)
		    IOUtils.closeQuietly(is);
	    }
	}

    }

    public static Localizer getLocalizer(String key) throws MissingResourceException {
	return getLocalizer(key, Locale.getDefault(), null);
    }

    public static Localizer getLocalizer(String key, Locale locale) throws MissingResourceException {
	return getLocalizer(key, locale, null);
    }

    /**
     * key 에 해당하는 프로퍼티 파일을 인자로하여 생성된 Localizer 객체를 리턴한다.
     * 
     * @param key
     * @param locale
     * @param classloader
     * @return
     * @throws MissingResourceException
     */
    public static Localizer getLocalizer(String key, Locale locale, ClassLoader classloader)
	    throws MissingResourceException {

	Locale localeToUse = locale;
	String keyToUse = key;
	ClassLoader cl = classloader;

	if (keyToUse == null)
	    throw new NullPointerException("No key name provided");
	if (localeToUse == null)
	    localeToUse = Locale.getDefault();

	if (cl != null) {
	    Localizer localizer = L10NUtils.SINGLETON.getLocalizerBundle(key, locale, cl);
	    if (localizer != null)
		return localizer;
	}

	cl = L10NUtils.class.getClassLoader();
	if (cl == null)
	    cl = ClassLoader.getSystemClassLoader();
	if (cl == null) {
	    if (log.isDebugEnabled())
		log.debug("no system classs loader");
	    cl = Thread.currentThread().getContextClassLoader();
	    if (cl == null) {
		if (log.isDebugEnabled())
		    log.debug("no context class loader");
	    } else if (log.isDebugEnabled())
		log.debug("Using context class loader");
	}

	Localizer localizer = L10NUtils.SINGLETON.getLocalizerBundle(key, locale, cl);
	if (localizer == null) {
	    ClassLoader clToUse = Thread.currentThread().getContextClassLoader();
	    localizer = L10NUtils.SINGLETON.getLocalizerBundle(key, locale, clToUse);
	    if (localizer == null)
		throw new MissingResourceException((new StringBuilder()).append("Can't locate bundle for class '")
			.append(key).append("'").toString(), key, "");
	}
	return localizer;
    }

    public Localizer getLocalizerBundle(String key, Locale locale, ClassLoader classloader)
	    throws MissingResourceException {

	LocalizerKey keyToUse = getLocalizerKey(key);
	if (keyToUse != null)
	    return LocalizerFactory.getLocalizer(keyToUse.bundleName, locale, classloader);

	return null;
    }

    private LocalizerKey getLocalizerKey(String key) {
	Integer i = new Integer(key);
	for (LocalizerKey k : localizers) {
	    if (k.codes.contains(i)) {
		return k;
	    }
	}
	return null;
    }

    private static class LocalizerKey {

	int hash;
	Locale locale;
	String bundleName;
	List<Integer> codes;

	private LocalizerKey(Integer code, Locale locale) {
	    this.bundleName = "*";
	    this.locale = locale;
	    this.codes = new ArrayList<Integer>(1);
	    this.codes.add(code);
	}

	private LocalizerKey() {
	    locale = null;
	    bundleName = null;
	    codes = new ArrayList<Integer>();
	}

	public int hashCode() {
	    if (hash == 0) {
		if (bundleName != null)
		    hash = bundleName.hashCode();
		if (locale != null)
		    hash = hash | locale.hashCode();
		if (codes != null)
		    hash = hash | codes.hashCode();
	    }
	    return hash;
	}

	public String toString() {

	    StringBuilder builder = new StringBuilder();
	    builder.append('[');
	    builder.append("LocalizerKey");
	    builder.append('=');
	    builder.append(bundleName);
	    builder.append(',');
	    builder.append(locale);
	    builder.append(',');
	    builder.append(codes.size());
	    builder.append(']');
	    return builder.toString();
	}

	public boolean equals(Object obj) {
	    if (obj == this)
		return true;
	    if (obj instanceof LocalizerKey) {
		LocalizerKey key = (LocalizerKey) obj;
		if ("*".equals(key.bundleName)) {
		    for (Integer c : key.codes) {
			if (codes.contains(c))
			    return true;
		    }
		}

	    }
	    return false;
	}

    }

    private static class I18nParsingHandler extends DefaultHandler {

	Locator locator;
	List<LocalizerKey> localizers;
	StringBuilder value;
	LocalizerKey key;

	private I18nParsingHandler() {
	    localizers = new ArrayList<LocalizerKey>();
	    value = new StringBuilder();
	}

	public void setDocumentLocator(Locator locator) {
	    this.locator = locator;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
	    value.append(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
	    if (!StringUtils.isEmpty(qName))
		if (qName.equals("localizer") && key != null)
		    localizers.add(key);
	    value.setLength(0);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException {
	    if (!StringUtils.isEmpty(qName))
		if (qName.equals("localizer")) {
		    key = new LocalizerKey();
		    int i = attributes.getLength();
		    for (int j = 0; j < i; j++) {
			String name = attributes.getQName(j);
			String value = attributes.getValue(j);
			if (name.equals("bundleName"))
			    key.bundleName = value;
			else if (name.equals("codes")) {
			    String[] codes = StringUtils.split(value, ",");
			    for (String code : codes) {
				if (code.contains("-")) {
				    String[] range = StringUtils.split(code, "-");
				    for (int k = Integer.parseInt(range[0]); k <= Integer.parseInt(range[1]); k++) {
					key.codes.add(k);
				    }
				} else {
				    key.codes.add(Integer.parseInt(code));
				}
			    }
			} else
			    throw new SAXParseException("Invalid attribute: " + name, locator);
		    }
		}

	}

    }
}