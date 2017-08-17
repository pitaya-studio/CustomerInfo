/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResult;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteResultInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteResultQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteResultService{
	
	public void save (HotelQuoteResult hotelQuoteResult);
	
	public void save (HotelQuoteResultInput hotelQuoteResultInput);
	
	public void update (HotelQuoteResult hotelQuoteResult);
	
	public HotelQuoteResult getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuoteResult> find(Page<HotelQuoteResult> page, HotelQuoteResultQuery hotelQuoteResultQuery);
	
	public List<HotelQuoteResult> find( HotelQuoteResultQuery hotelQuoteResultQuery);
	
	public HotelQuoteResult getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public List<HotelQuoteResult> findByHotelQuoteUuid(String uuid);
	
	public List<HotelQuoteResult> findByHotelQuoteConditionUuid(String uuid,String hotelUuid);
	
	public List<Map<String,Object>> getByQuoteUuid(String quoteUuid);

	public List<HotelQuoteResult> findByHotelQuoteConditionUuid(String uuid);
	
	/**
	 * 查询入住日期的最大最小值
	 * @param uuid  hotel_quote_condition.uuid
	 * @return
	 */
	public Object getDates(String uuid);
}
