/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;
import com.trekiz.admin.modules.hotelPl.entity.*;
import com.trekiz.admin.modules.hotelPl.input.*;
import com.trekiz.admin.modules.hotelPl.query.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlPreferentialRelService{
	
	public void save (HotelPlPreferentialRel hotelPlPreferentialRel);
	
	public void save (HotelPlPreferentialRelInput hotelPlPreferentialRelInput);
	
	public void update (HotelPlPreferentialRel hotelPlPreferentialRel);
	
	public HotelPlPreferentialRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialRel> find(Page<HotelPlPreferentialRel> page, HotelPlPreferentialRelQuery hotelPlPreferentialRelQuery);
	
	public List<HotelPlPreferentialRel> find( HotelPlPreferentialRelQuery hotelPlPreferentialRelQuery);
	
	public HotelPlPreferentialRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取所有的优惠关联信息
	*<p>Title: getPreferentialRelsByPreferentialUuid</p>
	* @return List<HotelPlPreferentialRel> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:45:51
	* @throws
	 */
	public List<HotelPlPreferentialRel> getPreferentialRelsByPreferentialUuid(String preferentialUuid);
}
