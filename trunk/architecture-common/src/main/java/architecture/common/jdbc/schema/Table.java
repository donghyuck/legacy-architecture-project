package architecture.common.jdbc.schema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author   donghyuck
 */
public class Table {
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="catalog"
	 */
	private String catalog;
	/**
	 * @uml.property  name="schema"
	 */
	private String schema;
	private Map<String, Column> columns = new LinkedHashMap<String, Column>();
	/**
	 * @uml.property  name="primaryKey"
	 * @uml.associationEnd  
	 */
	private Column primaryKey;

	public Table(String name) {
		this.name = name;
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
	 * @uml.property  name="catalog"
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * @param  catalog
	 * @uml.property  name="catalog"
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/**
	 * @return
	 * @uml.property  name="schema"
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param  schema
	 * @uml.property  name="schema"
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void addColumn(Column col) {
		columns.put(col.getName().toUpperCase(), col);
	}

	public Column getColumn(String name) {
		return columns.get(name.toUpperCase());
	}

	public String[] getColumnNames() {
		return columns.keySet().toArray(new String[columns.size()]);
	}

	/**
	 * @param  column
	 * @uml.property  name="primaryKey"
	 */
	public void setPrimaryKey(Column column) {
		primaryKey = column;
	}

	/**
	 * @return
	 * @uml.property  name="primaryKey"
	 */
	public Column getPrimaryKey() {
		return primaryKey;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Table table = (Table) o;
		if (name != null ? !name.equals(table.name) : table.name != null)
			return false;
		return true;
	}

	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

}
