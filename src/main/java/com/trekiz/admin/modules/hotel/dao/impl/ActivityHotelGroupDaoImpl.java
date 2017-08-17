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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupDaoImpl extends BaseDaoImpl<ActivityHotelGroup>  implements ActivityHotelGroupDao{
	@Override
	public ActivityHotelGroup getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroup activityHotelGroup where activityHotelGroup.uuid=? and activityHotelGroup.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroup)entity;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroup> findGroupsByActivityHotelUuid(String uuid) {
		Object obj=super.createQuery("from ActivityHotelGroup aa where aa.activityHotelUuid=? and delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL).list();
		if(obj!=null){
			return (List<ActivityHotelGroup>)obj;
		}
		return null;
	}
	
	/**
	 * 根据产品的uuid来跟新产品下团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByActivityUuid(String uuid, String status) {
		StringBuffer sb = new StringBuffer("update activity_hotel_group set status = "+status+" where activity_hotel_uuid in ("+uuid+") and delFlag = ? ");
		return super.createSqlQuery(sb.toString(),BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
	}
	/**
	 * 根据uuid来更新团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByUuid(String uuid, String status) {
		StringBuffer sb = new StringBuffer("update activity_hotel_group set status = "+status+" where uuid in ("+uuid+") and delFlag = ? ");
		return super.createSqlQuery(sb.toString(),BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
	}
	@Override
	public int updateIscommission(Integer id, int iscommission) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("update activity_hotel_group set iscommission = ? where id = ?");
		return createSqlQuery(sb.toString(), iscommission,id).executeUpdate();
		
	}
	
}
