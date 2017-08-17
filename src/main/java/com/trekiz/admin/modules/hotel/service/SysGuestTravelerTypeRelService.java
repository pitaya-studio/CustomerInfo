/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.SysGuestTravelerTypeRel;
import com.trekiz.admin.modules.hotel.input.SysGuestTravelerTypeRelInput;
import com.trekiz.admin.modules.hotel.query.SysGuestTravelerTypeRelQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface SysGuestTravelerTypeRelService{
	
	public void save (SysGuestTravelerTypeRel sysGuestTravelerTypeRel);
	
	public void save (SysGuestTravelerTypeRelInput sysGuestTravelerTypeRelInput);
	
	public void update (SysGuestTravelerTypeRel sysGuestTravelerTypeRel);
	
	public SysGuestTravelerTypeRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysGuestTravelerTypeRel> find(Page<SysGuestTravelerTypeRel> page, SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery);
	
	public List<SysGuestTravelerTypeRel> find( SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery);
	
	public SysGuestTravelerTypeRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public void removeBySysGuestTypeUuid(String sysGuestTypeUuid);
}
