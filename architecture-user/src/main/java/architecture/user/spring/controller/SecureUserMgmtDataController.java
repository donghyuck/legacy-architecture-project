/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.user.spring.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.Company;
import architecture.common.user.CompanyAlreadyExistsException;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.CompanyTemplate;
import architecture.common.user.Group;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.spring.controller.MyCloudDataController.ItemList;
import architecture.ee.web.ws.Property;
import architecture.ee.web.ws.Result;
import architecture.ee.web.ws.StringProperty;
import architecture.user.DefaultGroup;
import architecture.user.DefaultRole;
import architecture.user.GroupAlreadyExistsException;
import architecture.user.GroupManager;
import architecture.user.GroupNotFoundException;
import architecture.user.Role;
import architecture.user.RoleAlreadyExistsException;
import architecture.user.RoleManager;
import architecture.user.RoleNotFoundException;
import architecture.user.permission.PermissionType;
import architecture.user.permission.Permissions;
import architecture.user.permission.PermissionsManager;
import architecture.user.permission.PermissionsManagerHelper;


@Controller ("secure-user-mgmt-data-controller")
@RequestMapping("/secure/data")
public class SecureUserMgmtDataController {

	private static final Log log = LogFactory.getLog(SecureUserMgmtDataController.class);
	
	@Inject
	@Qualifier("roleManager")
	private RoleManager roleManager ;
	
	@Inject
	@Qualifier("userManager")
	private UserManager userManager ;

	@Inject
	@Qualifier("groupManager")
	private GroupManager groupManager ;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	
	@Inject
	@Qualifier("permissionsManager")
	private PermissionsManager permissionsManager;
	
	
	public SecureUserMgmtDataController() {
		// TODO 자동 생성된 생성자 스텁
	}

