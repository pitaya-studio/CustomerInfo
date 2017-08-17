/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPriceDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPrice;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPriceQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlPriceServiceImpl  extends BaseService implements HotelPlPriceService{
	@Autowired
	private HotelPlPriceDao hotelPlPriceDao;

	public void save (HotelPlPrice hotelPlPrice){
		super.setOptInfo(hotelPlPrice, BaseService.OPERATION_ADD);
		hotelPlPriceDao.saveObj(hotelPlPrice);
	}
	
	public void update (HotelPlPrice hotelPlPrice){
		super.setOptInfo(hotelPlPrice, BaseService.OPERATION_UPDATE);
		hotelPlPriceDao.updateObj(hotelPlPrice);
	}
	
	public HotelPlPrice getById(java.lang.Integer value) {
		return hotelPlPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPrice obj = hotelPlPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPrice> find(Page<HotelPlPrice> page, HotelPlPriceQuery hotelPlPriceQuery) {
		DetachedCriteria dc = hotelPlPriceDao.createDetachedCriteria();
		
	   	if(hotelPlPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPriceQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPriceQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPriceQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelPlPriceQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelPlPriceQuery.getHotelMealUuids()));
		}
		if(hotelPlPriceQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlPriceQuery.getStartDate()));
		}
		if(hotelPlPriceQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlPriceQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelGuestTypeUuid())){
			dc.add(Restrictions.eq("hotelGuestTypeUuid", hotelPlPriceQuery.getHotelGuestTypeUuid()));
		}
	   	if(hotelPlPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlPriceQuery.getCurrencyId()));
	   	}
	   	if(hotelPlPriceQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlPriceQuery.getAmount()));
	   	}
	   	if(hotelPlPriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelPlPriceQuery.getPriceType()));
	   	}
	   	if(hotelPlPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPriceQuery.getCreateBy()));
	   	}
		if(hotelPlPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPriceQuery.getCreateDate()));
		}
	   	if(hotelPlPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPriceQuery.getUpdateBy()));
	   	}
		if(hotelPlPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPriceDao.find(page, dc);
	}
	
	public List<HotelPlPrice> find( HotelPlPriceQuery hotelPlPriceQuery) {
		DetachedCriteria dc = hotelPlPriceDao.createDetachedCriteria();
		
	   	if(hotelPlPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPriceQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPriceQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPriceQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelPlPriceQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelPlPriceQuery.getHotelMealUuids()));
		}
		if(hotelPlPriceQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlPriceQuery.getStartDate()));
		}
		if(hotelPlPriceQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlPriceQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getHotelGuestTypeUuid())){
			dc.add(Restrictions.eq("hotelGuestTypeUuid", hotelPlPriceQuery.getHotelGuestTypeUuid()));
		}
	   	if(hotelPlPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlPriceQuery.getCurrencyId()));
	   	}
	   	if(hotelPlPriceQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlPriceQuery.getAmount()));
	   	}
	   	if(hotelPlPriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelPlPriceQuery.getPriceType()));
	   	}
	   	if(hotelPlPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPriceQuery.getCreateBy()));
	   	}
		if(hotelPlPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPriceQuery.getCreateDate()));
		}
	   	if(hotelPlPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPriceQuery.getUpdateBy()));
	   	}
		if(hotelPlPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPriceDao.find(dc);
	}
	
	public HotelPlPrice getByUuid(String uuid) {
		return hotelPlPriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPrice hotelPlPrice = getByUuid(uuid);
		hotelPlPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPrice);
	}
	
	public Map<String, Map<String, List<HotelPlPrice>>> findPlPricesByHotelPlUuid(String hotelPlUuid) {
		return hotelPlPriceDao.findPlPricesByHotelPlUuid(hotelPlUuid);
	} 
	
	/**
	 * 自动报价 根据条件筛选 符合条件的房费 add by zhanghao
	 * @return
	 */
	public List<HotelPlPrice> getHotelPlPriceQuery4AutoQuotedPrice(HotelPlPriceQuery hotelPlPriceQuery){
		return hotelPlPriceDao.getHotelPlPriceQuery4AutoQuotedPrice(hotelPlPriceQuery);
	}

	@Override
	public List<HotelPlPrice> getPriceList(String hotelPlUuid) {
		// TODO Auto-generated method stub
		return hotelPlPriceDao.getPriceList(hotelPlUuid);
	}
	
}
