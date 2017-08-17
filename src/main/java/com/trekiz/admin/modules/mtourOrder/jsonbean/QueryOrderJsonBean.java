package com.trekiz.admin.modules.mtourOrder.jsonbean;

import java.util.List;
import java.util.Map;
/**
 * 订单列表支出单封装实体
 * @author zhangchao
 * @time 2016/1/29
 */
public class QueryOrderJsonBean {
	private String groupNo;//团号
	private Integer applicantId;//申请人id ---当前用户
	private String applicant;//申请人姓名
	private List<Map<String,Object>> paymentObject;//支付对象[{paymentObjectCode:支付对象code,paymentObjectName:支付对象Name}]
	private Integer fundsType;//款项类型
	private Integer tourOperatorChannelCategoryCode;//1 地接社，2渠道商
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public Integer getFundsType() {
		return fundsType;
	}
	public void setFundsType(Integer fundsType) {
		this.fundsType = fundsType;
	}
	public Integer getTourOperatorChannelCategoryCode() {
		return tourOperatorChannelCategoryCode;
	}
	public void setTourOperatorChannelCategoryCode(
			Integer tourOperatorChannelCategoryCode) {
		this.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public Integer getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(Integer applicantId) {
		this.applicantId = applicantId;
	}
	public List<Map<String, Object>> getPaymentObject() {
		return paymentObject;
	}
	public void setPaymentObject(List<Map<String, Object>> paymentObject) {
		this.paymentObject = paymentObject;
	}
	
}
