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
import java.util.List;
import java.util.Map;

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
import architecture.common.user.UserManager;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.model.ItemList;
import architecture.ee.web.ws.Property;
import architecture.ee.web.ws.Result;
import architecture.ee.web.ws.StringProperty;
import architecture.user.DefaultGroup;
import architecture.user.GroupAlreadyExistsException;
import architecture.user.GroupManager;
import architecture.user.GroupNotFoundException;
import architecture.user.RoleManager;
import architecture.user.permission.PermissionType;
import architecture.user.permission.Permissions;
import architecture.user.permission.PermissionsManager;

@Controller("secure-user-data-controller")
@RequestMapping("/secure/data")
public class SecureUserDataController {

    private static final Log log = LogFactory.getLog(SecureUserDataController.class);

    @Inject
    @Qualifier("roleManager")
    private RoleManager roleManager;

    @Inject
    @Qualifier("userManager")
    private UserManager userManager;

    @Inject
    @Qualifier("groupManager")
    private GroupManager groupManager;

    @Inject
    @Qualifier("companyManager")
    private CompanyManager companyManager;

    @Inject
    @Qualifier("permissionsManager")
    private PermissionsManager permissionsManager;

    public SecureUserDataController() {

    }

    @RequestMapping(value = "/me/company/get.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Company getCompany(NativeWebRequest request) throws NotFoundException, CompanyNotFoundException {

	User user = SecurityHelper.getUser();
	long companyId = user.getCompanyId();
	return companyManager.getCompany(companyId);
    }

    @RequestMapping(value = "/me/company/update.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Result updateCompany(@RequestBody CompanyTemplate newCompany, NativeWebRequest request)
	    throws CompanyNotFoundException, CompanyAlreadyExistsException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	if (newCompany.getCompanyId() != myCompanyId)
	    return Result.newResult(new UnAuthorizedException());

	Company orgCompany = companyManager.getCompany(newCompany.getCompanyId());
	if (!StringUtils.equals(newCompany.getName(), orgCompany.getName())) {
	    orgCompany.setName(newCompany.getName());
	}
	if (!StringUtils.equals(newCompany.getDisplayName(), orgCompany.getDisplayName())) {
	    orgCompany.setDisplayName(newCompany.getDisplayName());
	}
	if (!StringUtils.equals(newCompany.getDomainName(), orgCompany.getDomainName())) {
	    orgCompany.setDomainName(newCompany.getDomainName());
	}
	if (!StringUtils.equals(newCompany.getDescription(), orgCompany.getDescription())) {
	    orgCompany.setDescription(newCompany.getDescription());
	}
	companyManager.updateCompany(orgCompany);

	return Result.newResult();
    }

    @RequestMapping(value = "/me/company/properties/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<Property> getCompanyPropertyList(NativeWebRequest request) throws CompanyNotFoundException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();

	Company company = companyManager.getCompany(myCompanyId);
	return toPropertyList(company.getProperties());
    }

    @RequestMapping(value = "/me/company/properties/update.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Result updateCompanyPropertyList(@RequestBody StringProperty[] newProperties, NativeWebRequest request)
	    throws CompanyNotFoundException, CompanyAlreadyExistsException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	Company company = companyManager.getCompany(myCompanyId);
	Map<String, String> properties = company.getProperties();
	for (StringProperty property : newProperties) {
	    properties.put(property.getName(), property.getValue());
	}
	if (newProperties.length > 0) {
	    companyManager.updateCompany(company);
	}
	return Result.newResult();
    }

    @RequestMapping(value = "/me/company/properties/delete.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Result deleteCompanyPropertyList(@RequestBody StringProperty[] newProperties, NativeWebRequest request)
	    throws CompanyNotFoundException, CompanyAlreadyExistsException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	Company company = companyManager.getCompany(myCompanyId);

	Map<String, String> properties = company.getProperties();
	for (StringProperty property : newProperties) {
	    properties.remove(property.getName());
	}
	if (newProperties.length > 0) {
	    company.setProperties(properties);
	    companyManager.updateCompany(company);
	}
	return Result.newResult();
    }

    @RequestMapping(value = "/me/company/groups/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ItemList getCompanyGroups(
	    @RequestParam(value = "startIndex", defaultValue = "0", required = false) Integer startIndex,
	    @RequestParam(value = "pageSize", defaultValue = "0", required = false) Integer pageSize,
	    NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	Company company = companyManager.getCompany(myCompanyId);

	int totalCount = companyManager.getTotalCompanyGroupCount(company);
	companyManager.getCompanyGroups(company);
	if (pageSize > 0) {
	    return new ItemList(companyManager.getCompanyGroups(company, startIndex, pageSize), totalCount);
	} else {
	    return new ItemList(companyManager.getCompanyGroups(company), totalCount);
	}
    }

    @RequestMapping(value = "/me/company/groups/create.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Result createCompanyGroup(@RequestBody DefaultGroup newGroup, NativeWebRequest request)
	    throws GroupAlreadyExistsException, CompanyNotFoundException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	Company company = companyManager.getCompany(myCompanyId);
	Group group = groupManager.createGroup(newGroup.getName(), newGroup.getDisplayName(), newGroup.getDescription(),
		company);
	return Result.newResult();
    }

