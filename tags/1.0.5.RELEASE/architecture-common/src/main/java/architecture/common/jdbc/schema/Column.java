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
package architecture.common.jdbc.schema;

import java.io.Serializable;

/**
 * @author  donghyuck
 */
public class Column implements Serializable {

	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="type"
	 */
	private int type;

	private String typeName;
	
	private int size;
	
	private String nullable ;
	
	private String comment ;
	
	private boolean primaryKey;
	
	private int ordinalPosition ;
	
	
	public Column(String name, int type) {
		this.name = name;
		this.type = type;
		this.typeName = null;
		this.nullable = null;
		this.primaryKey = false;
		this.comment = null;
		this.size = 0;
		this.ordinalPosition = 0;
	}

	
	
	/**
	 * @param name
	 * @param type
	 * @param typeName
	 * @param size
	 * @param nullable
	 * @param comment
	 */
	public Column(String name, int type, String typeName, int size, String nullable, String comment) {
		this.name = name;
		this.type = type;
		this.typeName = typeName;
		this.size = size;
		this.nullable = nullable;
		this.comment = comment;
		this.ordinalPosition = 0;
	}
	
	/**
	 * @return ordinalPosition
	 */
	public int getOrdinalPosition() {
		return ordinalPosition;
	}



	/**
	 * @param name
	 * @param type
	 * @param typeName
	 * @param size
	 * @param nullable
	 * @param comment
	 */
	public Column(String name, int type, String typeName, int size, String nullable, String comment, int ordinalPosition) {
		this.name = name;
		this.type = type;
		this.typeName = typeName;
		this.size = size;
		this.nullable = nullable;
		this.comment = comment;
		this.ordinalPosition = ordinalPosition;
	}



	/**
	 * @return primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}



	/**
	 * @param primaryKey 설정할 primaryKey
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}



	/**
	 * @return typeName
	 */
	public String getTypeName() {
		return typeName;
	}



	/**
	 * @return size
	 */
	public int getSize() {
		return size;
	}



	/**
	 * @return nullable
	 */
	public String getNullable() {
		return nullable;
	}



	/**
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}



	/**
	 * @param type 설정할 type
	 */
	public void setType(int type) {
		this.type = type;
	}



	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 * @uml.property  name="type"
	 */
	public int getType() {
		return type;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Column column = (Column) o;

		if (type != column.type)
			return false;
		if (name != null ? !name.equals(column.name) : column.name != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = (name != null ? name.hashCode() : 0);
		result = 29 * result + type;
		return result;
	}

}
