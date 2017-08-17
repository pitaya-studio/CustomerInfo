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
public class HotelPlRoomMemoServiceImpl  extends BaseService implements HotelPlRoomMemoService{
	@Autowired
	private HotelPlRoomMemoDao hotelPlRoomMemoDao;

	public void save (HotelPlRoomMemo hotelPlRoomMemo){
		super.setOptInfo(hotelPlRoomMemo, BaseService.OPERATION_ADD);
		hotelPlRoomMemoDao.saveObj(hotelPlRoomMemo);
	}
	
	public void save (HotelPlRoomMemoInput hotelPlRoomMemoInput){
		HotelPlRoomMemo hotelPlRoomMemo = hotelPlRoomMemoInput.getHotelPlRoomMemo();
		super.setOptInfo(hotelPlRoomMemo, BaseService.OPERATION_ADD);
		hotelPlRoomMemoDao.saveObj(hotelPlRoomMemo);
	}
	
	public void update (HotelPlRoomMemo hotelPlRoomMemo){
		super.setOptInfo(hotelPlRoomMemo, BaseService.OPERATION_UPDATE);
		hotelPlRoomMemoDao.updateObj(hotelPlRoomMemo);
	}
	
	public HotelPlRoomMemo getById(java.lang.Integer value) {
		return hotelPlRoomMemoDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlRoomMemo obj = hotelPlRoomMemoDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlRoomMemo> find(Page<HotelPlRoomMemo> page, HotelPlRoomMemoQuery hotelPlRoomMemoQuery) {
		DetachedCriteria dc = hotelPlRoomMemoDao.createDetachedCriteria();
		
	   	if(hotelPlRoomMemoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlRoomMemoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlRoomMemoQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlRoomMemoQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlRoomMemoQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlRoomMemoQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getHotelRoomId())){
			dc.add(Restrictions.eq("hotelRoomId", hotelPlRoomMemoQuery.getHotelRoomId()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlRoomMemoQuery.getMemo()));
		}
	   	if(hotelPlRoomMemoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlRoomMemoQuery.getCreateBy()));
	   	}
		if(hotelPlRoomMemoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlRoomMemoQuery.getCreateDate()));
		}
	   	if(hotelPlRoomMemoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlRoomMemoQuery.getUpdateBy()));
	   	}
		if(hotelPlRoomMemoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlRoomMemoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlRoomMemoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlRoomMemoDao.find(page, dc);
	}
	
	public List<HotelPlRoomMemo> find( HotelPlRoomMemoQuery hotelPlRoomMemoQuery) {
		DetachedCriteria dc = hotelPlRoomMemoDao.createDetachedCriteria();
		
	   	if(hotelPlRoomMemoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlRoomMemoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlRoomMemoQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlRoomMemoQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlRoomMemoQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlRoomMemoQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getHotelRoomId())){
			dc.add(Restrictions.eq("hotelRoomId", hotelPlRoomMemoQuery.getHotelRoomId()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlRoomMemoQuery.getMemo()));
		}
	   	if(hotelPlRoomMemoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlRoomMemoQuery.getCreateBy()));
	   	}
		if(hotelPlRoomMemoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlRoomMemoQuery.getCreateDate()));
		}
	   	if(hotelPlRoomMemoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlRoomMemoQuery.getUpdateBy()));
	   	}
		if(hotelPlRoomMemoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlRoomMemoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlRoomMemoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlRoomMemoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlRoomMemoDao.find(dc);
	}
	
	public HotelPlRoomMemo getByUuid(String uuid) {
		return hotelPlRoomMemoDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlRoomMemo hotelPlRoomMemo = getByUuid(uuid);
		hotelPlRoomMemo.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlRoomMemo);
	}
	
	public List<HotelPlRoomMemo> findPlRoomMemosByHotelPlUuid(String hotelPlUuid) {
		return hotelPlRoomMemoDao.findPlRoomMemosByHotelPlUuid(hotelPlUuid);
	}
}
