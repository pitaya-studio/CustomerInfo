/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service;

import java.util.List;

import com.trekiz.admin.modules.mtourCommon.entity.SerialNumber;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface SerialNumberService{
	/** 借款、退款、追加成本付款表 */
	public static final String TABLENAME_AIRTICKET_ORDER_MONEYAMOUNT = "airticket_order_moneyAmount";
	/** 成本记录表 */
	public static final String TABLENAME_COST_RECORD = "cost_record";
	/** 订单收款记录表 */
	public static final String TABLENAME_ORDERPAY = "orderpay";
	/** 其他收入收款表 */
	public static final String TABLENAME_PAY_GROUP = "pay_group";
	
	/**
	 * 获取流水号
	 * @param tableName
	 * @param recordId
	 * @return
	 */
	public String genSerialNumber(String tableName, Integer recordId);
	
	/**
	 * 获取最大code值
	 * @param tableName
	 * @return
	 */
	public String getMaxCodeByTableName(String tableName);
	
	/**
	 * 获取流水号对象
	 * @param tableName
	 * @param recordId
	 * @return
	 */
	public SerialNumber getSerialNumber(String tableName, Integer recordId);
	
	public String obtainSerialNumber(List<String[]> payInfos);
}
