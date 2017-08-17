package com.trekiz.admin.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author wuq
 * @version 2015-02-03
 * 自动匹配自定义标签类
 * 只支持input type="text"标签
 */
public class AutoCompleteTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 对应标签的id属性
	 * 必填项
	 */
	private String id;
	
	/**
	 * 对应标签的name属性
	 */
	private String name;
	
	/**
	 * 控制器，根据输入框的值查询自动匹配的数据
	 * 必填项
	 */
	private String url;
	
	/**
	 * 起始查询位置
	 */
	private String start;
	
	/**
	 * class样式的名称
	 */
	private String className;
	
	/**
	 * 对应标签的行样式
	 */
	private String style;
	
	/**
	 * 对应标签的onclick事件
	 */
	private String onclick;
	
	/**
	 * 对应标签的onfocus事件
	 */
	private String onfocus;
	
	/**
	 * 对应标签的onblur事件
	 */
	private String onblur;
	
	/**
	 * 对应标签的onchange事件
	 */
	private String onchange;
	
	/**
	 * 选中后的回调函数
	 * 该函数必须传入一个参数，参数名要求不能为this
	 */
	private String callback;
	
	@SuppressWarnings("static-access")
    public int doStartTag() throws JspException{
		try {
			//组装前端jsp标签
			StringBuffer jspTags = new StringBuffer();
			
			/**
			 * 公共属性字符串
			 * 将公共属性统一放置在自定义标签中
			 */
			StringBuffer commonAttributes = new StringBuffer();
			if(null != name && !"".equals(name)){
				commonAttributes.append("name='" + name + "' ");
			}
			if(null != className && !"".equals(className)){
				commonAttributes.append("class='" + className + "' ");
			}
			if(null != style && !"".equals(style)){
				commonAttributes.append("style='" + style + "' ");
			}
			if(null != onclick && !"".equals(onclick)){
				commonAttributes.append("onclick='" + onclick + "' ");
			}
			if(null != onfocus && !"".equals(onfocus)){
				commonAttributes.append("onfocus='" + onfocus + "' ");
			}
			if(null != onblur && !"".equals(onblur)){
				commonAttributes.append("onblur='" + onblur + "' ");
			}
			if(null != onchange && !"".equals(onchange)){
				commonAttributes.append("onchange='" + onchange + "' ");
			}
			
			if(null != id && !"".equals(id)){
				jspTags.append("<input type='text' id='" + id + "' ");
				if(null != url && !"".equals(url)){
					String keyDown = "";
					if(null != start && !"".equals(start)){
						keyDown = "getAutoCompleteTagFromDatabase('" + id + "','" + url + "',this,'" + start + "')";
						if(null != callback && !"".equals(callback)){
							keyDown = "getAutoCompleteTagFromDatabase('" + id + "','" + url + "',this,'" + start + "','" + callback + "')";
						}
					}else{
						keyDown = "getAutoCompleteTagFromDatabase('" + id + "','" + url + "',this,1)";
						if(null != callback && !"".equals(callback)){
							keyDown = "getAutoCompleteTagFromDatabase('" + id + "','" + url + "',this,1,'" + callback + "')";
						}
					}
					jspTags.append("onkeydown=" + keyDown + " ");
				}
				jspTags.append(commonAttributes);
				jspTags.append("/>");
				
			}else{
				jspTags.append("<span style='color:red;'>对不起！id属性必须设置！</span>");
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

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}
	
}
