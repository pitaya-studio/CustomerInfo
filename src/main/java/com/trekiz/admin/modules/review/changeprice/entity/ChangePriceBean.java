package com.trekiz.admin.modules.review.changeprice.entity;

/**
 * 改价bean review_detail表中的key定义 存储改价相关业务数据 
 * 只做展示用
 * @author chy 
 * @date 2014年12月18日17:40:09
 */
public class ChangePriceBean {

	/*游客/定金/团队 id ，如果是游客改价 travelerid存储游客的id 如为订金改价存储-1 团队改价 存储 0 团队签证费改价 存储 -2*/
	public final static String TRVALER_ID = "travelerid" ;
	
	/*游客/定金/团队  名称*/
	public final static String TRAVALER_NAME = "travelername";
	
	/*币种ID*/
	public final static String CURRENCY_ID = "currencyid";
	
	/*币种名称*/
	public final static String CURRENCY_NAME = "currencyname";
	
	/*原始应收价*/
	public final static String OLD_TOTAL_MONEY = "oldtotalmoney";
	
	/*当前应收价*/
	public final static String CUR_TOTAL_MONEY = "curtotalmoney";
	
	/*改后应收价*/
	public final static String CHANGED_TOTAL_MONEY = "changedtotalmoney";
	
	/*改价差额*/
	public final static String CHANGED_PRICE = "changedprice";
	
	/*改价款项*/
	public final static String CHANGED_FUND = "changedfund";
	
	/*改价备注*/
	public final static String CHANGEDREMARK = "remark";
	
	
}
