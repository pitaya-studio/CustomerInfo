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
public class HotelPlPreferentialRequireServiceImpl  extends BaseService implements HotelPlPreferentialRequireService{
	@Autowired
	private HotelPlPreferentialRequireDao hotelPlPreferentialRequireDao;

	public void save (HotelPlPreferentialRequire hotelPlPreferentialRequire){
		super.setOptInfo(hotelPlPreferentialRequire, BaseService.OPERATION_ADD);
		hotelPlPreferentialRequireDao.saveObj(hotelPlPreferentialRequire);
	}
	
	public void save (HotelPlPreferentialRequireInput hotelPlPreferentialRequireInput){
		HotelPlPreferentialRequire hotelPlPreferentialRequire = hotelPlPreferentialRequireInput.getHotelPlPreferentialRequire();
		super.setOptInfo(hotelPlPreferentialRequire, BaseService.OPERATION_ADD);
		hotelPlPreferentialRequireDao.saveObj(hotelPlPreferentialRequire);
	}
	
	public void update (HotelPlPreferentialRequire hotelPlPreferentialRequire){
		super.setOptInfo(hotelPlPreferentialRequire, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialRequireDao.updateObj(hotelPlPreferentialRequire);
	}
	
	public HotelPlPreferentialRequire getById(java.lang.Integer value) {
		return hotelPlPreferentialRequireDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialRequire obj = hotelPlPreferentialRequireDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialRequire> find(Page<HotelPlPreferentialRequire> page, HotelPlPreferentialRequireQuery hotelPlPreferentialRequireQuery) {
		DetachedCriteria dc = hotelPlPreferentialRequireDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRequireQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRequireQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRequireQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRequireQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialRequireQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRequireQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRequireQuery.getHotelPlPreferentialUuid()));
		}
	   	if(hotelPlPreferentialRequireQuery.getBookingNights()!=null){
	   		dc.add(Restrictions.eq("bookingNights", hotelPlPreferentialRequireQuery.getBookingNights()));
	   	}
	   	if(hotelPlPreferentialRequireQuery.getBookingNumbers()!=null){
	   		dc.add(Restrictions.eq("bookingNumbers", hotelPlPreferentialRequireQuery.getBookingNumbers()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getNotApplicableDate())){
			dc.add(Restrictions.eq("notApplicableDate", hotelPlPreferentialRequireQuery.getNotApplicableDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getNotApplicableRoom())){
			dc.add(Restrictions.eq("notApplicableRoom", hotelPlPreferentialRequireQuery.getNotApplicableRoom()));
		}
	   	if(hotelPlPreferentialRequireQuery.getApplicableThirdPerson()!=null){
	   		dc.add(Restrictions.eq("applicableThirdPerson", hotelPlPreferentialRequireQuery.getApplicableThirdPerson()));
	   	}
	   	if(hotelPlPreferentialRequireQuery.getIsSuperposition()!=null){
	   		dc.add(Restrictions.eq("isSuperposition", hotelPlPreferentialRequireQuery.getIsSuperposition()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlPreferentialRequireQuery.getMemo()));
		}
	   	if(hotelPlPreferentialRequireQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRequireQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRequireQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRequireQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRequireQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRequireQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRequireQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRequireQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRequireQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRequireDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialRequire> find( HotelPlPreferentialRequireQuery hotelPlPreferentialRequireQuery) {
		DetachedCriteria dc = hotelPlPreferentialRequireDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRequireQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRequireQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRequireQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRequireQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialRequireQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRequireQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRequireQuery.getHotelPlPreferentialUuid()));
		}
	   	if(hotelPlPreferentialRequireQuery.getBookingNights()!=null){
	   		dc.add(Restrictions.eq("bookingNights", hotelPlPreferentialRequireQuery.getBookingNights()));
	   	}
	   	if(hotelPlPreferentialRequireQuery.getBookingNumbers()!=null){
	   		dc.add(Restrictions.eq("bookingNumbers", hotelPlPreferentialRequireQuery.getBookingNumbers()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getNotApplicableDate())){
			dc.add(Restrictions.eq("notApplicableDate", hotelPlPreferentialRequireQuery.getNotApplicableDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getNotApplicableRoom())){
			dc.add(Restrictions.eq("notApplicableRoom", hotelPlPreferentialRequireQuery.getNotApplicableRoom()));
		}
	   	if(hotelPlPreferentialRequireQuery.getApplicableThirdPerson()!=null){
	   		dc.add(Restrictions.eq("applicableThirdPerson", hotelPlPreferentialRequireQuery.getApplicableThirdPerson()));
	   	}
	   	if(hotelPlPreferentialRequireQuery.getIsSuperposition()!=null){
	   		dc.add(Restrictions.eq("isSuperposition", hotelPlPreferentialRequireQuery.getIsSuperposition()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlPreferentialRequireQuery.getMemo()));
		}
	   	if(hotelPlPreferentialRequireQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRequireQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRequireQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRequireQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRequireQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRequireQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRequireQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRequireQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRequireQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRequireQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRequireDao.find(dc);
	}
	
	public HotelPlPreferentialRequire getByUuid(String uuid) {
		return hotelPlPreferentialRequireDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialRequire hotelPlPreferentialRequire = getByUuid(uuid);
		hotelPlPreferentialRequire.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialRequire);
	}
	
	public HotelPlPreferentialRequire findRequireByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialRequireDao.findRequireByPreferentialUuid(preferentialUuid);
	}
}
