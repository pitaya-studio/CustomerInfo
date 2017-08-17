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
public class HotelRoomOccuRateServiceImpl  extends BaseService implements HotelRoomOccuRateService{
	@Autowired
	private HotelRoomOccuRateDao hotelRoomOccuRateDao;

	public void save (HotelRoomOccuRate hotelRoomOccuRate){
		super.setOptInfo(hotelRoomOccuRate, BaseService.OPERATION_ADD);
		hotelRoomOccuRateDao.saveObj(hotelRoomOccuRate);
	}
	
	public void update (HotelRoomOccuRate hotelRoomOccuRate){
		super.setOptInfo(hotelRoomOccuRate, BaseService.OPERATION_UPDATE);
		hotelRoomOccuRateDao.updateObj(hotelRoomOccuRate);
	}
	
	public HotelRoomOccuRate getById(java.lang.Integer value) {
		return hotelRoomOccuRateDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelRoomOccuRate obj = hotelRoomOccuRateDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelRoomOccuRate> find(Page<HotelRoomOccuRate> page, HotelRoomOccuRate hotelRoomOccuRate) {
		DetachedCriteria dc = hotelRoomOccuRateDao.createDetachedCriteria();
		
	   	if(hotelRoomOccuRate.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoomOccuRate.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelRoomOccuRate.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelRoomOccuRate.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getHotelRoomUuid())){
			dc.add(Restrictions.like("hotelRoomUuid", "%"+hotelRoomOccuRate.getHotelRoomUuid()+"%"));
		}
	   	if(hotelRoomOccuRate.getOccupancy()!=null){
	   		dc.add(Restrictions.eq("count", hotelRoomOccuRate.getOccupancy()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getOccupancyRate())){
			dc.add(Restrictions.like("occupancyRate", "%"+hotelRoomOccuRate.getOccupancyRate()+"%"));
		}
	   	if(hotelRoomOccuRate.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelRoomOccuRate.getCreateBy()));
	   	}
		if(hotelRoomOccuRate.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelRoomOccuRate.getCreateDate()));
		}
	   	if(hotelRoomOccuRate.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelRoomOccuRate.getUpdateBy()));
	   	}
		if(hotelRoomOccuRate.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelRoomOccuRate.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelRoomOccuRate.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelRoomOccuRateDao.find(page, dc);
	}
	
	public List<HotelRoomOccuRate> find( HotelRoomOccuRate hotelRoomOccuRate) {
		DetachedCriteria dc = hotelRoomOccuRateDao.createDetachedCriteria();
		
	   	if(hotelRoomOccuRate.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoomOccuRate.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelRoomOccuRate.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelRoomOccuRate.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getHotelRoomUuid())){
			dc.add(Restrictions.like("hotelRoomUuid", "%"+hotelRoomOccuRate.getHotelRoomUuid()+"%"));
		}
	   	if(hotelRoomOccuRate.getOccupancy()!=null){
	   		dc.add(Restrictions.eq("count", hotelRoomOccuRate.getOccupancy()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getOccupancyRate())){
			dc.add(Restrictions.like("occupancyRate", "%"+hotelRoomOccuRate.getOccupancyRate()+"%"));
		}
	   	if(hotelRoomOccuRate.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelRoomOccuRate.getCreateBy()));
	   	}
		if(hotelRoomOccuRate.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelRoomOccuRate.getCreateDate()));
		}
	   	if(hotelRoomOccuRate.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelRoomOccuRate.getUpdateBy()));
	   	}
		if(hotelRoomOccuRate.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelRoomOccuRate.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRate.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelRoomOccuRate.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelRoomOccuRateDao.find(dc);
	}
	
	public HotelRoomOccuRate getByUuid(String uuid) {
		return hotelRoomOccuRateDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelRoomOccuRate hotelRoomOccuRate = getByUuid(uuid);
		hotelRoomOccuRate.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelRoomOccuRate);
	}
	
	public List<HotelRoomOccuRate> findRoomOccuRateByHotelRoom(String hotelRoomUuid) {
		HotelRoomOccuRate hotelRoomOccuRate = new HotelRoomOccuRate();
		hotelRoomOccuRate.setHotelRoomUuid(hotelRoomUuid);
		hotelRoomOccuRate.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		
		return this.find(hotelRoomOccuRate);
	}
	
}
