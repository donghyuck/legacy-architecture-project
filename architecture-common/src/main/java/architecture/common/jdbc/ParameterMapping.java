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
package architecture.common.jdbc;

import architecture.common.exception.ConfigurationError;
import architecture.common.util.L10NUtils;

/**
 * @author donghyuck
 */
public class ParameterMapping {

    public enum Mode {
	IN, OUT, NONE
    };

    private String cipher;

    private String cipherKey;

    private String cipherKeyAlg;

    private int size;

    private int index;

    private String property;
    
    private String column;

    private String encoding;

    private JdbcType jdbcType;

    private String jdbcTypeName;

    private String pattern;

    private String digest;

    private boolean primary;

    private Class<?> javaType = Object.class;

    private Mode mode = Mode.IN;

    private ParameterMapping() {
    }

    public int getSize() {
	return size;
    }

    public void setSize(int size) {
	this.size = size;
    }

    public String getCipher() {
	return cipher;
    }

    public String getCipherKey() {
	return cipherKey;
    }

    public String getCipherKeyAlg() {
	return cipherKeyAlg;
    }

    public void setCipher(String cipher) {
	this.cipher = cipher;
    }

    public void setCipherKey(String cipherKey) {
	this.cipherKey = cipherKey;
    }

    public void setCipherKeyAlg(String cipherKeyAlg) {
	this.cipherKeyAlg = cipherKeyAlg;
    }

    public String getDigest() {
	return digest;
    }

    public void setDigest(String digest) {
	this.digest = digest;
    }

    public Mode getMode() {
	return mode;
    }

    public void setMode(Mode mode) {
	this.mode = mode;
    }

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

    
    
    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }



    public static class Builder {

	/**
	 * @uml.property name="parameterMapping"
	 * @uml.associationEnd
	 */
	private ParameterMapping parameterMapping = new ParameterMapping();

	public Builder(String property) {
	    parameterMapping.property = property;
	}

	public Builder(String property, Class<?> javaType) {
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

	public Builder digest(String digest) {
	    parameterMapping.digest = digest;
	    return this;
	}

	public Builder cipher(String cipher) {
	    parameterMapping.cipher = cipher;
	    return this;
	}

	public Builder cipherKey(String cipherKey) {
	    parameterMapping.cipherKey = cipherKey;
	    return this;
	}

	public Builder cipherKeyAlg(String cipherKeyAlg) {
	    parameterMapping.cipherKeyAlg = cipherKeyAlg;
	    return this;
	}

	public Builder encoding(String encoding) {
	    parameterMapping.encoding = encoding;
	    return this;
	}

	public Builder mode(String mode) {
	    parameterMapping.mode = Mode.valueOf(mode);
	    return this;
	}

	public Builder size(String sizeString) {
	    parameterMapping.size = Integer.parseInt(sizeString);
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
	
	public Builder column(String column) {
	    parameterMapping.column = column;
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
		throw new ConfigurationError(L10NUtils.format("002003", e.getMessage()), e);
	    }
	}
    }
}