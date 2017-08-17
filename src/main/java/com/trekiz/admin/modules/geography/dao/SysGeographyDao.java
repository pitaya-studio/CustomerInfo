/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.geography.dao;
import java.util.List;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.geography.entity.SysGeography;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface SysGeographyDao  extends BaseDao<SysGeography> {
	
	public void save (SysGeography sysGeography);
	
	public void update (SysGeography sysGeography);
	
	public SysGeography getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public void updateGeographys(List<SysGeography> sysGeographys);
	
	public SysGeography getByUuid(String uuid);
}
