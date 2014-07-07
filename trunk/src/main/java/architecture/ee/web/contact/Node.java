package architecture.ee.web.contact;
import java.util.ArrayList;
import java.util.List;


public class Node {
	private Long id = -1L;
	private String text = "";
	private List<Node> items = new ArrayList<Node>();
	private boolean hasChildren;
	private List<Long> childIds = new ArrayList<Long>();
	private int lev = -1;
	private boolean checked = false;
	
	
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public int getLev() {
		return lev;
	}
	public void setLev(int lev) {
		this.lev = lev;
	}
	
	public List<Long> getChildIds() {
		return childIds;
	}
	public void setChildIds(List<Long> childIds) {
		this.childIds = childIds;
	}
	
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Node> getItems() {
		return items;
	}
	public void setItems(List<Node> items) {
		this.items = items;
	}
	
	
}
