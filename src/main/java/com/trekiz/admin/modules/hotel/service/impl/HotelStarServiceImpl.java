/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.entity.*;
import com.trekiz.admin.modules.hotel.dao.*;
import com.trekiz.admin.modules.hotel.service.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelStarServiceImpl  extends BaseService implements HotelStarService{
	@Autowired
	private HotelStarDao hotelStarDao;

	public void save (HotelStar hotelStar){
		super.setOptInfo(hotelStar, OPERATION_ADD);
		hotelStarDao.saveObj(hotelStar);
	}
	
	public void update (HotelStar hotelStar){
		super.setOptInfo(hotelStar, OPERATION_UPDATE);
		hotelStarDao.updateObj(hotelStar);
	}
	
	public HotelStar getById(java.lang.Long value) {
		return hotelStarDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		HotelStar entity = hotelStarDao.getById(value);
		entity.setDelFlag("1");
		hotelStarDao.updateObj(entity);
	}
	
	public void removeByUuid(String uuid) {
		HotelStar hotelStar = getByUuid(uuid);
		hotelStar.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelStar);
	}
	
	
	public Page<HotelStar> find(Page<HotelStar> page, HotelStar hotelStar) {
		DetachedCriteria dc = hotelStarDao.createDetachedCriteria();
		
	   	if(hotelStar.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelStar.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelStar.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelStar.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelStar.getDictUuid())){
			dc.add(Restrictions.like("dictUuid", "%"+hotelStar.getDictUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelStar.getLabel())){
			dc.add(Restrictions.like("label", "%"+hotelStar.getLabel()+"%"));
		}
		if (hotelStar.getValue() != null){
			dc.add(Restrictions.eq("value", hotelStar.getValue()));
		}
	   	if(hotelStar.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelStar.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelStar.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelStar.getDescription()+"%"));
		}
	   	if(hotelStar.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelStar.getWholesalerId()));
	   	}
	   	if(hotelStar.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelStar.getCreateBy()));
	   	}
		if(hotelStar.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelStar.getCreateDate()));
		}
	   	if(hotelStar.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelStar.getUpdateBy()));
	   	}
		if(hotelStar.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelStar.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelStar.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelStar.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelStarDao.find(page, dc);
	}
	
	public List<HotelStar> find( HotelStar hotelStar) {
		DetachedCriteria dc = hotelStarDao.createDetachedCriteria();
		
	   	if(hotelStar.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelStar.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelStar.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelStar.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelStar.getDictUuid())){
			dc.add(Restrictions.like("dictUuid", "%"+hotelStar.getDictUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelStar.getLabel())){
			dc.add(Restrictions.like("label", "%"+hotelStar.getLabel()+"%"));
		}
		if (hotelStar.getValue() != null){
			dc.add(Restrictions.eq("value", hotelStar.getValue()));
		}
	   	if(hotelStar.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelStar.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelStar.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelStar.getDescription()+"%"));
		}
	   	if(hotelStar.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelStar.getWholesalerId()));
	   	}
	   	if(hotelStar.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelStar.getCreateBy()));
	   	}
		if(hotelStar.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelStar.getCreateDate()));
		}
	   	if(hotelStar.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelStar.getUpdateBy()));
	   	}
		if(hotelStar.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelStar.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelStar.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelStar.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelStarDao.find(dc);
	}
	
	public boolean findIsExist(String uuid, String label, Long wholesalerId) {
		StringBuffer sb = new StringBuffer("from HotelStar hotelStar where hotelStar.uuid != ? and hotelStar.label = ? and hotelStar.wholesalerId = ? and hotelStar.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<HotelStar> hotelStars = hotelStarDao.find(sb.toString(), uuid, label, wholesalerId.intValue());
		
		if(hotelStars == null || hotelStars.size() == 0) {
			return false;
		}
		return true;
	}
	
	public HotelStar getByUuid(String uuid) {
		return hotelStarDao.getByUuid(uuid);
	}
	
}
