/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.IslandOrderControlDetail;
import com.trekiz.admin.modules.island.input.IslandOrderControlDetailInput;
import com.trekiz.admin.modules.island.query.IslandOrderControlDetailQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface IslandOrderControlDetailService {
	
	public void save (IslandOrderControlDetail islandOrderControlDetail);
	
	public void save (IslandOrderControlDetailInput islandOrderControlDetailInput);
	
	public void update (IslandOrderControlDetail islandOrderControlDetail);
	
	public IslandOrderControlDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<IslandOrderControlDetail> find(Page<IslandOrderControlDetail> page, IslandOrderControlDetailQuery islandOrderControlDetailQuery);
	
	public List<IslandOrderControlDetail> find(IslandOrderControlDetailQuery islandOrderControlDetailQuery);
	
	public IslandOrderControlDetail getByUuid(String uuid);
	
	public IslandOrderControlDetail getByOrderUuid(String orderUuid);
	
	public void removeByUuid(String uuid);
}
