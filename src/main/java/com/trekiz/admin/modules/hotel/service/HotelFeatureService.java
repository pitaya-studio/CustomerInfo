/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelFeature;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelFeatureService{
	
	public void save (HotelFeature hotelFeature);
	
	public void save (HotelFeature hotelFeature,List<HotelAnnex> list);
	
	public void update (HotelFeature hotelFeature);
	
	public void update (HotelFeature hotelFeature,List<HotelAnnex> list);
	
	public HotelFeature getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelFeature> find(Page<HotelFeature> page, HotelFeature hotelFeature);
	
	public List<HotelFeature> find( HotelFeature hotelFeature);
	
	public HotelFeature getByUuid(String value);
	
	public void removeByUuid(java.lang.String value);
}
