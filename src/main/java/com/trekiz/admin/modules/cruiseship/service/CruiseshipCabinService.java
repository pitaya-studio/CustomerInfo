/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipCabinInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipCabinQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipCabinService{
	
	public void save (CruiseshipCabin cruiseshipCabin);
	
	public void save (CruiseshipCabinInput cruiseshipCabinInput);
	
	public void update (CruiseshipCabin cruiseshipCabin);
	
	public CruiseshipCabin getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipCabin> find(Page<CruiseshipCabin> page, CruiseshipCabinQuery cruiseshipCabinQuery);
	
	public List<CruiseshipCabin> find( CruiseshipCabinQuery cruiseshipCabinQuery);
	
	public CruiseshipCabin getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
}
