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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupPriceDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupPriceDaoImpl extends BaseDaoImpl<ActivityHotelGroupPrice>  implements ActivityHotelGroupPriceDao{
	@Override
	public ActivityHotelGroupPrice getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupPrice activityHotelGroupPrice where activityHotelGroupPrice.uuid=? and activityHotelGroupPrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupPrice)entity;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroupPrice> getPriceListByGroupUuid(String uuid) {
		Object obj=super.createQuery("from ActivityHotelGroupPrice activityHotelGroupPrice where activityHotelGroupPrice.activityHotelGroupUuid=? and activityHotelGroupPrice.delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL).list();
		if(obj!=null){
			return (List<ActivityHotelGroupPrice>)obj;
		}
		return null;
	}
	
	public ActivityHotelGroupPrice getGroupPriceByGroupInfo(String groupUuid, String type) {
		Object entity = super.createQuery("from ActivityHotelGroupPrice activityHotelGroupPrice where activityHotelGroupPrice.activityHotelGroupUuid=? and activityHotelGroupPrice.type=? and activityHotelGroupPrice.delFlag=?", groupUuid, type, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupPrice)entity;
		}
		return null;
	}
	
}
