/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialTax;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialTaxInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialTaxQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialTaxService{
	
	public void save (HotelQuotePreferentialTax hotelQuotePreferentialTax);
	
	public void save (HotelQuotePreferentialTaxInput hotelQuotePreferentialTaxInput);
	
	public void update (HotelQuotePreferentialTax hotelQuotePreferentialTax);
	
	public HotelQuotePreferentialTax getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferentialTax> find(Page<HotelQuotePreferentialTax> page, HotelQuotePreferentialTaxQuery hotelQuotePreferentialTaxQuery);
	
	public List<HotelQuotePreferentialTax> find( HotelQuotePreferentialTaxQuery hotelQuotePreferentialTaxQuery);
	
	public HotelQuotePreferentialTax getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
