/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.order.entity.OrderContacts;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_order")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelOrder   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelOrder";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "订单uuid";
	public static final String ALIAS_ACTIVITY_HOTEL_UUID = "海岛游产品uuid";
	public static final String ALIAS_ACTIVITY_HOTEL_GROUP_UUID = "海岛游产品团期uuid";
	public static final String ALIAS_ORDER_NUM = "订单单号";
	public static final String ALIAS_ORDER_STATUS = "订单状态：0 全部订单；1 待确认报名；2 已确认报名；3 已取消；";
	public static final String ALIAS_ORDER_COMPANY = "预订单位-即渠道";
	public static final String ALIAS_ORDER_COMPANY_NAME = "预订单位名称";
	public static final String ALIAS_ORDER_SALER_ID = "跟进销售员id";
	public static final String ALIAS_ORDER_PERSON_NAME = "预订人名称";
	public static final String ALIAS_ORDER_PERSON_PHONE_NUM = "预订人联系电话";
	public static final String ALIAS_ORDER_TIME = "预订日期";
	public static final String ALIAS_ORDER_PERSON_NUM = "预定人数";
	public static final String ALIAS_FRONT_MONEY = "订金金额UUID";
	public static final String ALIAS_PAY_STATUS = "支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消";
	public static final String ALIAS_PAYED_MONEY = "已付金额UUID";
	public static final String ALIAS_PAY_TYPE = "支付方式1-支票 2-POS机付款 3-现金支付 4-汇款 5-快速支付";
	public static final String ALIAS_CREATE_BY = "创建者";
	public static final String ALIAS_CREATE_DATE = "创建日期";
	public static final String ALIAS_UPDATE_BY = "更新者";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_DEL_FLAG = "删除标记";
	public static final String ALIAS_CHANGE_GROUP_ID = "当前退换记录Id";
	public static final String ALIAS_GROUP_CHANGE_TYPE = "退换类型";
	public static final String ALIAS_COST_MONEY = "订单成本金额";
	public static final String ALIAS_AS_ACOUNT_TYPE = "达账状态";
	public static final String ALIAS_ACCOUNTED_MONEY = "达账金额UUID";
	public static final String ALIAS_PAY_DEPOSIT = "下订单时产品的预收定金";
	public static final String ALIAS_PLACE_HOLDER_TYPE = "占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位";
	public static final String ALIAS_SINGLE_DIFF = "下订单时的单房差";
	public static final String ALIAS_CANCEL_DESCRIPTION = "取消原因";
	public static final String ALIAS_IS_PAYMENT = "0 未付款 1 已付首款 2 已付尾款（全款）3 首款已达账 4 尾款（全款）已达账 5 开发票申请 6 已开发票";
	public static final String ALIAS_PAY_MODE = "付款方式";
	public static final String ALIAS_REMAIN_DAYS = "保留天数";
	public static final String ALIAS_ACTIVATION_DATE = "激活时间";
	public static final String ALIAS_LOCK_STATUS = "订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)";
	public static final String ALIAS_SPECIAL_DEMAND = "特殊需求";
	public static final String ALIAS_TOTAL_MONEY = "订单总价UUID";
	public static final String ALIAS_FILE_IDS = "文件ids";
	public static final String ALIAS_ORIGINAL_TOTAL_MONEY = "原始应收价 一次生成永不改变";
	public static final String ALIAS_IS_AFTER_SUPPLEMENT = "是否是补单产品，0：否，1：是";
	public static final String ALIAS_ORIGINAL_FRONT_MONEY = "原始订金金额（乘人数后金额）";
	public static final String ALIAS_PAYMENT_TYPE = "结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4";
	public static final String ALIAS_FORECASE_REPORT_NUM = "预报名间数";
	public static final String ALIAS_SUB_CONTROL_NUM = "酒店扣减控房间数";
	public static final String ALIAS_SUB_UN_CONTROL_NUM = "酒店扣减非控房间数";
	public static final String ALIAS_REMARK = "备注";
	
	//date formats
	public static final String FORMAT_ORDER_TIME = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_ACTIVATION_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"订单uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityHotelUuid;
	//"海岛游产品团期uuid"
	private java.lang.String activityHotelGroupUuid;
	//"订单单号"
	private java.lang.String orderNum;
	//"订单状态：0 全部订单；1 待确认报名；2 已确认报名；3 已取消；"
	private Integer orderStatus;
	//"预订单位-即渠道"
	private java.lang.Integer orderCompany;
	//"预订单位名称"
	private java.lang.String orderCompanyName;
	//"跟进销售员id"
	private java.lang.Integer orderSalerId;
	//"预订人名称"
	private java.lang.String orderPersonName;
	//"预订人联系电话"
	private java.lang.String orderPersonPhoneNum;
	//"预订日期"
	private java.util.Date orderTime;
	//"预定人数"
	private java.lang.Integer orderPersonNum;
	//"订金金额UUID"
	private java.lang.String frontMoney;
	//"支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消"
	private Integer payStatus;
	//"已付金额UUID"
	private java.lang.String payedMoney;
	//"支付方式1-支票 2-POS机付款 3-现金支付 4-汇款 5-快速支付"
	private Integer payType;
	//"创建者"
	private java.lang.Integer createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新者"
	private java.lang.Integer updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标记"
	private java.lang.String delFlag;
	//"当前退换记录Id"
	private java.lang.Integer changeGroupId;
	//"退换类型"
	private java.lang.Integer groupChangeType;
	//"订单成本金额"
	private java.lang.String costMoney;
	//"达账状态"
	private Integer asAcountType;
	//"达账金额UUID"
	private java.lang.String accountedMoney;
	//"下订单时产品的预收定金"
	private java.lang.String payDeposit;
	//"占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位"
	private Integer placeHolderType;
	//"下订单时的单房差"
	private java.lang.String singleDiff;
	//"取消原因"
	private java.lang.String cancelDescription;
	//"0 未付款 1 已付首款 2 已付尾款（全款）3 首款已达账 4 尾款（全款）已达账 5 开发票申请 6 已开发票"
	private Integer isPayment;
	//"付款方式"
	private java.lang.String payMode;
	//"保留天数"
	private java.lang.Integer remainDays;
	//"激活时间"
	private java.util.Date activationDate;
	//"订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)"
	private Integer lockStatus;
	//"特殊需求"
	private java.lang.String specialDemand;
	//"订单总价UUID"
	private java.lang.String totalMoney;
	//"文件ids"
	private java.lang.String fileIds;
	//"原始应收价 一次生成永不改变"
	private java.lang.String originalTotalMoney;
	//"是否是补单产品，0：否，1：是"
	private java.lang.Boolean isAfterSupplement;
	//"原始订金金额（乘人数后金额）"
	private java.lang.String originalFrontMoney;
	//"结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4"
	private java.lang.Integer paymentType;
	//"预报名间数"
	private java.lang.Integer forecaseReportNum;
	//"酒店扣减控房间数"
	private java.lang.Integer subControlNum;
	//"酒店扣减非控房间数"
	private java.lang.Integer subUnControlNum;
	//"备注"
	private java.lang.String remark;
	//"转报名时间"
	private java.util.Date applyTime;
	
	//数据结构
	//联系人信息集合
  	private List<OrderContacts> orderContactsList;
  	//游客类型费用、费用调整 等信息 集合
  	private List<HotelOrderPrice> hotelOrderPriceList;
  	//游客信息 集合
  	private List<HotelTraveler> hotelTravelerList;
	//订单的金额信息，订单总额、结算总额、应收、已收
  	private List<HotelMoneyAmount> hotelMoneyAmountList;
  	
  	/** 全部 */
	public final static int ORDER_STATUS_ALL = 0;
	/** 待确认报名 */
	public final static int ORDER_STATUS_TO_CONFIRM = 1;
	/** 已确认报名 */
	public final static int ORDER_STATUS_CONFIRMED = 2;
	/** 已取消 */
	public final static int ORDER_STATUS_CANCEL = 3;
	
	/** 订单锁定状态:正常 */
	public final static int LOCK_STATUS_NORMAL = 0;
	/** 订单锁定状态:锁定 */
	public final static int LOCK_STATUS_LOCK = 1;

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
	public HotelOrder(){
	}

	public HotelOrder(
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
	
		
	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}
	@Column(name="activity_hotel_uuid")
	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
	}
	
		
	public void setActivityHotelGroupUuid(java.lang.String value) {
		this.activityHotelGroupUuid = value;
	}
	@Column(name="activity_hotel_group_uuid")
	public java.lang.String getActivityHotelGroupUuid() {
		return this.activityHotelGroupUuid;
	}
	
		
	public void setOrderNum(java.lang.String value) {
		this.orderNum = value;
	}
	@Column(name="orderNum")
	public java.lang.String getOrderNum() {
		return this.orderNum;
	}
	
		
	public void setOrderStatus(Integer value) {
		this.orderStatus = value;
	}
	@Column(name="orderStatus")
	public Integer getOrderStatus() {
		return this.orderStatus;
	}
	
		
	public void setOrderCompany(java.lang.Integer value) {
		this.orderCompany = value;
	}
	@Column(name="orderCompany")
	public java.lang.Integer getOrderCompany() {
		return this.orderCompany;
	}
	
		
	public void setOrderCompanyName(java.lang.String value) {
		this.orderCompanyName = value;
	}
	@Column(name="orderCompanyName")
	public java.lang.String getOrderCompanyName() {
		return this.orderCompanyName;
	}
	
		
	public void setOrderSalerId(java.lang.Integer value) {
		this.orderSalerId = value;
	}
	@Column(name="orderSalerId")
	public java.lang.Integer getOrderSalerId() {
		return this.orderSalerId;
	}
	
		
	public void setOrderPersonName(java.lang.String value) {
		this.orderPersonName = value;
	}
	@Column(name="orderPersonName")
	public java.lang.String getOrderPersonName() {
		return this.orderPersonName;
	}
	
		
	public void setOrderPersonPhoneNum(java.lang.String value) {
		this.orderPersonPhoneNum = value;
	}
	@Column(name="orderPersonPhoneNum")
	public java.lang.String getOrderPersonPhoneNum() {
		return this.orderPersonPhoneNum;
	}
	@Transient	
	public String getOrderTimeString() {
		if(getOrderTime() != null) {
			return this.date2String(getOrderTime(), FORMAT_ORDER_TIME);
		} else {
			return null;
		}
	}
	public void setOrderTimeString(String value) {
		setOrderTime(this.string2Date(value, FORMAT_ORDER_TIME));
	}
	
		
	public void setOrderTime(java.util.Date value) {
		this.orderTime = value;
	}
	@Column(name="orderTime")
	public java.util.Date getOrderTime() {
		return this.orderTime;
	}
	
		
	public void setOrderPersonNum(java.lang.Integer value) {
		this.orderPersonNum = value;
	}
	@Column(name="orderPersonNum")
	public java.lang.Integer getOrderPersonNum() {
		return this.orderPersonNum;
	}
	
		
	public void setFrontMoney(java.lang.String value) {
		this.frontMoney = value;
	}
	@Column(name="front_money")
	public java.lang.String getFrontMoney() {
		return this.frontMoney;
	}
	
		
	public void setPayStatus(Integer value) {
		this.payStatus = value;
	}
	@Column(name="payStatus")
	public Integer getPayStatus() {
		return this.payStatus;
	}
	
		
	public void setPayedMoney(java.lang.String value) {
		this.payedMoney = value;
	}
	@Column(name="payed_money")
	public java.lang.String getPayedMoney() {
		return this.payedMoney;
	}
	
		
	public void setPayType(Integer value) {
		this.payType = value;
	}
	@Column(name="payType")
	public Integer getPayType() {
		return this.payType;
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
	
		
	public void setChangeGroupId(java.lang.Integer value) {
		this.changeGroupId = value;
	}
	@Column(name="changeGroupId")
	public java.lang.Integer getChangeGroupId() {
		return this.changeGroupId;
	}
	
		
	public void setGroupChangeType(java.lang.Integer value) {
		this.groupChangeType = value;
	}
	@Column(name="groupChangeType")
	public java.lang.Integer getGroupChangeType() {
		return this.groupChangeType;
	}
	
		
	public void setCostMoney(java.lang.String value) {
		this.costMoney = value;
	}
	@Column(name="cost_money")
	public java.lang.String getCostMoney() {
		return this.costMoney;
	}
	
		
	public void setAsAcountType(Integer value) {
		this.asAcountType = value;
	}
	@Column(name="asAcountType")
	public Integer getAsAcountType() {
		return this.asAcountType;
	}
	
		
	public void setAccountedMoney(java.lang.String value) {
		this.accountedMoney = value;
	}
	@Column(name="accounted_money")
	public java.lang.String getAccountedMoney() {
		return this.accountedMoney;
	}
	
		
	public void setPayDeposit(java.lang.String value) {
		this.payDeposit = value;
	}
	@Column(name="payDeposit")
	public java.lang.String getPayDeposit() {
		return this.payDeposit;
	}
	
		
	public void setPlaceHolderType(Integer value) {
		this.placeHolderType = value;
	}
	@Column(name="placeHolderType")
	public Integer getPlaceHolderType() {
		return this.placeHolderType;
	}
	
		
	public void setSingleDiff(java.lang.String value) {
		this.singleDiff = value;
	}
	@Column(name="singleDiff")
	public java.lang.String getSingleDiff() {
		return this.singleDiff;
	}
	
		
	public void setCancelDescription(java.lang.String value) {
		this.cancelDescription = value;
	}
	@Column(name="cancel_description")
	public java.lang.String getCancelDescription() {
		return this.cancelDescription;
	}
	
		
	public void setIsPayment(Integer value) {
		this.isPayment = value;
	}
	@Column(name="isPayment")
	public Integer getIsPayment() {
		return this.isPayment;
	}
	
		
	public void setPayMode(java.lang.String value) {
		this.payMode = value;
	}
	@Column(name="payMode")
	public java.lang.String getPayMode() {
		return this.payMode;
	}
	
		
	public void setRemainDays(java.lang.Integer value) {
		this.remainDays = value;
	}
	@Column(name="remainDays")
	public java.lang.Integer getRemainDays() {
		return this.remainDays;
	}
	@Transient	
	public String getActivationDateString() {
		if(getActivationDate() != null) {
			return this.date2String(getActivationDate(), FORMAT_ACTIVATION_DATE);
		} else {
			return null;
		}
	}
	public void setActivationDateString(String value) {
		setActivationDate(this.string2Date(value, FORMAT_ACTIVATION_DATE));
	}
	
		
	public void setActivationDate(java.util.Date value) {
		this.activationDate = value;
	}
	@Column(name="activationDate")
	public java.util.Date getActivationDate() {
		return this.activationDate;
	}
	
		
	public void setLockStatus(Integer value) {
		this.lockStatus = value;
	}
	@Column(name="lockStatus")
	public Integer getLockStatus() {
		return this.lockStatus;
	}
	
		
	public void setSpecialDemand(java.lang.String value) {
		this.specialDemand = value;
	}
	@Column(name="specialDemand")
	public java.lang.String getSpecialDemand() {
		return this.specialDemand;
	}
	
		
	public void setTotalMoney(java.lang.String value) {
		this.totalMoney = value;
	}
	@Column(name="total_money")
	public java.lang.String getTotalMoney() {
		return this.totalMoney;
	}
	
		
	public void setFileIds(java.lang.String value) {
		this.fileIds = value;
	}
	@Column(name="fileIds")
	public java.lang.String getFileIds() {
		return this.fileIds;
	}
	
		
	public void setOriginalTotalMoney(java.lang.String value) {
		this.originalTotalMoney = value;
	}
	@Column(name="original_total_money")
	public java.lang.String getOriginalTotalMoney() {
		return this.originalTotalMoney;
	}
	
		
	public void setIsAfterSupplement(java.lang.Boolean value) {
		this.isAfterSupplement = value;
	}
	@Column(name="is_after_supplement")
	public java.lang.Boolean getIsAfterSupplement() {
		return this.isAfterSupplement;
	}
	
		
	public void setOriginalFrontMoney(java.lang.String value) {
		this.originalFrontMoney = value;
	}
	@Column(name="original_front_money")
	public java.lang.String getOriginalFrontMoney() {
		return this.originalFrontMoney;
	}
	
		
	public void setPaymentType(java.lang.Integer value) {
		this.paymentType = value;
	}
	@Column(name="paymentType")
	public java.lang.Integer getPaymentType() {
		return this.paymentType;
	}
	
		
	public void setForecaseReportNum(java.lang.Integer value) {
		this.forecaseReportNum = value;
	}
	@Column(name="forecase_report_num")
	public java.lang.Integer getForecaseReportNum() {
		return this.forecaseReportNum;
	}
	
		
	public void setSubControlNum(java.lang.Integer value) {
		this.subControlNum = value;
	}
	@Column(name="sub_control_num")
	public java.lang.Integer getSubControlNum() {
		return this.subControlNum;
	}
	
		
	public void setSubUnControlNum(java.lang.Integer value) {
		this.subUnControlNum = value;
	}
	@Column(name="sub_unControl_num")
	public java.lang.Integer getSubUnControlNum() {
		return this.subUnControlNum;
	}
	
		
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	@Column(name="remark")
	public java.lang.String getRemark() {
		return this.remark;
	}

	@Column(name="apply_time")
	public java.util.Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(java.util.Date applyTime) {
		this.applyTime = applyTime;
	}
	
	@Transient
	public List<OrderContacts> getOrderContactsList() {
		return orderContactsList;
	}

	public void setOrderContactsList(List<OrderContacts> orderContactsList) {
		this.orderContactsList = orderContactsList;
	}

	@Transient
	public List<HotelOrderPrice> getHotelOrderPriceList() {
		return hotelOrderPriceList;
	}

	public void setHotelOrderPriceList(List<HotelOrderPrice> hotelOrderPriceList) {
		this.hotelOrderPriceList = hotelOrderPriceList;
	}

	@Transient
	public List<HotelTraveler> getHotelTravelerList() {
		return hotelTravelerList;
	}

	public void setHotelTravelerList(List<HotelTraveler> hotelTravelerList) {
		this.hotelTravelerList = hotelTravelerList;
	}

	@Transient
	public List<HotelMoneyAmount> getHotelMoneyAmountList() {
		return hotelMoneyAmountList;
	}

	public void setHotelMoneyAmountList(List<HotelMoneyAmount> hotelMoneyAmountList) {
		this.hotelMoneyAmountList = hotelMoneyAmountList;
	}



	
}

