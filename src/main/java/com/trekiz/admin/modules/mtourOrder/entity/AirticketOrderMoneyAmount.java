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
@Table(name = "airticket_order_moneyAmount")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AirticketOrderMoneyAmount   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "AirticketOrderMoneyAmount";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_SERIAL_NUM = "流水号UUID";
	public static final String ALIAS_CURRENCY_ID = "币种";
	public static final String ALIAS_AMOUNT = "价格";
	public static final String ALIAS_EXCHANGERATE = "汇率";
	public static final String ALIAS_AIRTICKET_ORDER_ID = "机票订单id";
	public static final String ALIAS_MONEY_TYPE = "款项类型（借款：1，退款：2，追加成本：3）";
	public static final String ALIAS_FUNDS_NAME = "款项名称";
	public static final String ALIAS_MEMO = "备注";
	public static final String ALIAS_STATUS = "状态(默认是1，撤消是0)";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	public static final String ALIAS_PAY_STATUS = "付款状态（0 没付款, 1:已付款）";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"流水号UUID"
	private java.lang.String serialNum;
	//"币种"
	private java.lang.Integer currencyId;
	//"价格"
	private Double amount;
	//"汇率"
	private Double exchangerate;
	//"机票订单id"
	private java.lang.Integer airticketOrderId;
	//"款项类型（借款：1，退款：2，追加成本：3）"
	private java.lang.Integer moneyType;
	//"款项名称"
	private java.lang.String fundsName;
	//"备注"
	private java.lang.String memo;
	//"状态(默认是1，撤消是0)"
	private java.lang.Integer status;
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
	//付款状态:0 待付款, 1:已付款
	private java.lang.Integer paystatus;
	//columns END

	
	/** 撤销状态 */
	public final static int STATUS_CANCEL = 0;
	/** 已提交状态 */
	public final static int STATUS_CONFIRM = 1;
	
	/** 付款状态:0 待付款 */
	public final static int PAYSTATUS_WAIT = 0;
	/** 付款状态:1:已付款 */
	public final static int PAYSTATUS_CONFIRM = 1;
	
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
	public AirticketOrderMoneyAmount(){
	}

	public AirticketOrderMoneyAmount(
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
	
		
	public void setSerialNum(java.lang.String value) {
		this.serialNum = value;
	}
	@Column(name="serialNum")
	public java.lang.String getSerialNum() {
		return this.serialNum;
	}
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	
		
	public void setAmount(Double value) {
		this.amount = value;
	}
	@Column(name="amount")
	public Double getAmount() {
		return this.amount;
	}
	
		
	public void setExchangerate(Double value) {
		this.exchangerate = value;
	}
	@Column(name="exchangerate")
	public Double getExchangerate() {
		return this.exchangerate;
	}
	
		
	public void setAirticketOrderId(java.lang.Integer value) {
		this.airticketOrderId = value;
	}
	@Column(name="airticket_order_id")
	public java.lang.Integer getAirticketOrderId() {
		return this.airticketOrderId;
	}
	
		
	public void setMoneyType(java.lang.Integer value) {
		this.moneyType = value;
	}
	@Column(name="moneyType")
	public java.lang.Integer getMoneyType() {
		return this.moneyType;
	}
	
		
	public void setFundsName(java.lang.String value) {
		this.fundsName = value;
	}
	@Column(name="funds_name")
	public java.lang.String getFundsName() {
		return this.fundsName;
	}
	
		
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	@Column(name="memo")
	public java.lang.String getMemo() {
		return this.memo;
	}
	
		
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.Integer getStatus() {
		return this.status;
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
	@Column(name="paystatus")
	public java.lang.Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(java.lang.Integer paystatus) {
		this.paystatus = paystatus;
	}
	
	/**
	 * 获取付款表的款项类型moneyType
	 * @Description: 
	 * @param @return   
	 * @return Integer  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-29
	 */
	@Transient
	public Integer getRefundMoneyType() {
		//Amount表的'款项类型（借款：1，退款：2，追加成本：3）'
		//refund表的款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款;7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；11：新审核退签证押金；12：新审核追加成本付款；13：批量借款）
		if(this.moneyType == null) {
			return null;
		}
		switch(this.moneyType) {
		case 1:return 4;
		case 2:return 2;
		case 3:return 6;
		}
		return null;
	}
	
	/**
	 * 获取付款表的款项类型moneyType
	 * @Description: 
	 * @param @return   
	 * @return Integer  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-29
	 */
	@Transient
	public static Integer getRefundMoneyType(Integer moneyType) {
		//Amount表的'款项类型（借款：1，退款：2，追加成本：3）'
		//refund表的款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款;7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；11：新审核退签证押金；12：新审核追加成本付款；13：批量借款）
		if(moneyType == null) {
			return null;
		}
		switch(moneyType) {
		case 1:return 4;
		case 2:return 2;
		case 3:return 6;
		}
		return null;
	}

	
}

