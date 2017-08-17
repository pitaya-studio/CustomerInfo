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
import com.trekiz.admin.modules.hotel.dao.HotelOrderPriceDao;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.input.HotelOrderPriceInput;
import com.trekiz.admin.modules.hotel.query.HotelOrderPriceQuery;
import com.trekiz.admin.modules.hotel.service.HotelOrderPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelOrderPriceServiceImpl  extends BaseService implements HotelOrderPriceService{
	@Autowired
	private HotelOrderPriceDao hotelOrderPriceDao;

	public void save (HotelOrderPrice hotelOrderPrice){
		super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_ADD);
		hotelOrderPriceDao.saveObj(hotelOrderPrice);
	}
	
	public void save (HotelOrderPriceInput hotelOrderPriceInput){
		HotelOrderPrice hotelOrderPrice = hotelOrderPriceInput.getHotelOrderPrice();
		super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_ADD);
		hotelOrderPriceDao.saveObj(hotelOrderPrice);
	}
	
	public void update (HotelOrderPrice hotelOrderPrice){
		super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_UPDATE);
		hotelOrderPriceDao.updateObj(hotelOrderPrice);
	}
	
	public HotelOrderPrice getById(java.lang.Integer value) {
		return hotelOrderPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelOrderPrice obj = hotelOrderPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelOrderPrice> find(Page<HotelOrderPrice> page, HotelOrderPriceQuery hotelOrderPriceQuery) {
		DetachedCriteria dc = hotelOrderPriceDao.createDetachedCriteria();
		
	   	if(hotelOrderPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelOrderPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelOrderPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", hotelOrderPriceQuery.getOrderUuid()));
		}
	   	if(hotelOrderPriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelOrderPriceQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getPriceName())){
			dc.add(Restrictions.eq("priceName", hotelOrderPriceQuery.getPriceName()));
		}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getActivityHotelGroupPriceUuid())){
			dc.add(Restrictions.eq("activityHotelGroupPriceUuid", hotelOrderPriceQuery.getActivityHotelGroupPriceUuid()));
		}
	   	if(hotelOrderPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelOrderPriceQuery.getCurrencyId()));
	   	}
	   	if(hotelOrderPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelOrderPriceQuery.getPrice()));
	   	}
	   	if(hotelOrderPriceQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", hotelOrderPriceQuery.getNum()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getRemark())){
			dc.add(Restrictions.eq("remark", hotelOrderPriceQuery.getRemark()));
		}
	   	if(hotelOrderPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelOrderPriceQuery.getCreateBy()));
	   	}
		if(hotelOrderPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelOrderPriceQuery.getCreateDate()));
		}
	   	if(hotelOrderPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelOrderPriceQuery.getUpdateBy()));
	   	}
		if(hotelOrderPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelOrderPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelOrderPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelOrderPriceDao.find(page, dc);
	}
	
	public List<HotelOrderPrice> find( HotelOrderPriceQuery hotelOrderPriceQuery) {
		DetachedCriteria dc = hotelOrderPriceDao.createDetachedCriteria();
		
	   	if(hotelOrderPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelOrderPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelOrderPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", hotelOrderPriceQuery.getOrderUuid()));
		}
	   	if(hotelOrderPriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelOrderPriceQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getPriceName())){
			dc.add(Restrictions.eq("priceName", hotelOrderPriceQuery.getPriceName()));
		}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getActivityHotelGroupPriceUuid())){
			dc.add(Restrictions.eq("activityHotelGroupPriceUuid", hotelOrderPriceQuery.getActivityHotelGroupPriceUuid()));
		}
	   	if(hotelOrderPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelOrderPriceQuery.getCurrencyId()));
	   	}
	   	if(hotelOrderPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelOrderPriceQuery.getPrice()));
	   	}
	   	if(hotelOrderPriceQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", hotelOrderPriceQuery.getNum()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getRemark())){
			dc.add(Restrictions.eq("remark", hotelOrderPriceQuery.getRemark()));
		}
	   	if(hotelOrderPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelOrderPriceQuery.getCreateBy()));
	   	}
		if(hotelOrderPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelOrderPriceQuery.getCreateDate()));
		}
	   	if(hotelOrderPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelOrderPriceQuery.getUpdateBy()));
	   	}
		if(hotelOrderPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelOrderPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelOrderPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelOrderPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelOrderPriceDao.find(dc);
	}
	
	public HotelOrderPrice getByUuid(String uuid) {
		return hotelOrderPriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelOrderPrice hotelOrderPrice = getByUuid(uuid);
		hotelOrderPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelOrderPrice);
	}
	
	public HotelOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid, String groupPriceUuid) {
		return hotelOrderPriceDao.getByOrderUuidAndGroupPriceUuid(orderUuid, groupPriceUuid);
	}
	
	public List<HotelOrderPrice> getByOrderUuid(String orderUuid){
		return hotelOrderPriceDao.getByOrderUuid(orderUuid);
	}
	
}
