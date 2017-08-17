/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.IslandOrderPriceDao;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class IslandOrderPriceDaoImpl extends BaseDaoImpl<IslandOrderPrice>  implements IslandOrderPriceDao{
	@Override
	public IslandOrderPrice getByUuid(String uuid) {
		Object entity = super.createQuery("from IslandOrderPrice islandOrderPrice where islandOrderPrice.uuid=? and islandOrderPrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (IslandOrderPrice)entity;
		}
		return null;
	}
	
	@Override
	public IslandOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid,String groupPriceUuid) {
		Object entity = super.createQuery("from IslandOrderPrice islandOrderPrice where islandOrderPrice.orderUuid=? and  islandOrderPrice.activityIslandGroupPriceUuid=? and islandOrderPrice.delFlag=?", orderUuid,groupPriceUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (IslandOrderPrice)entity;
		}
		return null;
	}
	
	@Override
	public List<IslandOrderPrice> getByOrderUuid(String orderUuid) {
		List<IslandOrderPrice> islandOrderPriceList = super.find("from IslandOrderPrice islandOrderPrice where islandOrderPrice.orderUuid=? and islandOrderPrice.delFlag=?", orderUuid, BaseEntity.DEL_FLAG_NORMAL);
		return islandOrderPriceList;
	}
	
	@Override
	public int updateOrderPriceNum(int num,String orderUuid,String groupPriceUuid) {
		int i = super.update("update IslandOrderPrice islandOrderPrice set islandOrderPrice.num=? where  islandOrderPrice.orderUuid=? and islandOrderPrice.activityIslandGroupPriceUuid=? and islandOrderPrice.delFlag=?", num,orderUuid,groupPriceUuid,BaseEntity.DEL_FLAG_NORMAL);
		return i;
	}
	
	@Override
	public int updateOrderPriceNumByUuid(int num,String uuid) {
		int i = super.update("update IslandOrderPrice islandOrderPrice set islandOrderPrice.num=? where  islandOrderPrice.uuid=? and islandOrderPrice.delFlag=?", num,uuid,BaseEntity.DEL_FLAG_NORMAL);
		return i;
	}
	
	
}
