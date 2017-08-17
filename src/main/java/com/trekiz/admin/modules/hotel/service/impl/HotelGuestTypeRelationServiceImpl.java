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
import com.trekiz.admin.modules.hotel.dao.HotelGuestTypeRelationDao;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;
import com.trekiz.admin.modules.hotel.input.HotelGuestTypeRelationInput;
import com.trekiz.admin.modules.hotel.query.HotelGuestTypeRelationQuery;
import com.trekiz.admin.modules.hotel.service.HotelGuestTypeRelationService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelGuestTypeRelationServiceImpl  extends BaseService implements HotelGuestTypeRelationService{
	@Autowired
	private HotelGuestTypeRelationDao hotelGuestTypeRelationDao;

	public void save (HotelGuestTypeRelation hotelGuestTypeRelation){
		super.setOptInfo(hotelGuestTypeRelation, BaseService.OPERATION_ADD);
		hotelGuestTypeRelationDao.saveObj(hotelGuestTypeRelation);
	}
	
	public void save (HotelGuestTypeRelationInput hotelGuestTypeRelationInput){
		HotelGuestTypeRelation hotelGuestTypeRelation = hotelGuestTypeRelationInput.getHotelGuestTypeRelation();
		super.setOptInfo(hotelGuestTypeRelation, BaseService.OPERATION_ADD);
		hotelGuestTypeRelationDao.saveObj(hotelGuestTypeRelation);
	}
	
	public void update (HotelGuestTypeRelation hotelGuestTypeRelation){
		super.setOptInfo(hotelGuestTypeRelation, BaseService.OPERATION_UPDATE);
		hotelGuestTypeRelationDao.updateObj(hotelGuestTypeRelation);
	}
	
	public HotelGuestTypeRelation getById(java.lang.Integer value) {
		return hotelGuestTypeRelationDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelGuestTypeRelation obj = hotelGuestTypeRelationDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelGuestTypeRelation> find(Page<HotelGuestTypeRelation> page, HotelGuestTypeRelationQuery hotelGuestTypeRelationQuery) {
		DetachedCriteria dc = hotelGuestTypeRelationDao.createDetachedCriteria();
		
	   	if(hotelGuestTypeRelationQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelGuestTypeRelationQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelGuestTypeRelationQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelGuestTypeUuid())){
			dc.add(Restrictions.eq("hotelGuestTypeUuid", hotelGuestTypeRelationQuery.getHotelGuestTypeUuid()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelGuestTypeName())){
			dc.add(Restrictions.eq("hotelGuestTypeName", hotelGuestTypeRelationQuery.getHotelGuestTypeName()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelGuestTypeRelationQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelGuestTypeRelationQuery.getHotelRoomUuid()));
		}
	   	if(hotelGuestTypeRelationQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelGuestTypeRelationQuery.getCreateBy()));
	   	}
		if(hotelGuestTypeRelationQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelGuestTypeRelationQuery.getCreateDate()));
		}
	   	if(hotelGuestTypeRelationQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelGuestTypeRelationQuery.getUpdateBy()));
	   	}
		if(hotelGuestTypeRelationQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelGuestTypeRelationQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelGuestTypeRelationQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelGuestTypeRelationDao.find(page, dc);
	}
	
	public List<HotelGuestTypeRelation> find( HotelGuestTypeRelationQuery hotelGuestTypeRelationQuery) {
		DetachedCriteria dc = hotelGuestTypeRelationDao.createDetachedCriteria();
		
	   	if(hotelGuestTypeRelationQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelGuestTypeRelationQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelGuestTypeRelationQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelGuestTypeUuid())){
			dc.add(Restrictions.eq("hotelGuestTypeUuid", hotelGuestTypeRelationQuery.getHotelGuestTypeUuid()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelGuestTypeName())){
			dc.add(Restrictions.eq("hotelGuestTypeName", hotelGuestTypeRelationQuery.getHotelGuestTypeName()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelGuestTypeRelationQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelGuestTypeRelationQuery.getHotelRoomUuid()));
		}
	   	if(hotelGuestTypeRelationQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelGuestTypeRelationQuery.getCreateBy()));
	   	}
		if(hotelGuestTypeRelationQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelGuestTypeRelationQuery.getCreateDate()));
		}
	   	if(hotelGuestTypeRelationQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelGuestTypeRelationQuery.getUpdateBy()));
	   	}
		if(hotelGuestTypeRelationQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelGuestTypeRelationQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelGuestTypeRelationQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelGuestTypeRelationQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelGuestTypeRelationDao.find(dc);
	}
	
	public HotelGuestTypeRelation getByUuid(String uuid) {
		return hotelGuestTypeRelationDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelGuestTypeRelation hotelGuestTypeRelation = getByUuid(uuid);
		hotelGuestTypeRelation.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelGuestTypeRelation);
	}
}
