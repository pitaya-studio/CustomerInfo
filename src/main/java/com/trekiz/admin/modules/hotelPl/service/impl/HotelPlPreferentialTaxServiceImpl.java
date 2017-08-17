/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.List;
import java.util.Map;

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
public class HotelPlPreferentialTaxServiceImpl  extends BaseService implements HotelPlPreferentialTaxService{
	@Autowired
	private HotelPlPreferentialTaxDao hotelPlPreferentialTaxDao;

	public void save (HotelPlPreferentialTax hotelPlPreferentialTax){
		super.setOptInfo(hotelPlPreferentialTax, BaseService.OPERATION_ADD);
		hotelPlPreferentialTaxDao.saveObj(hotelPlPreferentialTax);
	}
	
	public void save (HotelPlPreferentialTaxInput hotelPlPreferentialTaxInput){
		HotelPlPreferentialTax hotelPlPreferentialTax = hotelPlPreferentialTaxInput.getHotelPlPreferentialTax();
		super.setOptInfo(hotelPlPreferentialTax, BaseService.OPERATION_ADD);
		hotelPlPreferentialTaxDao.saveObj(hotelPlPreferentialTax);
	}
	
	public void update (HotelPlPreferentialTax hotelPlPreferentialTax){
		super.setOptInfo(hotelPlPreferentialTax, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialTaxDao.updateObj(hotelPlPreferentialTax);
	}
	
	public HotelPlPreferentialTax getById(java.lang.Integer value) {
		return hotelPlPreferentialTaxDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialTax obj = hotelPlPreferentialTaxDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialTax> find(Page<HotelPlPreferentialTax> page, HotelPlPreferentialTaxQuery hotelPlPreferentialTaxQuery) {
		DetachedCriteria dc = hotelPlPreferentialTaxDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialTaxQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialTaxQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialTaxQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialTaxQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialTaxQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialTaxQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialTaxQuery.getHotelPlPreferentialUuid()));
		}
	   	if(hotelPlPreferentialTaxQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotelPlPreferentialTaxQuery.getType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlPreferentialTaxQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlPreferentialTaxQuery.getPreferentialType()!=null){
	   		dc.add(Restrictions.eq("preferentialType", hotelPlPreferentialTaxQuery.getPreferentialType()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlPreferentialTaxQuery.getCurrencyId()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getPreferentialAmount()!=null){
	   		dc.add(Restrictions.eq("preferentialAmount", hotelPlPreferentialTaxQuery.getPreferentialAmount()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getChargeType()!=null){
	   		dc.add(Restrictions.eq("chargeType", hotelPlPreferentialTaxQuery.getChargeType()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getIstax()!=null){
	   		dc.add(Restrictions.eq("istax", hotelPlPreferentialTaxQuery.getIstax()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelPlPreferentialTaxQuery.getHotelMealUuids()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getIslandWayUuids())){
			dc.add(Restrictions.eq("islandWayUuids", hotelPlPreferentialTaxQuery.getIslandWayUuids()));
		}
	   	if(hotelPlPreferentialTaxQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialTaxQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialTaxQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialTaxQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialTaxQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialTaxQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialTaxQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialTaxQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialTaxQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialTaxDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialTax> find( HotelPlPreferentialTaxQuery hotelPlPreferentialTaxQuery) {
		DetachedCriteria dc = hotelPlPreferentialTaxDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialTaxQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialTaxQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialTaxQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialTaxQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialTaxQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialTaxQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialTaxQuery.getHotelPlPreferentialUuid()));
		}
	   	if(hotelPlPreferentialTaxQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotelPlPreferentialTaxQuery.getType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlPreferentialTaxQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlPreferentialTaxQuery.getPreferentialType()!=null){
	   		dc.add(Restrictions.eq("preferentialType", hotelPlPreferentialTaxQuery.getPreferentialType()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlPreferentialTaxQuery.getCurrencyId()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getPreferentialAmount()!=null){
	   		dc.add(Restrictions.eq("preferentialAmount", hotelPlPreferentialTaxQuery.getPreferentialAmount()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getChargeType()!=null){
	   		dc.add(Restrictions.eq("chargeType", hotelPlPreferentialTaxQuery.getChargeType()));
	   	}
	   	if(hotelPlPreferentialTaxQuery.getIstax()!=null){
	   		dc.add(Restrictions.eq("istax", hotelPlPreferentialTaxQuery.getIstax()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelPlPreferentialTaxQuery.getHotelMealUuids()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getIslandWayUuids())){
			dc.add(Restrictions.eq("islandWayUuids", hotelPlPreferentialTaxQuery.getIslandWayUuids()));
		}
	   	if(hotelPlPreferentialTaxQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialTaxQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialTaxQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialTaxQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialTaxQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialTaxQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialTaxQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialTaxQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialTaxQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialTaxQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialTaxDao.find(dc);
	}
	
	public HotelPlPreferentialTax getByUuid(String uuid) {
		return hotelPlPreferentialTaxDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialTax hotelPlPreferentialTax = getByUuid(uuid);
		hotelPlPreferentialTax.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialTax);
	}
	
	public Map<String, List<HotelPlPreferentialTax>> getTaxMapByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialTaxDao.getTaxMapByPreferentialUuid(preferentialUuid);
	}
	
	public List<HotelPlPreferentialTax> getTaxsByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialTaxDao.getTaxsByPreferentialUuid(preferentialUuid);
	}
}
