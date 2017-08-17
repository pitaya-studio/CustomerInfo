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

public interface HotelPlPreferentialTaxService{
	
	public void save (HotelPlPreferentialTax hotelPlPreferentialTax);
	
	public void save (HotelPlPreferentialTaxInput hotelPlPreferentialTaxInput);
	
	public void update (HotelPlPreferentialTax hotelPlPreferentialTax);
	
	public HotelPlPreferentialTax getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialTax> find(Page<HotelPlPreferentialTax> page, HotelPlPreferentialTaxQuery hotelPlPreferentialTaxQuery);
	
	public List<HotelPlPreferentialTax> find( HotelPlPreferentialTaxQuery hotelPlPreferentialTaxQuery);
	
	public HotelPlPreferentialTax getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取税金集合
	*<p>Title: getTaxsByPreferentialUuid</p>
	* @return List<HotelPlPreferentialTax> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:49:07
	* @throws
	 */
	public List<HotelPlPreferentialTax> getTaxsByPreferentialUuid(String preferentialUuid);
	
	/**
	 * 根据优惠信息uuid获取税金Map集合
	*<p>Title: getTaxsByPreferentialUuid</p>
	* @return List<HotelPlPreferentialTax> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:49:07
	* @throws
	 */
	public Map<String, List<HotelPlPreferentialTax>> getTaxMapByPreferentialUuid(String preferentialUuid);
}
