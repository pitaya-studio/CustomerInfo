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
public class HotelPlTaxPriceServiceImpl  extends BaseService implements HotelPlTaxPriceService{
	@Autowired
	private HotelPlTaxPriceDao hotelPlTaxPriceDao;

	public void save (HotelPlTaxPrice hotelPlTaxPrice){
		super.setOptInfo(hotelPlTaxPrice, BaseService.OPERATION_ADD);
		hotelPlTaxPriceDao.saveObj(hotelPlTaxPrice);
	}
	
	public void save (HotelPlTaxPriceInput hotelPlTaxPriceInput){
		HotelPlTaxPrice hotelPlTaxPrice = hotelPlTaxPriceInput.getHotelPlTaxPrice();
		super.setOptInfo(hotelPlTaxPrice, BaseService.OPERATION_ADD);
		hotelPlTaxPriceDao.saveObj(hotelPlTaxPrice);
	}
	
	public void update (HotelPlTaxPrice hotelPlTaxPrice){
		super.setOptInfo(hotelPlTaxPrice, BaseService.OPERATION_UPDATE);
		hotelPlTaxPriceDao.updateObj(hotelPlTaxPrice);
	}
	
	public HotelPlTaxPrice getById(java.lang.Integer value) {
		return hotelPlTaxPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlTaxPrice obj = hotelPlTaxPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlTaxPrice> find(Page<HotelPlTaxPrice> page, HotelPlTaxPriceQuery hotelPlTaxPriceQuery) {
		DetachedCriteria dc = hotelPlTaxPriceDao.createDetachedCriteria();
		
	   	if(hotelPlTaxPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlTaxPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlTaxPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlTaxPriceQuery.getHotelPlUuid()));
		}
	   	if(hotelPlTaxPriceQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelPlTaxPriceQuery.getWholesalerId()));
	   	}
	   	if(hotelPlTaxPriceQuery.getSupplierInfoId()!=null){
	   		dc.add(Restrictions.eq("supplierInfoId", hotelPlTaxPriceQuery.getSupplierInfoId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlTaxPriceQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlTaxPriceQuery.getHotelUuid()));
		}
	   	if(hotelPlTaxPriceQuery.getTaxType()!=null){
	   		dc.add(Restrictions.eq("taxType", hotelPlTaxPriceQuery.getTaxType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getTaxName())){
			dc.add(Restrictions.eq("taxName", hotelPlTaxPriceQuery.getTaxName()));
		}
		if(hotelPlTaxPriceQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlTaxPriceQuery.getStartDate()));
		}
		if(hotelPlTaxPriceQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlTaxPriceQuery.getEndDate()));
		}
	   	if(hotelPlTaxPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlTaxPriceQuery.getCurrencyId()));
	   	}
	   	if(hotelPlTaxPriceQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlTaxPriceQuery.getAmount()));
	   	}
	   	if(hotelPlTaxPriceQuery.getChargeType()!=null){
	   		dc.add(Restrictions.eq("chargeType", hotelPlTaxPriceQuery.getChargeType()));
	   	}
	   	if(hotelPlTaxPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlTaxPriceQuery.getCreateBy()));
	   	}
		if(hotelPlTaxPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlTaxPriceQuery.getCreateDate()));
		}
	   	if(hotelPlTaxPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlTaxPriceQuery.getUpdateBy()));
	   	}
		if(hotelPlTaxPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlTaxPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlTaxPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlTaxPriceDao.find(page, dc);
	}
	
	public List<HotelPlTaxPrice> find( HotelPlTaxPriceQuery hotelPlTaxPriceQuery) {
		DetachedCriteria dc = hotelPlTaxPriceDao.createDetachedCriteria();
		
	   	if(hotelPlTaxPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlTaxPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlTaxPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlTaxPriceQuery.getHotelPlUuid()));
		}
	   	if(hotelPlTaxPriceQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelPlTaxPriceQuery.getWholesalerId()));
	   	}
	   	if(hotelPlTaxPriceQuery.getSupplierInfoId()!=null){
	   		dc.add(Restrictions.eq("supplierInfoId", hotelPlTaxPriceQuery.getSupplierInfoId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlTaxPriceQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlTaxPriceQuery.getHotelUuid()));
		}
	   	if(hotelPlTaxPriceQuery.getTaxType()!=null){
	   		dc.add(Restrictions.eq("taxType", hotelPlTaxPriceQuery.getTaxType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getTaxName())){
			dc.add(Restrictions.eq("taxName", hotelPlTaxPriceQuery.getTaxName()));
		}
		if(hotelPlTaxPriceQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlTaxPriceQuery.getStartDate()));
		}
		if(hotelPlTaxPriceQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlTaxPriceQuery.getEndDate()));
		}
	   	if(hotelPlTaxPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlTaxPriceQuery.getCurrencyId()));
	   	}
	   	if(hotelPlTaxPriceQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlTaxPriceQuery.getAmount()));
	   	}
	   	if(hotelPlTaxPriceQuery.getChargeType()!=null){
	   		dc.add(Restrictions.eq("chargeType", hotelPlTaxPriceQuery.getChargeType()));
	   	}
	   	if(hotelPlTaxPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlTaxPriceQuery.getCreateBy()));
	   	}
		if(hotelPlTaxPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlTaxPriceQuery.getCreateDate()));
		}
	   	if(hotelPlTaxPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlTaxPriceQuery.getUpdateBy()));
	   	}
		if(hotelPlTaxPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlTaxPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlTaxPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlTaxPriceDao.find(dc);
	}
	
	public HotelPlTaxPrice getByUuid(String uuid) {
		return hotelPlTaxPriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlTaxPrice hotelPlTaxPrice = getByUuid(uuid);
		hotelPlTaxPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlTaxPrice);
	}
	
	public List<HotelPlTaxPrice> findHotelPlTaxPricesByHotelPlUuid(String hotelPlUuid) {
		return hotelPlTaxPriceDao.findHotelPlTaxPricesByHotelPlUuid(hotelPlUuid);
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的税金 add by zhanghao
	 * @return
	 */
	public List<HotelPlTaxPrice> getHotelPlTaxPrice4AutoQuotedPrice( HotelPlTaxPriceQuery hotelPlTaxPriceQuery){
		return hotelPlTaxPriceDao.getHotelPlTaxPrice4AutoQuotedPrice(hotelPlTaxPriceQuery);
	}
}
