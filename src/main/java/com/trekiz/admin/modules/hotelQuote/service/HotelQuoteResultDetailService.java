/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetail;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteResultDetailInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteResultDetailQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteResultDetailService{
	
	public void save (HotelQuoteResultDetail hotelQuoteResultDetail);
	
	public void save (HotelQuoteResultDetailInput hotelQuoteResultDetailInput);
	
	public void update (HotelQuoteResultDetail hotelQuoteResultDetail);
	
	public HotelQuoteResultDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuoteResultDetail> find(Page<HotelQuoteResultDetail> page, HotelQuoteResultDetailQuery hotelQuoteResultDetailQuery);
	
	public List<HotelQuoteResultDetail> find( HotelQuoteResultDetailQuery hotelQuoteResultDetailQuery);
	
	public HotelQuoteResultDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public List<HotelQuoteResultDetail> findByHotelQuoteUuid(String uuid);

	public List<HotelQuoteResultDetail> findByHotelQuoteConditionUuid(
			String uuid);
}
