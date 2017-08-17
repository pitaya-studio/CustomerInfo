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
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteConditionDetailDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionDetail;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionDetailInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionDetailQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionDetailService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteConditionDetailServiceImpl  extends BaseService implements HotelQuoteConditionDetailService{
	@Autowired
	private HotelQuoteConditionDetailDao hotelQuoteConditionDetailDao;

	public void save (HotelQuoteConditionDetail hotelQuoteConditionDetail){
		super.setOptInfo(hotelQuoteConditionDetail, BaseService.OPERATION_ADD);
		hotelQuoteConditionDetailDao.saveObj(hotelQuoteConditionDetail);
	}
	
	public void save (HotelQuoteConditionDetailInput hotelQuoteConditionDetailInput){
		HotelQuoteConditionDetail hotelQuoteConditionDetail = hotelQuoteConditionDetailInput.getHotelQuoteConditionDetail();
		super.setOptInfo(hotelQuoteConditionDetail, BaseService.OPERATION_ADD);
		hotelQuoteConditionDetailDao.saveObj(hotelQuoteConditionDetail);
	}
	
	public void update (HotelQuoteConditionDetail hotelQuoteConditionDetail){
		super.setOptInfo(hotelQuoteConditionDetail, BaseService.OPERATION_UPDATE);
		hotelQuoteConditionDetailDao.updateObj(hotelQuoteConditionDetail);
	}
	
	public HotelQuoteConditionDetail getById(java.lang.Integer value) {
		return hotelQuoteConditionDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteConditionDetail obj = hotelQuoteConditionDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteConditionDetail> find(Page<HotelQuoteConditionDetail> page, HotelQuoteConditionDetailQuery hotelQuoteConditionDetailQuery) {
		DetachedCriteria dc = hotelQuoteConditionDetailDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionDetailQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteConditionDetailQuery.getHotelQuoteConditionUuid()));
		}
		if(hotelQuoteConditionDetailQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelQuoteConditionDetailQuery.getInDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelQuoteConditionDetailQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelQuoteConditionDetailQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelMealRiseUuid())){
			dc.add(Restrictions.eq("hotelMealRiseUuid", hotelQuoteConditionDetailQuery.getHotelMealRiseUuid()));
		}
	   	if(hotelQuoteConditionDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionDetailQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionDetailQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionDetailQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionDetailDao.find(page, dc);
	}
	
	public List<HotelQuoteConditionDetail> find( HotelQuoteConditionDetailQuery hotelQuoteConditionDetailQuery) {
		DetachedCriteria dc = hotelQuoteConditionDetailDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionDetailQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteConditionDetailQuery.getHotelQuoteConditionUuid()));
		}
		if(hotelQuoteConditionDetailQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelQuoteConditionDetailQuery.getInDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelQuoteConditionDetailQuery.getHotelRoomUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelQuoteConditionDetailQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getHotelMealRiseUuid())){
			dc.add(Restrictions.eq("hotelMealRiseUuid", hotelQuoteConditionDetailQuery.getHotelMealRiseUuid()));
		}
	   	if(hotelQuoteConditionDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionDetailQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionDetailQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionDetailQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionDetailDao.find(dc);
	}
	
	public HotelQuoteConditionDetail getByUuid(String uuid) {
		return hotelQuoteConditionDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteConditionDetail hotelQuoteConditionDetail = getByUuid(uuid);
		hotelQuoteConditionDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteConditionDetail);
	}
}
