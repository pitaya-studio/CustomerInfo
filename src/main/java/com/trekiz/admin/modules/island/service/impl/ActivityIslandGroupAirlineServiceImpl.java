/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupAirlineDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupAirlineInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupAirlineQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupAirlineServiceImpl  extends BaseService implements ActivityIslandGroupAirlineService{
	@Autowired
	private ActivityIslandGroupAirlineDao activityIslandGroupAirlineDao;

	public void save (ActivityIslandGroupAirline activityIslandGroupAirline){
		super.setOptInfo(activityIslandGroupAirline, BaseService.OPERATION_ADD);
		activityIslandGroupAirlineDao.saveObj(activityIslandGroupAirline);
	}
	
	public void save (ActivityIslandGroupAirlineInput activityIslandGroupAirlineInput){
		ActivityIslandGroupAirline activityIslandGroupAirline = activityIslandGroupAirlineInput.getActivityIslandGroupAirline();
		super.setOptInfo(activityIslandGroupAirline, BaseService.OPERATION_ADD);
		activityIslandGroupAirlineDao.saveObj(activityIslandGroupAirline);
	}
	
	public void update (ActivityIslandGroupAirline activityIslandGroupAirline){
		super.setOptInfo(activityIslandGroupAirline, BaseService.OPERATION_UPDATE);
		activityIslandGroupAirlineDao.updateObj(activityIslandGroupAirline);
	}
	
	public ActivityIslandGroupAirline getById(java.lang.Integer value) {
		return activityIslandGroupAirlineDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroupAirline obj = activityIslandGroupAirlineDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroupAirline> find(Page<ActivityIslandGroupAirline> page, ActivityIslandGroupAirlineQuery activityIslandGroupAirlineQuery) {
		DetachedCriteria dc = activityIslandGroupAirlineDao.createDetachedCriteria();
		
	   	if(activityIslandGroupAirlineQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupAirlineQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupAirlineQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupAirlineQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupAirlineQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getAirline())){
			dc.add(Restrictions.eq("airline", activityIslandGroupAirlineQuery.getAirline()));
		}
		if(activityIslandGroupAirlineQuery.getDepartureTime()!=null){
			dc.add(Restrictions.eq("departureTime", activityIslandGroupAirlineQuery.getDepartureTime()));
		}
		if(activityIslandGroupAirlineQuery.getArriveTime()!=null){
			dc.add(Restrictions.eq("arriveTime", activityIslandGroupAirlineQuery.getArriveTime()));
		}
	   	if(activityIslandGroupAirlineQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupAirlineQuery.getCreateBy()));
	   	}
		if(activityIslandGroupAirlineQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupAirlineQuery.getCreateDate()));
		}
	   	if(activityIslandGroupAirlineQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupAirlineQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupAirlineQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupAirlineQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupAirlineQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupAirlineDao.find(page, dc);
	}
	
	public List<ActivityIslandGroupAirline> find( ActivityIslandGroupAirlineQuery activityIslandGroupAirlineQuery) {
		DetachedCriteria dc = activityIslandGroupAirlineDao.createDetachedCriteria();
		
	   	if(activityIslandGroupAirlineQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupAirlineQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupAirlineQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupAirlineQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupAirlineQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getAirline())){
			dc.add(Restrictions.eq("airline", activityIslandGroupAirlineQuery.getAirline()));
		}
		if(activityIslandGroupAirlineQuery.getDepartureTime()!=null){
			dc.add(Restrictions.eq("departureTime", activityIslandGroupAirlineQuery.getDepartureTime()));
		}
		if(activityIslandGroupAirlineQuery.getArriveTime()!=null){
			dc.add(Restrictions.eq("arriveTime", activityIslandGroupAirlineQuery.getArriveTime()));
		}
	   	if(activityIslandGroupAirlineQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupAirlineQuery.getCreateBy()));
	   	}
		if(activityIslandGroupAirlineQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupAirlineQuery.getCreateDate()));
		}
	   	if(activityIslandGroupAirlineQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupAirlineQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupAirlineQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupAirlineQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupAirlineQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupAirlineQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupAirlineDao.find(dc);
	}
	
	public ActivityIslandGroupAirline getByUuid(String uuid) {
		return activityIslandGroupAirlineDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroupAirline activityIslandGroupAirline = getByUuid(uuid);
		activityIslandGroupAirline.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroupAirline);
	}
	
	public List<ActivityIslandGroupAirline> getByactivityIslandGroup(String activityIslandGroupUuid){
		return activityIslandGroupAirlineDao.find("from ActivityIslandGroupAirline where activityIslandGroupUuid=? and delFlag=?", activityIslandGroupUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public List<ActivityIslandGroupAirline> getByactivityIsland(String activityIslandUuid){
		return activityIslandGroupAirlineDao.find("from ActivityIslandGroupAirline where activityIslandUuid=? and delFlag=?", activityIslandUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	/**
	 * 根据团期uuid获取航班信息
	 * @param groupUuid
	 * @return
	 */
	public List<ActivityIslandGroupAirline> getAirlineByGroupUuid(String groupUuid){
		return activityIslandGroupAirlineDao.find("from ActivityIslandGroupAirline where activityIslandGroupUuid=? and delFlag=?",groupUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
}
