/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.entity.*;
import com.trekiz.admin.modules.hotel.dao.*;
import com.trekiz.admin.modules.hotel.service.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelMealServiceImpl  extends BaseService implements HotelMealService{
	@Autowired
	private HotelMealDao hotelMealDao;

	public void save (HotelMeal hotelMeal){
		super.setOptInfo(hotelMeal, BaseService.OPERATION_ADD);
		hotelMealDao.saveObj(hotelMeal);
	}
	
	public void update (HotelMeal hotelMeal){
		super.setOptInfo(hotelMeal, BaseService.OPERATION_UPDATE);
		hotelMealDao.updateObj(hotelMeal);
	}
	
	public HotelMeal getById(java.lang.Integer value) {
		return hotelMealDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelMeal obj = hotelMealDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelMeal> find(Page<HotelMeal> page, HotelMeal hotelMeal) {
		DetachedCriteria dc = hotelMealDao.createDetachedCriteria();
		
	   	if(hotelMeal.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelMeal.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelMeal.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelMeal.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelMeal.getHotelUuid()+"%"));
		}
		
		if (StringUtils.isNotEmpty(hotelMeal.getMealName())){
			dc.add(Restrictions.like("mealName", "%"+hotelMeal.getMealName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getMealType())){
			dc.add(Restrictions.like("mealType", "%"+hotelMeal.getMealType()+"%"));
		}
	   	if(hotelMeal.getSuitableNum()!=null){
	   		dc.add(Restrictions.eq("suitableNum", hotelMeal.getSuitableNum()));
	   	}
	   	if(hotelMeal.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelMeal.getPrice()));
	   	}
	   	if(hotelMeal.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelMeal.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelMeal.getMealDescription())){
			dc.add(Restrictions.like("mealDescription", "%"+hotelMeal.getMealDescription()+"%"));
		}
	   	if(hotelMeal.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelMeal.getCreateBy()));
	   	}
		if(hotelMeal.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelMeal.getCreateDate()));
		}
	   	if(hotelMeal.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelMeal.getUpdateBy()));
	   	}
		if(hotelMeal.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelMeal.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelMeal.getDelFlag()+"%"));
		}
	   	if(hotelMeal.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelMeal.getWholesalerId()));
	   	}

		dc.addOrder(Order.asc("sort"));
		return hotelMealDao.find(page, dc);
	}
	
	public List<HotelMeal> find( HotelMeal hotelMeal) {
		DetachedCriteria dc = hotelMealDao.createDetachedCriteria();
		
	   	if(hotelMeal.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelMeal.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelMeal.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelMeal.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelMeal.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getMealName())){
			dc.add(Restrictions.like("mealName", "%"+hotelMeal.getMealName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getMealType())){
			dc.add(Restrictions.eq("mealType", hotelMeal.getMealType()));
		}
	   	if(hotelMeal.getSuitableNum()!=null){
	   		dc.add(Restrictions.eq("suitableNum", hotelMeal.getSuitableNum()));
	   	}
	   	if(hotelMeal.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelMeal.getPrice()));
	   	}
	   	if(hotelMeal.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelMeal.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelMeal.getMealDescription())){
			dc.add(Restrictions.eq("mealDescription", hotelMeal.getMealDescription()));
		}
	   	if(hotelMeal.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelMeal.getCreateBy()));
	   	}
		if(hotelMeal.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelMeal.getCreateDate()));
		}
	   	if(hotelMeal.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelMeal.getUpdateBy()));
	   	}
		if(hotelMeal.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelMeal.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelMeal.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelMeal.getDelFlag()));
		}
	   	if(hotelMeal.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelMeal.getWholesalerId()));
	   	}

		dc.addOrder(Order.asc("sort"));
		return hotelMealDao.find(dc);
	}
	
	public HotelMeal getByUuid(String uuid) {
		return hotelMealDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelMeal hotelMeal = getByUuid(uuid);
		hotelMeal.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelMeal);
	}
	
	public boolean findIsExist(HotelMeal hotelMeal) {
		StringBuffer sb = new StringBuffer("from HotelMeal hotelMeal where hotelMeal.hotelUuid = ? and  hotelMeal.uuid != ? and hotelMeal.mealName = ? and hotelMeal.wholesalerId = ? and hotelMeal.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<HotelMeal> hotelMeals = hotelMealDao.find(sb.toString(), hotelMeal.getHotelUuid(), hotelMeal.getUuid(), hotelMeal.getMealName(), hotelMeal.getWholesalerId());
		if(hotelMeals == null || hotelMeals.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<HotelMeal> getMealListByUuid(String hotelUuid) {
		StringBuffer sb=new StringBuffer("from HotelMeal where hotelUuid= ? and delFlag= "+BaseEntity.DEL_FLAG_NORMAL);
		List<HotelMeal> mealList=hotelMealDao.find(sb.toString(),hotelUuid);
		if(mealList!=null){
			return mealList;
		}
		return null;
	}

	@Override
	public List<HotelMeal> getMealListByUuids(List<String> mealUuids) {
		DetachedCriteria dc = hotelMealDao.createDetachedCriteria();
		if(CollectionUtils.isNotEmpty(mealUuids)) {
			dc.add(Restrictions.in("uuid", mealUuids));
		} else {
			return new ArrayList<HotelMeal>();
		}
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		
		return hotelMealDao.find(dc);
	}
	
	@Override
	public List<HotelRoomMeal> getMealListByRoomUuid(String roomUuid) {
		StringBuffer sb=new StringBuffer("from HotelRoomMeal where hotelRoomUuid= ? and delFlag= "+BaseEntity.DEL_FLAG_NORMAL);
		List<HotelRoomMeal> mealList=hotelMealDao.find(sb.toString(),roomUuid);
		if(mealList!=null){
			return mealList;
		}
		return null;
	}

	
}
