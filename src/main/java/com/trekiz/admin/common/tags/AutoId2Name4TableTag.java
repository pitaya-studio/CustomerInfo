package com.trekiz.admin.common.tags;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.collections.CollectionUtils;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.hotel.dao.HotelStarDao;

public class AutoId2Name4TableTag extends TagSupport {

	private static final long serialVersionUID = -3111782736944278402L;
	//接收传递进来的PageContext对象
    private PageContext pageContext;
    private static HotelStarDao hotelStarDao = SpringContextHolder.getBean(HotelStarDao.class);
	
    
    /** 对象名 */
    private String tableName;
    /** 转换源列名称（对象属性）*/
    private String sourceColumnName;
    /** 转换成列名称（对象属性）*/
    private String srcColumnName;
    /** 转换源 值*/
    private String value;
    /** 连接符 默认“，”*/
    private String regex=",";
    
    
    
    @SuppressWarnings("unused")
	@Override
    public int doStartTag() throws JspException {
    	StringBuffer sb = new StringBuffer();
    	
    	StringBuffer sql = new StringBuffer();
    	sql.append("select "+srcColumnName+" from "+tableName+" where "+sourceColumnName);
    	
    	sql.append(" in ");
    	sql.append(" ( ");
    	
		Object[] array = value.split(regex);
		for(Object val : array){
			sql.append("?,");
    	}
		sql.deleteCharAt(sql.lastIndexOf(","));
    	sql.append(" ) ");
    	
    	
    	List<Map<String,Object>> list = hotelStarDao.findBySql(sql.toString(),Map.class,array);
        
    	if(CollectionUtils.isNotEmpty(list)){
    		for(Map<String,Object> map:list){
    			sb.append(map.get(srcColumnName)+regex);
    		}
    		sb.deleteCharAt(sb.lastIndexOf(regex));
        }
    	
        try {
        	pageContext.getOut().print(sb.toString());
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

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public void setSrcColumnName(String srcColumnName) {
		this.srcColumnName = srcColumnName;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
    
    

}