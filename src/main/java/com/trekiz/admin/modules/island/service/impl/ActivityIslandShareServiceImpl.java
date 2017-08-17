/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

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
import com.trekiz.admin.modules.island.dao.ActivityIslandShareDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;
import com.trekiz.admin.modules.island.input.ActivityIslandShareInput;
import com.trekiz.admin.modules.island.query.ActivityIslandShareQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandShareService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandShareServiceImpl  extends BaseService implements ActivityIslandShareService{
	@Autowired
	private ActivityIslandShareDao activityIslandShareDao;

	public void save (ActivityIslandShare activityIslandShare){
		super.setOptInfo(activityIslandShare, BaseService.OPERATION_ADD);
		activityIslandShareDao.saveObj(activityIslandShare);
	}
	
	public void save (ActivityIslandShareInput activityIslandShareInput){
		ActivityIslandShare activityIslandShare = activityIslandShareInput.getActivityIslandShare();
		super.setOptInfo(activityIslandShare, BaseService.OPERATION_ADD);
		activityIslandShareDao.saveObj(activityIslandShare);
	}
	
	public void update (ActivityIslandShare activityIslandShare){
		super.setOptInfo(activityIslandShare, BaseService.OPERATION_UPDATE);
		activityIslandShareDao.updateObj(activityIslandShare);
	}
	
	public ActivityIslandShare getById(java.lang.Integer value) {
		return activityIslandShareDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandShare obj = activityIslandShareDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandShare> find(Page<ActivityIslandShare> page, ActivityIslandShareQuery activityIslandShareQuery) {
		DetachedCriteria dc = activityIslandShareDao.createDetachedCriteria();
		
	   	if(activityIslandShareQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandShareQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandShareQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandShareQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandShareQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandShareQuery.getActivityIslandUuid()));
		}
	   	if(activityIslandShareQuery.getShareUser()!=null){
	   		dc.add(Restrictions.eq("shareUser", activityIslandShareQuery.getShareUser()));
	   	}
	   	if(activityIslandShareQuery.getAcceptShareUser()!=null){
	   		dc.add(Restrictions.eq("acceptShareUser", activityIslandShareQuery.getAcceptShareUser()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandShareQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandShareQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandShareDao.find(page, dc);
	}
	
	public List<ActivityIslandShare> find( ActivityIslandShareQuery activityIslandShareQuery) {
		DetachedCriteria dc = activityIslandShareDao.createDetachedCriteria();
		
	   	if(activityIslandShareQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandShareQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandShareQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandShareQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandShareQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandShareQuery.getActivityIslandUuid()));
		}
	   	if(activityIslandShareQuery.getShareUser()!=null){
	   		dc.add(Restrictions.eq("shareUser", activityIslandShareQuery.getShareUser()));
	   	}
	   	if(activityIslandShareQuery.getAcceptShareUser()!=null){
	   		dc.add(Restrictions.eq("acceptShareUser", activityIslandShareQuery.getAcceptShareUser()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandShareQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandShareQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandShareDao.find(dc);
	}
	
	public ActivityIslandShare getByUuid(String uuid) {
		return activityIslandShareDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandShare activityIslandShare = getByUuid(uuid);
		activityIslandShare.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandShare);
	}
	

	public List<User> findShareUserByIsland(String activityIslandUuid) {
		List<ActivityIslandShare> shares = activityIslandShareDao.findShareUserByIsland(activityIslandUuid);
		List<User> shareUsers = new ArrayList<User>();
		if(shares != null && CollectionUtils.isNotEmpty(shares)) {
			for(ActivityIslandShare share : shares) {
				User user = UserUtils.getUser(share.getAcceptShareUser());
				shareUsers.add(user);
			}
		}
		
		return shareUsers;
	}
	
	public boolean saveActivityIslandShareData(String[] accShareUsers, Long shareUser, String islandUuid) {
		if(accShareUsers == null || accShareUsers.length == 0 || shareUser == null || StringUtils.isEmpty(islandUuid)) {
			return false;
		}
		
		//组装表数据
		List<ActivityIslandShare> shares = new ArrayList<ActivityIslandShare>();
		for(String accShareUser : accShareUsers) {
			if(!accShareUser.matches("\\d*")){
				continue;
			}
			ActivityIslandShare share = new ActivityIslandShare();
			share.setShareUser(shareUser);
			share.setAcceptShareUser(Long.parseLong(accShareUser));
			share.setActivityIslandUuid(islandUuid);
			super.setOptInfo(share, OPERATION_ADD);
			
			shares.add(share);
		}
		activityIslandShareDao.batchSave(shares);
		
		return true;
	}
	
	public boolean updateActivityIslandShareData(String[] accShareUsers, Long shareUser, String islandUuid) {
		//删除之前的旧数据
		int count = activityIslandShareDao.deleteShareDataByIslandUuid(islandUuid);
		return this.saveActivityIslandShareData(accShareUsers, shareUser, islandUuid);
	}
	@Override
	public List<ActivityIslandShare> findByActivityIslandUuid(String uuid) {
		return activityIslandShareDao.find("from ActivityIslandShare where activityIslandUuid=? and delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL);
	}
}
