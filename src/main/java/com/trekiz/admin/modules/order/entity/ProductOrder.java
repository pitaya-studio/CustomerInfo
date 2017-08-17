package com.trekiz.admin.modules.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trekiz.admin.common.mapper.CustomDateSerializer;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 订单通用实体类：单团、散拼、游学、大客户、自由行
 * @author 赵海明
 *
 */
@Entity
@Table(name = "productorder")
public class ProductOrder extends DataEntity {
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private Long id; 		// 编号
		/** 产品id */
		private Long productId;
		/** 产品出团信息表id */
		private ActivityGroup activityGroup;
		/** 订单单号 */
		private String orderNum;
		/** 预订单位 */
		private Long orderCompany;
		/** 预订单位名称 */
		private String orderCompanyName;
		/** 预订人 */
		private User orderSaler;
		/** 预订联系人名称 */
		private String orderPersonName;
		/** 预订联系人电话 */
		private String orderPersonPhoneNum;
		/** 预订日期 */
		private Date orderTime;
		/** 预订人数 */
		private Integer orderPersonNum;
		/** 订单种类 */
		private Integer orderStatus;
		
		/** 成人预订人数 */
		private Integer orderPersonNumAdult;
		/** 儿童预订人数 */
		private Integer orderPersonNumChild;
		/** 特殊预订人数 */
		private Integer orderPersonNumSpecial;
		/** 订金金额 */
		private String frontMoney;
		/** 支付状态 */
		private Integer payStatus;
		/** 订单原价总额 */
		private String totalMoney;
		/** 已支付金额 */
		private String payedMoney;
		
		/** 总达账金额*/
		private String accountedMoney;
		/** 占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位*/
		private Integer placeHolderType;
		
		/** 下订单时成人的产品结算价 */
		private String settlementAdultPrice;
		/** 下订单时儿童的产品结算价 */
		private String settlementcChildPrice;
		/** 下订单时特殊的产品结算价 */
		private String settlementSpecialPrice;
		/** 下订单时产品的预收订金 */
		private String payDeposit;
		/** 下订单时的单房差 */
		private String singleDiff;
		/**付款方式*/
		private String payMode;
		/**保留天数*/
		private Integer remainDays;
		/** 激活日期 */
		private Date activationDate;
		/** T1 平台，是否查看*/
		private String hasSeen;
		
		private String signKey;
		
		private String cancelDescription;
		
		/**是否是补单订单：0为否，1为是*/
		private Integer isAfterSupplement;
		
		/** 联运类型ID */
		private Long intermodalType;
		/** 订单锁定状态  0：正常，1：锁定   默认为0 */
		private Integer lockStatus;
		/** 确认单文件ID */
		private Long confirmationFileId;
		/** 出团通知文件ID */
	    private String openNoticeFileId;
		
		private String specialDemand;

		private String specialDemandFileIds;

		/*原始应收价*/
		private String originalTotalMoney;
		
		/** 销售id*/
		private Integer salerId;
		
		/** 销售名称*/
		private String salerName;
		
		/** t2是否向quauq结清服务费 0未结清 1已结清 */
		private Integer isPayedCharge;
		
		/** 渠道类型：0 普通渠道；1 门店；2 总社；3 集团客户 */
		private Integer agentType = -1;
		/**订单差额支付状态：0 未支付 1：已支付*/
		private Integer differencePayStatus=0;
		public Integer getIsPayedCharge() {
			return isPayedCharge;
		}

		public void setIsPayedCharge(Integer isPayedCharge) {
			this.isPayedCharge = isPayedCharge;
		}
		
		public Integer getSalerId() {
			return salerId;
		}

		public void setSalerId(Integer salerId) {
			this.salerId = salerId;
		}

		public String getSalerName() {
			return salerName;
		}

		public void setSalerName(String salerName) {
			this.salerName = salerName;
		}

		@OneToOne
	    @JoinColumn(name="productGroupId")
	    @JsonIgnore
	    @NotFound(action = NotFoundAction.IGNORE)
		public ActivityGroup getActivityGroup() {
			return activityGroup;
		}

		public void setActivityGroup(ActivityGroup activityGroup) {
			this.activityGroup = activityGroup;
		}

		@Column(name="original_total_money")
		public String getOriginalTotalMoney() {
			return originalTotalMoney;
		}
		
		public void setOriginalTotalMoney(String originalTotalMoney) {
			this.originalTotalMoney = originalTotalMoney;
		}
		
