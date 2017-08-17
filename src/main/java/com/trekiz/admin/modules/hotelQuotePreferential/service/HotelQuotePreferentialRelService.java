/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRel;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRelInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRelQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialRelService{
	
	public void save (HotelQuotePreferentialRel hotelQuotePreferentialRel);
	
	public void save (HotelQuotePreferentialRelInput hotelQuotePreferentialRelInput);
	
	public void update (HotelQuotePreferentialRel hotelQuotePreferentialRel);
	
	public HotelQuotePreferentialRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferentialRel> find(Page<HotelQuotePreferentialRel> page, HotelQuotePreferentialRelQuery hotelQuotePreferentialRelQuery);
	
	public List<HotelQuotePreferentialRel> find( HotelQuotePreferentialRelQuery hotelQuotePreferentialRelQuery);
	
	public HotelQuotePreferentialRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
