/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class HotelFloorServiceImpl  extends BaseService implements HotelFloorService{
	@Autowired
	private HotelFloorDao hotelFloorDao;

	public void save (HotelFloor hotelFloor){
		super.setOptInfo(hotelFloor, BaseService.OPERATION_ADD);
		hotelFloorDao.saveObj(hotelFloor);
	}
	
	public void update (HotelFloor hotelFloor){
		super.setOptInfo(hotelFloor, BaseService.OPERATION_UPDATE);
		hotelFloorDao.updateObj(hotelFloor);
	}
	
	public HotelFloor getById(java.lang.Integer value) {
		return hotelFloorDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelFloor obj = hotelFloorDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelFloor> find(Page<HotelFloor> page, HotelFloor hotelFloor) {
		DetachedCriteria dc = hotelFloorDao.createDetachedCriteria();
		
	   	if(hotelFloor.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelFloor.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelFloor.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelFloor.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelFloor.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getHotelRoomUuid())){
			dc.add(Restrictions.like("hotelRoomUuid", "%"+hotelFloor.getHotelRoomUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getFloorName())){
			dc.add(Restrictions.like("floorName", "%"+hotelFloor.getFloorName()+"%"));
		}
	   	if(hotelFloor.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelFloor.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelFloor.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelFloor.getDescription()+"%"));
		}
	   	if(hotelFloor.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelFloor.getCreateBy()));
	   	}
		if(hotelFloor.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelFloor.getCreateDate()));
		}
	   	if(hotelFloor.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelFloor.getUpdateBy()));
	   	}
		if(hotelFloor.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelFloor.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelFloor.getDelFlag()+"%"));
		}
	   	if(hotelFloor.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelFloor.getWholesalerId()));
	   	}

		dc.addOrder(Order.asc("sort"));
		return hotelFloorDao.find(page, dc);
	}
	
	public List<HotelFloor> find( HotelFloor hotelFloor) {
		DetachedCriteria dc = hotelFloorDao.createDetachedCriteria();
		
	   	if(hotelFloor.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelFloor.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelFloor.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelFloor.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelFloor.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getHotelRoomUuid())){
			dc.add(Restrictions.like("hotelRoomUuid", "%"+hotelFloor.getHotelRoomUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getFloorName())){
			dc.add(Restrictions.like("floorName", "%"+hotelFloor.getFloorName()+"%"));
		}
	   	if(hotelFloor.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelFloor.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelFloor.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelFloor.getDescription()+"%"));
		}
	   	if(hotelFloor.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelFloor.getCreateBy()));
	   	}
		if(hotelFloor.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelFloor.getCreateDate()));
		}
	   	if(hotelFloor.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelFloor.getUpdateBy()));
	   	}
		if(hotelFloor.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelFloor.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelFloor.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelFloor.getDelFlag()+"%"));
		}
	   	if(hotelFloor.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelFloor.getWholesalerId()));
	   	}

		dc.addOrder(Order.asc("sort"));
		return hotelFloorDao.find(dc);
	}
	
	public HotelFloor getByUuid(String uuid) {
		return hotelFloorDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelFloor hotelFloor = getByUuid(uuid);
		hotelFloor.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelFloor);
	}
	
	public boolean findFloorNameIsExist(HotelFloor hotelFloor) {
		StringBuffer sb = new StringBuffer("from HotelFloor hotelFloor where hotelFloor.uuid != ? and hotelFloor.hotelUuid=? and hotelFloor.hotelRoomUuid=? and hotelFloor.floorName = ? and hotelFloor.wholesalerId = ? and hotelFloor.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		List<HotelFloor> hotelFloors = hotelFloorDao.find(sb.toString(), hotelFloor.getUuid(), hotelFloor.getHotelUuid(), hotelFloor.getHotelRoomUuid(), hotelFloor.getFloorName(), hotelFloor.getWholesalerId());
		if(hotelFloors == null || hotelFloors.size() == 0) {
			return false;
		}
		return true;
	}
}
