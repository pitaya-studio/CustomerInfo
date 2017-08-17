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
public class HotelRoomOccuRateDetailServiceImpl  extends BaseService implements HotelRoomOccuRateDetailService{
	@Autowired
	private HotelRoomOccuRateDetailDao hotelRoomOccuRateDetailDao;

	public void save (HotelRoomOccuRateDetail hotelRoomOccuRateDetail){
		super.setOptInfo(hotelRoomOccuRateDetail, BaseService.OPERATION_ADD);
		hotelRoomOccuRateDetailDao.saveObj(hotelRoomOccuRateDetail);
	}
	
	public void update (HotelRoomOccuRateDetail hotelRoomOccuRateDetail){
		super.setOptInfo(hotelRoomOccuRateDetail, BaseService.OPERATION_UPDATE);
		hotelRoomOccuRateDetailDao.updateObj(hotelRoomOccuRateDetail);
	}
	
	public HotelRoomOccuRateDetail getById(java.lang.Integer value) {
		return hotelRoomOccuRateDetailDao.getById(value);
	}
	
	
	public Page<HotelRoomOccuRateDetail> find(Page<HotelRoomOccuRateDetail> page, HotelRoomOccuRateDetail hotelRoomOccuRateDetail) {
		DetachedCriteria dc = hotelRoomOccuRateDetailDao.createDetachedCriteria();
		
	   	if(hotelRoomOccuRateDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoomOccuRateDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelRoomOccuRateDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelRoomOccuRateDetail.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getHotelRoomUuid())){
			dc.add(Restrictions.like("hotelRoomUuid", "%"+hotelRoomOccuRateDetail.getHotelRoomUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getHotelRoomOccuRateUuid())){
			dc.add(Restrictions.like("hotelRoomOccuRateUuid", "%"+hotelRoomOccuRateDetail.getHotelRoomOccuRateUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getTravelerTypeUuid())){
			dc.add(Restrictions.like("travelerTypeUuid", "%"+hotelRoomOccuRateDetail.getTravelerTypeUuid()+"%"));
		}
	   	if(hotelRoomOccuRateDetail.getCount()!=null){
	   		dc.add(Restrictions.eq("count", hotelRoomOccuRateDetail.getCount()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getShortName())){
			dc.add(Restrictions.like("shortName", "%"+hotelRoomOccuRateDetail.getShortName()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelRoomOccuRateDetailDao.find(page, dc);
	}
	
	public List<HotelRoomOccuRateDetail> find( HotelRoomOccuRateDetail hotelRoomOccuRateDetail) {
		DetachedCriteria dc = hotelRoomOccuRateDetailDao.createDetachedCriteria();
		
	   	if(hotelRoomOccuRateDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoomOccuRateDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelRoomOccuRateDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelRoomOccuRateDetail.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getHotelRoomUuid())){
			dc.add(Restrictions.like("hotelRoomUuid", "%"+hotelRoomOccuRateDetail.getHotelRoomUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getHotelRoomOccuRateUuid())){
			dc.add(Restrictions.like("hotelRoomOccuRateUuid", "%"+hotelRoomOccuRateDetail.getHotelRoomOccuRateUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getTravelerTypeUuid())){
			dc.add(Restrictions.like("travelerTypeUuid", "%"+hotelRoomOccuRateDetail.getTravelerTypeUuid()+"%"));
		}
	   	if(hotelRoomOccuRateDetail.getCount()!=null){
	   		dc.add(Restrictions.eq("count", hotelRoomOccuRateDetail.getCount()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoomOccuRateDetail.getShortName())){
			dc.add(Restrictions.like("shortName", "%"+hotelRoomOccuRateDetail.getShortName()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelRoomOccuRateDetailDao.find(dc);
	}
	
	public HotelRoomOccuRateDetail getByUuid(String uuid) {
		return hotelRoomOccuRateDetailDao.getByUuid(uuid);
	}
	
	public List<HotelRoomOccuRateDetail> findOccuRateDetailByRoomOccuRate(String roomOccuRateUuid) {
		HotelRoomOccuRateDetail hotelRoomOccuRateDetail = new HotelRoomOccuRateDetail();
		hotelRoomOccuRateDetail.setHotelRoomOccuRateUuid(roomOccuRateUuid);
		return this.find(hotelRoomOccuRateDetail);
	}
}
