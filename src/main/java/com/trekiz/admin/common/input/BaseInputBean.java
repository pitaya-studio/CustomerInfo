package com.trekiz.admin.common.input;

public class BaseInputBean implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Class<?> classz;//映射的目标对象类
	private String prefix;//前缀
	private String regexSuper=";";//数据库表行记录分隔符
	private String regexChild="#";//属性分隔符
	private String[] propertyName;//对应的类属性名
	private String childListName;//主表中对应的list 属性名
	
	public Class<?> getClassz() {
		return classz;
	}
	public void setClassz(Class<?> classz) {
		this.classz = classz;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getRegexSuper() {
		return regexSuper;
	}
	public void setRegexSuper(String regexSuper) {
		this.regexSuper = regexSuper;
	}
	public String getRegexChild() {
		return regexChild;
	}
	public void setRegexChild(String regexChild) {
		this.regexChild = regexChild;
	}
	public String[] getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String[] propertyName) {
		this.propertyName = propertyName;
	}
	public String getChildListName() {
		return childListName;
	}
	public void setChildListName(String childListName) {
		this.childListName = childListName;
	}
	
	
	
}