		public Integer getLockStatus() {
			return lockStatus;
		}
		
		public void setLockStatus(Integer lockStatus) {
			this.lockStatus = lockStatus;
		}
		
		@Column(name="front_money")
		public String getFrontMoney() {
			return frontMoney;
		}
		
		public void setFrontMoney(String frontMoney) {
			this.frontMoney = frontMoney;
		}
		
		@Column(name="total_money")
		public String getTotalMoney() {
			return totalMoney;
		}
		
		public void setTotalMoney(String totalMoney) {
			this.totalMoney = totalMoney;
		}
		
		@Column(name="payed_money")
		public String getPayedMoney() {
			return payedMoney;
		}
		
		public void setPayedMoney(String payedMoney) {
			this.payedMoney = payedMoney;
		}
		
		@Column(name="accounted_money")
		public String getAccountedMoney() {
			return accountedMoney;
		}
		
		public void setAccountedMoney(String accountedMoney) {
			this.accountedMoney = accountedMoney;
		}
		
		public String getSettlementAdultPrice() {
			return settlementAdultPrice;
		}
		
		public void setSettlementAdultPrice(String settlementAdultPrice) {
			this.settlementAdultPrice = settlementAdultPrice;
		}
		
		public String getSettlementcChildPrice() {
			return settlementcChildPrice;
		}
		
		public void setSettlementcChildPrice(String settlementcChildPrice) {
			this.settlementcChildPrice = settlementcChildPrice;
		}
		
		public String getSettlementSpecialPrice() {
			return settlementSpecialPrice;
		}
		
		public void setSettlementSpecialPrice(String settlementSpecialPrice) {
			this.settlementSpecialPrice = settlementSpecialPrice;
		}
		
		public String getPayDeposit() {
			return payDeposit;
		}
		
		public void setPayDeposit(String payDeposit) {
			this.payDeposit = payDeposit;
		}
		
		public String getSingleDiff() {
			return singleDiff;
		}
		
		public void setSingleDiff(String singleDiff) {
			this.singleDiff = singleDiff;
		}
		
		public Integer getOrderPersonNumAdult() {
		    return orderPersonNumAdult;
		}
		
		public void setOrderPersonNumAdult(Integer orderPersonNumAdult) {
		    this.orderPersonNumAdult = orderPersonNumAdult;
		}
		
		public Integer getOrderPersonNumChild() {
		    return orderPersonNumChild;
		}
		
		public void setOrderPersonNumChild(Integer orderPersonNumChild) {
		    this.orderPersonNumChild = orderPersonNumChild;
		}
		
		public Integer getOrderPersonNumSpecial() {
			return orderPersonNumSpecial;
		}
		
		public void setOrderPersonNumSpecial(Integer orderPersonNumSpecial) {
			this.orderPersonNumSpecial = orderPersonNumSpecial;
		}
		
		
		public Integer getPlaceHolderType() {
		    return placeHolderType;
		}
		
		public void setPlaceHolderType(Integer placeHolderType) {
		    this.placeHolderType = placeHolderType;
		}
		
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_productorder")
		//@SequenceGenerator(name = "seq_order_productorder", sequenceName = "seq_order_productorder")
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public void setProductId(Long productId ){
		    this.productId = productId ;
		}
		
		public Long getProductId(){
		    return this.productId;
		}
	
		
		public void setOrderNum(String orderNum ){
		    this.orderNum = orderNum ;
		}
		
		@Length(min=0, max=50)
		public String getOrderNum(){
		    return this.orderNum;
		}
		
		public void setOrderStatus(Integer orderStatus ){
		    this.orderStatus = orderStatus ;
		}
		
		public Integer getOrderStatus(){
		    return this.orderStatus;
		}
		
		public void setOrderCompany(Long orderCompany ){
		    this.orderCompany = orderCompany ;
		}
		
		public Long getOrderCompany(){
		    return this.orderCompany;
		}
		
		public void setOrderCompanyName(String orderCompanyName ){
		    this.orderCompanyName = orderCompanyName ;
		}
		
		@Length(min=0, max=100)
		public String getOrderCompanyName(){
		    return this.orderCompanyName;
		}
		
		public void setOrderPersonName(String orderPersonName ){
		    this.orderPersonName = orderPersonName ;
		}
		
		@OneToOne(fetch = FetchType.EAGER)
		@JoinColumn(name="orderSalerId")
		@NotFound(action = NotFoundAction.IGNORE)
		@JsonIgnore
		public User getOrderSaler() {
			return orderSaler;
		}
		
