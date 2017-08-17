/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelShareDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;
import com.trekiz.admin.modules.hotel.input.ActivityHotelShareInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelShareQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelShareService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelShareServiceImpl  extends BaseService implements ActivityHotelShareService{
	@Autowired
	private ActivityHotelShareDao activityHotelShareDao;

	public void save (ActivityHotelShare activityHotelShare){
		super.setOptInfo(activityHotelShare, BaseService.OPERATION_ADD);
		activityHotelShareDao.saveObj(activityHotelShare);
	}
	
	public void save (ActivityHotelShareInput activityHotelShareInput){
		ActivityHotelShare activityHotelShare = activityHotelShareInput.getActivityHotelShare();
		super.setOptInfo(activityHotelShare, BaseService.OPERATION_ADD);
		activityHotelShareDao.saveObj(activityHotelShare);
	}
	
	public void update (ActivityHotelShare activityHotelShare){
		super.setOptInfo(activityHotelShare, BaseService.OPERATION_UPDATE);
		activityHotelShareDao.updateObj(activityHotelShare);
	}
	
	public ActivityHotelShare getById(java.lang.Integer value) {
		return activityHotelShareDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelShare obj = activityHotelShareDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelShare> find(Page<ActivityHotelShare> page, ActivityHotelShareQuery activityHotelShareQuery) {
		DetachedCriteria dc = activityHotelShareDao.createDetachedCriteria();
		
	   	if(activityHotelShareQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelShareQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelShareQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelShareQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelShareQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelShareQuery.getActivityHotelUuid()));
		}
	   	if(activityHotelShareQuery.getShareUser()!=null){
	   		dc.add(Restrictions.eq("shareUser", activityHotelShareQuery.getShareUser()));
	   	}
	   	if(activityHotelShareQuery.getAcceptShareUser()!=null){
	   		dc.add(Restrictions.eq("acceptShareUser", activityHotelShareQuery.getAcceptShareUser()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelShareQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelShareQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelShareDao.find(page, dc);
	}
	
	public List<ActivityHotelShare> find( ActivityHotelShareQuery activityHotelShareQuery) {
		DetachedCriteria dc = activityHotelShareDao.createDetachedCriteria();
		
	   	if(activityHotelShareQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelShareQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelShareQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelShareQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelShareQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelShareQuery.getActivityHotelUuid()));
		}
	   	if(activityHotelShareQuery.getShareUser()!=null){
	   		dc.add(Restrictions.eq("shareUser", activityHotelShareQuery.getShareUser()));
	   	}
	   	if(activityHotelShareQuery.getAcceptShareUser()!=null){
	   		dc.add(Restrictions.eq("acceptShareUser", activityHotelShareQuery.getAcceptShareUser()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelShareQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelShareQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelShareDao.find(dc);
	}
	
	public ActivityHotelShare getByUuid(String uuid) {
		return activityHotelShareDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelShare activityHotelShare = getByUuid(uuid);
		activityHotelShare.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelShare);
	}

	@Override
	public List<ActivityHotelShare> findByActivityHotelUuid(String uuid) {
		return activityHotelShareDao.findByActivityHotelUuid(uuid);
	}

	@Override
	public List<User> findUserByActivityHotelUuid(String hotelUuid) {
		List<ActivityHotelShare> shares=activityHotelShareDao.findByActivityHotelUuid(hotelUuid);
		List<User> userList=new ArrayList<User>();
		if(shares!=null && CollectionUtils.isNotEmpty(shares)){
			for(ActivityHotelShare share:shares){
				User user=UserUtils.getUser(share.getAcceptShareUser());
				userList.add(user);
			}
		}
			return userList;
		}
	}
