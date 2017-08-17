/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.input.HotelPlPreferentialMatterValueInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialMatterValueQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlPreferentialMatterValueService{
	
	public void save (HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue);
	
	public void save (HotelPlPreferentialMatterValueInput hotelPlPreferentialMatterValueInput);
	
	public void update (HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue);
	
	public HotelPlPreferentialMatterValue getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialMatterValue> find(Page<HotelPlPreferentialMatterValue> page, HotelPlPreferentialMatterValueQuery hotelPlPreferentialMatterValueQuery);
	
	public List<HotelPlPreferentialMatterValue> find( HotelPlPreferentialMatterValueQuery hotelPlPreferentialMatterValueQuery);
	
	public HotelPlPreferentialMatterValue getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
