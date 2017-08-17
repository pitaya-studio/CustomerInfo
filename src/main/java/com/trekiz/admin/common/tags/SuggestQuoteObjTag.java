package com.trekiz.admin.common.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

public class SuggestQuoteObjTag extends TagSupport{

	private static final long serialVersionUID = -8370839136453063875L;
	 /** 组件ID*/
    private String id;
    /** 组件名*/
    private String name;
    /** 组件默认值*/
    private String defaultValue;
    /** 组件自带样式*/
	private String style;
    /** 数据访问链接 */
    private String ajaxUrl;
    /** 组件显示值 */
    private String displayValue;
    /** 回调函数 */
    private String callback;
	
	public SuggestQuoteObjTag() {
		super();
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isEmpty(id)) {
			id = name;
		}
		
		try{
			String path = ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
			importJs(path, out);
			
			sb.append("<input type=\"hidden\" name=\"" + name + "\" id=\"" + id + "\" value=\""+ defaultValue +"\" />");
			sb.append("<input type=\"text\" name=\"suggest" + name + "\" id=\"suggest" + id + "\" style=\"" + style + "\" value=\"" + displayValue + "\" maxlength=\"16\"  onchange=\"suggestChange();\" />");
			sb.append("<span id='suggestDiv' class=\"suggest_ac_results\"></span>");
			
			sb.append("<script type=\"text/javascript\">");
			sb.append("function initSuggest(requestField){");
			sb.append("var commoncountrys=new Array();");
			sb.append("var countrys=new Array();");
			sb.append("var i=0;");
			sb.append("var j=0;");
			sb.append("$.ajax({");
			sb.append("type: \"POST\",");
			sb.append("url: \"" + ajaxUrl + "\",");
			sb.append("data: requestField,");
			sb.append("dataType: \"json\",");
			sb.append("success: function(data){");
			sb.append("if(data){");
			sb.append("var flag = false;");
			sb.append("$.each(data,function(){");
//			sb.append("if(this.hot == 1) {");
			sb.append("commoncountrys[i++] = new Array(this.id, this.agentName);");
//			sb.append("}");
			sb.append("countrys[j++] = new Array(this.id, this.agentName);");
			sb.append("if(this.id == $(\"#" + id + "\").val()) {");
			sb.append("$(\"#suggest" + id + "\").val(this.agentName);");
			sb.append("flag = true;");
			sb.append("}");
			sb.append("});");
			sb.append("if(!flag) {");
			sb.append("if($(\"#" + id + "\").val()) {");
			sb.append("$(\"#suggest" + id + "\").val($(\"#" + id + "\").val());}");
			sb.append("else {");
			sb.append("$(\"#" + id + "\").val('');");
			sb.append("$(\"#suggest" + id + "\").val('');");
			sb.append("}");
			sb.append("}");
			sb.append("$(\"#suggest" + id + "\").suggest(countrys,{hot_list:commoncountrys,dataContainer:'#" + id + "',");
			if(StringUtils.isNotEmpty(this.callback)) {
				sb.append("onSelect:function(){" + this.callback + "},");
			}
			sb.append("attachObject:\"#suggestDiv\"});");
			sb.append("}");
			sb.append("}");
			sb.append("});");
			sb.append("}");
			
			sb.append(" function suggestChange() {");
			sb.append("if($(\"#suggest" + id + "\").val() == '') {");
			sb.append("$(\"#" + id + "\").val('');");
			sb.append("}");
			sb.append("}");
			sb.append("</script>");
			
			out.println(sb.toString());
			out.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return super.doStartTag();
	}
	
	private void importJs(String path,JspWriter out) throws IOException{
		out.println("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + path + "/static/css/j.suggest.css\" />");
		out.println("<script type=\"text/javascript\" src=\"" + path + "/static/js/input-comp/j.dimensions.js\"></script>");
		out.println("<script type=\"text/javascript\" src=\"" + path + "/static/js/input-comp/j.suggestQuote.js\"></script>");
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}

	public String getAjaxUrl() {
		return ajaxUrl;
	}
	public void setAjaxUrl(String ajaxUrl) {
		this.ajaxUrl = ajaxUrl;
	}

	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	
	public String getCallback() {
		return this.callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	} 
}