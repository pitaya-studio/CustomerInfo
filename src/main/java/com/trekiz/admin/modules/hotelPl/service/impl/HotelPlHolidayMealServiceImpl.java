/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotelPl.entity.*;
import com.trekiz.admin.modules.hotelPl.dao.*;
import com.trekiz.admin.modules.hotelPl.service.*;
import com.trekiz.admin.modules.hotelPl.input.*;
import com.trekiz.admin.modules.hotelPl.query.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlHolidayMealServiceImpl  extends BaseService implements HotelPlHolidayMealService{
	@Autowired
	private HotelPlHolidayMealDao hotelPlHolidayMealDao;

	public void save (HotelPlHolidayMeal hotelPlHolidayMeal){
		super.setOptInfo(hotelPlHolidayMeal, BaseService.OPERATION_ADD);
		hotelPlHolidayMealDao.saveObj(hotelPlHolidayMeal);
	}
	
	public void save (HotelPlHolidayMealInput hotelPlHolidayMealInput){
		HotelPlHolidayMeal hotelPlHolidayMeal = hotelPlHolidayMealInput.getHotelPlHolidayMeal();
		super.setOptInfo(hotelPlHolidayMeal, BaseService.OPERATION_ADD);
		hotelPlHolidayMealDao.saveObj(hotelPlHolidayMeal);
	}
	
	public void update (HotelPlHolidayMeal hotelPlHolidayMeal){
		super.setOptInfo(hotelPlHolidayMeal, BaseService.OPERATION_UPDATE);
		hotelPlHolidayMealDao.updateObj(hotelPlHolidayMeal);
	}
	
	public HotelPlHolidayMeal getById(java.lang.Integer value) {
		return hotelPlHolidayMealDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlHolidayMeal obj = hotelPlHolidayMealDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlHolidayMeal> find(Page<HotelPlHolidayMeal> page, HotelPlHolidayMealQuery hotelPlHolidayMealQuery) {
		DetachedCriteria dc = hotelPlHolidayMealDao.createDetachedCriteria();
		
	   	if(hotelPlHolidayMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlHolidayMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlHolidayMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlHolidayMealQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlHolidayMealQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlHolidayMealQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getHolidayMealName())){
			dc.add(Restrictions.eq("holidayMealName", hotelPlHolidayMealQuery.getHolidayMealName()));
		}
		if(hotelPlHolidayMealQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlHolidayMealQuery.getStartDate()));
		}
		if(hotelPlHolidayMealQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlHolidayMealQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlHolidayMealQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlHolidayMealQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlHolidayMealQuery.getCurrencyId()));
	   	}
	   	if(hotelPlHolidayMealQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlHolidayMealQuery.getAmount()));
	   	}
	   	if(hotelPlHolidayMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlHolidayMealQuery.getCreateBy()));
	   	}
		if(hotelPlHolidayMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlHolidayMealQuery.getCreateDate()));
		}
	   	if(hotelPlHolidayMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlHolidayMealQuery.getUpdateBy()));
	   	}
		if(hotelPlHolidayMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlHolidayMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlHolidayMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlHolidayMealDao.find(page, dc);
	}
	
	public List<HotelPlHolidayMeal> find( HotelPlHolidayMealQuery hotelPlHolidayMealQuery) {
		DetachedCriteria dc = hotelPlHolidayMealDao.createDetachedCriteria();
		
	   	if(hotelPlHolidayMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlHolidayMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlHolidayMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlHolidayMealQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlHolidayMealQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlHolidayMealQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getHolidayMealName())){
			dc.add(Restrictions.eq("holidayMealName", hotelPlHolidayMealQuery.getHolidayMealName()));
		}
		if(hotelPlHolidayMealQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlHolidayMealQuery.getStartDate()));
		}
		if(hotelPlHolidayMealQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlHolidayMealQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlHolidayMealQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlHolidayMealQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlHolidayMealQuery.getCurrencyId()));
	   	}
	   	if(hotelPlHolidayMealQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlHolidayMealQuery.getAmount()));
	   	}
	   	if(hotelPlHolidayMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlHolidayMealQuery.getCreateBy()));
	   	}
		if(hotelPlHolidayMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlHolidayMealQuery.getCreateDate()));
		}
	   	if(hotelPlHolidayMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlHolidayMealQuery.getUpdateBy()));
	   	}
		if(hotelPlHolidayMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlHolidayMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlHolidayMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlHolidayMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlHolidayMealDao.find(dc);
	}
	
	public HotelPlHolidayMeal getByUuid(String uuid) {
		return hotelPlHolidayMealDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlHolidayMeal hotelPlHolidayMeal = getByUuid(uuid);
		hotelPlHolidayMeal.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlHolidayMeal);
	}
	
	public List<HotelPlHolidayMeal> findPlHolidayMealsByHotelPlUuid(String hotelPlUuid) {
		return hotelPlHolidayMealDao.findPlHolidayMealsByHotelPlUuid(hotelPlUuid);
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的强制餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlHolidayMeal> getHotelPlHolidayMeal4AutoQuotedPrice(HotelPlHolidayMealQuery hotelPlHolidayMealQuery){
		return hotelPlHolidayMealDao.getHotelPlHolidayMeal4AutoQuotedPrice(hotelPlHolidayMealQuery);
	}
	
}
