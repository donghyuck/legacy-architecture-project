package architecture.common.lifecycle.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import architecture.common.util.L10NUtils;
import architecture.common.xml.XmlProperties;

/**
 * @author    donghyuck
 */
public class XmlApplicationProperties extends AbstractApplicationProperties {

	/**
	 * @uml.property  name="properties"
	 * @uml.associationEnd  
	 */
	private XmlProperties properties;

	public XmlApplicationProperties(File fileToUse) throws IOException {
		properties = new XmlProperties(fileToUse);
	}

	public XmlApplicationProperties(InputStream in) throws Exception {
		properties = new XmlProperties(in);
	}

	public XmlApplicationProperties(String fileName) throws IOException {
		properties = new XmlProperties(fileName);
	}

	public Collection<String> getChildrenNames(String name) {
		return properties.getChildrenNames(name);
	}

	public Collection<String> getPropertyNames() {
		return properties.getPropertyNames();
	}

	public int size() {
		throw new UnsupportedOperationException(L10NUtils.format("002051"));
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException(L10NUtils.format("002051"));
	}

	public String get(Object key) {
		return properties.getProperty((String)key);
	}

	public String put(String key, String value) {
		properties.setProperty(key, value);
		return "";
	}

	public String remove(Object key) {
		properties.deleteProperty((String)key);
		return "";
	}

	public void putAll(Map<? extends String, ? extends String> propertyMap) {
		properties.setProperties((Map<String, String>) propertyMap);
	}

	public void clear() {
		throw new UnsupportedOperationException(L10NUtils.format("002051"));
	}

	public Set<String> keySet() {
		throw new UnsupportedOperationException(L10NUtils.format("002051"));
	}

	public Collection<String> values() {
		throw new UnsupportedOperationException(L10NUtils.format("002051"));
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		throw new UnsupportedOperationException(L10NUtils.format("002051"));
	}
	
}