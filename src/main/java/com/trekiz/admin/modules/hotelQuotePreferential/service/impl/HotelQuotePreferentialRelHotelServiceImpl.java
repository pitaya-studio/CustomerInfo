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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelHotelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRelHotel;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRelHotelInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRelHotelQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialRelHotelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialRelHotelServiceImpl  extends BaseService implements HotelQuotePreferentialRelHotelService{
	@Autowired
	private HotelQuotePreferentialRelHotelDao hotelQuotePreferentialRelHotelDao;

	public void save (HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel){
		super.setOptInfo(hotelQuotePreferentialRelHotel, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRelHotelDao.saveObj(hotelQuotePreferentialRelHotel);
	}
	
	public void save (HotelQuotePreferentialRelHotelInput hotelQuotePreferentialRelHotelInput){
		HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel = hotelQuotePreferentialRelHotelInput.getHotelQuotePreferentialRelHotel();
		super.setOptInfo(hotelQuotePreferentialRelHotel, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRelHotelDao.saveObj(hotelQuotePreferentialRelHotel);
	}
	
	public void update (HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel){
		super.setOptInfo(hotelQuotePreferentialRelHotel, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialRelHotelDao.updateObj(hotelQuotePreferentialRelHotel);
	}
	
	public HotelQuotePreferentialRelHotel getById(java.lang.Integer value) {
		return hotelQuotePreferentialRelHotelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialRelHotel obj = hotelQuotePreferentialRelHotelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialRelHotel> find(Page<HotelQuotePreferentialRelHotel> page, HotelQuotePreferentialRelHotelQuery hotelQuotePreferentialRelHotelQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRelHotelDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRelHotelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRelHotelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRelHotelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRelHotelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRelHotelQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRelHotelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelQuotePreferentialRelHotelQuery.getIslandWay()));
		}
	   	if(hotelQuotePreferentialRelHotelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRelHotelQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRelHotelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRelHotelQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRelHotelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRelHotelQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRelHotelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRelHotelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRelHotelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRelHotelDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialRelHotel> find( HotelQuotePreferentialRelHotelQuery hotelQuotePreferentialRelHotelQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRelHotelDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRelHotelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRelHotelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRelHotelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRelHotelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRelHotelQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRelHotelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelQuotePreferentialRelHotelQuery.getIslandWay()));
		}
	   	if(hotelQuotePreferentialRelHotelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRelHotelQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRelHotelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRelHotelQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRelHotelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRelHotelQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRelHotelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRelHotelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelHotelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRelHotelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRelHotelDao.find(dc);
	}
	
	public HotelQuotePreferentialRelHotel getByUuid(String uuid) {
		return hotelQuotePreferentialRelHotelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel = getByUuid(uuid);
		hotelQuotePreferentialRelHotel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialRelHotel);
	}
}
