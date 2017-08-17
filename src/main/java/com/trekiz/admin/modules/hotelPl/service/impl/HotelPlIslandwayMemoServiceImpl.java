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
public class HotelPlIslandwayMemoServiceImpl  extends BaseService implements HotelPlIslandwayMemoService{
	@Autowired
	private HotelPlIslandwayMemoDao hotelPlIslandwayMemoDao;

	public void save (HotelPlIslandwayMemo hotelPlIslandwayMemo){
		super.setOptInfo(hotelPlIslandwayMemo, BaseService.OPERATION_ADD);
		hotelPlIslandwayMemoDao.saveObj(hotelPlIslandwayMemo);
	}
	
	public void save (HotelPlIslandwayMemoInput hotelPlIslandwayMemoInput){
		HotelPlIslandwayMemo hotelPlIslandwayMemo = hotelPlIslandwayMemoInput.getHotelPlIslandwayMemo();
		super.setOptInfo(hotelPlIslandwayMemo, BaseService.OPERATION_ADD);
		hotelPlIslandwayMemoDao.saveObj(hotelPlIslandwayMemo);
	}
	
	public void update (HotelPlIslandwayMemo hotelPlIslandwayMemo){
		super.setOptInfo(hotelPlIslandwayMemo, BaseService.OPERATION_UPDATE);
		hotelPlIslandwayMemoDao.updateObj(hotelPlIslandwayMemo);
	}
	
	public HotelPlIslandwayMemo getById(java.lang.Integer value) {
		return hotelPlIslandwayMemoDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlIslandwayMemo obj = hotelPlIslandwayMemoDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlIslandwayMemo> find(Page<HotelPlIslandwayMemo> page, HotelPlIslandwayMemoQuery hotelPlIslandwayMemoQuery) {
		DetachedCriteria dc = hotelPlIslandwayMemoDao.createDetachedCriteria();
		
	   	if(hotelPlIslandwayMemoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlIslandwayMemoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlIslandwayMemoQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlIslandwayMemoQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlIslandwayMemoQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlIslandwayMemoQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlIslandwayMemoQuery.getIslandWay()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlIslandwayMemoQuery.getMemo()));
		}
	   	if(hotelPlIslandwayMemoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlIslandwayMemoQuery.getCreateBy()));
	   	}
		if(hotelPlIslandwayMemoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlIslandwayMemoQuery.getCreateDate()));
		}
	   	if(hotelPlIslandwayMemoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlIslandwayMemoQuery.getUpdateBy()));
	   	}
		if(hotelPlIslandwayMemoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlIslandwayMemoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlIslandwayMemoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlIslandwayMemoDao.find(page, dc);
	}
	
	public List<HotelPlIslandwayMemo> find( HotelPlIslandwayMemoQuery hotelPlIslandwayMemoQuery) {
		DetachedCriteria dc = hotelPlIslandwayMemoDao.createDetachedCriteria();
		
	   	if(hotelPlIslandwayMemoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlIslandwayMemoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlIslandwayMemoQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlIslandwayMemoQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlIslandwayMemoQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlIslandwayMemoQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlIslandwayMemoQuery.getIslandWay()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlIslandwayMemoQuery.getMemo()));
		}
	   	if(hotelPlIslandwayMemoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlIslandwayMemoQuery.getCreateBy()));
	   	}
		if(hotelPlIslandwayMemoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlIslandwayMemoQuery.getCreateDate()));
		}
	   	if(hotelPlIslandwayMemoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlIslandwayMemoQuery.getUpdateBy()));
	   	}
		if(hotelPlIslandwayMemoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlIslandwayMemoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayMemoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlIslandwayMemoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlIslandwayMemoDao.find(dc);
	}
	
	public HotelPlIslandwayMemo getByUuid(String uuid) {
		return hotelPlIslandwayMemoDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlIslandwayMemo hotelPlIslandwayMemo = getByUuid(uuid);
		hotelPlIslandwayMemo.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlIslandwayMemo);
	}
	
	public List<HotelPlIslandwayMemo> findPlIslandwayMemosByHotelPlUuid(String hotelPlUuid) {
		return hotelPlIslandwayMemoDao.findPlIslandwayMemosByHotelPlUuid(hotelPlUuid);
	}
}
