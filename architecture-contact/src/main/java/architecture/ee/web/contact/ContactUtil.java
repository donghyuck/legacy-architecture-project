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
			n.setLev(g.getParentGroupId() == 0 ? 1 : -1);  // 여기서는 TOP 인지 아닌지만 알면됨. 
			
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
	

	public static int getContactGroupDepth(ContactGroup targetGroup, int depth, Map<Long, ContactGroup> map){
		if(targetGroup.getParentGroupId() < 1){ //종료조건
			return depth;
		}else{
			return getContactGroupDepth(map.get(targetGroup.getParentGroupId()), depth+1, map);
		}
	}

	
	public static int getMaxContactGroupDepth(List<ContactGroup> list){
		int maxDepth = 0;
		for(ContactGroup g : list ){
			if(g.getLev() > maxDepth){
				maxDepth = g.getLev();
			}
		}
		return maxDepth;
	}
	
	public static String[] getContactGroupNames(List<ContactGroup> list, Long targetGroupId, int maxDepth){
		
		Map<Long, ContactGroup> groupMap = getMapFromContactGroupList(list); 
		String[] groupNames = new String[maxDepth];

		// list 에서 현재 groupId 에 해당하는 ContactGroup 객체를 가져온다. 
		ContactGroup startGroup = groupMap.get(targetGroupId);
		// STEP 1. startPoint를 채운다.
		int startIdx = startGroup.getLev()-1;
		groupNames[startIdx] = startGroup.getGroupName(); 

		// STEP 2. startPoint의 뒤 부분을 채운다. 
		// (만약, startGroup의 레벨이 maxDepth 보다 작다면 groupNames의 lev-1 인덱스부터 maxDepth-1 까지를 startGroup.Name과 동일한 값으로 채운다.)
		if(startGroup.getLev() < maxDepth){
			for(int i= startIdx + 1;  i < maxDepth ; i++){
				groupNames[i] = startGroup.getGroupName();
			}
		}
		
		// STEP 3. startPoint 의 앞 부분을 채운다.	
		// startIdx 에서부터 시작해서 앞부분을 채운다. 
		ContactGroup now = startGroup;
		for(int j= startIdx; j > 0; j--){
			ContactGroup parentGroup = groupMap.get(now.getParentGroupId());
			groupNames[j-1] = parentGroup.getGroupName();
			now = parentGroup;
		}
		
		return groupNames;
	}
	
	/*
	public static List<List<TableCell>> getSpanList(List<String[]> list){
		List<List<TableCell>> result = new ArrayList<List<TableCell>>();
		
		int rowspan = 1;
		for(int i=0; i< list.size(); i++){
			TableCell cell = new TableCell();
			String[] row = list.get(i);
			
			int colspan = 1;
			for(int j=0; j< row.length; j++){
				if(row[j] == row[j+1]){
					colspan++;
				}else{
					cell.setColspan(colspan);
					cell.setLabel(row[j]);
				}
			}
			
			// cell 을 
		}
		
		return result;
	}*/
	
	/**
	 * DB에서 조회한 List<Contact> 로부터 List<List<TableCell>> 를 얻는다. 
	 * @param List<Contact> list :연락처 데이터를 가지고 있는 list
	 * @return List<List<TableCell>> : 변환된 TableCell 2차원 리스트 
	 * @author jay
	 * */
	public static List<List<TableCell>> getTableCellListFromContactList(List<Contact> list){
		List<List<TableCell>> result = new ArrayList<List<TableCell>>();
		
		for(Contact c: list){
			List<TableCell> row = new ArrayList<TableCell>();
			// 동적 GroupsNames 세팅
			String[] groupNames = c.getGroupNames();
			for(int i= 0; i < groupNames.length; i++){
				TableCell cell = new TableCell();
				cell.setLabel(groupNames[i]);
				row.add(cell);
			}
			// 나머지 데이터
			TableCell cell_1 = new TableCell();
			TableCell cell_2 = new TableCell();
			TableCell cell_3 = new TableCell();
			cell_1.setLabel(c.getName());
			cell_2.setLabel("<a href='tel:+" + getTransPhoneNumber(c.getPhone()) + "'><i class='fa fa-phone'></i>"+ c.getPhone() +"</a>");
			cell_3.setLabel("<a href='mailto:" + c.getEmail() +"'><i class='fa fa-envelope'></i>"+c.getEmail() + "</a>");
			row.add(cell_1);
			row.add(cell_2);
			row.add(cell_3);
			
			result.add(row);
		}
		return result;
	}
	
	/**
	 * list 의 모든 cell 을 순회하며 각 셀의 rowspan 값과 colspan 값을 계산한다.
	 * 계산이 끝나면 중복된 데이터를 가지고 있는 셀을 삭제한다.
	 * @param list : 원 데이터를 가지고 있는 TableCell 객체의 2차원 리스트
	 * @author jay
	 * */
	public static List<List<TableCell>> getSpanedList(List<List<TableCell>> list){
		// colspan , rowspan 계산
		for(int i=0; i< list.size(); i++){
			List<TableCell> row = list.get(i);
			for(int j=0; j< row.size(); j++){
				TableCell cell = row.get(j);
				cell.setColspan(getColSpan(list, j, i));
				cell.setRowspan(getRowSpan(list, j, i));
			}
		}
		
		// 중복된 데이터를 가지고 있는 셀 삭제
		for(int k=0; k< list.size(); k++){
			List<TableCell> row = list.get(k);
			List<TableCell> delCells = new ArrayList<TableCell>();
			for(int l=0; l< row.size(); l++){
				TableCell cell = row.get(l);
				if(cell.isRemove()){
					delCells.add(cell);
				}
			}
			// 현재 row에서 중복된 cell  삭제
			for(int z=0; z < delCells.size(); z++){
				row.remove(delCells.get(z));
			}
			
		}
		return list;
	}
	
	/**
	 * 2차원 리스트에서 특정 셀의 colspan 값을 리턴한다. 
	 * [startX, targetY] 좌표에서 시작해서 아래로 이동하며 동일한 값인지를 체크한다. 
	 * 동일한 값이면 rowspan 을 증가시키고, 동일한 값을 가지고 있는 셀은 추후 삭제되도록 체크를 해둔다.
	 * 동일하지 않은 값이 발견된 시점에서 루프를 종료시킨다.  
	 * @param list : 원 데이터를 가지고 있는 TableCell 객체의 2차원 리스트
	 * @param targetX : X축 인덱스(고정)
	 * @param startY : Y축 시작 인덱스
	 * @return 병합될 Y축 rowspan 값
	 * @author jay
	 * */
	public static int getRowSpan(List<List<TableCell>> list, int targetX, int startY){
		int rowspan = 1;
		TableCell targetCell = list.get(startY).get(targetX);
		if(!targetCell.isRemove()){
			for(int y=startY+1; y< list.size(); y++){
				List<TableCell> row = list.get(y);
				TableCell bottom = row.get(targetX);
				if(targetCell.getLabel().equals(bottom.getLabel())){
					rowspan ++;
					//row.remove(targetX); //값이 동일한 셀은 삭제
					bottom.setRemove(true);
				}else{
					break; //인접한 셀의 값이 다르면 중지
				}
			}
		}
		return rowspan;
	}

	/**
	 * 2차원 리스트에서 특정 셀의 colspan 값을 리턴한다. 
	 * [startX, targetY] 좌표에서 시작해서 우측으로 이동하며 동일한 값인지를 체크한다. 
	 * 동일한 값이면 colspan 을 증가시키고, 동일한 값을 가지고 있는 셀은 추후 삭제되도록 체크를 해둔다.
	 * 동일하지 않은 값이 발견된 시점에서 루프를 종료시킨다.  
	 * @param list : 원 데이터를 가지고 있는 TableCell 객체의 2차원 리스트
	 * @param startX : X축의 시작 인덱스
	 * @param targetY : Y축 인덱스(고정)
	 * @return 병합될 X축 colspan 값
	 * @author jay
	 * */
	public static int getColSpan(List<List<TableCell>> list, int startX, int targetY){
		int colspan = 1;
		TableCell targetCell = list.get(targetY).get(startX);
		if(!targetCell.isRemove()){
			List<TableCell> targetRow = list.get(targetY); 
			//System.out.println("target Cell 의 라벨 : " + targetCell.getLabel());
			for(int x= startX+1; x < targetRow.size(); x++){
				TableCell right = targetRow.get(x);
				if(right.getLabel().equals(targetCell.getLabel())){
					colspan ++;
					//targetRow.remove(x);
					right.setRemove(true);
				}else{
					break;
				}
			}
		}
		return colspan;
	}
	
	/**
	 * TableCell 리스트를 Html table 코드로 변환한다.
	 * @param src : 병합될 정보를 가지고 있는 TableCell 객체의 2차원 리스트 
	 * @return String 
	 * @author jay
	 * */
	public static String getHtmlTableFromTableCellList(List<List<TableCell>> src){
		String result = "<table class='table table-bordered k-table'>";
		result += "<tr style='background:#f5f5f5;'><th colspan='3'>상품 및 서비스</th><th>담당자</th><th colspan='2'>연락처</th></tr>";
		for(List<TableCell> row : src){
			result += "<tr>";
			for(TableCell cell: row){
				result += "<td " + (cell.getColspan() > 1? "colspan='" + cell.getColspan() + "'" : ""); 
				result +=  (cell.getRowspan() > 1? "rowspan='" + cell.getRowspan() + "'" : "");
				result += ">" + cell.getLabel() + "</td>";
			}
			result += "</tr>";
		}
		result += "</table>";
		return result;
	}
	
	public static String getTransPhoneNumber(String oldPhoneNumber){
		return oldPhoneNumber.replace(")", "").replace("-", "");
	}
}
