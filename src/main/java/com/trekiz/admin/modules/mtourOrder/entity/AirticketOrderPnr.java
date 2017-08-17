/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@Table(name = "airticket_order_pnr")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AirticketOrderPnr   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "AirticketOrderPnr";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_AIRTICKET_ORDER_ID = "机票订单id";
	public static final String ALIAS_CODE_TYPE = "大编号类型(0:PNR或者1:地接社)";
	public static final String ALIAS_FLIGHT_PNR = "大编号输入（PNR或者地接社ID）";
	public static final String ALIAS_AIRLINE = "关联航空公司（航空公司信息表sys_airline_info的二字码）";
	public static final String ALIAS_TICKET_DEADLINE = "出票期限";
	public static final String ALIAS_FRONT_DEADLINE = "定金期限";
	public static final String ALIAS_LIST_DEADLINE = "名单期限";
	public static final String ALIAS_RENAME_DEADLINE = "改名期限";
	public static final String ALIAS_CANCEL_DEADLINE = "机票订单id";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "创建时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_TICKET_DEADLINE = "yyyy-MM-dd";
	public static final String FORMAT_FRONT_DEADLINE = "yyyy-MM-dd";
	public static final String FORMAT_LIST_DEADLINE = "yyyy-MM-dd";
	public static final String FORMAT_RENAME_DEADLINE = "yyyy-MM-dd";
	public static final String FORMAT_CANCEL_DEADLINE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"机票订单id"
	private java.lang.Integer airticketOrderId;
	//"大编号类型(0:PNR或者1:地接社)"
	private java.lang.Integer codeType;
	//"大编号输入（PNR或者地接社ID）"
	private java.lang.String flightPnr;
	//"关联航空公司（航空公司信息表sys_airline_info的二字码）"
	private java.lang.String airline;
	//"出票期限"
	private java.util.Date ticketDeadline;
	//"定金期限"
	private java.util.Date frontDeadline;
	//"名单期限"
	private java.util.Date listDeadline;
	//"改名期限"
	private java.util.Date renameDeadline;
	//"机票订单id"
	private java.util.Date cancelDeadline;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"创建时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	//PNR组表的UUID
	private String airticketOrderPnrGroupUuid;
	private Integer costSupplyId;//cost_supply_id
	private String costSupplyType;//cost_supply_type
	private String costBankName;//cost_bank_name
	private String costAccountNo;//cost_account_no
	@Column(name="cost_supply_id")
	public Integer getCostSupplyId() {
		return costSupplyId;
	}

	public void setCostSupplyId(Integer costSupplyId) {
		this.costSupplyId = costSupplyId;
	}
	@Column(name="cost_bank_name")
	public String getCostBankName() {
		return costBankName;
	}

	public void setCostBankName(String costBankName) {
		this.costBankName = costBankName;
	}
	@Column(name="cost_account_no")
	public String getCostAccountNo() {
		return costAccountNo;
	}

	public void setCostAccountNo(String costAccountNo) {
		this.costAccountNo = costAccountNo;
	}
	@Column(name="cost_supply_type")
	public String getCostSupplyType() {
		return costSupplyType;
	}

	public void setCostSupplyType(String costSupplyType) {
		this.costSupplyType = costSupplyType;
	}

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
	public AirticketOrderPnr(){
	}

	public AirticketOrderPnr(
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
	
		
	public void setAirticketOrderId(java.lang.Integer value) {
		this.airticketOrderId = value;
	}
	@Column(name="airticket_order_id")
	public java.lang.Integer getAirticketOrderId() {
		return this.airticketOrderId;
	}
	
		
	public void setCodeType(java.lang.Integer value) {
		this.codeType = value;
	}
	@Column(name="code_type")
	public java.lang.Integer getCodeType() {
		return this.codeType;
	}
	
		
	public void setFlightPnr(java.lang.String value) {
		this.flightPnr = value;
	}
	@Column(name="flight_pnr")
	public java.lang.String getFlightPnr() {
		return this.flightPnr;
	}
	
		
	public void setAirline(java.lang.String value) {
		this.airline = value;
	}
	@Column(name="airline")
	public java.lang.String getAirline() {
		return this.airline;
	}
	@Transient	
	public String getTicketDeadlineString() {
		if(getTicketDeadline() != null) {
			return this.date2String(getTicketDeadline(), FORMAT_TICKET_DEADLINE);
		} else {
			return null;
		}
	}
	public void setTicketDeadlineString(String value) {
		setTicketDeadline(this.string2Date(value, FORMAT_TICKET_DEADLINE));
	}
	
		
	public void setTicketDeadline(java.util.Date value) {
		this.ticketDeadline = value;
	}
	@Column(name="ticket_deadline")
	public java.util.Date getTicketDeadline() {
		return this.ticketDeadline;
	}
	@Transient	
	public String getFrontDeadlineString() {
		if(getFrontDeadline() != null) {
			return this.date2String(getFrontDeadline(), FORMAT_FRONT_DEADLINE);
		} else {
			return null;
		}
	}
	public void setFrontDeadlineString(String value) {
		setFrontDeadline(this.string2Date(value, FORMAT_FRONT_DEADLINE));
	}
	
		
	public void setFrontDeadline(java.util.Date value) {
		this.frontDeadline = value;
	}
	@Column(name="front_deadline")
	public java.util.Date getFrontDeadline() {
		return this.frontDeadline;
	}
	@Transient	
	public String getListDeadlineString() {
		if(getListDeadline() != null) {
			return this.date2String(getListDeadline(), FORMAT_LIST_DEADLINE);
		} else {
			return null;
		}
	}
	public void setListDeadlineString(String value) {
		setListDeadline(this.string2Date(value, FORMAT_LIST_DEADLINE));
	}
	
		
	public void setListDeadline(java.util.Date value) {
		this.listDeadline = value;
	}
	@Column(name="list_deadline")
	public java.util.Date getListDeadline() {
		return this.listDeadline;
	}
	@Transient	
	public String getRenameDeadlineString() {
		if(getRenameDeadline() != null) {
			return this.date2String(getRenameDeadline(), FORMAT_RENAME_DEADLINE);
		} else {
			return null;
		}
	}
	public void setRenameDeadlineString(String value) {
		setRenameDeadline(this.string2Date(value, FORMAT_RENAME_DEADLINE));
	}
	
		
	public void setRenameDeadline(java.util.Date value) {
		this.renameDeadline = value;
	}
	@Column(name="rename_deadline")
	public java.util.Date getRenameDeadline() {
		return this.renameDeadline;
	}
	@Transient	
	public String getCancelDeadlineString() {
		if(getCancelDeadline() != null) {
			return this.date2String(getCancelDeadline(), FORMAT_CANCEL_DEADLINE);
		} else {
			return null;
		}
	}
	public void setCancelDeadlineString(String value) {
		setCancelDeadline(this.string2Date(value, FORMAT_CANCEL_DEADLINE));
	}
	
		
	public void setCancelDeadline(java.util.Date value) {
		this.cancelDeadline = value;
	}
	@Column(name="cancel_deadline")
	public java.util.Date getCancelDeadline() {
		return this.cancelDeadline;
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
	
	@Column(name="airticket_order_pnrGroup_uuid")
	public String getAirticketOrderPnrGroupUuid() {
		return airticketOrderPnrGroupUuid;
	}

	public void setAirticketOrderPnrGroupUuid(String airticketOrderPnrGroupUuid) {
		this.airticketOrderPnrGroupUuid = airticketOrderPnrGroupUuid;
	}


	
}

