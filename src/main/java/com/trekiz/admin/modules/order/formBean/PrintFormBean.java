package com.trekiz.admin.modules.order.formBean;

import java.util.Date;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.entity.Currency;

/**
 * 
 * 打印单公用实体类
 * 打印单显示打印和下载需要的属性需要在该类中设置
 * add by zhanghao 20150507
 */
public class PrintFormBean {
	
	/**
	 * 审核表ID
	 */
	private Long reviewId;
	
	/**
	 * 审核表团号
	 */
	private String groupCode;
	
	/**
	 * 填写日期
	 */
	private Date revCreateDate;
	

	/**
	 * 申报原因
	 */
	private String revBorrowRemark = "";
	
	/**
	 * 申请人
	 */
	private String operatorName;
	
	/**
	 * 产品创建人
	 */
	private String productCreater;
	
	/**
	 * 付款日期
	 */
	private Date payDate;
	
	
	/**
	 * 金额（借款、退款等）
	 */
	private String moneyAmount;
	
	/**
	 * 大写金额（借款、退款等）
	 */
	private String moneyAmountCn;
	
	/**
	 * 币种信息
	 */
	private Currency revCurrency;
	
	/**
	 * 财务
	 */
	private String cw = "";
	
	/**
	 * 出纳
	 */
	private String cashier = "";
	
	/**
	 * 总经理
	 */
	private String majorCheckPerson = "";

	/**
	 * 部门经理
	 */
	private String deptmanager = "";
	
	/**
	 * 打印日期
	 */
	private Date printDate;
	
	/**
	 * 借款金额
	 */
	private String revBorrowAmount;
	
	/**
	 * 借款金额大写
	 */
	private String revBorrowAmountDx;
	
	/**
	 * 申请单名称
	 */
	private String printFormName;
	
	/**
	 * 财务主管
	 */
	private String cwmanager = "";
	
	/**
	 * 订单ID
	 */
	private Long orderId;
	
	/**
	 * 借款单位
	 */
	private String borrowDept;
	
	/**
	 * 确认付款日期字符串显示
	 */
	private String payDateStr;

	/**
	 * 确认付款状态
	 */
	private String payStatus;

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Long getReviewId() {
		return this.reviewId;
	}
	
	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public Date getRevCreateDate() {
		return revCreateDate;
	}

	public void setRevCreateDate(Date revCreateDate) {
		this.revCreateDate = revCreateDate;
	}

	public String getRevBorrowRemark() {
		return revBorrowRemark;
	}

	public void setRevBorrowRemark(String revBorrowRemark) {
		this.revBorrowRemark = revBorrowRemark;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getProductCreater() {
		return productCreater;
	}

	public void setProductCreater(String productCreater) {
		this.productCreater = productCreater;
	}

	public Date getPayDate() {
		return payDate;
	}
	public String getPayDateString() {
		return DateUtils.date2String(payDate);
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getMoneyAmount() {
		return moneyAmount;
	}

	public void setMoneyAmount(String moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public String getMoneyAmountCn() {
		if(moneyAmountCn==null){
			
		}
		return moneyAmountCn;
	}

	public void setMoneyAmountCn(String moneyAmountCn) {
		this.moneyAmountCn = moneyAmountCn;
	}

	public Currency getRevCurrency() {
		return revCurrency;
	}

	public void setRevCurrency(Currency revCurrency) {
		this.revCurrency = revCurrency;
	}

	public String getCw() {
		return cw;
	}

	public void setCw(String cw) {
		this.cw = cw;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getMajorCheckPerson() {
		return majorCheckPerson;
	}

	public void setMajorCheckPerson(String majorCheckPerson) {
		this.majorCheckPerson = majorCheckPerson;
	}

	public String getDeptmanager() {
		return deptmanager;
	}

	public void setDeptmanager(String deptmanager) {
		this.deptmanager = deptmanager;
	}

	public Date getPrintDate() {
		return printDate;
	}
	public String getPrintDateString() {
		return DateUtils.date2String(printDate);
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	
	public String getRevBorrowAmount() {
		return this.revBorrowAmount;
	}
	
	public void setRevBorrowAmount(String revBorrowAmount) {
		this.revBorrowAmount = revBorrowAmount;
	}
	
	public String getRevBorrowAmountDx() {
		return this.revBorrowAmountDx;
	}
	
	public void setRevBorrowAmountDx(String revBorrowAmountDx) {
		this.revBorrowAmountDx = revBorrowAmountDx;
	}
	
	public void setPrintFormName(String printFormName) {
		this.printFormName = printFormName;
	}
	
	public String getPrintFormName() {
		return this.printFormName;
	}
	
	public void setCwmanager(String cwmanager) {
		this.cwmanager = cwmanager;
	}
	
	public String getCwmanager() {
		return this.cwmanager;
	}
	
	public Long getOrderId() {
		return this.orderId;
	}
	
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public String getBorrowDept() {
		return borrowDept;
	}
	
	public void setBorrowDept(String borrowDept) {
		this.borrowDept = borrowDept;
	}

	public String getPayDateStr() {
		return this.payDateStr;
	}

	public void setPayDateStr(String payDateStr) {
		this.payDateStr = payDateStr;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
}
