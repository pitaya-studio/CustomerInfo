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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealRiseDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupMealRiseDaoImpl extends BaseDaoImpl<ActivityHotelGroupMealRise>  implements ActivityHotelGroupMealRiseDao{
	@Override
	public ActivityHotelGroupMealRise getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupMealRise activityHotelGroupMealRise where activityHotelGroupMealRise.uuid=? and activityHotelGroupMealRise.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupMealRise)entity;
		}
		return null;
	}
	
	@Override
	public List<ActivityHotelGroupMealRise> getMealRiseByMealUuid(String uuid) {
		return super.find("from ActivityHotelGroupMealRise activityHotelGroupMealRise where activityHotelGroupMealRise.activityHotelGroupMealUuid=? and activityHotelGroupMealRise.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL);
	}

	public List<ActivityHotelGroupMealRise> getbyGroupMealUuid(String mealuuid) {
		return super.find("from ActivityHotelGroupMealRise where activityHotelGroupMealUuid=? and delFlag=?", mealuuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public List<ActivityHotelGroupMealRise> getbyGroupUuid(String groupuuid) {
		return super.find("from ActivityHotelGroupMealRise where activityHotelGroupUuid=? and delFlag=?", groupuuid,BaseEntity.DEL_FLAG_NORMAL);
	}
}
