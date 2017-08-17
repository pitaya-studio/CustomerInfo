/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.region.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.region.entity.SysGeoRegionRel;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysGeoRegionRelService{
	
	public void save (SysGeoRegionRel sysGeoRegionRel);
	
	public void update (SysGeoRegionRel sysGeoRegionRel);
	
	public SysGeoRegionRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysGeoRegionRel> find(Page<SysGeoRegionRel> page, SysGeoRegionRel sysGeoRegionRel);
	
	public List<SysGeoRegionRel> find( SysGeoRegionRel sysGeoRegionRel);

	public void delRel(int id);
}
