/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionPreferentialRel;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionPreferentialRelInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionPreferentialRelQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteConditionPreferentialRelService{
	
	public void save (HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel);
	
	public void save (HotelQuoteConditionPreferentialRelInput hotelQuoteConditionPreferentialRelInput);
	
	public void update (HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel);
	
	public HotelQuoteConditionPreferentialRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuoteConditionPreferentialRel> find(Page<HotelQuoteConditionPreferentialRel> page, HotelQuoteConditionPreferentialRelQuery hotelQuoteConditionPreferentialRelQuery);
	
	public List<HotelQuoteConditionPreferentialRel> find( HotelQuoteConditionPreferentialRelQuery hotelQuoteConditionPreferentialRelQuery);
	
	public HotelQuoteConditionPreferentialRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
