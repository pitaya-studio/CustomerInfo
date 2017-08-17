/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.region.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.region.entity.SysRegion;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysRegionService{
	
	public void save (SysRegion sysRegion);
	
	public void update (SysRegion sysRegion);
	
	public SysRegion getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysRegion> find(Page<SysRegion> page, SysRegion sysRegion);
	
	public List<SysRegion> find( SysRegion sysRegion);
	
	public void addSysRegionRel(SysRegion sysRegion,String cells)  throws ServiceException;

	public Page<Map<String,Object>> findRegionList(HttpServletRequest request,
			HttpServletResponse response, SysRegion sysRegion);

	public List<Map<String, Object>> showRegionDetail(int id);
	
}
