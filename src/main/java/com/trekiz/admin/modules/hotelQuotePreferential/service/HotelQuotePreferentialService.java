/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferential;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialService{
	
	public void save (HotelQuotePreferential hotelQuotePreferential);
	
	public void save (HotelQuotePreferentialInput hotelQuotePreferentialInput);
	
	public void update (HotelQuotePreferential hotelQuotePreferential);
	
	public HotelQuotePreferential getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferential> find(Page<HotelQuotePreferential> page, HotelQuotePreferentialQuery hotelQuotePreferentialQuery);
	
	public List<HotelQuotePreferential> find( HotelQuotePreferentialQuery hotelQuotePreferentialQuery);
	
	public HotelQuotePreferential getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public HotelPlPreferential getWholePlPreferentialByUuid(String uuid);
	
	/**
	 * 查询出的报价的数据转换成价单类的结构返回，前台以便展示
	 * @param hotelQuoteUuid
	 * @return
	 */
	public List<HotelPlPreferential> findQuotePreferentialsByHotelQuoteUuid (String hotelQuoteUuid);
}
