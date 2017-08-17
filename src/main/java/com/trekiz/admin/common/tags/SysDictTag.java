package com.trekiz.admin.common.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;

import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.utils.DictUtils;

/**
 * @author wuq
 * @version 2015-01-30
 * 字典取值自定义标签类
 * 支持动态设置属性和值
 * 从数据表sys_dict中取值，过滤(delFlag=1)的记录
 * 支持三种标签：select、checkbox、radio
 */
public class SysDictTag extends TagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 种类：
	 * select：下拉框
	 * checkbox：复选框
	 * radio：单选框
	 */
	private String method;
	
	/**
	 * 对应标签的id属性
	 */
	private String id;
	
	/**
	 * 对应标签的name属性
	 */
	private String name;
	
	/**
	 * 对应sys_dict表中字段type的值
	 */
	private String type;
	
	/**
	 * class样式的名称
	 */
	private String className;
	
	/**
	 * 对应标签的行样式
	 */
	private String style;
	
	/**
	 * 对应标签的size属性
	 */
	private String size;
	
	/**
	 * 对应标签的onclick事件
	 */
	private String onclick;
	
	/**
	 * 对应标签的onchange事件
	 */
	private String onchange;
	
	/**
	 * 对应标签的onselect事件
	 */
	private String onselect;
	
	/**
	 * 设置默认选中项
	 * 和value的值相同的情况下，会被选中
	 */
	private String choose;
	
	/**
	 * 设置换行
	 * newline表示每行放置多少个
	 */
	private String newline;
	
	/**
	 * 介绍动态设置的属性和值
	 */
	private Map<String, String> dynAttributes = new HashMap<String, String>(); 

	//实现处理动态属性的setDynamicAttribute方法 
	public void setDynamicAttribute(String uri, String localName, Object value)
			throws JspException {
		
		//将所有的动态属性名称和属性值保存在Map对象中 
		dynAttributes.put(localName, value.toString()); 
		
	}
	
	@SuppressWarnings("static-access")
    public int doStartTag() throws JspException{
		
		try {
			
			//获得动态属性名称集合 
			Set<String> keys = dynAttributes.keySet(); 
			
			//组装前端jsp标签
			StringBuffer jspTags = new StringBuffer();
			
			/**
			 * 公共属性字符串
			 * 将公共属性统一放置在自定义标签中
			 */
			StringBuffer commonAttributes = new StringBuffer();
			if(null != className && !"".equals(className)){
				commonAttributes.append("class='" + className + "' ");
			}
			if(null != style && !"".equals(style)){
				commonAttributes.append("style='" + style + "' ");
			}
			if(null != size && !"".equals(size)){
				commonAttributes.append("size='" + size + "' ");
			}
			if(null != onclick && !"".equals(onclick)){
				commonAttributes.append("onclick='" + onclick + "' ");
			}
			if(null != onchange && !"".equals(onchange)){
				commonAttributes.append("onchange='" + onchange + "' ");
			}
			if(null != onselect && !"".equals(onselect)){
				commonAttributes.append("onselect='" + onselect + "' ");
			}
			
			/**
			 * 根据类型type,从sys_dict表中取值
			 */
			List<Dict> list = DictUtils.getDictList(type);
			
			if("select".equals(method)){
				//组装select下拉框
				jspTags.append("<select id='" + id + "' name='" + name + "' ");
				
				jspTags.append(commonAttributes);
				jspTags.append(">");
				jspTags.append("<option value=''>请选择</option>");
				
				//循环组装option
				if(null != list && list.size() > 0){
					for(Dict dict : list){
						jspTags.append("<option ");
						//设置默认选中项
						if(dict.getValue().equals(choose)){
							jspTags.append("selected='selected' ");
						}
						jspTags.append("value='"+dict.getValue()+"'>"+dict.getLabel()+"</option>");
					}
				}
				
				//循环组装动态属性option
				if(null != keys && keys.size() > 0){
					for(String key : keys){
						jspTags.append("<option ");
						//设置默认选中项
						if(key.equals(choose)){
							jspTags.append("selected='selected' ");
						}
						String value = dynAttributes.get(key); 
						jspTags.append("value='"+key+"'>"+value+"</option>");
					}
				}
				
				jspTags.append("</select>");
			}else if("checkbox".equals(method) || "radio".equals(method)){
				//组装checkbox复选框、radio单选框
				if(null != list && list.size() > 0){
					for(int i = 0;i < list.size();i++){
						String br = "";
						if(null != newline && !"".equals(newline)){
							int next = Integer.parseInt(newline);
							//设置每行放置多少个，然后换行
							if(((i + 1) % next) == 0){
								br = "<br/>";
							}
						}
						jspTags.append("<input type='" + method + "' name='" + name + "' value='" + list.get(i).getValue() + "' ");
						if(null != choose && !"".equals(choose)){
							String[] chooses = choose.split(",");
							//设置默认选中项
							for(String s : chooses){
								if(list.get(i).getValue().endsWith(s)){
									jspTags.append("checked='checked' ");
								}
							}
						}
						jspTags.append(commonAttributes);
						jspTags.append("/>" + list.get(i).getLabel() + br);
					}
				}	
				
				//循环组装动态属性
				if(null != keys && keys.size() > 0){
					for(String key : keys){
						String value = dynAttributes.get(key); 
						jspTags.append("<input type='" + method + "' name='" + name + "' value='" + key + "' ");
						
						if(null != choose && !"".equals(choose)){
							String[] chooses = choose.split(",");
							//设置默认选中项
							for(String s : chooses){
								if(s.equals(key)){
									jspTags.append("checked='checked' ");
								}
							}
						}
						
						jspTags.append(commonAttributes);
						jspTags.append("/>" + value);
					}
				}
			}else{
				jspTags.append("<span style='color:red;'>对不起！method属性设置错误，只支持：select、checkbox、radio。</span>");
			}
			
			pageContext.getOut().println(jspTags.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//return super.doStartTag();
		return this.EVAL_PAGE;
	}
	
	//释放资源
	public void release(){
		super.release();
	}

	
	public void setMethod(String method) {
		this.method = method;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setOnselect(String onselect) {
		this.onselect = onselect;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}
	
	public void setNewline(String newline) {
		this.newline = newline;
	}
	
}