		public void setOrderSaler(User orderSaler) {
			this.orderSaler = orderSaler;
		}
		
		@Length(min=0, max=50)
		public String getOrderPersonName(){
		    return this.orderPersonName;
		}
		
		public void setOrderPersonPhoneNum(String orderPersonPhoneNum ){
		    this.orderPersonPhoneNum = orderPersonPhoneNum ;
		}
		
		@Length(min=0, max=50)
		public String getOrderPersonPhoneNum(){
		    return this.orderPersonPhoneNum;
		}
		
		public void setOrderTime(Date orderTime ){
		    this.orderTime = orderTime ;
		}
		
		@JsonSerialize(using = CustomDateSerializer.class)
		public Date getOrderTime(){
		    return this.orderTime;
		}
		
		public void setOrderPersonNum(Integer orderPersonNum ){
		    this.orderPersonNum = orderPersonNum ;
		}
		
		public Integer getOrderPersonNum(){
		    return this.orderPersonNum;
		}
		
		
		
		
		public void setPayStatus(Integer payStatus ){
		    this.payStatus = payStatus ;
		}
		
		@Column(name="payStatus",unique=false,nullable=false)
		public Integer getPayStatus(){
		    return this.payStatus;
		}
		
		@Column(name="cancel_description", nullable=true, unique=false)
		public String getCancelDescription() {
			return cancelDescription;
		}
		
		public void setCancelDescription(String cancelDescription) {
			this.cancelDescription = cancelDescription;
		}
		@Column(name="payMode",unique=false,nullable=false)
		public String getPayMode() {
			return payMode;
		}
		
		public void setPayMode(String payMode) {
			this.payMode = payMode;
		}
		@Column(name="remainDays",unique=false,nullable=false)
		public Integer getRemainDays() {
			return remainDays;
		}
		
		public void setRemainDays(Integer remainDays) {
			this.remainDays = remainDays;
		}
		
		@Column(name="confirmationFileId")
		public Long getConfirmationFileId() {
			return confirmationFileId;
		}
		
		public void setConfirmationFileId(Long confirmationFileId) {
			this.confirmationFileId = confirmationFileId;
		}
		
		@Column(name="openNoticeFileId")
		public String getOpenNoticeFileId() {
			return openNoticeFileId;
		}

		public void setOpenNoticeFileId(String openNoticeFileId) {
			this.openNoticeFileId = openNoticeFileId;
		}
		
		@Column(name = "is_after_supplement", columnDefinition = "tinyint default 0")
		public Integer getIsAfterSupplement() {
			return isAfterSupplement;
		}
		
		public void setIsAfterSupplement(Integer isAfterSupplement) {
			this.isAfterSupplement = isAfterSupplement;
		}
		
		@Temporal(TemporalType.TIMESTAMP)
		@JsonSerialize(using = CustomDateSerializer.class)
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		public Date getActivationDate() {
			return activationDate;
		}
		
		public void setActivationDate(Date activationDate) {
			this.activationDate = activationDate;
		}
		
		@PrePersist
		public void preSaveActivationDate(){
			this.activationDate = this.orderTime;
		}
		
		@Column(name="intermodal_type",unique=false,nullable=true)
		public Long getIntermodalType() {
		  return intermodalType;
		}
		
		public void setIntermodalType(Long intermodalType) {
		  this.intermodalType = intermodalType;
		}
		
		public String getSpecialDemand() {
			return specialDemand;
		}
		
		public void setSpecialDemand(String specialDemand) {
			this.specialDemand = specialDemand;
		}

		public String getSpecialDemandFileIds() {
			return specialDemandFileIds;
		}

		public void setSpecialDemandFileIds(String specialDemandFileIds) {
			this.specialDemandFileIds = specialDemandFileIds;
		}

		public String getHasSeen() {
			return hasSeen;
		}

		public void setHasSeen(String hasSeen) {
			this.hasSeen = hasSeen;
		}

		@Column(name="agent_type")
		public Integer getAgentType() {
			return agentType;
		}

		public void setAgentType(Integer agentType) {
			this.agentType = agentType;
		}
		
		@Column(name = "differencePayStatus")
		public Integer getDifferencePayStatus() {
			return differencePayStatus;
		}

		public void setDifferencePayStatus(Integer differencePayStatus) {
			this.differencePayStatus = differencePayStatus;
		}
		
}
