/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;
import com.trekiz.admin.modules.hotel.input.SysGuestTypeInput;
import com.trekiz.admin.modules.hotel.query.SysGuestTypeQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface SysGuestTypeService{
	
	public void save (SysGuestType sysGuestType);
	
	public void save (SysGuestTypeInput sysGuestTypeInput);
	
	public void update (SysGuestType sysGuestType);
	
	public SysGuestType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysGuestType> find(Page<SysGuestType> page, SysGuestTypeQuery sysGuestTypeQuery);
	
	public List<SysGuestType> find( SysGuestTypeQuery sysGuestTypeQuery);
	
	public SysGuestType getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 查询批发商下所有的住客类型集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<SysGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，其余属性来源于sys_guest_type表
	 * 			
	 */
	public List<SysGuestType> findAllListByCompanyIdAndHotelUuid(int wholesalerId ,String hotelUuid);
	
}
