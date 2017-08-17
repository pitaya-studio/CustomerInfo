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

public interface HotelFloorService{
	
	public void save (HotelFloor hotelFloor);
	
	public void update (HotelFloor hotelFloor);
	
	public HotelFloor getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelFloor> find(Page<HotelFloor> page, HotelFloor hotelFloor);
	
	public List<HotelFloor> find( HotelFloor hotelFloor);
	
	public HotelFloor getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean findFloorNameIsExist(HotelFloor hotelFloor);
}
