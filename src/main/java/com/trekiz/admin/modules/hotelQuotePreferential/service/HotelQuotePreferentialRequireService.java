/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRequire;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRequireInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRequireQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialRequireService{
	
	public void save (HotelQuotePreferentialRequire hotelQuotePreferentialRequire);
	
	public void save (HotelQuotePreferentialRequireInput hotelQuotePreferentialRequireInput);
	
	public void update (HotelQuotePreferentialRequire hotelQuotePreferentialRequire);
	
	public HotelQuotePreferentialRequire getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferentialRequire> find(Page<HotelQuotePreferentialRequire> page, HotelQuotePreferentialRequireQuery hotelQuotePreferentialRequireQuery);
	
	public List<HotelQuotePreferentialRequire> find( HotelQuotePreferentialRequireQuery hotelQuotePreferentialRequireQuery);
	
	public HotelQuotePreferentialRequire getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
