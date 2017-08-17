package com.trekiz.admin.modules.finance.param;

import java.io.Serializable;

/**
 * 财务筛选参数集合对象
 * @author wangyang
 * @date 2016.8.23
 * */
public class ParamEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 团号*/
	private String groupCode;
	/** 订单编号*/
	private String orderNum;
	/** 计调*/
	private String jd;
	/** 销售*/
	private String saler;
	/** 团队类型*/
	private String orderS;
	
	
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getJd() {
		return jd;
	}
	public void setJd(String jd) {
		this.jd = jd;
	}
	public String getSaler() {
		return saler;
	}
	public void setSaler(String saler) {
		this.saler = saler;
	}
	public String getOrderS() {
		return orderS;
	}
	public void setOrderS(String orderS) {
		this.orderS = orderS;
	}
	
}
