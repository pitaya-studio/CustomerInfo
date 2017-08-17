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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupControlDetailDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupControlDetailDaoImpl extends BaseDaoImpl<ActivityHotelGroupControlDetail>  implements ActivityHotelGroupControlDetailDao{
	@Override
	public ActivityHotelGroupControlDetail getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupControlDetail activityHotelGroupControlDetail where activityHotelGroupControlDetail.uuid=? and activityHotelGroupControlDetail.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupControlDetail)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroupControlDetail> getDetailListByGroupUuid(
			String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupControlDetail activityHotelGroupControlDetail where activityHotelGroupControlDetail.activityHotelGroupUuid=? and activityHotelGroupControlDetail.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).list();
		if(entity != null) {
			return (List<ActivityHotelGroupControlDetail>)entity;
		}
		return null;
	}
	
}
