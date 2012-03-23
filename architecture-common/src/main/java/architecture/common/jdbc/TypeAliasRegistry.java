package architecture.common.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import architecture.common.exception.ConfigurationError;
import architecture.common.util.ClassUtils;

/**
 * IBatis org.apache.ibatis.type.TypeAliasRegistry 의 소스에서 가져옴.
 * 
 * @author donghyuck
 * 
 */
public class TypeAliasRegistry {

	
	private final HashMap<String, Class<?>> TYPE_ALIASES = new HashMap<String, Class<?>>();

	public TypeAliasRegistry() {
		registerAlias("string", String.class);

		registerAlias("byte", Byte.class);
		registerAlias("long", Long.class);
		registerAlias("short", Short.class);
		registerAlias("int", Integer.class);
		registerAlias("integer", Integer.class);
		registerAlias("double", Double.class);
		registerAlias("float", Float.class);
		registerAlias("boolean", Boolean.class);

		registerAlias("byte[]", Byte[].class);
		registerAlias("long[]", Long[].class);
		registerAlias("short[]", Short[].class);
		registerAlias("int[]", Integer[].class);
		registerAlias("integer[]", Integer[].class);
		registerAlias("double[]", Double[].class);
		registerAlias("float[]", Float[].class);
		registerAlias("boolean[]", Boolean[].class);

		registerAlias("_byte", byte.class);
		registerAlias("_long", long.class);
		registerAlias("_short", short.class);
		registerAlias("_int", int.class);
		registerAlias("_integer", int.class);
		registerAlias("_double", double.class);
		registerAlias("_float", float.class);
		registerAlias("_boolean", boolean.class);

		registerAlias("_byte[]", byte[].class);
		registerAlias("_long[]", long[].class);
		registerAlias("_short[]", short[].class);
		registerAlias("_int[]", int[].class);
		registerAlias("_integer[]", int[].class);
		registerAlias("_double[]", double[].class);
		registerAlias("_float[]", float[].class);
		registerAlias("_boolean[]", boolean[].class);

		registerAlias("date", Date.class);
		registerAlias("decimal", BigDecimal.class);
		registerAlias("bigdecimal", BigDecimal.class);
		registerAlias("object", Object.class);

		registerAlias("date[]", Date[].class);
		registerAlias("decimal[]", BigDecimal[].class);
		registerAlias("bigdecimal[]", BigDecimal[].class);
		registerAlias("object[]", Object[].class);

		registerAlias("map", Map.class);
		registerAlias("hashmap", HashMap.class);
		registerAlias("list", List.class);
		registerAlias("arraylist", ArrayList.class);
		registerAlias("collection", Collection.class);
		registerAlias("iterator", Iterator.class);

		registerAlias("ResultSet", ResultSet.class);
	}

	@SuppressWarnings("unchecked")
    // throws class cast exception as well if types cannot be assigned
	public <T> Class<T> resolveAlias(String string) {
		try {
			if (string == null)
				return null;
			String key = string.toLowerCase();
			Class<T> value;
			if (TYPE_ALIASES.containsKey(key)) {
				value = (Class<T>) TYPE_ALIASES.get(key);
			} else {
				value = (Class<T>) ClassUtils.getClass(string);
			}
			return value;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Could not resolve type alias '" + string + "'.  Cause: " + e, e);
		}
	}

	 
	public void registerAlias(String alias, Class<?> value) {
	    assert alias != null;
	    String key = alias.toLowerCase();
	    if (TYPE_ALIASES.containsKey(key) && !TYPE_ALIASES.get(key).equals(value.getName()) && TYPE_ALIASES.get(alias) != null) {
	      if (!value.equals(TYPE_ALIASES.get(alias))) {
	        throw new ConfigurationError("The alias '" + alias + "' is already mapped to the value '" + TYPE_ALIASES.get(alias).getName() + "'.");
	      }
	    }
	    TYPE_ALIASES.put(key, value);
	  }
	
}
