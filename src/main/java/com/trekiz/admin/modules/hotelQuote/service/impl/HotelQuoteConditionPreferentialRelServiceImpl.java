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
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteConditionPreferentialRelDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionPreferentialRel;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionPreferentialRelInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionPreferentialRelQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionPreferentialRelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteConditionPreferentialRelServiceImpl  extends BaseService implements HotelQuoteConditionPreferentialRelService{
	@Autowired
	private HotelQuoteConditionPreferentialRelDao hotelQuoteConditionPreferentialRelDao;

	public void save (HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel){
		super.setOptInfo(hotelQuoteConditionPreferentialRel, BaseService.OPERATION_ADD);
		hotelQuoteConditionPreferentialRelDao.saveObj(hotelQuoteConditionPreferentialRel);
	}
	
	public void save (HotelQuoteConditionPreferentialRelInput hotelQuoteConditionPreferentialRelInput){
		HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel = hotelQuoteConditionPreferentialRelInput.getHotelQuoteConditionPreferentialRel();
		super.setOptInfo(hotelQuoteConditionPreferentialRel, BaseService.OPERATION_ADD);
		hotelQuoteConditionPreferentialRelDao.saveObj(hotelQuoteConditionPreferentialRel);
	}
	
	public void update (HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel){
		super.setOptInfo(hotelQuoteConditionPreferentialRel, BaseService.OPERATION_UPDATE);
		hotelQuoteConditionPreferentialRelDao.updateObj(hotelQuoteConditionPreferentialRel);
	}
	
	public HotelQuoteConditionPreferentialRel getById(java.lang.Integer value) {
		return hotelQuoteConditionPreferentialRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteConditionPreferentialRel obj = hotelQuoteConditionPreferentialRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteConditionPreferentialRel> find(Page<HotelQuoteConditionPreferentialRel> page, HotelQuoteConditionPreferentialRelQuery hotelQuoteConditionPreferentialRelQuery) {
		DetachedCriteria dc = hotelQuoteConditionPreferentialRelDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionPreferentialRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionPreferentialRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionPreferentialRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionPreferentialRelQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteConditionPreferentialRelQuery.getHotelQuoteConditionUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelQuoteConditionPreferentialRelQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuoteConditionPreferentialRelQuery.getHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuoteConditionPreferentialRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionPreferentialRelQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionPreferentialRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionPreferentialRelQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionPreferentialRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionPreferentialRelQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionPreferentialRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionPreferentialRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionPreferentialRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionPreferentialRelDao.find(page, dc);
	}
	
	public List<HotelQuoteConditionPreferentialRel> find( HotelQuoteConditionPreferentialRelQuery hotelQuoteConditionPreferentialRelQuery) {
		DetachedCriteria dc = hotelQuoteConditionPreferentialRelDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionPreferentialRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionPreferentialRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionPreferentialRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionPreferentialRelQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteConditionPreferentialRelQuery.getHotelQuoteConditionUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelQuoteConditionPreferentialRelQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuoteConditionPreferentialRelQuery.getHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuoteConditionPreferentialRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionPreferentialRelQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionPreferentialRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionPreferentialRelQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionPreferentialRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionPreferentialRelQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionPreferentialRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionPreferentialRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionPreferentialRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionPreferentialRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionPreferentialRelDao.find(dc);
	}
	
	public HotelQuoteConditionPreferentialRel getByUuid(String uuid) {
		return hotelQuoteConditionPreferentialRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel = getByUuid(uuid);
		hotelQuoteConditionPreferentialRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteConditionPreferentialRel);
	}
}
