/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface IslandOrderPriceDao  extends BaseDao<IslandOrderPrice> {
	
	public IslandOrderPrice getByUuid(String uuid);
	
	public IslandOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid,String groupPriceUuid);
	
	public List<IslandOrderPrice> getByOrderUuid(String orderUuid);
	
	public int updateOrderPriceNum(int num,String orderUuid,String groupPriceUuid);
	
	public int updateOrderPriceNumByUuid(int num,String uuid);
	
}
