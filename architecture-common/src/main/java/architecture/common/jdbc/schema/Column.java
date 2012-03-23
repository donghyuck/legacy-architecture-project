package architecture.common.jdbc.schema;

/**
 * @author  donghyuck
 */
public class Column {

  /**
 */
private String name;
  /**
 */
private int type;

  public Column(String name, int type) {
    this.name = name;
    this.type = type;
  }

  /**
 * @return
 */
public String getName() {
    return name;
  }

  /**
 * @return
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
