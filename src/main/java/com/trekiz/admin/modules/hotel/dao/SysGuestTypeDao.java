/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface SysGuestTypeDao  extends BaseDao<SysGuestType> {
	
	public SysGuestType getByUuid(String uuid);
	
	/**
	 * 查询批发商下所有的住客类型集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<SysGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，其余属性来源于sys_guest_type表
	 * 			
	 */
	public List<SysGuestType> findAllListByCompanyIdAndHotelUuid(int wholesalerId ,String hotelUuid);
}
