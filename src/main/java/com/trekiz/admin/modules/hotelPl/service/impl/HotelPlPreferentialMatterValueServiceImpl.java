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
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.input.HotelPlPreferentialMatterValueInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialMatterValueQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPreferentialMatterValueService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialMatterValueServiceImpl  extends BaseService implements HotelPlPreferentialMatterValueService{
	@Autowired
	private HotelPlPreferentialMatterValueDao hotelPlPreferentialMatterValueDao;

	public void save (HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue){
		super.setOptInfo(hotelPlPreferentialMatterValue, BaseService.OPERATION_ADD);
		hotelPlPreferentialMatterValueDao.saveObj(hotelPlPreferentialMatterValue);
	}
	
	public void save (HotelPlPreferentialMatterValueInput hotelPlPreferentialMatterValueInput){
		HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue = hotelPlPreferentialMatterValueInput.getHotelPlPreferentialMatterValue();
		super.setOptInfo(hotelPlPreferentialMatterValue, BaseService.OPERATION_ADD);
		hotelPlPreferentialMatterValueDao.saveObj(hotelPlPreferentialMatterValue);
	}
	
	public void update (HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue){
		super.setOptInfo(hotelPlPreferentialMatterValue, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialMatterValueDao.updateObj(hotelPlPreferentialMatterValue);
	}
	
	public HotelPlPreferentialMatterValue getById(java.lang.Integer value) {
		return hotelPlPreferentialMatterValueDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialMatterValue obj = hotelPlPreferentialMatterValueDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialMatterValue> find(Page<HotelPlPreferentialMatterValue> page, HotelPlPreferentialMatterValueQuery hotelPlPreferentialMatterValueQuery) {
		DetachedCriteria dc = hotelPlPreferentialMatterValueDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialMatterValueQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialMatterValueQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialMatterValueQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialMatterValueQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialMatterValueQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialMatterValueQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialMatterUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialMatterUuid", hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialMatterUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getMyKey())){
			dc.add(Restrictions.eq("myKey", hotelPlPreferentialMatterValueQuery.getMyKey()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getMyValue())){
			dc.add(Restrictions.eq("myValue", hotelPlPreferentialMatterValueQuery.getMyValue()));
		}
	   	if(hotelPlPreferentialMatterValueQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialMatterValueQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialMatterValueQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialMatterValueQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialMatterValueQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialMatterValueQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialMatterValueQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialMatterValueQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialMatterValueQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialMatterValueDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialMatterValue> find( HotelPlPreferentialMatterValueQuery hotelPlPreferentialMatterValueQuery) {
		DetachedCriteria dc = hotelPlPreferentialMatterValueDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialMatterValueQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialMatterValueQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialMatterValueQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialMatterValueQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialMatterValueQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialMatterValueQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialMatterUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialMatterUuid", hotelPlPreferentialMatterValueQuery.getHotelPlPreferentialMatterUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getMyKey())){
			dc.add(Restrictions.eq("myKey", hotelPlPreferentialMatterValueQuery.getMyKey()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getMyValue())){
			dc.add(Restrictions.eq("myValue", hotelPlPreferentialMatterValueQuery.getMyValue()));
		}
	   	if(hotelPlPreferentialMatterValueQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialMatterValueQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialMatterValueQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialMatterValueQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialMatterValueQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialMatterValueQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialMatterValueQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialMatterValueQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialMatterValueQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialMatterValueQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialMatterValueDao.find(dc);
	}
	
	public HotelPlPreferentialMatterValue getByUuid(String uuid) {
		return hotelPlPreferentialMatterValueDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue = getByUuid(uuid);
		hotelPlPreferentialMatterValue.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialMatterValue);
	}
}
