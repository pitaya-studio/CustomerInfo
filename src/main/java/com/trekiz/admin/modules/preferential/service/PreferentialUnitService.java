/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.preferential.entity.PreferentialUnit;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface PreferentialUnitService{
	
	public void save (PreferentialUnit preferentialUnit);
	
	public void update (PreferentialUnit preferentialUnit);
	
	public PreferentialUnit getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PreferentialUnit> find(Page<PreferentialUnit> page, PreferentialUnit preferentialUnit);
	
	public List<PreferentialUnit> find( PreferentialUnit preferentialUnit);
	
	public PreferentialUnit getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
