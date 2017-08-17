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

public interface HotelPlPreferentialMatterService{
	
	public void save (HotelPlPreferentialMatter hotelPlPreferentialMatter);
	
	public void save (HotelPlPreferentialMatterInput hotelPlPreferentialMatterInput);
	
	public void update (HotelPlPreferentialMatter hotelPlPreferentialMatter);
	
	public HotelPlPreferentialMatter getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialMatter> find(Page<HotelPlPreferentialMatter> page, HotelPlPreferentialMatterQuery hotelPlPreferentialMatterQuery);
	
	public List<HotelPlPreferentialMatter> find( HotelPlPreferentialMatterQuery hotelPlPreferentialMatterQuery);
	
	public HotelPlPreferentialMatter getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取优惠事项信息
	*<p>Title: findMatterByPreferentialUuid</p>
	* @return HotelPlPreferentialMatter 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午10:51:07
	* @throws
	 */
	public HotelPlPreferentialMatter findMatterByPreferentialUuid(String preferentialUuid);
}
