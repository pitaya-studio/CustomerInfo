/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.pay.dao.PayHotelOrderDao;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class PayHotelOrderDaoImpl extends BaseDaoImpl<PayHotelOrder>  implements PayHotelOrderDao{
	@Override
	public PayHotelOrder getByUuid(String uuid) {
		Object entity = super.createQuery("from PayHotelOrder payHotelOrder where payHotelOrder.uuid=? and payHotelOrder.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PayHotelOrder)entity;
		}
		return null;
	}

	@Override
	public PayHotelOrder getByPid(Integer pid) {
		Object entity = super.createQuery("select payHotelOrder from PayHotelOrder payHotelOrder,HotelOrder ho where payHotelOrder.orderUuid=ho.uuid and  ho.id=?", pid).uniqueResult();
		if(entity != null) {
			return (PayHotelOrder)entity;
		}
		return null;
	}
	
}
