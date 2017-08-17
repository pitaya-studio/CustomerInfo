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
public class HotelPlPreferentialRelServiceImpl  extends BaseService implements HotelPlPreferentialRelService{
	@Autowired
	private HotelPlPreferentialRelDao hotelPlPreferentialRelDao;

	public void save (HotelPlPreferentialRel hotelPlPreferentialRel){
		super.setOptInfo(hotelPlPreferentialRel, BaseService.OPERATION_ADD);
		hotelPlPreferentialRelDao.saveObj(hotelPlPreferentialRel);
	}
	
	public void save (HotelPlPreferentialRelInput hotelPlPreferentialRelInput){
		HotelPlPreferentialRel hotelPlPreferentialRel = hotelPlPreferentialRelInput.getHotelPlPreferentialRel();
		super.setOptInfo(hotelPlPreferentialRel, BaseService.OPERATION_ADD);
		hotelPlPreferentialRelDao.saveObj(hotelPlPreferentialRel);
	}
	
	public void update (HotelPlPreferentialRel hotelPlPreferentialRel){
		super.setOptInfo(hotelPlPreferentialRel, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialRelDao.updateObj(hotelPlPreferentialRel);
	}
	
	public HotelPlPreferentialRel getById(java.lang.Integer value) {
		return hotelPlPreferentialRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialRel obj = hotelPlPreferentialRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialRel> find(Page<HotelPlPreferentialRel> page, HotelPlPreferentialRelQuery hotelPlPreferentialRelQuery) {
		DetachedCriteria dc = hotelPlPreferentialRelDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialRelQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRelQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getRelHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("relHotelPlPreferentialUuid", hotelPlPreferentialRelQuery.getRelHotelPlPreferentialUuid()));
		}
	   	if(hotelPlPreferentialRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRelQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRelQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRelQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRelDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialRel> find( HotelPlPreferentialRelQuery hotelPlPreferentialRelQuery) {
		DetachedCriteria dc = hotelPlPreferentialRelDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialRelQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRelQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getRelHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("relHotelPlPreferentialUuid", hotelPlPreferentialRelQuery.getRelHotelPlPreferentialUuid()));
		}
	   	if(hotelPlPreferentialRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRelQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRelQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRelQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRelDao.find(dc);
	}
	
	public HotelPlPreferentialRel getByUuid(String uuid) {
		return hotelPlPreferentialRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialRel hotelPlPreferentialRel = getByUuid(uuid);
		hotelPlPreferentialRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialRel);
	}
	
	public List<HotelPlPreferentialRel> getPreferentialRelsByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialRelDao.getPreferentialRelsByPreferentialUuid(preferentialUuid);
	}
}
