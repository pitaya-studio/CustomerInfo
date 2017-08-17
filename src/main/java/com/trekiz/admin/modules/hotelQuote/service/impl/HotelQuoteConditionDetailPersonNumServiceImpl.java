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
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteConditionDetailPersonNumDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionDetailPersonNum;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionDetailPersonNumInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionDetailPersonNumQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionDetailPersonNumService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteConditionDetailPersonNumServiceImpl  extends BaseService implements HotelQuoteConditionDetailPersonNumService{
	@Autowired
	private HotelQuoteConditionDetailPersonNumDao hotelQuoteConditionDetailPersonNumDao;

	public void save (HotelQuoteConditionDetailPersonNum hotelQuoteConditionDetailPersonNum){
		super.setOptInfo(hotelQuoteConditionDetailPersonNum, BaseService.OPERATION_ADD);
		hotelQuoteConditionDetailPersonNumDao.saveObj(hotelQuoteConditionDetailPersonNum);
	}
	
	public void save (HotelQuoteConditionDetailPersonNumInput hotelQuoteConditionDetailPersonNumInput){
		HotelQuoteConditionDetailPersonNum hotelQuoteConditionDetailPersonNum = hotelQuoteConditionDetailPersonNumInput.getHotelQuoteConditionDetailPersonNum();
		super.setOptInfo(hotelQuoteConditionDetailPersonNum, BaseService.OPERATION_ADD);
		hotelQuoteConditionDetailPersonNumDao.saveObj(hotelQuoteConditionDetailPersonNum);
	}
	
	public void update (HotelQuoteConditionDetailPersonNum hotelQuoteConditionDetailPersonNum){
		super.setOptInfo(hotelQuoteConditionDetailPersonNum, BaseService.OPERATION_UPDATE);
		hotelQuoteConditionDetailPersonNumDao.updateObj(hotelQuoteConditionDetailPersonNum);
	}
	
	public HotelQuoteConditionDetailPersonNum getById(java.lang.Integer value) {
		return hotelQuoteConditionDetailPersonNumDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteConditionDetailPersonNum obj = hotelQuoteConditionDetailPersonNumDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteConditionDetailPersonNum> find(Page<HotelQuoteConditionDetailPersonNum> page, HotelQuoteConditionDetailPersonNumQuery hotelQuoteConditionDetailPersonNumQuery) {
		DetachedCriteria dc = hotelQuoteConditionDetailPersonNumDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionDetailPersonNumQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionDetailPersonNumQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionDetailPersonNumQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteConditionUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getTravelerType())){
			dc.add(Restrictions.eq("travelerType", hotelQuoteConditionDetailPersonNumQuery.getTravelerType()));
		}
	   	if(hotelQuoteConditionDetailPersonNumQuery.getPersonNum()!=null){
	   		dc.add(Restrictions.eq("personNum", hotelQuoteConditionDetailPersonNumQuery.getPersonNum()));
	   	}
	   	if(hotelQuoteConditionDetailPersonNumQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionDetailPersonNumQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionDetailPersonNumQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionDetailPersonNumQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionDetailPersonNumQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionDetailPersonNumQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionDetailPersonNumQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionDetailPersonNumQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionDetailPersonNumQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionDetailPersonNumDao.find(page, dc);
	}
	
	public List<HotelQuoteConditionDetailPersonNum> find( HotelQuoteConditionDetailPersonNumQuery hotelQuoteConditionDetailPersonNumQuery) {
		DetachedCriteria dc = hotelQuoteConditionDetailPersonNumDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionDetailPersonNumQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionDetailPersonNumQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionDetailPersonNumQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteConditionDetailPersonNumQuery.getHotelQuoteConditionUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getTravelerType())){
			dc.add(Restrictions.eq("travelerType", hotelQuoteConditionDetailPersonNumQuery.getTravelerType()));
		}
	   	if(hotelQuoteConditionDetailPersonNumQuery.getPersonNum()!=null){
	   		dc.add(Restrictions.eq("personNum", hotelQuoteConditionDetailPersonNumQuery.getPersonNum()));
	   	}
	   	if(hotelQuoteConditionDetailPersonNumQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionDetailPersonNumQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionDetailPersonNumQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionDetailPersonNumQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionDetailPersonNumQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionDetailPersonNumQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionDetailPersonNumQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionDetailPersonNumQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionDetailPersonNumQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionDetailPersonNumQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionDetailPersonNumDao.find(dc);
	}
	
	public HotelQuoteConditionDetailPersonNum getByUuid(String uuid) {
		return hotelQuoteConditionDetailPersonNumDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteConditionDetailPersonNum hotelQuoteConditionDetailPersonNum = getByUuid(uuid);
		hotelQuoteConditionDetailPersonNum.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteConditionDetailPersonNum);
	}

	@Override
	public List<HotelQuoteConditionDetailPersonNum> getByHotelQuoteConditionUuid(String uuid) {
		String sql="from HotelQuoteConditionDetailPersonNum where delFlag='0' and hotelQuoteConditionUuid=?";
		return hotelQuoteConditionDetailPersonNumDao.find(sql, uuid);
	}
}
