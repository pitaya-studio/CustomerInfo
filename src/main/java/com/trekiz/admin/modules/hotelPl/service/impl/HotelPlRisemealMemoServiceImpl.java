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
public class HotelPlRisemealMemoServiceImpl  extends BaseService implements HotelPlRisemealMemoService{
	@Autowired
	private HotelPlRisemealMemoDao hotelPlRisemealMemoDao;

	public void save (HotelPlRisemealMemo hotelPlRisemealMemo){
		super.setOptInfo(hotelPlRisemealMemo, BaseService.OPERATION_ADD);
		hotelPlRisemealMemoDao.saveObj(hotelPlRisemealMemo);
	}
	
	public void save (HotelPlRisemealMemoInput hotelPlRisemealMemoInput){
		HotelPlRisemealMemo hotelPlRisemealMemo = hotelPlRisemealMemoInput.getHotelPlRisemealMemo();
		super.setOptInfo(hotelPlRisemealMemo, BaseService.OPERATION_ADD);
		hotelPlRisemealMemoDao.saveObj(hotelPlRisemealMemo);
	}
	
	public void update (HotelPlRisemealMemo hotelPlRisemealMemo){
		super.setOptInfo(hotelPlRisemealMemo, BaseService.OPERATION_UPDATE);
		hotelPlRisemealMemoDao.updateObj(hotelPlRisemealMemo);
	}
	
	public HotelPlRisemealMemo getById(java.lang.Integer value) {
		return hotelPlRisemealMemoDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlRisemealMemo obj = hotelPlRisemealMemoDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlRisemealMemo> find(Page<HotelPlRisemealMemo> page, HotelPlRisemealMemoQuery hotelPlRisemealMemoQuery) {
		DetachedCriteria dc = hotelPlRisemealMemoDao.createDetachedCriteria();
		
	   	if(hotelPlRisemealMemoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlRisemealMemoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlRisemealMemoQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlRisemealMemoQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlRisemealMemoQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlRisemealMemoQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelPlRisemealMemoQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlRisemealMemoQuery.getMemo()));
		}
	   	if(hotelPlRisemealMemoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlRisemealMemoQuery.getCreateBy()));
	   	}
		if(hotelPlRisemealMemoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlRisemealMemoQuery.getCreateDate()));
		}
	   	if(hotelPlRisemealMemoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlRisemealMemoQuery.getUpdateBy()));
	   	}
		if(hotelPlRisemealMemoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlRisemealMemoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlRisemealMemoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlRisemealMemoDao.find(page, dc);
	}
	
	public List<HotelPlRisemealMemo> find( HotelPlRisemealMemoQuery hotelPlRisemealMemoQuery) {
		DetachedCriteria dc = hotelPlRisemealMemoDao.createDetachedCriteria();
		
	   	if(hotelPlRisemealMemoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlRisemealMemoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlRisemealMemoQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlRisemealMemoQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlRisemealMemoQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlRisemealMemoQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelPlRisemealMemoQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlRisemealMemoQuery.getMemo()));
		}
	   	if(hotelPlRisemealMemoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlRisemealMemoQuery.getCreateBy()));
	   	}
		if(hotelPlRisemealMemoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlRisemealMemoQuery.getCreateDate()));
		}
	   	if(hotelPlRisemealMemoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlRisemealMemoQuery.getUpdateBy()));
	   	}
		if(hotelPlRisemealMemoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlRisemealMemoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlRisemealMemoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlRisemealMemoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlRisemealMemoDao.find(dc);
	}
	
	public HotelPlRisemealMemo getByUuid(String uuid) {
		return hotelPlRisemealMemoDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlRisemealMemo hotelPlRisemealMemo = getByUuid(uuid);
		hotelPlRisemealMemo.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlRisemealMemo);
	}
	
	public List<HotelPlRisemealMemo> findPlRisemealMemosByHotelPlUuid(String hotelPlUuid) {
		return hotelPlRisemealMemoDao.findPlRisemealMemosByHotelPlUuid(hotelPlUuid);
	}
}
