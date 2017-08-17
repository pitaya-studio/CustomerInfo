/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelOrderDaoImpl extends BaseDaoImpl<HotelOrder>  implements HotelOrderDao{
	@Override
	public HotelOrder getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelOrder hotelOrder where hotelOrder.uuid=? and hotelOrder.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelOrder)entity;
		}
		return null;
	}

	@Override
	public Integer getBookingPersonNum(String groupUuid) {
		Object entity=super.createSqlQuery("SELECT  SUM(orderPersonNum) FROM hotel_order WHERE orderStatus = '1' AND activity_hotel_group_uuid=?", groupUuid).uniqueResult();
		if(entity!=null){
			return ((BigDecimal)entity).intValue();
		}
		return 0;
	}
	
	@Override
	public Integer getForecaseReportNum(String groupUuid) {
		Object entity=super.createSqlQuery("SELECT SUM(ifnull(forecase_report_num, 0)) FROM hotel_order WHERE orderStatus = '1' AND activity_hotel_group_uuid=?", groupUuid).uniqueResult();
		if(entity!=null){
			return ((BigDecimal)entity).intValue();
		}
		return 0;
	}
	
}
