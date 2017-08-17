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
public class HotelPlPreferentialRelHotelServiceImpl  extends BaseService implements HotelPlPreferentialRelHotelService{
	@Autowired
	private HotelPlPreferentialRelHotelDao hotelPlPreferentialRelHotelDao;

	public void save (HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel){
		super.setOptInfo(hotelPlPreferentialRelHotel, BaseService.OPERATION_ADD);
		hotelPlPreferentialRelHotelDao.saveObj(hotelPlPreferentialRelHotel);
	}
	
	public void save (HotelPlPreferentialRelHotelInput hotelPlPreferentialRelHotelInput){
		HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel = hotelPlPreferentialRelHotelInput.getHotelPlPreferentialRelHotel();
		super.setOptInfo(hotelPlPreferentialRelHotel, BaseService.OPERATION_ADD);
		hotelPlPreferentialRelHotelDao.saveObj(hotelPlPreferentialRelHotel);
	}
	
	public void update (HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel){
		super.setOptInfo(hotelPlPreferentialRelHotel, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialRelHotelDao.updateObj(hotelPlPreferentialRelHotel);
	}
	
	public HotelPlPreferentialRelHotel getById(java.lang.Integer value) {
		return hotelPlPreferentialRelHotelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferentialRelHotel obj = hotelPlPreferentialRelHotelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferentialRelHotel> find(Page<HotelPlPreferentialRelHotel> page, HotelPlPreferentialRelHotelQuery hotelPlPreferentialRelHotelQuery) {
		DetachedCriteria dc = hotelPlPreferentialRelHotelDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRelHotelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRelHotelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRelHotelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRelHotelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRelHotelQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRelHotelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlPreferentialRelHotelQuery.getIslandWay()));
		}
	   	if(hotelPlPreferentialRelHotelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRelHotelQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRelHotelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRelHotelQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRelHotelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRelHotelQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRelHotelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRelHotelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRelHotelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRelHotelDao.find(page, dc);
	}
	
	public List<HotelPlPreferentialRelHotel> find( HotelPlPreferentialRelHotelQuery hotelPlPreferentialRelHotelQuery) {
		DetachedCriteria dc = hotelPlPreferentialRelHotelDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialRelHotelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialRelHotelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialRelHotelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialRelHotelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getHotelPlPreferentialUuid())){
			dc.add(Restrictions.eq("hotelPlPreferentialUuid", hotelPlPreferentialRelHotelQuery.getHotelPlPreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialRelHotelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlPreferentialRelHotelQuery.getIslandWay()));
		}
	   	if(hotelPlPreferentialRelHotelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialRelHotelQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialRelHotelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialRelHotelQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialRelHotelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialRelHotelQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialRelHotelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialRelHotelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialRelHotelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialRelHotelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialRelHotelDao.find(dc);
	}
	
	public HotelPlPreferentialRelHotel getByUuid(String uuid) {
		return hotelPlPreferentialRelHotelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel = getByUuid(uuid);
		hotelPlPreferentialRelHotel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferentialRelHotel);
	}
	
	public HotelPlPreferentialRelHotel getRelHotelByPreferentialUuid(String preferentialUuid) {
		return hotelPlPreferentialRelHotelDao.getRelHotelByPreferentialUuid(preferentialUuid);
	}
}