	@RequestMapping(value="/mgmt/role/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getRoleList(
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws NotFoundException {				
		User user = SecurityHelper.getUser();	
		int totalCount = roleManager.getTotalRoleCount();		
		return new ItemList( roleManager.getRoles(), totalCount );	
	}

	
	@RequestMapping(value="/mgmt/role/create.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result createRole(
			@RequestBody DefaultRole newRole,
			NativeWebRequest request) throws RoleNotFoundException, RoleAlreadyExistsException {				
		User user = SecurityHelper.getUser();			
		roleManager.createRole(newRole.getName(), newRole.getDescription());
		return Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/role/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateRole(
			@RequestBody DefaultRole newRole,
			NativeWebRequest request) throws RoleNotFoundException, RoleAlreadyExistsException {		
		
		User user = SecurityHelper.getUser();			
		Role role = roleManager.getRole(newRole.getRoleId());
		if( !StringUtils.equals(newRole.getName(), role.getName())){
			role.setName(newRole.getName());
		}
		if( newRole.getMask() != role.getMask()){
			role.setMask(newRole.getMask());
		}
		if( !StringUtils.equals(newRole.getDescription(), role.getDescription())){
			role.setDescription(newRole.getDescription());
		}			
		roleManager.updateRole(role);
		return Result.newResult();
	}	
	
	
	@RequestMapping(value="/mgmt/company/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getCompanyList(
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws NotFoundException {			
		
		User user = SecurityHelper.getUser();	
		int totalCount = companyManager.getTotalCompanyCount();
		if( pageSize > 0 ){			
			return new ItemList( companyManager.getCompanies(startIndex, pageSize), totalCount );
		}else{
			return new ItemList( companyManager.getCompanies(), totalCount );
		}
	}

	
	@RequestMapping(value="/mgmt/company/create.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result createCompany(
			@RequestBody CompanyTemplate newCompany,
			NativeWebRequest request) throws CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();		
		companyManager.createCompany(newCompany.getName(), newCompany.getDisplayName(), newCompany.getDomainName(), newCompany.getDescription());		
		return Result.newResult();
	}	
	
	@RequestMapping(value="/mgmt/company/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateCompany(
			@RequestBody CompanyTemplate newCompany,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {					
		User user = SecurityHelper.getUser();				
		Company oldCompany = companyManager.getCompany(newCompany.getCompanyId());
		if( !StringUtils.equals(newCompany.getName(), oldCompany.getName())){
			oldCompany.setName(newCompany.getName());
		}
		if( !StringUtils.equals(newCompany.getDisplayName(), oldCompany.getDisplayName())){
			oldCompany.setDisplayName(newCompany.getDisplayName());
		}		
		if( !StringUtils.equals(newCompany.getDomainName(), oldCompany.getDomainName())){
			oldCompany.setDomainName(newCompany.getDomainName());
		}	
		if( !StringUtils.equals(newCompany.getDescription(), oldCompany.getDescription())){
			oldCompany.setDescription(newCompany.getDescription());
		}	
		companyManager.updateCompany(oldCompany);		
		return Result.newResult();
	}	

	@RequestMapping(value="/mgmt/company/properties/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property> getCompanyPropertyList(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			NativeWebRequest request) throws CompanyNotFoundException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		return toPropertyList(company.getProperties());
	}	

	@RequestMapping(value="/mgmt/company/properties/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateCompanyPropertyList(
			@RequestBody StringProperty[] newProperties,
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		Map<String, String> properties = company.getProperties();	
		for (StringProperty property : newProperties) {
			properties.put(property.getName(), property.getValue());
		}	
		if( newProperties.length > 0){
			companyManager.updateCompany(company);
		}
		return Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/company/properties/delete.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result deleteCompanyPropertyList(
			@RequestBody StringProperty[] newProperties,
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		Map<String, String> properties = company.getProperties();	
		for (StringProperty property : newProperties) {
			properties.remove(property.getName());
		}
		if( newProperties.length > 0){
			company.setProperties(properties);
			companyManager.updateCompany(company);
		}
		return Result.newResult();
	}
		
	@RequestMapping(value="/mgmt/company/groups/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getCompanyGroups(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		int totalCount = companyManager.getTotalCompanyGroupCount(company);
		companyManager.getCompanyGroups(company);
		if( pageSize > 0 ){			
			return new ItemList( companyManager.getCompanyGroups(company, startIndex, pageSize), totalCount );
		}else{
			return new ItemList( companyManager.getCompanyGroups(company), totalCount );
		}
	}	

	@RequestMapping(value="/mgmt/company/groups/create.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result createCompanyGroup(
			@RequestBody DefaultGroup newGroup,
			NativeWebRequest request) throws GroupAlreadyExistsException, CompanyNotFoundException {		
		
		User user = SecurityHelper.getUser();					
		Company company = companyManager.getCompany(newGroup.getCompany().getCompanyId());
		Group group = groupManager.createGroup(newGroup.getName(), newGroup.getDisplayName(), newGroup.getDescription(), company);
				
		return Result.newResult();
	}	
	
	@RequestMapping(value="/mgmt/company/groups/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateCompanyGroup(
			@RequestBody DefaultGroup newGroup,
			NativeWebRequest request) throws GroupNotFoundException, GroupAlreadyExistsException {		
		
		User user = SecurityHelper.getUser();			
		Group group = groupManager.getGroup(newGroup.getGroupId());		
		
		if( !StringUtils.equals(newGroup.getName(), group.getName())){
			group.setName(newGroup.getName());
		}	
		if( !StringUtils.equals(newGroup.getDisplayName(), group.getDisplayName())){
			group.setDisplayName(newGroup.getDisplayName());
		}			
		if( !StringUtils.equals(newGroup.getDescription(), group.getDescription())){
			group.setDescription(newGroup.getDescription());
		}		
		groupManager.updateGroup(group);
		return Result.newResult();
	}	
	
	@RequestMapping(value="/mgmt/company/users/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getCompanyUsers(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		User user = SecurityHelper.getUser();			
		Company company = companyManager.getCompany(companyId);		
		int totalCount = companyManager.getTotalCompanyUserCount(company);
		if( pageSize > 0 ){			
			return new ItemList( companyManager.getCompanyUsers(company, startIndex, pageSize), totalCount );
		}else{
			return new ItemList( companyManager.getCompanyUsers(company), totalCount );
		}
	}		

	@RequestMapping(value="/mgmt/company/users/find.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList findCompanyUsers(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,	
			@RequestParam(value="groupId", defaultValue="0", required=false ) Long groupId,	
			@RequestParam(value="nameOrEmail", defaultValue="", required=true ) String nameOrEmail,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,			
			NativeWebRequest request) throws  UserNotFoundException, CompanyNotFoundException, GroupNotFoundException {		
		
		User user = SecurityHelper.getUser();
		
		String nameOrEmailToUse = "%";
		
		if( !StringUtils.isBlank(nameOrEmail)){
			nameOrEmailToUse = "%"+ nameOrEmail + "%";
		}
		
		List<User> list = Collections.EMPTY_LIST;		
		int totalCount = 0;		
		if(companyId > 0){
			Company company = companyManager.getCompany(companyId);	
			if( groupId >0){
				Group group = groupManager.getGroup(groupId);
				if( company.getCompanyId() == group.getCompanyId() ){
					totalCount = userManager.getFoundUserCountWithGroupFilter(company, group, nameOrEmailToUse);
					if( pageSize > 0 ){		
						list = userManager.findUsersWithGroupFilter(company, group, nameOrEmailToUse, startIndex, pageSize);				
					}else{
						list = userManager.findUsersWithGroupFilter(company, group, nameOrEmailToUse);
					}
				}
			}else{			
				totalCount = userManager.getFoundUserCount(company, nameOrEmailToUse);		
				if( pageSize > 0 ){		
					list = userManager.findUsers(company, nameOrEmailToUse, startIndex, pageSize);				
				}else{
					list = userManager.findUsers(company, nameOrEmailToUse);
				}
			}
		}else{
			totalCount = userManager.getFoundUserCount(nameOrEmailToUse);
			if( totalCount > 0)
				list = userManager.findUsers(nameOrEmailToUse);
		}		
		return new ItemList(list, totalCount );
	}	
	
	
	/** GROUP MGMT 
	 * @throws GroupNotFoundException **/
	@RequestMapping(value="/mgmt/group/properties/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property> getGroupPropertyList(
			@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,
			NativeWebRequest request) throws GroupNotFoundException {			
		
		User user = SecurityHelper.getUser();	
		Group g = groupManager.getGroup(groupId);
		return toPropertyList(g.getProperties());
	}	

	@RequestMapping(value="/mgmt/group/properties/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateGroupPropertyList(
			@RequestBody StringProperty[] newProperties,
			@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,
			NativeWebRequest request) throws GroupNotFoundException, GroupAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Group g = groupManager.getGroup(groupId);
		Map<String, String> properties = g.getProperties();	
		for (StringProperty property : newProperties) {
			properties.put(property.getName(), property.getValue());
		}	
		if( newProperties.length > 0){
			g.setProperties(properties);
			groupManager.updateGroup(g);
		}
		return Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/group/properties/delete.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result deleteGroupPropertyList(
			@RequestBody StringProperty[] newProperties,
			@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,
			NativeWebRequest request) throws GroupNotFoundException, GroupAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Group g = groupManager.getGroup(groupId);
		Map<String, String> properties = g.getProperties();	
		for (StringProperty property : newProperties) {
			properties.remove(property.getName());
		}
		if( newProperties.length > 0){
			g.setProperties(properties);
			groupManager.updateGroup(g);
		}
		return Result.newResult();
	}
	

	@RequestMapping(value="/mgmt/group/roles/list_with_ownership.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Ownership> getGroupRoleOwnership(
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		
		Group g = groupManager.getGroup(groupId);
		List<Role> roles = roleManager.getRoles();
		List<Role> finalGroupRoles = roleManager.getFinalGroupRoles(g.getGroupId());
		List<Ownership> list = new ArrayList<Ownership>(roles.size());		
		for( Role role : roles){
			boolean inherited = false;
			boolean ownership = false;
			if( finalGroupRoles.contains(role)){
				ownership = true;				
			}				
			list.add(new Ownership( role, ownership, inherited) );
		}		
		return list;		
	}
	

	@RequestMapping(value="/mgmt/group/roles/add.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result addGroupRole(
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		@RequestParam(value="roleId", defaultValue="0", required=true ) Long roleId,	
		NativeWebRequest request) throws  RoleNotFoundException, GroupNotFoundException {	
		Group group = groupManager.getGroup(groupId);
		Role role = roleManager.getRole(roleId);
		roleManager.addGroupRole(group, role);
		return  Result.newResult();	
	}

	@RequestMapping(value="/mgmt/group/roles/remove.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result removeGroupRole(
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		@RequestParam(value="roleId", defaultValue="0", required=true ) Long roleId,	
		NativeWebRequest request) throws  RoleNotFoundException, GroupNotFoundException {	
		
		Group group = groupManager.getGroup(groupId);
		Role role = roleManager.getRole(roleId);
		roleManager.removeGroupRole(group, role);

		return  Result.newResult();
	}

	
	@RequestMapping(value="/mgmt/group/roles/update.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result updateGroupRoles(
		@RequestBody List<DefaultRole> newRoles,	
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {			
		
		Group group = groupManager.getGroup(groupId);		
		// group roles
		List<Role> finalGroupRoles = roleManager.getFinalGroupRoles(group.getGroupId());
		
		List<Role> plus = new ArrayList<Role>();
		List<Role> minus = new ArrayList<Role>();
		
		for( Role role : newRoles ){
			if( !finalGroupRoles.contains(role) ){
				plus.add(role);
			}		
		}
		for( Role role : finalGroupRoles ){
			if( !newRoles.contains(role) ){
				minus.add(role);
			}		
		}		
		if( minus.size() > 0)
			roleManager.removeGroupRole(group, minus);		
		if( plus.size() > 0)
			roleManager.addGroupRole(group, plus);
		
		return Result.newResult();
	}	
	
	@RequestMapping(value="/mgmt/group/users/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getGroupUsers(
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
		@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,		
		NativeWebRequest request) throws GroupNotFoundException {	
		Group group = groupManager.getGroup(groupId);				
		List<User> list = groupManager.getGroupUsers(group, startIndex, pageSize);
		int totalCount = groupManager.getTotalGroupUserCount(group);
		
		return new ItemList(list, totalCount);
	}

	@RequestMapping(value="/mgmt/group/users/add.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result addGroupMemberships(
		@RequestParam(value="userIds[]", required=true ) Long[] userIds,	
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		
		Group group = groupManager.getGroup(groupId);
		List<User> users = new ArrayList<User>();
		for( long userId : userIds){
			User user = userManager.getUser(userId);
			if( group.getCompanyId() == user.getCompanyId() )
				users.add(user);
		}
		
		groupManager.addMembership(group, users);
		
		return  Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/group/users/remove.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result removeGroupMemberships(
		@RequestParam(value="userIds[]", required=true ) Long[] userIds,	
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		
		Group group = groupManager.getGroup(groupId);
		List<User> users = new ArrayList<User>();
		for( long userId : userIds){
			User user = userManager.getUser(userId);
			if( group.getCompanyId() == user.getCompanyId() )
				users.add(user);
		}
		
		groupManager.removeMembership(group, users);
		
		return  Result.newResult();
	}
	
	/** USER MGMT **/
	
	@RequestMapping(value="/mgmt/user/properties/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property> getUserPropertyList(
			@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,
			NativeWebRequest request) throws  UserNotFoundException {			
		
		User user = SecurityHelper.getUser();	
		User u = userManager.getUser(userId);
		return toPropertyList(u.getProperties());
	}	

	@RequestMapping(value="/mgmt/user/properties/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateUserPropertyList(
			@RequestBody StringProperty[] newProperties,
			@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,
			NativeWebRequest request) throws UserNotFoundException, UserAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		User u = userManager.getUser(userId);
		Map<String, String> properties = u.getProperties();	
		for (StringProperty property : newProperties) {
			properties.put(property.getName(), property.getValue());
		}	
		if( newProperties.length > 0){
			userManager.updateUser(u);
		}
		return Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/user/properties/delete.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result deleteUserPropertyList(
			@RequestBody StringProperty[] newProperties,
			@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,
			NativeWebRequest request) throws UserNotFoundException, UserAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		User u = userManager.getUser(userId);
		Map<String, String> properties = u.getProperties();	
		for (StringProperty property : newProperties) {
			properties.remove(property.getName());
		}
		if( newProperties.length > 0){
			u.setProperties(properties);
			userManager.updateUser(u);
		}
		return Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/user/groups/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Group> getUserGroups(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException {	
		User u = userManager.getUser(userId);
		return groupManager.getUserGroups(u);				
	}

	@RequestMapping(value="/mgmt/user/groups/list_with_membership.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Membership> getUserGroupMemberships(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException {	
		
		User user = userManager.getUser(userId);
		List<Group> groups = companyManager.getCompanyGroups(user.getCompany());
		List<Group> memberships = groupManager.getUserGroups(user);
		List<Membership> list = new ArrayList<Membership>(groups.size());
		for( Group g : groups){
			if( memberships.contains(g)) {
				list.add(new Membership(g, true ) );
			} else {
				list.add(new Membership(g, false) );
			}
		}
		
		return list;
	}
	
	public static class Membership{
		private long groupId;
		private String name;
		private String displayName ;
		private String description;
		private boolean membership;		
		
		private Membership(Group group, boolean membership) {
			this.membership = membership;
			this.groupId = group.getGroupId();
			this.name = group.getName();
			this.displayName = group.getDisplayName();
			this.description = group.getDescription();			
		}		
		/**
		 * 
		 */
		public Membership() {
		}
		/**
		 * @return groupId
		 */
		public long getGroupId() {
			return groupId;
		}
		/**
		 * @param groupId 설정할 groupId
		 */
		public void setGroupId(long groupId) {
			this.groupId = groupId;
		}
		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name 설정할 name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return displayName
		 */
		public String getDisplayName() {
			return displayName;
		}
		/**
		 * @param displayName 설정할 displayName
		 */
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		/**
		 * @return description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description 설정할 description
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @return membership
		 */
		public boolean isMembership() {
			return membership;
		}
		/**
		 * @param membership 설정할 membership
		 */
		public void setMembership(boolean membership) {
			this.membership = membership;
		}
	}

	@RequestMapping(value="/mgmt/user/groups/add.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result addGroupMembership(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		User user = userManager.getUser(userId);
		Group group = groupManager.getGroup(groupId);
		
		if( user.getCompanyId() == group.getCompanyId() )
			groupManager.addMembership(group, user);
		
		return  Result.newResult();	
	}

	@RequestMapping(value="/mgmt/user/groups/remove.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result removeGroupMembership(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		@RequestParam(value="groupId", defaultValue="0", required=true ) Long groupId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		
		User user = userManager.getUser(userId);
		Group group = groupManager.getGroup(groupId);
		
		if( user.getCompanyId() == group.getCompanyId() )
			groupManager.removeMembership(group, user);		
		return  Result.newResult();
	}
	
	
	@RequestMapping(value="/mgmt/user/roles/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Role> getFinalUserRoles(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		
		User user = userManager.getUser(userId);
		return roleManager.getFinalUserRoles(user.getUserId());
	}
	
	@RequestMapping(value="/mgmt/user/roles/list_from_groups.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Role> getUserGroupRoles(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {			
		User user = userManager.getUser(userId);
		return roleManager.getUserRolesFromGroup(user.getUserId());
	}

	@RequestMapping(value="/mgmt/user/roles/list_from_user.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Role> getUserRoles(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {			
		User user = userManager.getUser(userId);	
    	return extractUserRoles(roleManager.getFinalUserRoles(user.getUserId()), roleManager.getUserRolesFromGroup(user.getUserId()));		
	}
	

	@RequestMapping(value="/mgmt/user/roles/list_with_ownership.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Ownership> getUserRoleOwnership(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {	
		
		User user = userManager.getUser(userId);		
		List<Role> roles = roleManager.getRoles();
		List<Role> finalUserRoles = roleManager.getFinalUserRoles(user.getUserId());
		List<Role> userRolesFromGroup = roleManager.getUserRolesFromGroup(user.getUserId());
		List<Ownership> list = new ArrayList<Ownership>(roles.size());		
		for( Role role : roles){
			boolean inherited = false;
			boolean ownership = false;
			if( userRolesFromGroup.contains(role)){
				inherited = true;				
			}	
			if( finalUserRoles.contains(role)){
				ownership = true;				
			}				
			list.add(new Ownership( role, ownership, inherited) );
		}		
		return list;		
	}
		
	public static class Ownership{
		private long objectId;
		private String name;
		private String displayName ;
		private String description;
		private boolean ownership;		
		private boolean inherited;

		private Ownership(Role role, boolean ownership, boolean inherited ) {
			this.ownership = ownership;
			this.inherited = inherited;
			this.objectId = role.getRoleId();
			this.name = role.getName();
			this.displayName = role.getName();
			this.description = role.getDescription();
		}
		
		private Ownership(Role role, boolean ownership ) {
			this.ownership = ownership;
			this.inherited = false;
			this.objectId = role.getRoleId();
			this.name = role.getName();
			this.displayName = role.getName();
			this.description = role.getDescription();
		}		
		/**
		 * 
		 */
		public Ownership() {
		}
		/**
		 * @return objectId
		 */
		public long getObjectId() {
			return objectId;
		}
		/**
		 * @param objectId 설정할 objectId
		 */
		public void setObjectId(long objectId) {
			this.objectId = objectId;
		}
		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name 설정할 name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return displayName
		 */
		public String getDisplayName() {
			return displayName;
		}
		/**
		 * @param displayName 설정할 displayName
		 */
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		/**
		 * @return description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description 설정할 description
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @return ownership
		 */
		public boolean isOwnership() {
			return ownership;
		}
		/**
		 * @param ownership 설정할 ownership
		 */
		public void setOwnership(boolean ownership) {
			this.ownership = ownership;
		}
		/**
		 * @return inherited
		 */
		public boolean isInherited() {
			return inherited;
		}
		/**
		 * @param inherited 설정할 inherited
		 */
		public void setInherited(boolean inherited) {
			this.inherited = inherited;
		}
		
	}
	
	protected List<Role> extractUserRoles(List<Role> finalUserRoles, List<Role> finalGroupRoles){
		List<Role> roles = new ArrayList<Role>();
		for( Role role : finalUserRoles ){
			if(!finalGroupRoles.contains(role)){
				roles.add(role);
			}
		}
		return roles;		
	}

	@RequestMapping(value="/mgmt/user/roles/add.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result addUserRole(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		@RequestParam(value="roleId", defaultValue="0", required=true ) Long roleId,	
		NativeWebRequest request) throws UserNotFoundException, RoleNotFoundException {	
		User user = userManager.getUser(userId);
		Role role = roleManager.getRole(roleId);
		roleManager.addUserRole(user, role);		
		return  Result.newResult();	
	}

	@RequestMapping(value="/mgmt/user/roles/remove.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result removeUserRole(
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		@RequestParam(value="roleId", defaultValue="0", required=true ) Long roleId,	
		NativeWebRequest request) throws UserNotFoundException, RoleNotFoundException {	
		
		User user = userManager.getUser(userId);
		Role role = roleManager.getRole(roleId);
		roleManager.removeUserRole(user, role);

		return  Result.newResult();
	}

	
	@RequestMapping(value="/mgmt/user/roles/update.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result updateUserRoles(
		@RequestBody List<DefaultRole> newRoles,	
		@RequestParam(value="userId", defaultValue="0", required=true ) Long userId,	
		NativeWebRequest request) throws UserNotFoundException, GroupNotFoundException {			
		
		User user = userManager.getUser(userId);
		// total roles 
		List<Role> finalUserRoles = roleManager.getFinalUserRoles(user.getUserId());
		
		// group roles
		List<Role> finalGroupRoles = roleManager.getUserRolesFromGroup(user.getUserId());
		
		// user roles
		List<Role> userRoles = extractUserRoles(finalUserRoles, finalGroupRoles);
		
		List<Role> plus = new ArrayList<Role>();
		List<Role> minus = new ArrayList<Role>();
		
		for( Role role : newRoles ){
			if( !userRoles.contains(role) && !finalGroupRoles.contains(role) ){
				plus.add(role);
			}		
		}
		for( Role role : userRoles ){
			if( !newRoles.contains(role) && !finalGroupRoles.contains(role) ){
				minus.add(role);
			}		
		}		
		if( minus.size() > 0)
			roleManager.removeUserRole(user, minus);		
		if( plus.size() > 0)
			roleManager.addUserRole(user, plus);
		
		return Result.newResult();
	}
	
	protected List<Property> toPropertyList (Map<String, String> properties){
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	/** PERMISSIONS MGMT */
	
	private PermissionsManagerHelper getPermissionsManagerHelper(int objectType, long objectId){
		PermissionsManagerHelper helper = new PermissionsManagerHelper(objectType, objectId);
		helper.setPermissionsManager(permissionsManager);
		return helper;
	}
	
	@RequestMapping(value="/mgmt/permissions/list.json",method={RequestMethod.POST} )
	@ResponseBody
	public Map<String, Object> listAllPermissions(
		@RequestBody PermsSetForm permsSetGroup
	){
		User currentUser = SecurityHelper.getUser();
		
		List<PermSet> list1 = new ArrayList<PermSet>();
		List<PermSet> list2 = new ArrayList<PermSet>();
		List<UserPermSet> list3 = new ArrayList<UserPermSet>();
		List<GroupPermSet> list4 = new ArrayList<GroupPermSet>();
		TreeMap<User, UserPermSet> tree1 = new TreeMap<User, UserPermSet>(new Comparator<User>(){
			public int compare(User o1, User o2) {
				return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
			}
		});
		TreeMap<Group, GroupPermSet> tree2 = new TreeMap<Group, GroupPermSet>(new Comparator<Group>(){
			public int compare(Group o1, Group o2) {
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}
		});		
		PermissionsManagerHelper helper = getPermissionsManagerHelper(permsSetGroup.getObjectType(), permsSetGroup.getObjectId());		
		
		for( String permissionName : permsSetGroup.getPerms())
		{
			long permission = Permissions.PermissionAtom.valueOf(permissionName).getAtomId();		
			if( Permissions.PermissionAtom.valueOf(permissionName) != null)
				permission = Permissions.PermissionAtom.valueOf(permissionName).getAtomId();
			else		
				permission = permissionsManager.getPermissionMask(permissionName);
			
			
			log.debug("permission:" + permissionName + "(" + permission + ")");
			
			// anonymous 
			PermSet p1 = new PermSet(permissionName);
			p1.setAdditive(helper.anonymousUserHasPermission(PermissionType.ADDITIVE, permission));
			p1.setNegative(helper.anonymousUserHasPermission(PermissionType.NEGATIVE, permission));
			p1.setInherited(false);
			list1.add(p1);
			// member 
			PermSet p2 = new PermSet(permissionName);
			p2.setAdditive(helper.registeredUserHasPermission(PermissionType.ADDITIVE, permission));
			p2.setNegative(helper.registeredUserHasPermission(PermissionType.NEGATIVE, permission));
			p2.setInherited(false);
			list2.add(p2);			

			// users 

			
			log.debug("user : " +  helper.usersWithPermissionCount(PermissionType.ADDITIVE, permission));
			for(User user : helper.usersWithPermission(PermissionType.ADDITIVE, permission)){
				if(tree1.containsKey(user))
				{					
					UserPermSet up = tree1.get(user);
					up.getPermSet(permissionName, true).setAdditive(true);					
				}else{
					UserPermSet up = new UserPermSet(user);
					up.getPermSet(permissionName, true).setAdditive(true);	
					tree1.put(user, up);
				}				
			}		
			for(User user : helper.usersWithPermission(PermissionType.NEGATIVE, permission)){
				if(tree1.containsKey(user))
				{					
					UserPermSet up = tree1.get(user);
					up.getPermSet(permissionName, true).setNegative(true);					
				}else{
					UserPermSet up = new UserPermSet(user);
					up.getPermSet(permissionName, true).setNegative(true);	
					tree1.put(user, up);
				}	
			}			
			
			
			// groups
			log.debug("group : " +  helper.groupsWithPermissionCount(PermissionType.ADDITIVE, permission));

			for(Group group : helper.groupsWithPermission(PermissionType.ADDITIVE, permission)){
				if(tree1.containsKey(group))
				{					
					GroupPermSet gp = tree2.get(group);
					gp.getPermSet(permissionName, true).setAdditive(true);					
				}else{
					GroupPermSet gp = new GroupPermSet(group);
					gp.getPermSet(permissionName, true).setAdditive(true);	
					tree2.put(group, gp);
				}				
			}		
			for(Group group : helper.groupsWithPermission(PermissionType.NEGATIVE, permission)){
				if(tree1.containsKey(group))
				{					
					GroupPermSet gp = tree2.get(group);
					gp.getPermSet(permissionName, true).setNegative(true);					
				}else{
					GroupPermSet gp = new GroupPermSet(group);
					gp.getPermSet(permissionName, true).setNegative(true);	
					tree2.put(group, gp);
				}	
			}			
			
			
		}		
		
		list3.addAll(tree1.values());
		list4.addAll(tree2.values());	
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("anonymous", list1);
		map.put("member", list2);
		map.put("users", list3);
		map.put("groups", list4);
		return map;
	}
	
	
	
	static public class PermsSetForm {	
		
		private int objectType ;
		private long objectId ;
		private List<String> perms;
		
		public PermsSetForm() {
			objectType = 0;
			objectId = 0 ;
			perms = Collections.EMPTY_LIST;
		}
		
		public int getObjectType() {
			return objectType;
		}
		public void setObjectType(int objectType) {
			this.objectType = objectType;
		}
		public long getObjectId() {
			return objectId;
		}
		public void setObjectId(long objectId) {
			this.objectId = objectId;
		}
		public List<String> getPerms() {
			return perms;
		}
		public void setPerms(List<String> perms) {
			this.perms = perms;
		}		
	}

	static public class UserPermSet {
		private User user;
		private Map<String, PermSet> perms;
		
		public UserPermSet(User user) {
			this.user = user;
			this.perms = new HashMap<String,PermSet>();
		}

		public User getUser() {
			return user;
		}
		
		public void setUser(User user) {
			this.user = user;
		}
		
		public Map<String, PermSet> getPerms() {
			return perms;
		}
		
		public void setPerms(Map<String, PermSet> perms) {
			this.perms = perms;
		}
		
		public PermSet getPermSet(String name, boolean createIfNotExist){
			PermSet has = perms.get(name);
			if( has == null && createIfNotExist ){
				has= new PermSet(name);		
				has.additive = false;
				has.inherited = false;
				has.negative = false;
				perms.put(name, has);
			}
			return has;			
		}
		
		public boolean hasPermSet(String name){
			
			if( perms.get(name) == null )
				return false;
			else 
				return true;
		}
		
	}
	
	static public class GroupPermSet {
		private Group groups;
		private List<PermSet> perms;
		
		public GroupPermSet(Group user) {
			this.groups = user;
			this.perms = new ArrayList<PermSet>();
		}

		public Group getUser() {
			return groups;
		}
		
		public void setUser(Group user) {
			this.groups = user;
		}
		
		public List<PermSet> getPerms() {
			return perms;
		}
		
		public void setPerms(List<PermSet> perms) {
			this.perms = perms;
		}
		
		public PermSet getPermSet(String name, boolean createIfNotExist){
			PermSet has = null;
			for(PermSet p : perms){
				if( p.getName().equals(name) )
				{		
					has = p;					
					break;
				}
			}
			if( has == null && createIfNotExist){
				has= new PermSet(name);		
				has.additive = false;
				has.inherited = false;
				has.negative = false;
				perms.add(has);
			}
			return has;			
		}
		
		public boolean hasPermSet(String name){
			boolean has = false;
			for(PermSet p : perms){
				if( p.getName().equals(name) )
				{		
					has = true;					
					break;
				}
			}
			return has;
		}
		
	}
	
	static public class PermSet {		

		private String name;		
		private boolean inherited;
		private boolean additive;		
		private boolean negative;		
		
		public PermSet(String permission) {
			this.name = permission;
		}

		public boolean isInherited() {
			return inherited;
		}

		public void setInherited(boolean inherited) {
			this.inherited = inherited;
		}

		public boolean isAdditive() {
			return additive;
		}

		public void setAdditive(boolean additive) {
			this.additive = additive;
		}

		public boolean isNegative() {
			return negative;
		}

		public void setNegative(boolean negative) {
			this.negative = negative;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
			
	}
}
