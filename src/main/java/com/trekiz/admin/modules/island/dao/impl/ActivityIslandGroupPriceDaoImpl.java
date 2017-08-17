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

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupPriceDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupPriceDaoImpl extends BaseDaoImpl<ActivityIslandGroupPrice>  implements ActivityIslandGroupPriceDao{
	@Override
	public ActivityIslandGroupPrice getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandGroupPrice activityIslandGroupPrice where activityIslandGroupPrice.uuid=? and activityIslandGroupPrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandGroupPrice)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivityIslandGroupPrice> getPriceListByGroupUuid(String groupUuid) {
		return super.createQuery("from ActivityIslandGroupPrice activityIslandGroupPrice where activityIslandGroupPrice.activityIslandGroupUuid=? and activityIslandGroupPrice.delFlag=?", groupUuid, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityIslandGroupPrice> getLowPriceListByUuid(
			String activityIslandUuid) {
		return super.createQuery("from ActivityIslandGroupPrice activityIslandGroupPrice where activityIslandGroupPrice.activityIslandUuid=?", activityIslandUuid).list();
	}

	@Override
	public int updateByActivityIslandGroupUUid(String islandGroupUuid) {
		
		super.updateBySql("update activity_island_group_price set delFlag = ? where activity_island_group_uuid = ?", Context.DEL_FLAG_DELETE,islandGroupUuid);
		return 1;
	}
	
	public ActivityIslandGroupPrice getGroupPriceByGroupInfo(String activityIslandGroupUuid, String travelerTypeUuid, String spaceLevel) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT groupPrice.currency_id AS currencyId, groupPrice.price AS price ");
		sb.append("FROM activity_island_group_price groupPrice ");
		sb.append("LEFT JOIN activity_island_group_airline groupAirline ON groupPrice.activity_island_group_airline_uuid = groupAirline.uuid ");
		sb.append("WHERE groupPrice.activity_island_group_uuid = ? AND groupPrice.type = ? AND groupAirline.space_level = ? AND groupPrice.delFlag=?");
		List<?> list = super.createSqlQuery(sb.toString(), activityIslandGroupUuid, travelerTypeUuid, spaceLevel, BaseEntity.DEL_FLAG_NORMAL).list();
		if(list != null && list.size() > 0) {
			ActivityIslandGroupPrice groupPrice = new ActivityIslandGroupPrice();
			Object[] objArr = (Object[])list.get(0);
			groupPrice.setCurrencyId((Integer)objArr[0]);
			if(objArr[1] != null) {
				groupPrice.setPrice(((java.math.BigDecimal)objArr[1]).doubleValue());
			}
			
			return groupPrice;
		}
		
		return null;
	}
	
	public List<ActivityIslandGroupPrice> getGroupPriceByAirlineUuid(String activityIslandGroupAirlineUuid){
		return super.find("from ActivityIslandGroupPrice where activityIslandGroupAirlineUuid=? and delFlag=?", activityIslandGroupAirlineUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
