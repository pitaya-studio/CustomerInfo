/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequireNotAppDate;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlPreferentialRequireNotAppDateDao  extends BaseDao<HotelPlPreferentialRequireNotAppDate> {
	
	public HotelPlPreferentialRequireNotAppDate getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据价单优惠信息集合删除不适用日期集合
	 * @Description: 
	 * @param @param plPreferentialUuids
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-12 上午11:12:54
	 */
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids);
	
}
