/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.IslandTravelerPapersType;
import com.trekiz.admin.modules.island.input.IslandTravelerPapersTypeInput;
import com.trekiz.admin.modules.island.query.IslandTravelerPapersTypeQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface IslandTravelerPapersTypeService{
	
	public void save (IslandTravelerPapersType islandTravelerPapersType);
	
	public void save (IslandTravelerPapersTypeInput islandTravelerPapersTypeInput);
	
	public void update (IslandTravelerPapersType islandTravelerPapersType);
	
	public IslandTravelerPapersType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<IslandTravelerPapersType> find(Page<IslandTravelerPapersType> page, IslandTravelerPapersTypeQuery islandTravelerPapersTypeQuery);
	
	public List<IslandTravelerPapersType> find( IslandTravelerPapersTypeQuery islandTravelerPapersTypeQuery);
	
	public IslandTravelerPapersType getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
