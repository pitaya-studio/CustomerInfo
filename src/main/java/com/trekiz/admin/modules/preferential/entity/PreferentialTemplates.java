/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "preferential_templates")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PreferentialTemplates   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "PreferentialTemplates";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_NAME = "模板名称";
	public static final String ALIAS_TYPE = "类型;预留字段；可设置成通用或者指定某个批发商";
	public static final String ALIAS_OUT_HTML = "页面输出代码";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//优惠模板的常量定义 1代表住付优惠，2提前预订，3打包优惠，4房/餐/交通优惠
	/**住付优惠*/
	public static final Integer PREFERENTIAL_TEMPLATE_TYPE_0=1;
	/**提前预订优惠*/
	public static final Integer PREFERENTIAL_TEMPLATE_TYPE_1=2;
	/**打包优惠*/
	public static final Integer PREFERENTIAL_TEMPLATE_TYPE_2=3;
	/**房/餐/交通优惠*/
	public static final Integer PREFERENTIAL_TEMPLATE_TYPE_3=4;
	/**类型转换成对应的UUID，V1版本根据常量指定固定的优惠类型*/
	public static final Map<Integer,String> type2Uuid=new HashMap<Integer,String>();
	public static final Map<String,Integer> uuid2Type=new HashMap<String,Integer>();
	static{
		type2Uuid.put(PREFERENTIAL_TEMPLATE_TYPE_0, "9e68dc9f10b3409fa465ecd6578f0179");
		type2Uuid.put(PREFERENTIAL_TEMPLATE_TYPE_1, "9e68dc9f10b3409fa465ecd4458f0179");
		type2Uuid.put(PREFERENTIAL_TEMPLATE_TYPE_2, "9e68dc9f10b3409fa465ecd2345f0179");
		type2Uuid.put(PREFERENTIAL_TEMPLATE_TYPE_3, "9e68dc9f10b3409fa465ecd2215f0179");
		uuid2Type.put("9e68dc9f10b3409fa465ecd6578f0179", PREFERENTIAL_TEMPLATE_TYPE_0);
		uuid2Type.put("9e68dc9f10b3409fa465ecd4458f0179", PREFERENTIAL_TEMPLATE_TYPE_1);
		uuid2Type.put("9e68dc9f10b3409fa465ecd2345f0179", PREFERENTIAL_TEMPLATE_TYPE_2);
		uuid2Type.put("9e68dc9f10b3409fa465ecd2215f0179", PREFERENTIAL_TEMPLATE_TYPE_3);
	}
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"模板名称"
	private java.lang.String name;
	//"类型;预留字段；可设置成通用或者指定某个批发商"
	private java.lang.String type;
	//"页面输出代码"
	private java.lang.String outHtml;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除状态"
	private java.lang.String delFlag;
	//columns END
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public PreferentialTemplates(){
	}

	public PreferentialTemplates(
		java.lang.Integer id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Integer getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setName(java.lang.String value) {
		this.name = value;
	}
	@Column(name="name")
	public java.lang.String getName() {
		return this.name;
	}
	
		
	public void setType(java.lang.String value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.String getType() {
		return this.type;
	}
	
		
	public void setOutHtml(java.lang.String value) {
		this.outHtml = value;
	}
	@Column(name="out_html")
	public java.lang.String getOutHtml() {
		return this.outHtml;
	}
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	@Transient	
	public String getCreateDateString() {
		if(getCreateDate() != null) {
			return this.date2String(getCreateDate(), FORMAT_CREATE_DATE);
		} else {
			return null;
		}
	}
	public void setCreateDateString(String value) {
		setCreateDate(this.string2Date(value, FORMAT_CREATE_DATE));
	}
	
		
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	@Column(name="createDate")
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	
		
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	@Transient	
	public String getUpdateDateString() {
		if(getUpdateDate() != null) {
			return this.date2String(getUpdateDate(), FORMAT_UPDATE_DATE);
		} else {
			return null;
		}
	}
	public void setUpdateDateString(String value) {
		setUpdateDate(this.string2Date(value, FORMAT_UPDATE_DATE));
	}
	
		
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	@Column(name="updateDate")
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

