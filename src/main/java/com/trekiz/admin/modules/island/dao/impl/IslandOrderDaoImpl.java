/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.IslandOrder;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class IslandOrderDaoImpl extends BaseDaoImpl<IslandOrder>  implements IslandOrderDao{
	@Override
	public IslandOrder getByUuid(String uuid) {
		Object entity = super.createQuery("from IslandOrder islandOrder where islandOrder.uuid=? and islandOrder.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (IslandOrder)entity;
		}
		return null;
	}
	
	@Override
	public IslandOrder getById(Integer id){
		Object entity = super.createQuery("from IslandOrder islandOrder where islandOrder.id=? and islandOrder.delFlag=?", id, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (IslandOrder)entity;
		}
		return null;
	}

	@Override
	public Integer getBookingPersonNum(String groupUuid) {
		Object entity = super.createSqlQuery("SELECT SUM(orderPersonNum) FROM island_order WHERE orderStatus = '1' AND activity_island_group_uuid=?", groupUuid).uniqueResult();
		if(entity != null) {
			return ((BigDecimal)entity).intValue();
		}
		return 0;
	}
	
	public Integer getForecaseReportNum(String groupUuid) {
		Object entity=super.createSqlQuery("SELECT SUM(ifnull(forecase_report_roomNum, 0)) FROM island_order WHERE orderStatus = '1' AND activity_island_group_uuid=?", groupUuid).uniqueResult();
		if(entity!=null){
			return ((BigDecimal)entity).intValue();
		}
		return 0;
	}
	
}
