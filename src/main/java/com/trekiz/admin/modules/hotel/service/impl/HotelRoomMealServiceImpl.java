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
import com.trekiz.admin.modules.hotel.dao.HotelRoomMealDao;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;
import com.trekiz.admin.modules.hotel.input.HotelRoomMealInput;
import com.trekiz.admin.modules.hotel.query.HotelRoomMealQuery;
import com.trekiz.admin.modules.hotel.service.HotelRoomMealService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelRoomMealServiceImpl  extends BaseService implements HotelRoomMealService{
	@Autowired
	private HotelRoomMealDao hotelRoomMealDao;

	public void save (HotelRoomMeal hotelRoomMeal){
		super.setOptInfo(hotelRoomMeal, BaseService.OPERATION_ADD);
		hotelRoomMealDao.saveObj(hotelRoomMeal);
	}
	
	public void save (HotelRoomMealInput hotelRoomMealInput){
		HotelRoomMeal hotelRoomMeal = hotelRoomMealInput.getHotelRoomMeal();
		super.setOptInfo(hotelRoomMeal, BaseService.OPERATION_ADD);
		hotelRoomMealDao.saveObj(hotelRoomMeal);
	}
	
	public void update (HotelRoomMeal hotelRoomMeal){
		super.setOptInfo(hotelRoomMeal, BaseService.OPERATION_UPDATE);
		hotelRoomMealDao.updateObj(hotelRoomMeal);
	}
	
	public HotelRoomMeal getById(java.lang.Integer value) {
		return hotelRoomMealDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelRoomMeal obj = hotelRoomMealDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelRoomMeal> find(Page<HotelRoomMeal> page, HotelRoomMealQuery hotelRoomMealQuery) {
		DetachedCriteria dc = hotelRoomMealDao.createDetachedCriteria();
		
	   	if(hotelRoomMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoomMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelRoomMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelRoomMealQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelRoomMealQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelRoomMealQuery.getHotelMealUuid()));
		}
	   	if(hotelRoomMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelRoomMealQuery.getCreateBy()));
	   	}
		if(hotelRoomMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelRoomMealQuery.getCreateDate()));
		}
	   	if(hotelRoomMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelRoomMealQuery.getUpdateBy()));
	   	}
		if(hotelRoomMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelRoomMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelRoomMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelRoomMealDao.find(page, dc);
	}
	
	public List<HotelRoomMeal> find( HotelRoomMealQuery hotelRoomMealQuery) {
		DetachedCriteria dc = hotelRoomMealDao.createDetachedCriteria();
		
	   	if(hotelRoomMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoomMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelRoomMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelRoomMealQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelRoomMealQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelRoomMealQuery.getHotelMealUuid()));
		}
	   	if(hotelRoomMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelRoomMealQuery.getCreateBy()));
	   	}
		if(hotelRoomMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelRoomMealQuery.getCreateDate()));
		}
	   	if(hotelRoomMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelRoomMealQuery.getUpdateBy()));
	   	}
		if(hotelRoomMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelRoomMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelRoomMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelRoomMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelRoomMealDao.find(dc);
	}
	
	public List<HotelMeal> findByHotelRoomUUid(String hotelRoomUuid){
		return hotelRoomMealDao.findByHotelRoomUUid(hotelRoomUuid);
	}
	
	public HotelRoomMeal getByUuid(String uuid) {
		return hotelRoomMealDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelRoomMeal hotelRoomMeal = getByUuid(uuid);
		hotelRoomMeal.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelRoomMeal);
	}

	public List<HotelRoomMeal> findByHotelUuid(String hotelUuid) {
		return hotelRoomMealDao.findByHotelUuid(hotelUuid);
	}
}
