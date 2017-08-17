/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelControlRuleService{
	
	public void save (HotelControlRule hotelControlRule);
	
	public void update (HotelControlRule hotelControlRule);
	
	public HotelControlRule getById(java.lang.Integer value);
	
	public Page<HotelControlRule> find(Page<HotelControlRule> page, HotelControlRule hotelControlRule);
	
	public List<HotelControlRule> find( HotelControlRule hotelControlRule);
	
	public HotelControlRule getByUuid(String uuid);
}
