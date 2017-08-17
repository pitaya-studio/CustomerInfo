/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelGuestTypeDao  extends BaseDao<HotelGuestType> {
	
	public HotelGuestType getByUuid(String uuid);
	
	/**
	 * 获取批发商下所有的住客类型
	*<p>Title: findByWholesalerId</p>
	* @return List<HotelGuestType> 返回类型
	* @author majiancheng
	* @date 2015-7-3 下午6:20:55
	* @throws
	 */
	public List<HotelGuestType> findByWholesalerId(Integer wholesalerId);
	
	/**
	 * 查询批发商下 第N人类型 的游客类型简写和批发商住客类型UUID的集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<HotelGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，简写来源系统游客类型的简写
	 * 			
	 */
	public List<HotelGuestType> findShortNameAndGuestTypeUuidList(Integer wholesalerId,List<String> shortNameParams);
	
	/**
	 * 根据批发商id获取所有的酒店住客类型
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<HotelGuestType>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午12:04:20
	 */
	public List<HotelGuestType> findAllByWholesalerId(Integer wholesalerId);
	
}
