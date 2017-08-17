/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.input.HotelPlTaxExceptionInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxExceptionQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlTaxExceptionService{
	
	public void save (HotelPlTaxException hotelPlTaxException);
	
	public void save (HotelPlTaxExceptionInput hotelPlTaxExceptionInput);
	
	public void update (HotelPlTaxException hotelPlTaxException);
	
	public HotelPlTaxException getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlTaxException> find(Page<HotelPlTaxException> page, HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery);
	
	public List<HotelPlTaxException> find( HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery);
	
	public HotelPlTaxException getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取酒店价单税金例外集合
	*<p>Title: findTaxExceptionsByHotelPlUuids</p>
	* @return List<HotelPlTaxException> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:26:12
	* @throws
	 */
	public List<HotelPlTaxException> findTaxExceptionsByHotelPlUuids(String hotelPlUuid);
	
	/**
	 * 自动报价 根据条件筛选 符合条件的税金 add by zhanghao
	 * @return
	 */
	public List<HotelPlTaxException> getHotelPlTaxException4AutoQuotedPrice( HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery);
	
	
}
