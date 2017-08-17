/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetailPrice;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteResultDetailPriceInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteResultDetailPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteResultDetailPriceService{
	
	public void save (HotelQuoteResultDetailPrice hotelQuoteResultDetailPrice);
	
	public void save (HotelQuoteResultDetailPriceInput hotelQuoteResultDetailPriceInput);
	
	public void update (HotelQuoteResultDetailPrice hotelQuoteResultDetailPrice);
	
	public HotelQuoteResultDetailPrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuoteResultDetailPrice> find(Page<HotelQuoteResultDetailPrice> page, HotelQuoteResultDetailPriceQuery hotelQuoteResultDetailPriceQuery);
	
	public List<HotelQuoteResultDetailPrice> find( HotelQuoteResultDetailPriceQuery hotelQuoteResultDetailPriceQuery);
	
	public HotelQuoteResultDetailPrice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
