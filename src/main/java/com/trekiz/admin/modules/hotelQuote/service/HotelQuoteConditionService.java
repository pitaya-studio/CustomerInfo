/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteCondition;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteConditionService{
	
	public void save (HotelQuoteCondition hotelQuoteCondition);
	
	public void save (HotelQuoteConditionInput hotelQuoteConditionInput);
	
	public void update (HotelQuoteCondition hotelQuoteCondition);
	
	public HotelQuoteCondition getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuoteCondition> find(Page<HotelQuoteCondition> page, HotelQuoteConditionQuery hotelQuoteConditionQuery);
	
	public List<HotelQuoteCondition> find( HotelQuoteConditionQuery hotelQuoteConditionQuery);
	
	public HotelQuoteCondition getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public HotelQuoteCondition findByHotelQuoteUuid(String uuid);

	public List<HotelQuoteCondition> findByQuoteUuid(String uuid);
	
	public List<HotelQuoteCondition> findByQuoteConditionUuid(String uuid);
}
