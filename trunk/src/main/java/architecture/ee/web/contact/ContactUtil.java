package architecture.ee.web.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUtil {


	public static boolean notEmpty(List list){
		if(list != null && list.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static  Map<Long, Contact> getContactMapFromList(List<Contact> list) {
		Map<Long, Contact> result = new HashMap<Long, Contact>();
		for(Contact c: list){
			result.put(c.getContactId(), c);
		}
		return result;
	}
	
	public static  Map<Long, ContactGroup> getContactGroupMapFromList(List<ContactGroup> list) {
		Map<Long, ContactGroup> result = new HashMap<Long, ContactGroup>();
		for(ContactGroup g: list){
			result.put(g.getGroupId(), g);
		}
		return result;
	}
	
	public static List<Node> getNodeListFromGroupList(List<ContactGroup> list, String groupIds){
		List<Node> result = new ArrayList<Node>();
		String[] groups = groupIds.split(",");
		
		for(ContactGroup g : list){
			Node n = new Node();
			n.setId(g.getGroupId());
			n.setText(g.getGroupName());
			n.setChildIds(new ArrayList<Long>(g.getChildGroupIds()));
			//System.out.println("g.getChildGroupIds()" + g.getChildGroupIds());
			//n.setLev(g.getLev());
			n.setLev(g.getParentGroupId() == 0 ? 1 : -1);
			
			if(!groupIds.equals("")){ //checked 처리
				for(int i=0; i< groups.length; i++ ){
					if(g.getGroupId() == Long.parseLong(groups[i])){
						System.out.println("now " + n.getText() + " is set cheked ================");
						n.setChecked(true);
					}
				}
			}
			result.add(n);
		}
		return result;
	}
	
	public static Map<Long, Node> getMapFromNodeList(List<Node> list){
		Map<Long, Node> map = new HashMap<Long, Node>();
		for(int i= 0; i< list.size(); i++){ 
			Node n = list.get(i);
			map.put(n.getId(), n);
		}
		return map;
	}
	
	public static Map<Long, ContactGroup> getMapFromContactGroupList(List<ContactGroup> list){
		Map<Long, ContactGroup> groupMap = new HashMap<Long, ContactGroup>();
		for(int i= 0; i< list.size(); i++){ 
			ContactGroup g = list.get(i);
			groupMap.put(g.getGroupId(), g);
		}
		return groupMap;
	}
	
	
	// check
	public static void setSelectedGroupIds(List<ContactGroup> list, String groupIds){
		String[] groups = groupIds.split(",");
		for(ContactGroup g : list){
			for(int i=0; i< groups.length; i++ ){
				if(g.getGroupId() == Long.parseLong(groups[i])){
					
				}
			}
		}
	}
	
	public static void setChildGroupIds(List<ContactGroup> list){
		Map<Long, ContactGroup> groupMap = getMapFromContactGroupList(list);
		
		for(int j=0; j< list.size(); j++){
			ContactGroup group = list.get(j);
			if(group.getParentGroupId() > 0 ){ //부모가 있다면
				ContactGroup parent = groupMap.get(group.getParentGroupId());
				parent.getChildGroupIds().add(group.getGroupId());
			}
		}
		
		// 결과 출력
		/*
		for(int k=0; k< list.size(); k++){
			ContactGroup p = list.get(k);
			
			List<Long> childList = p.getChildGroupIds();
			
			String childs = "";
			for(int l=0; l < childList.size(); l++){
				childs += childList.get(l) + ",";
			}
			if(childList.size() > 0){
				log.debug("Group Id [" + p.getGroupId() + " : " + p.getGroupName() + "] 의 자식 번호 : " + childs);
			}else{
				log.debug("Group Id [" + p.getGroupId() + " : " + p.getGroupName() + "] 은 자식이 없습니다. ");
			}
		}*/
		
	}

}
