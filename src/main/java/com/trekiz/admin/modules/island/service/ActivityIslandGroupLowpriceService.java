/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupLowprice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupLowpriceInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupLowpriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupLowpriceService{
	
	public void save (ActivityIslandGroupLowprice activityIslandGroupLowprice);
	
	public void save (ActivityIslandGroupLowpriceInput activityIslandGroupLowpriceInput);
	
	public void update (ActivityIslandGroupLowprice activityIslandGroupLowprice);
	
	public ActivityIslandGroupLowprice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroupLowprice> find(Page<ActivityIslandGroupLowprice> page, ActivityIslandGroupLowpriceQuery activityIslandGroupLowpriceQuery);
	
	public List<ActivityIslandGroupLowprice> find( ActivityIslandGroupLowpriceQuery activityIslandGroupLowpriceQuery);
	
	public ActivityIslandGroupLowprice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<ActivityIslandGroupLowprice> getbyactivityIslandGroupUuid(String activityIslandGroupUuid);
	
	public List<ActivityIslandGroupPrice> getLowPrice(String activityIslandUuid);
	
	public void getLowPriceList(String uuid);
	public List<ActivityIslandGroupLowprice> getLowprice(String uuid);
	
}
