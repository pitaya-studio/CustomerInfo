package com.trekiz.admin.modules.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "refund")
public class Refund extends DataEntityTTS {
	private static final long serialVersionUID = 6889036336229097632L;
	/** 主键ID */
	private String id;
	/** 支付方式（现金、支票、汇款等） */
	private Integer payType;
	/** 支付方式表ID */
	private String payTypeId;
	/** 付款金额UUID */
	private String moneySerialNum;
	/** 支付凭证附件id */
	private String payVoucher;
	/** 备注信息 */
	private String remarks;

	/** 是否合并支付（0：不合并;1：合并） */
	private String mergePayFlag;
	/** 款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款;7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；11：新审核退签证押金；12：新审核追加成本付款；13：批量借款） */
	private Integer moneyType;
	/** 记录状态 */
	private String status;
	/** 业务表ID、其中包括(review表id、cost_record表id、airticket_order_moneyAmount表id、review_new表id) */
	private Long recordId;
	/** 合并后的总金额UUID */
	private String mergeMoneySerialNum;
	/** 收款单位 */
	private String payee;
	/** 订单类型 */
	private Integer orderType;
	/** 批发商uuid */
	private String companyUuid;
	/** 批次号 */
	private String batchNumber;

	/** 成本录入付款 */
	public static final int MONEY_TYPE_COST = 1;
	/** 退款付款 */
	public static final int MONEY_TYPE_RETURNMONEY = 2;
	/** 返佣付款 */
	public static final int MONEY_TYPE_PAYBACK = 3;
	/** 借款付款 */
	public static final int MONEY_TYPE_BORROW = 4;
	/** 退签证押金 */
	public static final int MONEY_TYPE_RETURNVISADEPOSIT = 5;
	/** 追加成本付款 */
	public static final int MONEY_TYPE_ADDCOST = 6;
	/** 新审核成本录入付款 */
	public static final int MONEY_TYPE_NEWCOST = 7;
	/** 新审核退款付款 */
	public static final int MONEY_TYPE_NEWRETURNMONEY = 8;
	/** 新审核返佣付款 */
	public static final int MONEY_TYPE_NEWPAYBACK = 9;
	/** 新审核借款付款 */
	public static final int MONEY_TYPE_NEWBORROW = 10;
	/** 新审核退签证押金 */
	public static final int MONEY_TYPE_NEWRETURNVISADEPOSIT = 11;
	/** 新审核追加成本付款 */
	public static final int MONEY_TYPE_NEWADDCOST = 12;
	/** 批量借款 */
	public static final int MONEY_TYPE_BATCHBORROW = 13;
	/** quauq服务费付款 */
	public static final int MONEY_TYPE_QUAUQ = 14;
	/** 总社服务费付款 */
	public static final int MONEY_TYPE_PARTNER = 15;

	@Column(name = "merge_money_serial_num")
	public String getMergeMoneySerialNum() {
		return mergeMoneySerialNum;
	}

	public void setMergeMoneySerialNum(String mergeMoneySerialNum) {
		this.mergeMoneySerialNum = mergeMoneySerialNum;
	}

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name = "pay_type_Id")
	public String getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	@Column(name = "money_serial_num")
	public String getMoneySerialNum() {
		return moneySerialNum;
	}

	public void setMoneySerialNum(String moneySerialNum) {
		this.moneySerialNum = moneySerialNum;
	}

	@Column(name = "pay_voucher")
	public String getPayVoucher() {
		return payVoucher;
	}

	public void setPayVoucher(String payVoucher) {
		this.payVoucher = payVoucher;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "mergePayFlag")
	public String getMergePayFlag() {
		return mergePayFlag;
	}

	public void setMergePayFlag(String mergePayFlag) {
		this.mergePayFlag = mergePayFlag;
	}

	@Column(name = "moneyType")
	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "record_id")
	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	@Column(name = "payee")
	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	@Column(name = "orderType")
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name = "companyUuid")
	public String getCompanyUuid() {
		return this.companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	@Column(name="batch_number")
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
}
