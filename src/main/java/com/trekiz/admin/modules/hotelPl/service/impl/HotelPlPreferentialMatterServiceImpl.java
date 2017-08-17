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
public class HotelPlPreferentialMatterServiceImpl  extends BaseService implements HotelPlPreferentialMatterService{
	@Autowired
	private HotelPlPreferentialMatterDao hotelPlPreferentialMatterDao;

	public void save (HotelPlPreferentialMatter hotelPlPreferentialMatter){
		super.setOptInfo(hotelPlPreferentialMatter, BaseService.OPERATION_ADD);
		hotelPlPreferentialMatterDao.saveObj(hotelPlPreferentialMatter);
	}
	
	public void save (HotelPlPreferentialMatterInput hotelPlPreferentialMatterInput){
		HotelPlPreferentialMatter hotelPlPreferentialMatter = hotelPlPreferentialMatterInput.getHotelPlPreferentialMatter();
		super.setOptInfo(hotelPlPreferentialMatter, BaseService.OPERATION_ADD);
		hotelPlPreferentialMatterDao.saveObj(hotelPlPreferentialMatter);
	}
	
	public void update (HotelPlPreferentialMatter hotelPlPreferentialMatter){
		super.setOptInfo(hotelPlPreferentialMatter, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialMatterDao.updateObj(hotelPlPreferentialMatter);
	}
	
	public HotelPlPreferentialMatter getById(java.lang.Integer value) {
		return hotelPlPreferentialMatterDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialMatter obj = hotelPlPreferentialMatterDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialMatter> find(Page<HotelPlPreferentialMatter> page, HotelPlPreferentialMatterQuery hotelPlPreferentialMatterQuery) {
		DetachedCriteria dc = hotelPlPreferentialMatterDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialMatterQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialMatterQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialMatterQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialMatterQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialMatterQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialMatterQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialMatterQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getPreferentialTemplatesUuid())){
			dc.add(Restrictions.eq("preferentialTemplatesUuid", hotelPlPreferentialMatterQuery.getPreferentialTemplatesUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlPreferentialMatterQuery.getMemo()));
		}
	   	if(hotelPlPreferentialMatterQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialMatterQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialMatterQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialMatterQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialMatterQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialMatterQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialMatterQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialMatterQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialMatterQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialMatterDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialMatter> find( HotelPlPreferentialMatterQuery hotelPlPreferentialMatterQuery) {
		DetachedCriteria dc = hotelPlPreferentialMatterDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialMatterQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialMatterQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialMatterQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialMatterQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialMatterQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialMatterQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialMatterQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getPreferentialTemplatesUuid())){
			dc.add(Restrictions.eq("preferentialTemplatesUuid", hotelPlPreferentialMatterQuery.getPreferentialTemplatesUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlPreferentialMatterQuery.getMemo()));
		}
	   	if(hotelPlPreferentialMatterQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialMatterQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialMatterQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialMatterQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialMatterQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialMatterQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialMatterQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialMatterQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialMatterQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialMatterDao.find(dc);
	}
	
	public HotelPlPreferentialMatter getByUuid(String uuid) {
		return hotelPlPreferentialMatterDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialMatter hotelPlPreferentialMatter = getByUuid(uuid);
		hotelPlPreferentialMatter.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialMatter);
	}
	
	public HotelPlPreferentialMatter findMatterByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialMatterDao.findMatterByPreferentialUuid(preferentialUuid);
	}
}
