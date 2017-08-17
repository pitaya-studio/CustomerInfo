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
import com.trekiz.admin.modules.hotel.dao.HotelTravelervisaDao;
import com.trekiz.admin.modules.hotel.entity.HotelTravelervisa;
import com.trekiz.admin.modules.hotel.input.HotelTravelervisaInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelervisaQuery;
import com.trekiz.admin.modules.hotel.service.HotelTravelervisaService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelTravelervisaServiceImpl  extends BaseService implements HotelTravelervisaService{
	@Autowired
	private HotelTravelervisaDao hotelTravelervisaDao;

	public void save (HotelTravelervisa hotelTravelervisa){
		super.setOptInfo(hotelTravelervisa, BaseService.OPERATION_ADD);
		hotelTravelervisaDao.saveObj(hotelTravelervisa);
	}
	
	public void save (HotelTravelervisaInput hotelTravelervisaInput){
		HotelTravelervisa hotelTravelervisa = hotelTravelervisaInput.getHotelTravelervisa();
		super.setOptInfo(hotelTravelervisa, BaseService.OPERATION_ADD);
		hotelTravelervisaDao.saveObj(hotelTravelervisa);
	}
	
	public void update (HotelTravelervisa hotelTravelervisa){
		super.setOptInfo(hotelTravelervisa, BaseService.OPERATION_UPDATE);
		hotelTravelervisaDao.updateObj(hotelTravelervisa);
	}
	
	public HotelTravelervisa getById(java.lang.Long value) {
		return hotelTravelervisaDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		HotelTravelervisa obj = hotelTravelervisaDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelTravelervisa> find(Page<HotelTravelervisa> page, HotelTravelervisaQuery hotelTravelervisaQuery) {
		DetachedCriteria dc = hotelTravelervisaDao.createDetachedCriteria();
		
	   	if(hotelTravelervisaQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTravelervisaQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelTravelervisaQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getHotelOrderUuid())){
			dc.add(Restrictions.eq("hotelOrderUuid", hotelTravelervisaQuery.getHotelOrderUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getHotelTravelerUuid())){
			dc.add(Restrictions.eq("hotelTravelerUuid", hotelTravelervisaQuery.getHotelTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getCountry())){
			dc.add(Restrictions.eq("country", hotelTravelervisaQuery.getCountry()));
		}
	   	if(hotelTravelervisaQuery.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", hotelTravelervisaQuery.getVisaTypeId()));
	   	}
	   	if(hotelTravelervisaQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTravelervisaQuery.getCreateBy()));
	   	}
		if(hotelTravelervisaQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTravelervisaQuery.getCreateDate()));
		}
	   	if(hotelTravelervisaQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTravelervisaQuery.getUpdateBy()));
	   	}
		if(hotelTravelervisaQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTravelervisaQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelTravelervisaQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelTravelervisaDao.find(page, dc);
	}
	
	public List<HotelTravelervisa> find( HotelTravelervisaQuery hotelTravelervisaQuery) {
		DetachedCriteria dc = hotelTravelervisaDao.createDetachedCriteria();
		
	   	if(hotelTravelervisaQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTravelervisaQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelTravelervisaQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getHotelOrderUuid())){
			dc.add(Restrictions.eq("hotelOrderUuid", hotelTravelervisaQuery.getHotelOrderUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getHotelTravelerUuid())){
			dc.add(Restrictions.eq("hotelTravelerUuid", hotelTravelervisaQuery.getHotelTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getCountry())){
			dc.add(Restrictions.eq("country", hotelTravelervisaQuery.getCountry()));
		}
	   	if(hotelTravelervisaQuery.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", hotelTravelervisaQuery.getVisaTypeId()));
	   	}
	   	if(hotelTravelervisaQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTravelervisaQuery.getCreateBy()));
	   	}
		if(hotelTravelervisaQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTravelervisaQuery.getCreateDate()));
		}
	   	if(hotelTravelervisaQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTravelervisaQuery.getUpdateBy()));
	   	}
		if(hotelTravelervisaQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTravelervisaQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelervisaQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelTravelervisaQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelTravelervisaDao.find(dc);
	}
	
	public HotelTravelervisa getByUuid(String uuid) {
		return hotelTravelervisaDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelTravelervisa hotelTravelervisa = getByUuid(uuid);
		hotelTravelervisa.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelTravelervisa);
	}
}
