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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupLowpriceDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupLowpriceDaoImpl extends BaseDaoImpl<ActivityHotelGroupLowprice>  implements ActivityHotelGroupLowpriceDao{
	@Override
	public ActivityHotelGroupLowprice getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupLowprice activityHotelGroupLowprice where activityHotelGroupLowprice.uuid=? and activityHotelGroupLowprice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupLowprice)entity;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroupLowprice> getLowPriceListByGroupUuid(
			String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupLowprice activityHotelGroupLowprice where activityHotelGroupLowprice.activityHotelGroupUuid=? and activityHotelGroupLowprice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).list();
		if(entity != null) {
			return (List<ActivityHotelGroupLowprice>)entity;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroupPrice> getLowpriceListByUuid(String uuid) {
		TravelerType tt=getSysType(TravelerType.ALIAS_ADULT_UUID);
		//String adultUuid=tt.getSysTravelerType();
		String adultUuid=tt.getUuid();
		List<ActivityHotelGroupPrice> priceList=super.createSqlQuery("select aigp.* from activity_hotel_group_price aigp where CONCAT(aigp.currency_id,aigp.price) in " +
				"(select CONCAT(aigp1.currency_id,IFNULL(min(aigp1.price),0)) " +
				"from activity_hotel_group_price aigp1 where aigp1.activity_hotel_uuid=? AND aigp1.type=? group by aigp1.currency_id)" +
				"and aigp.activity_hotel_uuid=? AND aigp.type=? and aigp.delFlag=?", uuid,adultUuid,uuid,adultUuid,BaseEntity.DEL_FLAG_NORMAL).addEntity(ActivityHotelGroupPrice.class).list();
		return priceList;
	}
	private TravelerType getSysType(String aliasAdultUuid) {
		Object obj=super.createSqlQuery("select t.* from traveler_type t,sys_dict s where t.sys_traveler_type = s.uuid and t.sys_traveler_type=? " +
				" and wholesaler_id = ? ", aliasAdultUuid,UserUtils.getUser().getCompany().getId()).addEntity(TravelerType.class).uniqueResult();
		if(obj != null) {
			return (TravelerType)obj;
		}
		return null;
	}
	
	
	
}
