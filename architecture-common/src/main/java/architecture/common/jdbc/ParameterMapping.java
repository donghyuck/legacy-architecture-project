package architecture.common.jdbc;

import architecture.common.exception.ConfigurationError;

/**
 * @author  donghyuck
 */
public class ParameterMapping {

	/**
	 * @uml.property  name="index"
	 */
	private int index;

	/**
	 * @uml.property  name="property"
	 */
	private String property;

	/**
	 * @uml.property  name="encoding"
	 */
	private String encoding;
	
	/**
	 * @uml.property  name="jdbcType"
	 * @uml.associationEnd  
	 */
	private JdbcType jdbcType;

	/**
	 * @uml.property  name="jdbcTypeName"
	 */
	private String jdbcTypeName;

	/**
	 * @uml.property  name="pattern"
	 */
	private String pattern;
	
	/**
	 * @uml.property  name="primary"
	 */
	private boolean primary ;
	
	/**
	 * @uml.property  name="javaType"
	 */
	private Class<?> javaType = Object.class;

	private ParameterMapping() {}
	
	/**
	 * @return
	 * @uml.property  name="property"
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return
	 * @uml.property  name="primary"
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @return
	 * @uml.property  name="encoding"
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 * @uml.property  name="encoding"
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return
	 * @uml.property  name="javaType"
	 */
	public Class<?> getJavaType() {
		return javaType;
	}

	/**
	 * @return
	 * @uml.property  name="jdbcType"
	 */
	public JdbcType getJdbcType() {
		return jdbcType;
	}

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @return
	 * @uml.property  name="jdbcTypeName"
	 */
	public String getJdbcTypeName() {
		return jdbcTypeName;
	}


	/**
	 * @return
	 * @uml.property  name="pattern"
	 */
	public String getPattern() {
		return pattern;
	}


	/**
	 * @author  donghyuck
	 */
	public static class Builder {
				
		/**
		 * @uml.property  name="parameterMapping"
		 * @uml.associationEnd  
		 */
		private ParameterMapping parameterMapping = new ParameterMapping();
		
		public Builder(String property) {
			parameterMapping.property = property;
		}
		
		public Builder(String property, Class<?> javaType ) {
			parameterMapping.property = property;
			parameterMapping.javaType = javaType;
		}
		
		public Builder javaType(Class<?> javaType) {
		    parameterMapping.javaType = javaType;		    
		    return this;
		}
		
		public Builder jdbcType(JdbcType jdbcType) {
		    parameterMapping.jdbcType = jdbcType;		    
		    return this;
		}

		public Builder encoding(String encoding) {
		    parameterMapping.encoding = encoding;		    
		    return this;
		}

		public Builder primary(boolean primary) {
		    parameterMapping.primary = primary;		    
		    return this;
		}
		
		public Builder jdbcTypeName(String jdbcTypeName) {
		    parameterMapping.jdbcTypeName = jdbcTypeName;
		    parameterMapping.jdbcType = resolveJdbcType(jdbcTypeName);		    
		    return this;
		}

		public Builder pattern(String pattern) {
		    parameterMapping.pattern = pattern;		    
		    return this;
		}

		public Builder index(int index) {
		    parameterMapping.index = index;		    
		    return this;
		}
		
		public ParameterMapping build() {
		   return parameterMapping;
		}
				
		protected JdbcType resolveJdbcType(String alias) {
			if (alias == null)
				return null;
			try {
				return JdbcType.valueOf(alias.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ConfigurationError("Error resolving JdbcType. Cause: " + e, e);
			}
		}
	}

}
