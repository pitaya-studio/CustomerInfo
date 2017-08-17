/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetailPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelQuoteResultDetailPriceDao  extends BaseDao<HotelQuoteResultDetailPrice> {
	
	public HotelQuoteResultDetailPrice getByUuid(String uuid);
}