    @RequestMapping(value = "/me/company/groups/update.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Result updateCompanyGroup(@RequestBody DefaultGroup newGroup, NativeWebRequest request)
	    throws GroupNotFoundException, GroupAlreadyExistsException, CompanyNotFoundException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	Company company = companyManager.getCompany(myCompanyId);

	Group group = groupManager.getGroup(newGroup.getGroupId());
	if (group.getCompanyId() != company.getCompanyId()) {
	    return Result.newResult(new UnAuthorizedException());
	}

	if (!StringUtils.equals(newGroup.getName(), group.getName())) {
	    group.setName(newGroup.getName());
	}
	if (!StringUtils.equals(newGroup.getDisplayName(), group.getDisplayName())) {
	    group.setDisplayName(newGroup.getDisplayName());
	}
	if (!StringUtils.equals(newGroup.getDescription(), group.getDescription())) {
	    group.setDescription(newGroup.getDescription());
	}
	groupManager.updateGroup(group);
	return Result.newResult();
    }

    @RequestMapping(value = "/me/company/users/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ItemList getCompanyUsers(
	    @RequestParam(value = "startIndex", defaultValue = "0", required = false) Integer startIndex,
	    @RequestParam(value = "pageSize", defaultValue = "0", required = false) Integer pageSize,
	    NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {

	User user = SecurityHelper.getUser();
	long myCompanyId = user.getCompanyId();
	Company company = companyManager.getCompany(myCompanyId);

	int totalCount = companyManager.getTotalCompanyUserCount(company);
	if (pageSize > 0) {
	    return new ItemList(companyManager.getCompanyUsers(company, startIndex, pageSize), totalCount);
	} else {
	    return new ItemList(companyManager.getCompanyUsers(company), totalCount);
	}
    }

    @RequestMapping(value = "/permissions/add.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Permission addUserPermission(
	    @RequestParam(value = "objectType", defaultValue = "0", required = false) Integer objectType,
	    @RequestParam(value = "objectId", defaultValue = "0", required = false) Long objectId,
	    @RequestParam(value = "permission", defaultValue = "0", required = true) String permission,
	    @RequestParam(value = "type", defaultValue = "ADDITIVE", required = false) String type) {
	User user = SecurityHelper.getUser();
	PermissionType permissionType = PermissionType.valueOf(type);
	long mask = permissionsManager.getPermissionMask(permission);
	permissionsManager.addUserPermission(objectType, objectId, user, permissionType, mask);
	return new Permission(objectType, objectId, permission, type, true);
    }

    @RequestMapping(value = "/me/permissions/has.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Permission hasPermissions(
	    @RequestParam(value = "objectType", defaultValue = "0", required = false) Integer objectType,
	    @RequestParam(value = "objectId", defaultValue = "0", required = false) Long objectId,
	    @RequestParam(value = "permission", defaultValue = "0", required = true) String permission,
	    @RequestParam(value = "type", defaultValue = "ADDITIVE", required = false) String type) {
	User user = SecurityHelper.getUser();
	PermissionType permissionType = PermissionType.valueOf(type);
	Permissions perms1 = permissionsManager.getFinalUserPerms(objectType, objectId, user.getUserId(),
		permissionType);

	long mask = 0L;
	if (Permissions.PermissionAtom.valueOf(permission) != null)
	    mask = Permissions.PermissionAtom.valueOf(permission).getAtomId();
	else
	    mask = permissionsManager.getPermissionMask(permission);

	return new Permission(objectType, objectId, permission, type, perms1.hasPermission(mask));
    }

    protected List<Property> toPropertyList(Map<String, String> properties) {
	List<Property> list = new ArrayList<Property>();
	for (String key : properties.keySet()) {
	    String value = properties.get(key);
	    list.add(new Property(key, value));
	}
	return list;
    }

    static public class Permission {

	private int objectType;

	private long objectId;

	private String permissionType;

	private String permission;

	private boolean granted;

	private long targetId;

	public Permission() {
	    this.objectType = 0;
	    this.objectId = -1L;
	    this.granted = false;
	    this.targetId = -1L;
	    this.permissionType = "ADDITIVE";
	}

	public Permission(int objectType, long objectId, String permissionType, String permission, boolean granted) {
	    this.objectType = objectType;
	    this.objectId = objectId;
	    this.permissionType = permissionType;
	    this.permission = permission;
	    this.granted = granted;
	    this.targetId = -1L;
	}

	public long getTargetId() {
	    return targetId;
	}

	public void setTargetId(long targetId) {
	    this.targetId = targetId;
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

	public String getPermissionType() {
	    return permissionType;
	}

	public void setPermissionType(String permissionType) {
	    this.permissionType = permissionType;
	}

	public String getPermission() {
	    return permission;
	}

	public void setPermission(String permission) {
	    this.permission = permission;
	}

	public boolean isGranted() {
	    return granted;
	}

	public void setGranted(boolean granted) {
	    this.granted = granted;
	}

    }
}
