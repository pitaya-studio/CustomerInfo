/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionDetail;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionDetailInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionDetailQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteConditionDetailService{
	
	public void save (HotelQuoteConditionDetail hotelQuoteConditionDetail);
	
	public void save (HotelQuoteConditionDetailInput hotelQuoteConditionDetailInput);
	
	public void update (HotelQuoteConditionDetail hotelQuoteConditionDetail);
	
	public HotelQuoteConditionDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuoteConditionDetail> find(Page<HotelQuoteConditionDetail> page, HotelQuoteConditionDetailQuery hotelQuoteConditionDetailQuery);
	
	public List<HotelQuoteConditionDetail> find( HotelQuoteConditionDetailQuery hotelQuoteConditionDetailQuery);
	
	public HotelQuoteConditionDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
