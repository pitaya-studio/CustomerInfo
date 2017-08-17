/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealRiseDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupMealRiseInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupMealRiseQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealRiseService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupMealRiseServiceImpl  extends BaseService implements ActivityHotelGroupMealRiseService{
	@Autowired
	private ActivityHotelGroupMealRiseDao activityHotelGroupMealRiseDao;

	public void save (ActivityHotelGroupMealRise activityHotelGroupMealRise){
		super.setOptInfo(activityHotelGroupMealRise, BaseService.OPERATION_ADD);
		activityHotelGroupMealRiseDao.saveObj(activityHotelGroupMealRise);
	}
	
	public void save (ActivityHotelGroupMealRiseInput activityHotelGroupMealRiseInput){
		ActivityHotelGroupMealRise activityHotelGroupMealRise = activityHotelGroupMealRiseInput.getActivityHotelGroupMealRise();
		super.setOptInfo(activityHotelGroupMealRise, BaseService.OPERATION_ADD);
		activityHotelGroupMealRiseDao.saveObj(activityHotelGroupMealRise);
	}
	
	public void update (ActivityHotelGroupMealRise activityHotelGroupMealRise){
		super.setOptInfo(activityHotelGroupMealRise, BaseService.OPERATION_UPDATE);
		activityHotelGroupMealRiseDao.updateObj(activityHotelGroupMealRise);
	}
	
	public ActivityHotelGroupMealRise getById(java.lang.Integer value) {
		return activityHotelGroupMealRiseDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroupMealRise obj = activityHotelGroupMealRiseDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroupMealRise> find(Page<ActivityHotelGroupMealRise> page, ActivityHotelGroupMealRiseQuery activityHotelGroupMealRiseQuery) {
		DetachedCriteria dc = activityHotelGroupMealRiseDao.createDetachedCriteria();
		
	   	if(activityHotelGroupMealRiseQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupMealRiseQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupMealRiseQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupMealRiseQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupMealRiseQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", activityHotelGroupMealRiseQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getHotelMealRiseUuid())){
			dc.add(Restrictions.eq("hotelMealRiseUuid", activityHotelGroupMealRiseQuery.getHotelMealRiseUuid()));
		}
	   	if(activityHotelGroupMealRiseQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupMealRiseQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupMealRiseQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityHotelGroupMealRiseQuery.getPrice()));
	   	}
	   	if(activityHotelGroupMealRiseQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupMealRiseQuery.getCreateBy()));
	   	}
		if(activityHotelGroupMealRiseQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupMealRiseQuery.getCreateDate()));
		}
	   	if(activityHotelGroupMealRiseQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupMealRiseQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupMealRiseQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupMealRiseQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupMealRiseQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupMealRiseDao.find(page, dc);
	}
	
	public List<ActivityHotelGroupMealRise> find( ActivityHotelGroupMealRiseQuery activityHotelGroupMealRiseQuery) {
		DetachedCriteria dc = activityHotelGroupMealRiseDao.createDetachedCriteria();
		
	   	if(activityHotelGroupMealRiseQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupMealRiseQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupMealRiseQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupMealRiseQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupMealRiseQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", activityHotelGroupMealRiseQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getHotelMealRiseUuid())){
			dc.add(Restrictions.eq("hotelMealRiseUuid", activityHotelGroupMealRiseQuery.getHotelMealRiseUuid()));
		}
	   	if(activityHotelGroupMealRiseQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupMealRiseQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupMealRiseQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityHotelGroupMealRiseQuery.getPrice()));
	   	}
	   	if(activityHotelGroupMealRiseQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupMealRiseQuery.getCreateBy()));
	   	}
		if(activityHotelGroupMealRiseQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupMealRiseQuery.getCreateDate()));
		}
	   	if(activityHotelGroupMealRiseQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupMealRiseQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupMealRiseQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupMealRiseQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealRiseQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupMealRiseQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupMealRiseDao.find(dc);
	}
	
	public ActivityHotelGroupMealRise getByUuid(String uuid) {
		return activityHotelGroupMealRiseDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroupMealRise activityHotelGroupMealRise = getByUuid(uuid);
		activityHotelGroupMealRise.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroupMealRise);
	}

	@Override
	public List<ActivityHotelGroupMealRise> getMealRiseByMealUuid(String uuid) {
		
		return activityHotelGroupMealRiseDao.getMealRiseByMealUuid(uuid);
	}
}
