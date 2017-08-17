/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.HotelOrderPriceDao;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelOrderPriceDaoImpl extends BaseDaoImpl<HotelOrderPrice>  implements HotelOrderPriceDao{
	@Override
	public HotelOrderPrice getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelOrderPrice hotelOrderPrice where hotelOrderPrice.uuid=? and hotelOrderPrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelOrderPrice)entity;
		}
		return null;
	}
	
	
	@Override
	public HotelOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid,String groupPriceUuid) {
		Object entity = super.createQuery("from HotelOrderPrice hotelOrderPrice where hotelOrderPrice.orderUuid=? and  hotelOrderPrice.activityHotelGroupPriceUuid=? and hotelOrderPrice.delFlag=?", orderUuid,groupPriceUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelOrderPrice)entity;
		}
		return null;
	}
	

	@Override
	public List<HotelOrderPrice> getByOrderUuid(String orderUuid) {
		List<HotelOrderPrice> hotelOrderPriceList = super.find("from HotelOrderPrice hotelOrderPrice where hotelOrderPrice.orderUuid=? and hotelOrderPrice.delFlag=?", orderUuid, BaseEntity.DEL_FLAG_NORMAL);
		return hotelOrderPriceList;
	}
	
	@Override
	public int updateOrderPriceNum(int num, String orderUuid, String groupPriceUuid) {
		int i = super.update("update HotelOrderPrice hotelOrderPrice set hotelOrderPrice.num=? where  hotelOrderPrice.orderUuid=? and hotelOrderPrice.activityHotelGroupPriceUuid=? and hotelOrderPrice.delFlag=?", num, orderUuid,groupPriceUuid, BaseEntity.DEL_FLAG_NORMAL);
		return i;
	}
	
	@Override
	public int updateOrderPriceNumByUuid(int num, String uuid) {
		int i = super.update("update HotelOrderPrice hotelOrderPrice set hotelOrderPrice.num=? where  hotelOrderPrice.uuid=?  and hotelOrderPrice.delFlag=?", num, uuid, BaseEntity.DEL_FLAG_NORMAL);
		return i;
	}
}
