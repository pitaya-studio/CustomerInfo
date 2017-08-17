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
import com.trekiz.admin.modules.hotelPl.dao.HotelPlTaxExceptionDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.input.HotelPlTaxExceptionInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxExceptionQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlTaxExceptionService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlTaxExceptionServiceImpl  extends BaseService implements HotelPlTaxExceptionService{
	@Autowired
	private HotelPlTaxExceptionDao hotelPlTaxExceptionDao;

	public void save (HotelPlTaxException hotelPlTaxException){
		super.setOptInfo(hotelPlTaxException, BaseService.OPERATION_ADD);
		hotelPlTaxExceptionDao.saveObj(hotelPlTaxException);
	}
	
	public void save (HotelPlTaxExceptionInput hotelPlTaxExceptionInput){
		HotelPlTaxException hotelPlTaxException = hotelPlTaxExceptionInput.getHotelPlTaxException();
		super.setOptInfo(hotelPlTaxException, BaseService.OPERATION_ADD);
		hotelPlTaxExceptionDao.saveObj(hotelPlTaxException);
	}
	
	public void update (HotelPlTaxException hotelPlTaxException){
		super.setOptInfo(hotelPlTaxException, BaseService.OPERATION_UPDATE);
		hotelPlTaxExceptionDao.updateObj(hotelPlTaxException);
	}
	
	public HotelPlTaxException getById(java.lang.Integer value) {
		return hotelPlTaxExceptionDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlTaxException obj = hotelPlTaxExceptionDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlTaxException> find(Page<HotelPlTaxException> page, HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery) {
		DetachedCriteria dc = hotelPlTaxExceptionDao.createDetachedCriteria();
		
	   	if(hotelPlTaxExceptionQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlTaxExceptionQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlTaxExceptionQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlTaxExceptionQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlTaxExceptionQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlTaxExceptionQuery.getHotelUuid()));
		}
	   	if(hotelPlTaxExceptionQuery.getExceptionType()!=null){
	   		dc.add(Restrictions.eq("exceptionType", hotelPlTaxExceptionQuery.getExceptionType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getExceptionName())){
			dc.add(Restrictions.eq("exceptionName", hotelPlTaxExceptionQuery.getExceptionName()));
		}
		if(hotelPlTaxExceptionQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlTaxExceptionQuery.getStartDate()));
		}
		if(hotelPlTaxExceptionQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlTaxExceptionQuery.getEndDate()));
		}
	   	if(StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getTaxType())){
	   		dc.add(Restrictions.eq("taxType", hotelPlTaxExceptionQuery.getTaxType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getTravelType())){
			dc.add(Restrictions.eq("travelType", hotelPlTaxExceptionQuery.getTravelType()));
		}
	   	if(hotelPlTaxExceptionQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlTaxExceptionQuery.getCreateBy()));
	   	}
		if(hotelPlTaxExceptionQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlTaxExceptionQuery.getCreateDate()));
		}
	   	if(hotelPlTaxExceptionQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlTaxExceptionQuery.getUpdateBy()));
	   	}
		if(hotelPlTaxExceptionQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlTaxExceptionQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlTaxExceptionQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlTaxExceptionDao.find(page, dc);
	}
	
	public List<HotelPlTaxException> find( HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery) {
		DetachedCriteria dc = hotelPlTaxExceptionDao.createDetachedCriteria();
		
	   	if(hotelPlTaxExceptionQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlTaxExceptionQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlTaxExceptionQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlTaxExceptionQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlTaxExceptionQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlTaxExceptionQuery.getHotelUuid()));
		}
	   	if(hotelPlTaxExceptionQuery.getExceptionType()!=null){
	   		dc.add(Restrictions.eq("exceptionType", hotelPlTaxExceptionQuery.getExceptionType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getExceptionName())){
			dc.add(Restrictions.eq("exceptionName", hotelPlTaxExceptionQuery.getExceptionName()));
		}
		if(hotelPlTaxExceptionQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlTaxExceptionQuery.getStartDate()));
		}
		if(hotelPlTaxExceptionQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlTaxExceptionQuery.getEndDate()));
		}
	   	if(StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getTaxType())){
	   		dc.add(Restrictions.eq("taxType", hotelPlTaxExceptionQuery.getTaxType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getTravelType())){
			dc.add(Restrictions.eq("travelType", hotelPlTaxExceptionQuery.getTravelType()));
		}
	   	if(hotelPlTaxExceptionQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlTaxExceptionQuery.getCreateBy()));
	   	}
		if(hotelPlTaxExceptionQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlTaxExceptionQuery.getCreateDate()));
		}
	   	if(hotelPlTaxExceptionQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlTaxExceptionQuery.getUpdateBy()));
	   	}
		if(hotelPlTaxExceptionQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlTaxExceptionQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlTaxExceptionQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlTaxExceptionQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlTaxExceptionDao.find(dc);
	}
	
	public HotelPlTaxException getByUuid(String uuid) {
		return hotelPlTaxExceptionDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlTaxException hotelPlTaxException = getByUuid(uuid);
		hotelPlTaxException.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlTaxException);
	}
	
	public List<HotelPlTaxException> findTaxExceptionsByHotelPlUuids(String hotelPlUuid) {
		return hotelPlTaxExceptionDao.findTaxExceptionsByHotelPlUuids(hotelPlUuid);
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的税金 add by zhanghao
	 * @return
	 */
	public List<HotelPlTaxException> getHotelPlTaxException4AutoQuotedPrice( HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery){
		return hotelPlTaxExceptionDao.getHotelPlTaxException4AutoQuotedPrice(hotelPlTaxExceptionQuery);
	}
}
