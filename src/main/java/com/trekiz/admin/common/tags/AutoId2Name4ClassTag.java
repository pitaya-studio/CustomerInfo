package com.trekiz.admin.common.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.hotel.dao.HotelStarDao;

public class AutoId2Name4ClassTag extends TagSupport {

	private static final long serialVersionUID = -3111782736944278402L;
	//接收传递进来的PageContext对象
    private PageContext pageContext;
    private static HotelStarDao hotelStarDao = SpringContextHolder.getBean(HotelStarDao.class);
	
    
    /** 对象名 */
    private String classzName;
    /** 转换源列名称（对象属性）*/
    private String sourceProName;
    /** 转换成列名称（对象属性）*/
    private String srcProName;
    /** 转换源 值*/
    private String value;
    /** 连接符 默认“，”*/
    private String regex=",";
    
    @Override
    public int doStartTag() throws JspException {
    	StringBuffer sb = new StringBuffer();
    	
    	StringBuffer sql = new StringBuffer();
    	sql.append("select "+srcProName+" from "+classzName+" where "+sourceProName);
    	sql.append(" in ");
    	sql.append(" ( ");
    	
		Object[] array = value.split(regex);
		for(@SuppressWarnings("unused") Object val : array){
			sql.append("?,");
    	}
		sql.deleteCharAt(sql.lastIndexOf(","));
    	sql.append(" ) ");
    	List<String> list = hotelStarDao.find(sql.toString(), array);
        
    	if(CollectionUtils.isNotEmpty(list)){
    		for(String s:list){
    			if(StringUtils.isNotBlank(s)){
    				sb.append(s+regex);
    			}
    		}
    		if(sb.length()>0){
    			sb.deleteCharAt(sb.lastIndexOf(regex));
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
        return 0;
    }
    
    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

	
	public void setClasszName(String classzName) {
		this.classzName = classzName;
	}

	public void setSourceProName(String sourceProName) {
		this.sourceProName = sourceProName;
	}

	public void setSrcProName(String srcProName) {
		this.srcProName = srcProName;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
    
    

}