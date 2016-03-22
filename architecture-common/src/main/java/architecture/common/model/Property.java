package architecture.common.model;

public class Property implements java.io.Serializable {

    private String name;

    private Object value;

    public Property() {

    }

    public Property(String name, Object value) {
	this.name = name;
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
	this.value = value;
    }

}