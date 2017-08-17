package com.trekiz.admin.review.configuration.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * Copyright   2015  QUAUQ Technology Co. Ltd.
 *
 * 部门树形显示时的节点
 *
 * @author zhenxing.yan
 * @date 2015年11月15日
 */
public class DepartmentNode implements Serializable,Comparable<DepartmentNode>{

	private static final long serialVersionUID = 1L;

	private Long id;//部门id
	
	private String name; //部门名称（中文）
	
	private String nameEn; //部门名称（英文）
	
	private String code; //部门编号
	
	private Integer sort; //排序值
	
	private Set<DepartmentNode> children;//子节点

	public DepartmentNode() {
		children=new TreeSet<DepartmentNode>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Set<DepartmentNode> getChildren() {
		return children;
	}

	public void clearChildren(){
		children=new TreeSet<DepartmentNode>();
	}
	
	public void addChild(DepartmentNode child){
		if (child!=null) {
			children.add(child);
		}
	}
	
	public boolean hasChildren(){
		if (children!=null&&children.size()>0) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int compareTo(DepartmentNode other) {
		if (other==null) {
			return 1;
		}
		if (this.sort==null) {
			return -1;
		}
		if (other.sort==null) {
			return 1;
		}
		if (this.sort>other.sort) {
			return 1;
		}else if(this.sort<=other.sort){
			return -1;
		}
		return 0;
	}
}
