/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrAirlineDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirline;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrAirlineDaoImpl extends BaseDaoImpl<AirticketOrderPnrAirline>  implements AirticketOrderPnrAirlineDao{
	@Override
	public AirticketOrderPnrAirline getByUuid(String uuid) {
		Object entity = super.createQuery("from AirticketOrderPnrAirline airticketOrderPnrAirline where airticketOrderPnrAirline.uuid=? and airticketOrderPnrAirline.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderPnrAirline)entity;
		}
		return null;
	}

	@Override
	public List<AirticketOrderPnrAirline> findByOrderPpnrUuid(String uuid) {
		return super.find("from AirticketOrderPnrAirline where airticket_order_pnr_uuid=? and delFlag = 0",uuid);
	}
	
}
