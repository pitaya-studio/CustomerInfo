/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service.impl;

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
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteResultDetailPriceDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetailPrice;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteResultDetailPriceInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteResultDetailPriceQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultDetailPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteResultDetailPriceServiceImpl  extends BaseService implements HotelQuoteResultDetailPriceService{
	@Autowired
	private HotelQuoteResultDetailPriceDao hotelQuoteResultDetailPriceDao;

	public void save (HotelQuoteResultDetailPrice hotelQuoteResultDetailPrice){
		super.setOptInfo(hotelQuoteResultDetailPrice, BaseService.OPERATION_ADD);
		hotelQuoteResultDetailPriceDao.saveObj(hotelQuoteResultDetailPrice);
	}
	
	public void save (HotelQuoteResultDetailPriceInput hotelQuoteResultDetailPriceInput){
		HotelQuoteResultDetailPrice hotelQuoteResultDetailPrice = hotelQuoteResultDetailPriceInput.getHotelQuoteResultDetailPrice();
		super.setOptInfo(hotelQuoteResultDetailPrice, BaseService.OPERATION_ADD);
		hotelQuoteResultDetailPriceDao.saveObj(hotelQuoteResultDetailPrice);
	}
	
	public void update (HotelQuoteResultDetailPrice hotelQuoteResultDetailPrice){
		super.setOptInfo(hotelQuoteResultDetailPrice, BaseService.OPERATION_UPDATE);
		hotelQuoteResultDetailPriceDao.updateObj(hotelQuoteResultDetailPrice);
	}
	
	public HotelQuoteResultDetailPrice getById(java.lang.Integer value) {
		return hotelQuoteResultDetailPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteResultDetailPrice obj = hotelQuoteResultDetailPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteResultDetailPrice> find(Page<HotelQuoteResultDetailPrice> page, HotelQuoteResultDetailPriceQuery hotelQuoteResultDetailPriceQuery) {
		DetachedCriteria dc = hotelQuoteResultDetailPriceDao.createDetachedCriteria();
		
	   	if(hotelQuoteResultDetailPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteResultDetailPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteResultDetailPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteResultDetailPriceQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelQuoteResultUuid())){
			dc.add(Restrictions.eq("hotelQuoteResultUuid", hotelQuoteResultDetailPriceQuery.getHotelQuoteResultUuid()));
		}
		if(hotelQuoteResultDetailPriceQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelQuoteResultDetailPriceQuery.getInDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelQuoteResultDetailPriceQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelQuoteResultDetailPriceQuery.getHotelMealUuid()));
		}
	   	if(hotelQuoteResultDetailPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteResultDetailPriceQuery.getCreateBy()));
	   	}
		if(hotelQuoteResultDetailPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteResultDetailPriceQuery.getCreateDate()));
		}
	   	if(hotelQuoteResultDetailPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteResultDetailPriceQuery.getUpdateBy()));
	   	}
		if(hotelQuoteResultDetailPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteResultDetailPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteResultDetailPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteResultDetailPriceDao.find(page, dc);
	}
	
	public List<HotelQuoteResultDetailPrice> find( HotelQuoteResultDetailPriceQuery hotelQuoteResultDetailPriceQuery) {
		DetachedCriteria dc = hotelQuoteResultDetailPriceDao.createDetachedCriteria();
		
	   	if(hotelQuoteResultDetailPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteResultDetailPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteResultDetailPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteResultDetailPriceQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelQuoteResultUuid())){
			dc.add(Restrictions.eq("hotelQuoteResultUuid", hotelQuoteResultDetailPriceQuery.getHotelQuoteResultUuid()));
		}
		if(hotelQuoteResultDetailPriceQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelQuoteResultDetailPriceQuery.getInDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelQuoteResultDetailPriceQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelQuoteResultDetailPriceQuery.getHotelMealUuid()));
		}
	   	if(hotelQuoteResultDetailPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteResultDetailPriceQuery.getCreateBy()));
	   	}
		if(hotelQuoteResultDetailPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteResultDetailPriceQuery.getCreateDate()));
		}
	   	if(hotelQuoteResultDetailPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteResultDetailPriceQuery.getUpdateBy()));
	   	}
		if(hotelQuoteResultDetailPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteResultDetailPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteResultDetailPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteResultDetailPriceDao.find(dc);
	}
	
	public HotelQuoteResultDetailPrice getByUuid(String uuid) {
		return hotelQuoteResultDetailPriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteResultDetailPrice hotelQuoteResultDetailPrice = getByUuid(uuid);
		hotelQuoteResultDetailPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteResultDetailPrice);
	}
}
