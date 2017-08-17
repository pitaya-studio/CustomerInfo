package com.trekiz.admin.common.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;

public class SysDefineDictTag extends TagSupport {

	private static final long serialVersionUID = -3111782736944278402L;
	//接收传递进来的PageContext对象
    private PageContext pageContext;
    
    /** 组件ID*/
    private String id;
    /** 组件名*/
    private String name;
    /** 字典类型*/
    private String type;
    /** 组件默认值*/
    private String defaultValue;
    /** 取值范围 1、system表示superadmin维护的基础信息 ；2、company表示批发商维护的基础信息*/
    private String dataScope = DATA_SCOPE_COMPANY;
    /** 组件种类  目前支持select、radio、checkbox*/
    private String input = "select";
    /** 组件自带样式*/
    private String style;
    /** 是否只读 */
    private boolean readonly = false;
    /** 样式名称*/
    private String className;
    /** onclick事件*/
    private String onclick;
    /** onchange事件*/
    private String onchange;
    /** onselect事件*/
    private String onselect;
    
    /**增加默认元素*/
    private String element;
    /**默认元素的顺序 默认加在最后*/
    private String elementOrder;
    /** 连接符 默认“，”*/
    private String regex=",";
    /** 元素title标题 */
    private String title;

    public static final String DATA_SCOPE_SYSTEM = "system";
    public static final String DATA_SCOPE_COMPANY = "company";
    
