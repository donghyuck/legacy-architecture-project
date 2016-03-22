/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.common.lifecycle.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.util.StringUtils;

public class XmlApplicationPropertiesOld extends AbstractApplicationProperties {

    private File file;
    private Document doc;
    private Map<String, String> propertyCache;
    private Lock lock;

    public XmlApplicationPropertiesOld(File fileToUse) throws IOException {

	propertyCache = new HashMap<String, String>();

	lock = new ReentrantLock();

	file = fileToUse;

	if (!file.exists()) {
	    File tempFile = new File(file.getParentFile(),
		    (new StringBuilder()).append(file.getName()).append(".tmp").toString());

	    log.debug(tempFile.getAbsolutePath());

	    if (tempFile.exists()) {
		log.error((new StringBuilder()).append("WARNING: ").append(file.getName())
			.append(" was not found, but temp file from ")
			.append("previous write operation was. Attempting automatic recovery. Please ")
			.append("check file for data consistency.").toString());
		tempFile.renameTo(file);
	    } else {
		throw new FileNotFoundException((new StringBuilder()).append("XML properties file does not exist: ")
			.append(file.getName()).toString());
	    }
	} else {
	    File tempFile = new File(file.getParentFile(),
		    (new StringBuilder()).append(file.getName()).append(".tmp").toString());
	    if (tempFile.exists()) {
		log.error((new StringBuilder()).append("WARNING: found a temp file: ").append(tempFile.getName())
			.append(". This may ")
			.append("indicate that a previous write operation failed. Attempting automatic ")
			.append("recovery. Please check file ").append(file.getName()).append(" for data consistency.")
			.toString());
		if (tempFile.lastModified() > file.lastModified()) {
		    boolean error = false;
		    Reader reader = null;
		    try {
			reader = new InputStreamReader(new FileInputStream(tempFile),
				ApplicationConstants.DEFAULT_CHAR_ENCODING);
			SAXReader xmlReader = new SAXReader();
			xmlReader.read(reader);
		    } catch (Exception e) {
			error = true;
		    } finally {
			try {
			    reader.close();
			} catch (Exception e) {
			}
		    }
		    if (error) {
			String bakFile = (new StringBuilder()).append(tempFile.getName()).append("-")
				.append(System.currentTimeMillis()).append(".bak").toString();
			tempFile.renameTo(new File(tempFile.getParentFile(), bakFile));
		    } else {
			String bakFile = (new StringBuilder()).append(file.getName()).append("-")
				.append(System.currentTimeMillis()).append(".bak").toString();
			file.renameTo(new File(file.getParentFile(), bakFile));
			try {
			    Thread.sleep(100L);
			} catch (Exception e) {
			}
			tempFile.renameTo(file);
		    }
		} else {
		    boolean error = false;
		    Reader reader = null;
		    try {
			reader = new InputStreamReader(new FileInputStream(file),
				ApplicationConstants.DEFAULT_CHAR_ENCODING);
			SAXReader xmlReader = new SAXReader();
			xmlReader.read(reader);
		    } catch (Exception e) {
			error = true;
		    } finally {
			try {
			    reader.close();
			} catch (Exception e) {
			}
		    }
		    if (error) {
			String bakFileName = (new StringBuilder()).append(file.getName()).append("-")
				.append(System.currentTimeMillis()).append(".bak").toString();
			File bakFile = new File(file.getParentFile(), bakFileName);
			file.renameTo(bakFile);
			try {
			    Thread.sleep(100L);
			} catch (Exception e) {
			}
			tempFile.renameTo(file);
		    } else {
			String bakFile = (new StringBuilder()).append(tempFile.getName()).append("-")
				.append(System.currentTimeMillis()).append(".bak").toString();
			tempFile.renameTo(new File(tempFile.getParentFile(), bakFile));
		    }
		}
	    }
	}
	if (!file.canRead())
	    throw new IOException((new StringBuilder()).append("XML properties file must be readable: ")
		    .append(file.getName()).toString());
	if (!file.canWrite())
	    throw new IOException((new StringBuilder()).append("XML properties file must be writable: ")
		    .append(file.getName()).toString());

	Reader reader = null;
	try {
	    reader = new InputStreamReader(new FileInputStream(file), ApplicationConstants.DEFAULT_CHAR_ENCODING);
	    buildDoc(reader);
	} catch (Exception e) {
	    log.error((new StringBuilder()).append("Error creating XML properties file ").append(file.getName())
		    .append(": ").append(e.getMessage()).toString());
	    throw new IOException(e.getMessage());
	} finally {
	    try {
		reader.close();
	    } catch (Exception e) {
		log.debug(e.getMessage(), e);
	    }
	}
    }

    public XmlApplicationPropertiesOld(InputStream in) throws Exception {
	propertyCache = new HashMap<String, String>();
	lock = new ReentrantLock();
	Reader reader = new BufferedReader(new InputStreamReader(in));
	buildDoc(reader);
    }

    public XmlApplicationPropertiesOld(String fileName) throws IOException {
	this(new File(fileName));
    }

    public Collection<String> getPropertyNames() {
	List<String> propNames = new LinkedList<String>();
	List<Element> elements = doc.getRootElement().elements();
	if (elements.size() == 0)
	    return Collections.emptyList();
	for (Element element : elements) {
	    getElementNames(propNames, element, element.getName());
	}
	return propNames;
    }

    public void clear() {
	throw new UnsupportedOperationException("Not implemented in xml version");
    }

    public boolean containsKey(Object key) {
	return get(key) != null;
    }

    public boolean containsValue(Object value) {
	throw new UnsupportedOperationException("Not implemented in xml version");
    }

