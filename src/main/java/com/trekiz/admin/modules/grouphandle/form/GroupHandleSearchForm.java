package com.trekiz.admin.modules.grouphandle.form;

/**  
 * @Title: GroupHandleSearchForm.java
 * @Package com.trekiz.admin.modules.grouphandle.form
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xinwei.wang  
 * @date 2016-2016年1月27日 上午10:33:48
 * @version V1.0  
 */
public class GroupHandleSearchForm {
	    
	    //查询列表条件     团期列表 或  游客列表  
	    private String showList;
	
	    //查询条件
	    private String commonCode; //团号  订单编号
	    private String salerId;  //销售
	    private String agentinfoId;  //渠道
	    private String activityProductKind;  //团队类型
	    
	    private String travelerName; //游客 姓名
	    private String visaCountryId; //国家
	    private String visaTypeId; //签证类型  （sys_dict 表的  value 值）
	    
	    private String  aboutSigningTimeStart; //预计签约时间
	    private String  aboutSigningTimeEnd; //预计签约时间	    
	    private String  signingTimeStart; //实际签约时间
	    private String  signingTimeEnd; //实际签约时间
	    private String  visaStauts; //签证状态
	    
	    //排序条件
	    private String  orderBy;

		public String getShowList() {
			return showList;
		}

		public void setShowList(String showList) {
			this.showList = showList;
		}

		public String getCommonCode() {
			return commonCode;
		}

		public void setCommonCode(String commonCode) {
			this.commonCode = commonCode;
		}

		public String getSalerId() {
			return salerId;
		}

		public void setSalerId(String salerId) {
			this.salerId = salerId;
		}

		public String getAgentinfoId() {
			return agentinfoId;
		}

		public void setAgentinfoId(String agentinfoId) {
			this.agentinfoId = agentinfoId;
		}

		public String getActivityProductKind() {
			return activityProductKind;
		}

		public void setActivityProductKind(String activityProductKind) {
			this.activityProductKind = activityProductKind;
		}

		public String getTravelerName() {
			return travelerName;
		}

		public void setTravelerName(String travelerName) {
			this.travelerName = travelerName;
		}

		public String getVisaCountryId() {
			return visaCountryId;
		}

		public void setVisaCountryId(String visaCountryId) {
			this.visaCountryId = visaCountryId;
		}

		public String getVisaTypeId() {
			return visaTypeId;
		}

		public void setVisaTypeId(String visaTypeId) {
			this.visaTypeId = visaTypeId;
		}

		public String getAboutSigningTimeStart() {
			return aboutSigningTimeStart;
		}

		public void setAboutSigningTimeStart(String aboutSigningTimeStart) {
			this.aboutSigningTimeStart = aboutSigningTimeStart;
		}

		public String getAboutSigningTimeEnd() {
			return aboutSigningTimeEnd;
		}

		public void setAboutSigningTimeEnd(String aboutSigningTimeEnd) {
			this.aboutSigningTimeEnd = aboutSigningTimeEnd;
		}

		public String getSigningTimeStart() {
			return signingTimeStart;
		}

		public void setSigningTimeStart(String signingTimeStart) {
			this.signingTimeStart = signingTimeStart;
		}

		public String getSigningTimeEnd() {
			return signingTimeEnd;
		}

		public void setSigningTimeEnd(String signingTimeEnd) {
			this.signingTimeEnd = signingTimeEnd;
		}

		public String getVisaStauts() {
			return visaStauts;
		}

		public void setVisaStauts(String visaStauts) {
			this.visaStauts = visaStauts;
		}

		public String getOrderBy() {
			return orderBy;
		}

		public void setOrderBy(String orderBy) {
			this.orderBy = orderBy;
		}
	    
}
