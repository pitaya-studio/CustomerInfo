/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.SysTravelerType;
import com.trekiz.admin.modules.hotel.input.SysTravelerTypeInput;
import com.trekiz.admin.modules.hotel.query.SysTravelerTypeQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface SysTravelerTypeService{
	
	public void save (SysTravelerType sysTravelerType);
	
	public void save (SysTravelerTypeInput sysTravelerTypeInput);
	
	public void update (SysTravelerType sysTravelerType);
	
	public SysTravelerType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysTravelerType> find(Page<SysTravelerType> page, SysTravelerTypeQuery sysTravelerTypeQuery);
	
	public List<SysTravelerType> find( SysTravelerTypeQuery sysTravelerTypeQuery);
	
	public SysTravelerType getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean findIsNameExist(String uuid, String travelerTypeName);
	
	public boolean findIsShortNameExist(String uuid, String travelerTypeShortName);

	public List<String> findExistTravelerUuids(String sysGuestTypeUuid, String value,String type);
}