    public Set<java.util.Map.Entry<String, String>> entrySet() {
	throw new UnsupportedOperationException("Not implemented in xml version");
    }

    public String get(Object o) {

	String name = (String) o;

	String value = propertyCache.get(name);
	if (value != null)
	    return value;

	String propName[] = parsePropertyName(name);
	Element element = doc.getRootElement();

	for (int i = 0; i < propName.length; i++) {

	    element = element.element(propName[i]);

	    if (element == null)
		return null;
	}

	lock.lock();
	try {
	    value = element.getText();

	    if ("".equals(value))
		return null;

	    value = value.trim();
	    propertyCache.put(name, value);

	    return value;
	} finally {
	    lock.unlock();
	}

    }

    public Collection getChildrenNames(String parent) {
	String propName[] = parsePropertyName(parent);
	Element element = doc.getRootElement();
	for (int i = 0; i < propName.length; i++) {
	    element = element.element(propName[i]);
	    if (element == null)
		return Collections.emptyList();
	}

	List<Element> children = element.elements();
	int childCount = children.size();
	List<String> childrenNames = new ArrayList<String>(childCount);

	for (Element e : children) {
	    childrenNames.add(e.getName());
	}

	return childrenNames;
    }

    public String getAttribute(String name, String attribute) {
	if (name == null || attribute == null)
	    return null;
	String propName[] = parsePropertyName(name);
	Element element = doc.getRootElement();
	int i = 0;
	do {
	    if (i >= propName.length)
		break;
	    String child = propName[i];
	    element = element.element(child);
	    if (element == null)
		break;
	    i++;
	} while (true);
	if (element != null)
	    return element.attributeValue(attribute);
	else
	    return null;
    }

    public boolean isEmpty() {
	return false;
    }

    public Set<String> keySet() {
	throw new UnsupportedOperationException("Not implemented in xml version");
    }

    public synchronized String put(String name, String value) {

	propertyCache.put(name, value);
	String propName[] = parsePropertyName(name);
	Element element = doc.getRootElement();
	for (String n : propName) {
	    if (element.element(n) == null)
		element.addElement(n);
	    element = element.element(n);
	}
	element.setText(value);
	saveProperties();
	return null;
    }

    public synchronized void putAll(Map<? extends String, ? extends String> m) {
	for (Entry e : m.entrySet()) {
	    String propertyName = (String) e.getKey();
	    String propertyValue = (String) e.getValue();
	    String propName[] = parsePropertyName(propertyName);
	    Element element = doc.getRootElement();
	    for (String p : propName) {
		if (element.element(p) == null)
		    element.addElement(p);
		element = element.element(p);
	    }
	    if (propertyValue != null)
		element.setText(propertyValue);
	}
	saveProperties();
    }

    public synchronized String remove(Object key) {
	String name = (String) key;
	propertyCache.remove(name);
	String propName[] = parsePropertyName(name);
	Element element = doc.getRootElement();
	for (int i = 0; i < propName.length - 1; i++) {
	    element = element.element(propName[i]);
	    if (element == null)
		return null;
	}

	String value = element.getText();
	element.remove(element.element(propName[propName.length - 1]));
	saveProperties();
	return value;
    }

    public int size() {
	throw new UnsupportedOperationException("Not implemented in xml version");
    }

    public Collection<String> values() {
	throw new UnsupportedOperationException("Not implemented in xml version");
    }

    private void buildDoc(Reader in) throws Exception {
	SAXReader xmlReader = new SAXReader();
	doc = xmlReader.read(in);
    }

    private String[] parsePropertyName(String name) {
	return StringUtils.split(name, '.');
    }

    private void getElementNames(List<String> list, Element e, String name) {
	if (e.elements().isEmpty()) {
	    list.add(name);
	} else {
	    List<Element> children = e.elements();
	    for (Element child : children) {
		getElementNames(list, child,
			(new StringBuilder()).append(name).append('.').append(child.getName()).toString());
	    }
	}
    }

    private synchronized void saveProperties() {
	Writer writer = null;
	boolean error = false;
	File tempFile = null;
	try {
	    tempFile = new File(file.getParentFile(),
		    (new StringBuilder()).append(file.getName()).append(".tmp").toString());
	    writer = new OutputStreamWriter(new FileOutputStream(tempFile), ApplicationConstants.DEFAULT_CHAR_ENCODING);
	    XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
	    xmlWriter.write(doc);
	} catch (Exception e) {
	    log.error((new StringBuilder()).append("Unable to write to file ").append(file.getName()).append(".tmp")
		    .append(": ").append(e.getMessage()).toString());
	    error = true;
	} finally {
	    try {
		writer.flush();
		writer.close();
	    } catch (Exception e) {
		log.error(e);
		error = true;
	    }
	}
	if (!error) {
	    error = false;
	    if (file.exists() && !file.delete()) {
		log.error((new StringBuilder()).append("Error deleting property file: ").append(file.getAbsolutePath())
			.toString());
		return;
	    }
	    try {
		writer = new OutputStreamWriter(new FileOutputStream(file), ApplicationConstants.DEFAULT_CHAR_ENCODING);
		XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
		xmlWriter.write(doc);
	    } catch (Exception e) {
		log.error((new StringBuilder()).append("Unable to write to file '").append(file.getName()).append("': ")
			.append(e.getMessage()).toString());
		error = true;
		try {
		    file.delete();
		} catch (Exception fe) {
		}
	    } finally {
		try {
		    writer.flush();
		    writer.close();
		} catch (Exception e) {
		    log.error(e);
		    error = true;
		}
	    }
	    if (!error)
		tempFile.delete();
	}

    }
}
