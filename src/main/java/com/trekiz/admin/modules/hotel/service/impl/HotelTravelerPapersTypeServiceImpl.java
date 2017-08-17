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
import com.trekiz.admin.modules.hotel.dao.HotelTravelerPapersTypeDao;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerPapersType;
import com.trekiz.admin.modules.hotel.input.HotelTravelerPapersTypeInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerPapersTypeQuery;
import com.trekiz.admin.modules.hotel.service.HotelTravelerPapersTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelTravelerPapersTypeServiceImpl  extends BaseService implements HotelTravelerPapersTypeService{
	@Autowired
	private HotelTravelerPapersTypeDao hotelTravelerPapersTypeDao;

	public void save (HotelTravelerPapersType hotelTravelerPapersType){
		super.setOptInfo(hotelTravelerPapersType, BaseService.OPERATION_ADD);
		hotelTravelerPapersTypeDao.saveObj(hotelTravelerPapersType);
	}
	
	public void save (HotelTravelerPapersTypeInput hotelTravelerPapersTypeInput){
		HotelTravelerPapersType hotelTravelerPapersType = hotelTravelerPapersTypeInput.getHotelTravelerPapersType();
		super.setOptInfo(hotelTravelerPapersType, BaseService.OPERATION_ADD);
		hotelTravelerPapersTypeDao.saveObj(hotelTravelerPapersType);
	}
	
	public void update (HotelTravelerPapersType hotelTravelerPapersType){
		super.setOptInfo(hotelTravelerPapersType, BaseService.OPERATION_UPDATE);
		hotelTravelerPapersTypeDao.updateObj(hotelTravelerPapersType);
	}
	
	public HotelTravelerPapersType getById(java.lang.Integer value) {
		return hotelTravelerPapersTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelTravelerPapersType obj = hotelTravelerPapersTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelTravelerPapersType> find(Page<HotelTravelerPapersType> page, HotelTravelerPapersTypeQuery hotelTravelerPapersTypeQuery) {
		DetachedCriteria dc = hotelTravelerPapersTypeDao.createDetachedCriteria();
		
	   	if(hotelTravelerPapersTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTravelerPapersTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelTravelerPapersTypeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getHotelTravelerUuid())){
			dc.add(Restrictions.eq("hotelTravelerUuid", hotelTravelerPapersTypeQuery.getHotelTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", hotelTravelerPapersTypeQuery.getOrderUuid()));
		}
	   	if(hotelTravelerPapersTypeQuery.getPapersType()!=null){
	   		dc.add(Restrictions.eq("papersType", hotelTravelerPapersTypeQuery.getPapersType()));
	   	}
		if(hotelTravelerPapersTypeQuery.getValidityDate()!=null){
			dc.add(Restrictions.eq("validityDate", hotelTravelerPapersTypeQuery.getValidityDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getIdCard())){
			dc.add(Restrictions.eq("idCard", hotelTravelerPapersTypeQuery.getIdCard()));
		}
		if(hotelTravelerPapersTypeQuery.getIssueDate()!=null){
			dc.add(Restrictions.eq("issueDate", hotelTravelerPapersTypeQuery.getIssueDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getIssuePlace())){
			dc.add(Restrictions.eq("issuePlace", hotelTravelerPapersTypeQuery.getIssuePlace()));
		}
	   	if(hotelTravelerPapersTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTravelerPapersTypeQuery.getCreateBy()));
	   	}
		if(hotelTravelerPapersTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTravelerPapersTypeQuery.getCreateDate()));
		}
	   	if(hotelTravelerPapersTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTravelerPapersTypeQuery.getUpdateBy()));
	   	}
		if(hotelTravelerPapersTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTravelerPapersTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelTravelerPapersTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelTravelerPapersTypeDao.find(page, dc);
	}
	
	public List<HotelTravelerPapersType> find( HotelTravelerPapersTypeQuery hotelTravelerPapersTypeQuery) {
		DetachedCriteria dc = hotelTravelerPapersTypeDao.createDetachedCriteria();
		
	   	if(hotelTravelerPapersTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTravelerPapersTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelTravelerPapersTypeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getHotelTravelerUuid())){
			dc.add(Restrictions.eq("hotelTravelerUuid", hotelTravelerPapersTypeQuery.getHotelTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", hotelTravelerPapersTypeQuery.getOrderUuid()));
		}
	   	if(hotelTravelerPapersTypeQuery.getPapersType()!=null){
	   		dc.add(Restrictions.eq("papersType", hotelTravelerPapersTypeQuery.getPapersType()));
	   	}
		if(hotelTravelerPapersTypeQuery.getValidityDate()!=null){
			dc.add(Restrictions.eq("validityDate", hotelTravelerPapersTypeQuery.getValidityDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getIdCard())){
			dc.add(Restrictions.eq("idCard", hotelTravelerPapersTypeQuery.getIdCard()));
		}
		if(hotelTravelerPapersTypeQuery.getIssueDate()!=null){
			dc.add(Restrictions.eq("issueDate", hotelTravelerPapersTypeQuery.getIssueDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getIssuePlace())){
			dc.add(Restrictions.eq("issuePlace", hotelTravelerPapersTypeQuery.getIssuePlace()));
		}
	   	if(hotelTravelerPapersTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTravelerPapersTypeQuery.getCreateBy()));
	   	}
		if(hotelTravelerPapersTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTravelerPapersTypeQuery.getCreateDate()));
		}
	   	if(hotelTravelerPapersTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTravelerPapersTypeQuery.getUpdateBy()));
	   	}
		if(hotelTravelerPapersTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTravelerPapersTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerPapersTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelTravelerPapersTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelTravelerPapersTypeDao.find(dc);
	}
	
	public HotelTravelerPapersType getByUuid(String uuid) {
		return hotelTravelerPapersTypeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelTravelerPapersType hotelTravelerPapersType = getByUuid(uuid);
		hotelTravelerPapersType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelTravelerPapersType);
	}
}
