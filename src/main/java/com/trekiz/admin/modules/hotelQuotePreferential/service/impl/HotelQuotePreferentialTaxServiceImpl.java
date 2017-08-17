/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service.impl;

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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialTaxDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialTax;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialTaxInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialTaxQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialTaxService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialTaxServiceImpl  extends BaseService implements HotelQuotePreferentialTaxService{
	@Autowired
	private HotelQuotePreferentialTaxDao hotelQuotePreferentialTaxDao;

	public void save (HotelQuotePreferentialTax hotelQuotePreferentialTax){
		super.setOptInfo(hotelQuotePreferentialTax, BaseService.OPERATION_ADD);
		hotelQuotePreferentialTaxDao.saveObj(hotelQuotePreferentialTax);
	}
	
	public void save (HotelQuotePreferentialTaxInput hotelQuotePreferentialTaxInput){
		HotelQuotePreferentialTax hotelQuotePreferentialTax = hotelQuotePreferentialTaxInput.getHotelQuotePreferentialTax();
		super.setOptInfo(hotelQuotePreferentialTax, BaseService.OPERATION_ADD);
		hotelQuotePreferentialTaxDao.saveObj(hotelQuotePreferentialTax);
	}
	
	public void update (HotelQuotePreferentialTax hotelQuotePreferentialTax){
		super.setOptInfo(hotelQuotePreferentialTax, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialTaxDao.updateObj(hotelQuotePreferentialTax);
	}
	
	public HotelQuotePreferentialTax getById(java.lang.Integer value) {
		return hotelQuotePreferentialTaxDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialTax obj = hotelQuotePreferentialTaxDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialTax> find(Page<HotelQuotePreferentialTax> page, HotelQuotePreferentialTaxQuery hotelQuotePreferentialTaxQuery) {
		DetachedCriteria dc = hotelQuotePreferentialTaxDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialTaxQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialTaxQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialTaxQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialTaxQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialTaxQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialTaxQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialTaxQuery.getHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotelQuotePreferentialTaxQuery.getType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelQuotePreferentialTaxQuery.getTravelerTypeUuid()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getPreferentialType()!=null){
	   		dc.add(Restrictions.eq("preferentialType", hotelQuotePreferentialTaxQuery.getPreferentialType()));
	   	}
	   	if(hotelQuotePreferentialTaxQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelQuotePreferentialTaxQuery.getCurrencyId()));
	   	}
	   	if(hotelQuotePreferentialTaxQuery.getPreferentialAmount()!=null){
	   		dc.add(Restrictions.eq("preferentialAmount", hotelQuotePreferentialTaxQuery.getPreferentialAmount()));
	   	}
	   	if(hotelQuotePreferentialTaxQuery.getChargeType()!=null){
	   		dc.add(Restrictions.eq("chargeType", hotelQuotePreferentialTaxQuery.getChargeType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getIstax())){
			dc.add(Restrictions.eq("istax", hotelQuotePreferentialTaxQuery.getIstax()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelQuotePreferentialTaxQuery.getHotelMealUuids()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getIslandWayUuids())){
			dc.add(Restrictions.eq("islandWayUuids", hotelQuotePreferentialTaxQuery.getIslandWayUuids()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialTaxQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialTaxQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialTaxQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialTaxQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialTaxQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialTaxQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialTaxQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialTaxDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialTax> find( HotelQuotePreferentialTaxQuery hotelQuotePreferentialTaxQuery) {
		DetachedCriteria dc = hotelQuotePreferentialTaxDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialTaxQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialTaxQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialTaxQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialTaxQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialTaxQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialTaxQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialTaxQuery.getHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotelQuotePreferentialTaxQuery.getType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelQuotePreferentialTaxQuery.getTravelerTypeUuid()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getPreferentialType()!=null){
	   		dc.add(Restrictions.eq("preferentialType", hotelQuotePreferentialTaxQuery.getPreferentialType()));
	   	}
	   	if(hotelQuotePreferentialTaxQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelQuotePreferentialTaxQuery.getCurrencyId()));
	   	}
	   	if(hotelQuotePreferentialTaxQuery.getPreferentialAmount()!=null){
	   		dc.add(Restrictions.eq("preferentialAmount", hotelQuotePreferentialTaxQuery.getPreferentialAmount()));
	   	}
	   	if(hotelQuotePreferentialTaxQuery.getChargeType()!=null){
	   		dc.add(Restrictions.eq("chargeType", hotelQuotePreferentialTaxQuery.getChargeType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getIstax())){
			dc.add(Restrictions.eq("istax", hotelQuotePreferentialTaxQuery.getIstax()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelQuotePreferentialTaxQuery.getHotelMealUuids()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getIslandWayUuids())){
			dc.add(Restrictions.eq("islandWayUuids", hotelQuotePreferentialTaxQuery.getIslandWayUuids()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialTaxQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialTaxQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialTaxQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialTaxQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialTaxQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialTaxQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialTaxQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialTaxQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialTaxQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialTaxDao.find(dc);
	}
	
	public HotelQuotePreferentialTax getByUuid(String uuid) {
		return hotelQuotePreferentialTaxDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialTax hotelQuotePreferentialTax = getByUuid(uuid);
		hotelQuotePreferentialTax.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialTax);
	}
}
