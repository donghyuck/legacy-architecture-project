package architecture.ee.jdbc.schema;

/**
 * @author  donghyuck
 */
public class Column {

  /**
 * @uml.property  name="name"
 */
private String name;
  /**
 * @uml.property  name="type"
 */
private int type;

  public Column(String name, int type) {
    this.name = name;
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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Column column = (Column) o;

    if (type != column.type) return false;
    if (name != null ? !name.equals(column.name) : column.name != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (name != null ? name.hashCode() : 0);
    result = 29 * result + type;
    return result;
  }

}
