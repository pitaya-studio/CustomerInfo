/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.flight.dao.FlightControlHotelDetailDao;
import com.trekiz.admin.modules.flight.entity.FlightControlHotelDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class FlightControlHotelDetailDaoImpl extends BaseDaoImpl<FlightControlHotelDetail>  implements FlightControlHotelDetailDao{
	@Override
	public FlightControlHotelDetail getByUuid(String uuid) {
		Object entity = super.createQuery("from FlightControlHotelDetail flightControlHotelDetail where flightControlHotelDetail.uuid=? and flightControlHotelDetail.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (FlightControlHotelDetail)entity;
		}
		return null;
	}
	
}
