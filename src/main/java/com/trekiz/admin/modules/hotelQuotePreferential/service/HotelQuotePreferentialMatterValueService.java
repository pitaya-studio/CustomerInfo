/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatterValue;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialMatterValueInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialMatterValueQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialMatterValueService{
	
	public void save (HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue);
	
	public void save (HotelQuotePreferentialMatterValueInput hotelQuotePreferentialMatterValueInput);
	
	public void update (HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue);
	
	public HotelQuotePreferentialMatterValue getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferentialMatterValue> find(Page<HotelQuotePreferentialMatterValue> page, HotelQuotePreferentialMatterValueQuery hotelQuotePreferentialMatterValueQuery);
	
	public List<HotelQuotePreferentialMatterValue> find( HotelQuotePreferentialMatterValueQuery hotelQuotePreferentialMatterValueQuery);
	
	public HotelQuotePreferentialMatterValue getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
