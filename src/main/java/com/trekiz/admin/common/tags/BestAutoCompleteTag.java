package com.trekiz.admin.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author wuq
 * @version 2015-02-26
 * 性能更好的自动匹配（补全）自定义标签类
 * 只支持input type="text"标签
 */
public class BestAutoCompleteTag extends TagSupport {
	
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
	 * 选中后的回调函数
	 * 该函数必须传入一个参数，参数名要求不能为this
	 */
	private String callback;
	
	//////////////////////////////////以下为公共属性
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
				jspTags.append("<input type='text' class='ForAutocompleteClass' id='" + id + "' ");
				jspTags.append(commonAttributes);
				jspTags.append("/>");
				
			}else{
				jspTags.append("<span style='color:red;'>对不起！id属性必须设置！</span>");
			}
			
			pageContext.getRequest().setAttribute("autoCompleteController", url);
			pageContext.getRequest().setAttribute("autoCompleteFun", callback);
			
			jspTags.append("<input id='autoCompleteUrlHander' type='hidden' value='"+url+"' />");
			jspTags.append("<input id='autoCompleteCallBack' type='hidden' value='"+callback+"' />");
			jspTags.append("<input id='autoCompleteStart' type='hidden' value='"+start+"' />");
			
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

	public void setCallback(String callback) {
		this.callback = callback;
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

}
