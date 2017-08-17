/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatter;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialMatterInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialMatterQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialMatterService{
	
	public void save (HotelQuotePreferentialMatter hotelQuotePreferentialMatter);
	
	public void save (HotelQuotePreferentialMatterInput hotelQuotePreferentialMatterInput);
	
	public void update (HotelQuotePreferentialMatter hotelQuotePreferentialMatter);
	
	public HotelQuotePreferentialMatter getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferentialMatter> find(Page<HotelQuotePreferentialMatter> page, HotelQuotePreferentialMatterQuery hotelQuotePreferentialMatterQuery);
	
	public List<HotelQuotePreferentialMatter> find( HotelQuotePreferentialMatterQuery hotelQuotePreferentialMatterQuery);
	
	public HotelQuotePreferentialMatter getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
