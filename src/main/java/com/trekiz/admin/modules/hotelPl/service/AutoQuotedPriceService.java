/*
\ * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.Map;

import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceResultJsonBean;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AutoQuotedPriceService{
	/**
	 * 验证是否超出选择房型的容住率，add by zhanghao
	 * @param query 报价条件
	 */
	public Map<String,String> checkRoomCapacity(QuotedPriceQuery query);
	
	/**
	 * 自动报价计算 add by zhanghao
	 * @param query
	 * @return
	 */
	public QuotedPriceResultJsonBean autoQuotedPrice(QuotedPriceQuery query);
}