    @Override
    public int doStartTag() throws JspException {
    	ServletRequest request = pageContext.getRequest();
    	
        StringBuffer sb = new StringBuffer();
        StringBuffer attributes = new StringBuffer();
        
//        Map<String,String> map = DictUtils.getValueAndLabelMap(type,StringUtils.toLong(UserUtils.getUser().getCompany().getId()));
        Map<String,String> map = DictUtils.getDictViewsMapByType(type, dataScope);
        
        Set<String> keys = map.keySet();
        
        //id为空时则取name字段
        if(StringUtils.isEmpty(id)) {
        	this.id = name;
        }
        
        //判断request中是否有当前组件值
        String value = request.getAttribute(name) == null ? null : String.valueOf(request.getAttribute(name));
        
        if(StringUtils.isEmpty(defaultValue) && StringUtils.isNotEmpty(value)) {
        	defaultValue = value;
        }
        
        if(StringUtils.isNotEmpty(className)) {
        	attributes.append(" class=\""+ className +"\"");
        }
        
        if(StringUtils.isNotEmpty(style)) {
        	attributes.append(" style=\""+ style +"\"");
        }
        
        if(StringUtils.isNotEmpty(onclick)) {
        	attributes.append(" onclick=\""+ onclick +"\"");
        }
        
        if(StringUtils.isNotEmpty(onchange)) {
        	attributes.append(" onchange=\""+ onchange +"\"");
        }
        
        if(StringUtils.isNotEmpty(onselect)) {
        	attributes.append(" onselect=\""+ onselect +"\"");
        }
        
        if(StringUtils.isNotEmpty(title)) {
        	attributes.append(" title=\"" + title + "\"");
        }
        
        if(readonly) {
        	if("select".equalsIgnoreCase(input) || "radio".equalsIgnoreCase(input)) {
            	if(StringUtils.isNotEmpty(defaultValue) && keys.contains(defaultValue)) {
                	String label = map.get(defaultValue);
                	sb.append("<span" + attributes + ">"+label+"</span>");
        		}
        	} else if("checkbox".equalsIgnoreCase(input)) {
        		if(StringUtils.isNotEmpty(defaultValue)) {
        			String[] defaultArray = defaultValue.split(regex);
        			if(defaultArray != null && defaultArray.length >0) {
        				sb.append("<span" + attributes + ">");
        				int length = sb.length();
        				for(String val : defaultArray) {
                        	String label = map.get(val);
                        	if(StringUtils.isNotEmpty(label)) {
                            	sb.append(label+"、");
                        	}
        				}
        				if(sb.length() > length) {
            				sb = new StringBuffer(sb.substring(0, (sb.length() - 1)));
        				}
        				sb.append("</span>");
        			}
        		}
        	}
        } else {
            if("select".equalsIgnoreCase(input)) {
            	sb.append("<select id=\"" + id + "\" name=\"" + name + "\" ");
            	sb.append(attributes);
            	sb.append(">");
            	if(StringUtils.isNotEmpty(elementOrder)&&elementOrder.equals("top")){
            		if(StringUtils.isNotEmpty(element)){
                		sb.append(element);
                	} 
            	}
        		sb.append("<option value=\"\">请选择</option>");
        		
            	for(String key : keys) {
            		sb.append("<option value=\""+ key +"\"");
            		if(StringUtils.isNotEmpty(defaultValue) && key.equals(defaultValue)) {
            			sb.append(" selected=\"selected\"");
            		}
            		sb.append(">");
            		sb.append(map.get(key));
            		sb.append("</option>");
            	}
            	if(StringUtils.isEmpty(elementOrder)||elementOrder.equals("last")){
            		if(StringUtils.isNotEmpty(element)){
                		sb.append(element);
                	}
            	}
            	sb.append("</select>");
            } else if("radio".equalsIgnoreCase(input)) {
            	if(StringUtils.isNotEmpty(elementOrder)&&elementOrder.equals("top")){
            		if(StringUtils.isNotEmpty(element)){
                		sb.append(element);
                	}
            	}
            	for(String key : keys) {
            		sb.append("<label for=\""+ name +"_" + key +"\">");
            		
            		sb.append("<input name=\"" + name + "\" id=\""+ name +"_" + key +"\" type=\"radio\" value=\""+ key +"\"");
            		sb.append(attributes);
            		
            		if(StringUtils.isNotEmpty(defaultValue) && key.equals(defaultValue)) {
            			sb.append(" checked=\"checked\"");
            		}
            		
            		sb.append("/>");
            		
            		sb.append(map.get(key));
            		sb.append("</label>");
            	}
            	if(StringUtils.isEmpty(elementOrder)||elementOrder.equals("last")){
            		if(StringUtils.isNotEmpty(element)){
                		sb.append(element);
                	}
            	}
            } else if("checkbox".equalsIgnoreCase(input)) {
            	if(StringUtils.isNotEmpty(elementOrder)&&elementOrder.equals("top")){
            		if(StringUtils.isNotEmpty(element)){
                		sb.append(element);
                	}
            	}
            	//获取默认值，并转化成数组数据
            	List<String> defValList = new ArrayList<String>();
            	if(StringUtils.isNotEmpty(defaultValue)) {
        			String[] defVals = defaultValue.split(",");
        			defValList = Arrays.asList(defVals);
        		}
            	
            	for(String key : keys) {
            		sb.append("<label for=\""+ name +"_" + key +"\">");
            		
            		sb.append("<input name=\"" + name + "\" id=\""+ name +"_" + key +"\" type=\"checkbox\" value=\""+ key +"\"");
            		sb.append(attributes);
            		
            		
            		if(defValList.contains(key)) {
            			sb.append(" checked=\"checked\"");
            		}
            		
            		sb.append("/>");
            		
            		sb.append(map.get(key));
            		sb.append("</label>");
            	}
            	if(StringUtils.isEmpty(elementOrder)||elementOrder.equals("last")){
            		if(StringUtils.isNotEmpty(element)){
                		sb.append(element);
                	}
            	}
            	
            }
        }
        
        try {
        	pageContext.getOut().println(sb.toString());
        } catch (IOException e) {
        	e.printStackTrace();
        }
		return EVAL_PAGE;
    }
    
    @Override
    public int doEndTag() throws JspException {
    	this.setId(null);
        return 0;
    }
    
    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
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
    
    public void setDefaultValue(String defaultValue) {
    	this.defaultValue = defaultValue;
    }
    
    public void setDataScope(String dataScope) {
    	this.dataScope = dataScope;
    }
    
    public void setInput(String input) {
    	this.input = input;
    }
    
    public void setStyle(String style) {
    	this.style = style;
    }
    
    public void setClassName(String className) {
    	this.className = className;
    }
    
    public void setReadonly(boolean readonly) {
    	this.readonly = readonly;
    }
    
    public void setOnclick(String onclick) {
    	this.onclick = onclick;
    }
    
    public void setOnselect(String onselect) {
    	this.onselect = onselect;
    }
    
    public void setOnchange(String onchange) {
    	this.onchange = onchange;
    }

	public void setElement(String element) {
		this.element = element;
	}

	public void setElementOrder(String elementOrder) {
		this.elementOrder = elementOrder;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}