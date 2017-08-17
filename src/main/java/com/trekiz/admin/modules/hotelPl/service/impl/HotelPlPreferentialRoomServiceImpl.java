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
public class HotelPlPreferentialRoomServiceImpl  extends BaseService implements HotelPlPreferentialRoomService{
	@Autowired
	private HotelPlPreferentialRoomDao hotelPlPreferentialRoomDao;

	public void save (HotelPlPreferentialRoom hotelPlPreferentialRoom){
		super.setOptInfo(hotelPlPreferentialRoom, BaseService.OPERATION_ADD);
		hotelPlPreferentialRoomDao.saveObj(hotelPlPreferentialRoom);
	}
	
	public void save (HotelPlPreferentialRoomInput hotelPlPreferentialRoomInput){
		HotelPlPreferentialRoom hotelPlPreferentialRoom = hotelPlPreferentialRoomInput.getHotelPlPreferentialRoom();
		super.setOptInfo(hotelPlPreferentialRoom, BaseService.OPERATION_ADD);
		hotelPlPreferentialRoomDao.saveObj(hotelPlPreferentialRoom);
	}
	
	public void update (HotelPlPreferentialRoom hotelPlPreferentialRoom){
		super.setOptInfo(hotelPlPreferentialRoom, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialRoomDao.updateObj(hotelPlPreferentialRoom);
	}
	
	public HotelPlPreferentialRoom getById(java.lang.Integer value) {
		return hotelPlPreferentialRoomDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialRoom obj = hotelPlPreferentialRoomDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialRoom> find(Page<HotelPlPreferentialRoom> page, HotelPlPreferentialRoomQuery hotelPlPreferentialRoomQuery) {
		DetachedCriteria dc = hotelPlPreferentialRoomDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRoomQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRoomQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRoomQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelPlPreferentialRoomQuery.getHotelRoomUuid()));
		}
	   	if(hotelPlPreferentialRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", hotelPlPreferentialRoomQuery.getNights()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelPlPreferentialRoomQuery.getHotelMealUuids()));
		}
	   	if(hotelPlPreferentialRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRoomQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRoomQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRoomQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRoomQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRoomDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialRoom> find( HotelPlPreferentialRoomQuery hotelPlPreferentialRoomQuery) {
		DetachedCriteria dc = hotelPlPreferentialRoomDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRoomQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRoomQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRoomQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelPlPreferentialRoomQuery.getHotelRoomUuid()));
		}
	   	if(hotelPlPreferentialRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", hotelPlPreferentialRoomQuery.getNights()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelPlPreferentialRoomQuery.getHotelMealUuids()));
		}
	   	if(hotelPlPreferentialRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRoomQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRoomQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRoomQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRoomQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRoomDao.find(dc);
	}
	
	public HotelPlPreferentialRoom getByUuid(String uuid) {
		return hotelPlPreferentialRoomDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialRoom hotelPlPreferentialRoom = getByUuid(uuid);
		hotelPlPreferentialRoom.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialRoom);
	}
	
	public List<HotelPlPreferentialRoom> findRoomsByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialRoomDao.findRoomsByPreferentialUuid(preferentialUuid);
	}
}
