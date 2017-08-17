package com.trekiz.admin.modules.sys.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * 
 * @description 部门结构
 *
 * @author baiyakun
 *
 * @create_time 2014-9-15
 */
@Entity
@Table(name = "department")
@DynamicInsert @DynamicUpdate
public class Department extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name; //部门名称（中文）
	
	@Column(name="name_en")
	private String nameEn; //部门名称（英文）
	
	private String code; //部门编号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	private Department parent; //父级部门
	
	@Column(name = "parent_id", unique = false, nullable = false, insertable=false, updatable=false) //父级id
	private Long parentId;
	
	@Column(name="parent_ids")
	private String parentIds; //所有父类ID
	
	private String announcement; //0：不启用公告；1：启用公告（默认为0）
	
	private String lowestLevel; //0：不是最低级别；1：最低级别部门（默认为0）
	
	private Integer level; //菜单的级别：0为最大
	
	private Integer sort; //排序值
	
	private String description; //部门描述
	
	private String city; //部门所在城市
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Office office; //所属公司
	
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="parent")
	@Where(clause="delFlag='0'")
	@OrderBy(value="sort")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Department> childList = Lists.newArrayList();// 拥有子菜单列表
	
	@OneToMany(mappedBy = "department", fetch=FetchType.LAZY)
	@Where(clause="delFlag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "dept_select_menu", joinColumns = { @JoinColumn(name = "dept_id") }, inverseJoinColumns = { @JoinColumn(name = "menu_id") })
	@Where(clause="delFlag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Menu> selectMenuList = Lists.newArrayList(); // 拥有菜单列表
	
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	
	
	public Department(){
		super();
		this.sort = 30;
	}
	
	public Department(Long id){
		this();
		this.id = id;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnore
	public Department getParent() {
		return parent;
	}

	public void setParent_id(Department parent) {
		this.parent = parent;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	} 

	public String getParentIds() {
		return parentIds;
	} 

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public String getLowestLevel() {
		return lowestLevel;
	}

	public void setLowestLevel(String lowestLevel) {
		this.lowestLevel = lowestLevel;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@JsonIgnore
	public List<Department> getChildList() {
		return childList;
	}

	public void setChildList(List<Department> childList) {
		this.childList = childList;
	}

	@JsonIgnore
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}
	
	


	@Transient
	public static void sortList(List<Department> list, List<Department> sourcelist, Long parentId) {
		for (int i=0; i<sourcelist.size(); i++){
			Department e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId() != null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Department child = sourcelist.get(j);
					if (child.getParent() != null && child.getParent().getId() != null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	@Transient
	public boolean isRoot() {
		return isRoot(this.id);
	}
	
	@Transient
	public static boolean isRoot(Long id) {
		return id != null && id.equals(1L);
	}
	
	@Length(min=1, max=1)
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public List<Menu> getSelectMenuList() {
		return selectMenuList;
	}

	public void setSelectMenuList(List<Menu> selectMenuList) {
		this.selectMenuList = selectMenuList;
	}
	
	/*********************************查看菜单*************************************/
	@Transient
	public List<Long> getSelectMenuIdList() {
		List<Long> menuIdList = Lists.newArrayList();
		for (Menu menu : selectMenuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	@Transient
	public void setSelectMenuIdList(List<Long> selectMenuIdList) {
		selectMenuList = Lists.newArrayList();
		for (Long menuId : selectMenuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			selectMenuList.add(menu);
		}
	}

	@Transient
	public String getSelectMenuIds() {
		List<Long> nameIdList = Lists.newArrayList();
		for (Menu menu : selectMenuList) {
			nameIdList.add(menu.getId());
		}
		return StringUtils.join(nameIdList, ",");
	}
	
	@Transient
	public void setSelectMenuIds(String selectMenuIds) {
		selectMenuList = Lists.newArrayList();
		if (selectMenuIds != null){
			String[] ids = StringUtils.split(selectMenuIds, ",");
			for (String menuId : ids) {
				Menu menu = new Menu();
				menu.setId(new Long(menuId));
				selectMenuList.add(menu);
			}
		}
	}
}