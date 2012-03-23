package architecture.common.jdbc;

import architecture.common.exception.ConfigurationError;

public class ParameterMapping {

	private int index;

	private String property;

	private String encoding;
	
	private JdbcType jdbcType;

	private String jdbcTypeName;

	private String pattern;
	
	private boolean primary ;
	
	private Class<?> javaType = Object.class;

	private ParameterMapping() {}
	
	public String getProperty() {
		return property;
	}

	public boolean isPrimary() {
		return primary;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public int getIndex() {
		return index;
	}
	
	public String getJdbcTypeName() {
		return jdbcTypeName;
	}


	public String getPattern() {
		return pattern;
	}


	public static class Builder {
				
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
