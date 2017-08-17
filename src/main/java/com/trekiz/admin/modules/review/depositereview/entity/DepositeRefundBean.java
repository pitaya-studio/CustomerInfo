package com.trekiz.admin.modules.review.depositereview.entity;
/**
 * 定义退签证押金的审核详情信息字段 key
 * @author chy
 *@version 1.0 2014年12月29日09:51:14
 */
public class DepositeRefundBean {

	/*游客id*/
	public final static String DEPOSITE_TRAVELER_ID = "travelerId";
	
	/*游客姓名*/
	public final static String DEPOSITE_TRAVELER_NAME = "travelerName";
	
	/*押金币种*/
	public final static String DEPOSITE_CURRENCY = "depositPriceCurrency";
	
	/*押金金额*/
	public final static String DEPOSITE_PRICE = "depositPrice";
	
	/*达账金额*/
	public final static String DEPOSITE_PAY_PRICE = "payPrice";
	
	/*申请押金金额*/
	public final static String DEPOSITE_REFUND_PRICE = "refundPrice";
	
	/*申请原因*/
	public final static String DEPOSITE_REFUND_REMARK = "remark";
}
