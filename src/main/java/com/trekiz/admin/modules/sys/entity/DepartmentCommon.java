package com.trekiz.admin.modules.sys.entity;

import java.util.List;
import java.util.Set;

/**
 * 
 * @description 部门公用部分：处理显示区域
 *
 * @author baiyakun
 *
 * @create_time 2014-9-25
 */
public class DepartmentCommon {
	
	/** 查询类型 */
	private String checkType = "";
	
	/** 是否是总部人员 */
	private Boolean isHQ = false;
	
	/** 总部部门ID */
	private Long HQId = 1L;
	
	/** 是否可操作 */
	private Boolean operation = false;
	
	/** 是否是自己本部门 */
	private Boolean isSelfDept = true;

	/** 是否是管理员 */
	private Boolean isManager = false;
	
	/** 是否是计调经理 */
	private Boolean isOPManager = false;
	
	/** 是否是计调专员 */
	private Boolean isOP = false;
	
	/** 是否是销售 */
	private Boolean isSale = false;
	
	/** 是否是销售经理 */
	private Boolean isSaleManager = false;
	
	/** 是否是销售经理 */
	private Boolean isOther = false;
	
	/** 要显示区域 */
	private Set<Department> showAreaList = null;
	
	/** 标记要显示的区域是否是父子关系 */
	private Boolean isParentsAndChildren = true;
	
	/** 要查询部门ID */
	private String departmentId = "";
	
	/** 当前用户 */
	private User user;
	
	/** 权限标识符 */
	private String permission = "";
	
	/** 用户在此部门拥有角色类型 */
	private List<String> roleTypeList = null;
	
	private Department dept;
	
	
	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	/** 是否是管理员 */
	public Boolean getIsManager() {
		return isManager;
	}
	/** 是否是管理员 */
	public void setIsManager(Boolean isManager) {
		this.isManager = isManager;
	}
	/** 是否是计调专员 */
	public Boolean getIsOP() {
		return isOP;
	}
	/** 是否是计调专员 */
	public void setIsOP(Boolean isOP) {
		this.isOP = isOP;
	}

	public Set<Department> getShowAreaList() {
		return showAreaList;
	}

	public void setShowAreaList(Set<Department> showAreaList) {
		this.showAreaList = showAreaList;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public Boolean getIsOPManager() {
		return isOPManager;
	}

	public void setIsOPManager(Boolean isOPManager) {
		this.isOPManager = isOPManager;
	}
	/** 是否是销售 */
	public Boolean getIsSale() {
		return isSale;
	}
	/** 是否是销售 */
	public void setIsSale(Boolean isSale) {
		this.isSale = isSale;
	}
	/** 是否是销售经理 */
	public Boolean getIsSaleManager() {
		return isSaleManager;
	}
	/** 是否是销售经理 */
	public void setIsSaleManager(Boolean isSaleManager) {
		this.isSaleManager = isSaleManager;
	}

	public Boolean getIsOther() {
		return isOther;
	}

	public void setIsOther(Boolean isOther) {
		this.isOther = isOther;
	}

	public Boolean getIsHQ() {
		return isHQ;
	}

	public void setIsHQ(Boolean isHQ) {
		this.isHQ = isHQ;
	}

	public Long getHQId() {
		return HQId;
	}

	public void setHQId(Long hQId) {
		HQId = hQId;
	}

	public Boolean getOperation() {
		return operation;
	}

	public void setOperation(Boolean operation) {
		this.operation = operation;
	}

	public Boolean getIsSelfDept() {
		return isSelfDept;
	}

	public void setIsSelfDept(Boolean isSelfDept) {
		this.isSelfDept = isSelfDept;
	}

	public List<String> getRoleTypeList() {
		return roleTypeList;
	}

	public void setRoleTypeList(List<String> roleTypeList) {
		this.roleTypeList = roleTypeList;
	}

	public Boolean getIsParentsAndChildren() {
		return isParentsAndChildren;
	}

	public void setIsParentsAndChildren(Boolean isParentsAndChildren) {
		this.isParentsAndChildren = isParentsAndChildren;
	}
}
