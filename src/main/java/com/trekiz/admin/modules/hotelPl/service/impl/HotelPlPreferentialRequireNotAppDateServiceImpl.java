/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

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
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRequireNotAppDateDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequireNotAppDate;
import com.trekiz.admin.modules.hotelPl.input.HotelPlPreferentialRequireNotAppDateInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialRequireNotAppDateQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPreferentialRequireNotAppDateService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialRequireNotAppDateServiceImpl  extends BaseService implements HotelPlPreferentialRequireNotAppDateService{
	@Autowired
	private HotelPlPreferentialRequireNotAppDateDao hotelPlPreferentialRequireNotAppDateDao;

	public void save (HotelPlPreferentialRequireNotAppDate hotelPlPreferentialRequireNotAppDate){
		super.setOptInfo(hotelPlPreferentialRequireNotAppDate, BaseService.OPERATION_ADD);
		hotelPlPreferentialRequireNotAppDateDao.saveObj(hotelPlPreferentialRequireNotAppDate);
	}
	
	public void save (HotelPlPreferentialRequireNotAppDateInput hotelPlPreferentialRequireNotAppDateInput){
		HotelPlPreferentialRequireNotAppDate hotelPlPreferentialRequireNotAppDate = hotelPlPreferentialRequireNotAppDateInput.getHotelPlPreferentialRequireNotAppDate();
		super.setOptInfo(hotelPlPreferentialRequireNotAppDate, BaseService.OPERATION_ADD);
		hotelPlPreferentialRequireNotAppDateDao.saveObj(hotelPlPreferentialRequireNotAppDate);
	}
	
	public void update (HotelPlPreferentialRequireNotAppDate hotelPlPreferentialRequireNotAppDate){
		super.setOptInfo(hotelPlPreferentialRequireNotAppDate, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialRequireNotAppDateDao.updateObj(hotelPlPreferentialRequireNotAppDate);
	}
	
	public HotelPlPreferentialRequireNotAppDate getById(java.lang.Integer value) {
		return hotelPlPreferentialRequireNotAppDateDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialRequireNotAppDate obj = hotelPlPreferentialRequireNotAppDateDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialRequireNotAppDate> find(Page<HotelPlPreferentialRequireNotAppDate> page, HotelPlPreferentialRequireNotAppDateQuery hotelPlPreferentialRequireNotAppDateQuery) {
		DetachedCriteria dc = hotelPlPreferentialRequireNotAppDateDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRequireNotAppDateQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRequireNotAppDateQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRequireNotAppDateQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialRequireUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialRequireUuid", hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialRequireUuid()));
		}
		if(hotelPlPreferentialRequireNotAppDateQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlPreferentialRequireNotAppDateQuery.getStartDate()));
		}
		if(hotelPlPreferentialRequireNotAppDateQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlPreferentialRequireNotAppDateQuery.getEndDate()));
		}
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getDayNum()!=null){
	   		dc.add(Restrictions.eq("dayNum", hotelPlPreferentialRequireNotAppDateQuery.getDayNum()));
	   	}
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRequireNotAppDateQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRequireNotAppDateQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRequireNotAppDateQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRequireNotAppDateQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRequireNotAppDateQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRequireNotAppDateQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRequireNotAppDateQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRequireNotAppDateDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialRequireNotAppDate> find( HotelPlPreferentialRequireNotAppDateQuery hotelPlPreferentialRequireNotAppDateQuery) {
		DetachedCriteria dc = hotelPlPreferentialRequireNotAppDateDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRequireNotAppDateQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRequireNotAppDateQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRequireNotAppDateQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialRequireUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialRequireUuid", hotelPlPreferentialRequireNotAppDateQuery.getHotelPlPreferentialRequireUuid()));
		}
		if(hotelPlPreferentialRequireNotAppDateQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlPreferentialRequireNotAppDateQuery.getStartDate()));
		}
		if(hotelPlPreferentialRequireNotAppDateQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlPreferentialRequireNotAppDateQuery.getEndDate()));
		}
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getDayNum()!=null){
	   		dc.add(Restrictions.eq("dayNum", hotelPlPreferentialRequireNotAppDateQuery.getDayNum()));
	   	}
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRequireNotAppDateQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRequireNotAppDateQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRequireNotAppDateQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRequireNotAppDateQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRequireNotAppDateQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRequireNotAppDateQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRequireNotAppDateQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireNotAppDateQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRequireNotAppDateQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRequireNotAppDateDao.find(dc);
	}
	
	public HotelPlPreferentialRequireNotAppDate getByUuid(String uuid) {
		return hotelPlPreferentialRequireNotAppDateDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialRequireNotAppDate hotelPlPreferentialRequireNotAppDate = getByUuid(uuid);
		hotelPlPreferentialRequireNotAppDate.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialRequireNotAppDate);
	}
	
	public boolean batchDelete(String[] uuids) {
		return hotelPlPreferentialRequireNotAppDateDao.batchDelete(uuids);
	}
	
}
