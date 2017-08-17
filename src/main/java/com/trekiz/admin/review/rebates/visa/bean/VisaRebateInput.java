package com.trekiz.admin.review.rebates.visa.bean;

public class VisaRebateInput {
	
	
	private String visaOrderId;//订单id
	
	//游客借款信息
	private String[] trvids;//游客ID
	private String[] trvnames;//游客姓名
	private String[] refundCurrency;//游客币种
	private String[] trvborrownames;//游客款项名称
	private String[] lendPrice;//借款金额  trvamounts
	private String[] trvsettlementprices;//游客结算价
	private String[] trvborrownotes;//游客借款备注
	
	//团队借款信息
	private String[] groupborrownames;//团队款项名称
	private String[] teamCurrency;//团队款项币种  groupborrowcurrents
	private String[] teamMoney;//团队款项借款额  groupborrowamounts
	private String[] groupborrownodes;//团队借款备注
	
	private String  otherRemarks;//总团队借款备注
	
	private String totalRebatesJe;//累计返佣金额
	
	//累计返佣金额 审批通过后会更新此字段，每个申请汇总之前的返佣金额 update by zhanghao 20151230 修复bug11479
	private String totalRebatesJe4update;//累计返佣金额
	//累计返佣金额 审批通过后会更新此字段，每个申请汇总之前的返佣金额 update by zhanghao 20151230 修复bug11479

	private String supplyInfo;//返佣供应商表(rebate_supplier)的ID
	private String supplyName;//返佣供应商表(rebate_supplier)的name
	private String accountNo;//plat_bank_info表id
	
	public String getVisaOrderId() {
		return visaOrderId;
	}

	public void setVisaOrderId(String visaOrderId) {
		this.visaOrderId = visaOrderId;
	}

	public String[] getTrvids() {
		return trvids;
	}

	public void setTrvids(String[] trvids) {
		this.trvids = trvids;
	}

	public String[] getTrvnames() {
		return trvnames;
	}

	public void setTrvnames(String[] trvnames) {
		this.trvnames = trvnames;
	}

	public String[] getRefundCurrency() {
		return refundCurrency;
	}

	public void setRefundCurrency(String[] refundCurrency) {
		this.refundCurrency = refundCurrency;
	}

	public String[] getTrvborrownames() {
		return trvborrownames;
	}

	public void setTrvborrownames(String[] trvborrownames) {
		this.trvborrownames = trvborrownames;
	}

	public String[] getLendPrice() {
		return lendPrice;
	}

	public void setLendPrice(String[] lendPrice) {
		this.lendPrice = lendPrice;
	}

	public String[] getTrvsettlementprices() {
		return trvsettlementprices;
	}

	public void setTrvsettlementprices(String[] trvsettlementprices) {
		this.trvsettlementprices = trvsettlementprices;
	}

	public String[] getTrvborrownotes() {
		return trvborrownotes;
	}

	public void setTrvborrownotes(String[] trvborrownotes) {
		this.trvborrownotes = trvborrownotes;
	}

	public String[] getGroupborrownames() {
		return groupborrownames;
	}

	public void setGroupborrownames(String[] groupborrownames) {
		this.groupborrownames = groupborrownames;
	}

	public String[] getTeamCurrency() {
		return teamCurrency;
	}

	public void setTeamCurrency(String[] teamCurrency) {
		this.teamCurrency = teamCurrency;
	}

	public String[] getTeamMoney() {
		return teamMoney;
	}

	public void setTeamMoney(String[] teamMoney) {
		this.teamMoney = teamMoney;
	}

	public String[] getGroupborrownodes() {
		return groupborrownodes;
	}

	public void setGroupborrownodes(String[] groupborrownodes) {
		this.groupborrownodes = groupborrownodes;
	}

	public String getOtherRemarks() {
		return otherRemarks;
	}

	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}

	public String getTotalRebatesJe() {
		return totalRebatesJe;
	}

	public void setTotalRebatesJe(String totalRebatesJe) {
		this.totalRebatesJe = totalRebatesJe;
	}

	public String getTotalRebatesJe4update() {
		return totalRebatesJe4update;
	}

	public void setTotalRebatesJe4update(String totalRebatesJe4update) {
		this.totalRebatesJe4update = totalRebatesJe4update;
	}

	public String getSupplyInfo() {
		return supplyInfo;
	}

	public void setSupplyInfo(String supplyInfo) {
		this.supplyInfo = supplyInfo;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
}
