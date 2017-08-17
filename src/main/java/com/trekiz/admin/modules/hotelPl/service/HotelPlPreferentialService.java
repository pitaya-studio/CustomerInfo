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

public interface HotelPlPreferentialService{
	
	public void save (HotelPlPreferential hotelPlPreferential);
	
	public void save (HotelPlPreferentialInput hotelPlPreferentialInput);
	
	public void update (HotelPlPreferential hotelPlPreferential);
	
	public HotelPlPreferential getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferential> find(Page<HotelPlPreferential> page, HotelPlPreferentialQuery hotelPlPreferentialQuery);
	
	public List<HotelPlPreferential> find( HotelPlPreferentialQuery hotelPlPreferentialQuery);
	
	public HotelPlPreferential getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取酒店价单优惠信息集合
	*<p>Title: findPlPreferentialsByHotelPlUuid</p>
	* @return List<HotelPlPreferential> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午4:02:37
	* @throws
	 */
	public List<HotelPlPreferential> findPlPreferentialsByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据价单优惠信息设置关联的子表信息 add by zhanghao
	 * @param hotelPlPreferentials
	 */
	public void setPlPreferentialsParam(List<HotelPlPreferential> hotelPlPreferentials);
	/**
	 * 自动报价 根据条件筛选 符合条件的优惠信息 add by zhanghao
	 * 只筛选符合日期的优惠，具体是否适用会进一步做筛选
	 * @return
	 */
	public List<HotelPlPreferential> getHotelPlPreferentials4AutoQuotedPrice( HotelPlPreferentialQuery hotelPlPreferentialQuery);
	
	/**
	 * 根据优惠uuid获取优惠所有信息（包括子表数据）
	*<p>Title: getWholePlPreferentialByUuid</p>
	* @return HotelPlPreferential 返回类型
	* @author majiancheng
	* @date 2015-7-17 下午5:46:51
	* @throws
	 */
	public HotelPlPreferential getWholePlPreferentialByUuid(String uuid);
	
	/**
	 * 根据价单uuid获取可关联的优惠信息
	*<p>Title: getRelPlPreferentialsByPlUuid</p>
	* @return List<HotelPlPreferential> 返回类型
	* @author majiancheng
	* @date 2015-7-23 上午11:59:18
	* @throws
	 */
	public List<HotelPlPreferential> getRelPlPreferentialsByPlUuid(String hotelPlUuid);
}
