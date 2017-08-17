/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service.impl;

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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRoomDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRoom;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRoomInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRoomQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialRoomService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialRoomServiceImpl  extends BaseService implements HotelQuotePreferentialRoomService{
	@Autowired
	private HotelQuotePreferentialRoomDao hotelQuotePreferentialRoomDao;

	public void save (HotelQuotePreferentialRoom hotelQuotePreferentialRoom){
		super.setOptInfo(hotelQuotePreferentialRoom, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRoomDao.saveObj(hotelQuotePreferentialRoom);
	}
	
	public void save (HotelQuotePreferentialRoomInput hotelQuotePreferentialRoomInput){
		HotelQuotePreferentialRoom hotelQuotePreferentialRoom = hotelQuotePreferentialRoomInput.getHotelQuotePreferentialRoom();
		super.setOptInfo(hotelQuotePreferentialRoom, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRoomDao.saveObj(hotelQuotePreferentialRoom);
	}
	
	public void update (HotelQuotePreferentialRoom hotelQuotePreferentialRoom){
		super.setOptInfo(hotelQuotePreferentialRoom, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialRoomDao.updateObj(hotelQuotePreferentialRoom);
	}
	
	public HotelQuotePreferentialRoom getById(java.lang.Integer value) {
		return hotelQuotePreferentialRoomDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialRoom obj = hotelQuotePreferentialRoomDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialRoom> find(Page<HotelQuotePreferentialRoom> page, HotelQuotePreferentialRoomQuery hotelQuotePreferentialRoomQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRoomDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRoomQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRoomQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRoomQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelQuotePreferentialRoomQuery.getHotelRoomUuid()));
		}
	   	if(hotelQuotePreferentialRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", hotelQuotePreferentialRoomQuery.getNights()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelQuotePreferentialRoomQuery.getHotelMealUuids()));
		}
	   	if(hotelQuotePreferentialRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRoomQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRoomQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRoomQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRoomQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRoomDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialRoom> find( HotelQuotePreferentialRoomQuery hotelQuotePreferentialRoomQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRoomDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRoomQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRoomQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRoomQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", hotelQuotePreferentialRoomQuery.getHotelRoomUuid()));
		}
	   	if(hotelQuotePreferentialRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", hotelQuotePreferentialRoomQuery.getNights()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getHotelMealUuids())){
			dc.add(Restrictions.eq("hotelMealUuids", hotelQuotePreferentialRoomQuery.getHotelMealUuids()));
		}
	   	if(hotelQuotePreferentialRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRoomQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRoomQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRoomQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRoomQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRoomDao.find(dc);
	}
	
	public HotelQuotePreferentialRoom getByUuid(String uuid) {
		return hotelQuotePreferentialRoomDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialRoom hotelQuotePreferentialRoom = getByUuid(uuid);
		hotelQuotePreferentialRoom.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialRoom);
	}
}
