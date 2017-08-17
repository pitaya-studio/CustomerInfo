/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelContactDao;
import com.trekiz.admin.modules.hotel.entity.HotelContact;
import com.trekiz.admin.modules.hotel.service.HotelContactService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelContactServiceImpl  extends BaseService implements HotelContactService{
	@Autowired
	private HotelContactDao hotelContactDao;

	public void save (HotelContact hotelContact){
		hotelContactDao.saveObj(hotelContact);
	}
	
	public void update (HotelContact hotelContact){
		hotelContactDao.updateObj(hotelContact);
	}
	
	public HotelContact getById(java.lang.Long value) {
		return hotelContactDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		HotelContact obj = hotelContactDao.getById(value);
		obj.setDelFlag("1");
		hotelContactDao.updateObj(obj);
	}	
	
	
	public Page<HotelContact> find(Page<HotelContact> page, HotelContact hotelContact) {
		DetachedCriteria dc = hotelContactDao.createDetachedCriteria();
		
	   	if(hotelContact.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelContact.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelContact.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelContact.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelContact.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getName())){
			dc.add(Restrictions.like("name", "%"+hotelContact.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getMobile())){
			dc.add(Restrictions.like("mobile", "%"+hotelContact.getMobile()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getTelephone())){
			dc.add(Restrictions.like("telephone", "%"+hotelContact.getTelephone()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getFax())){
			dc.add(Restrictions.like("fax", "%"+hotelContact.getFax()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getEmail())){
			dc.add(Restrictions.like("email", "%"+hotelContact.getEmail()+"%"));
		}
	   	if(hotelContact.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelContact.getCreateBy()));
	   	}
		if(hotelContact.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelContact.getCreateDate()));
		}
	   	if(hotelContact.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelContact.getUpdateBy()));
	   	}
		if(hotelContact.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelContact.getUpdateDate()));
		}
	   	if(hotelContact.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", hotelContact.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotelContact.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelContact.getDelFlag()+"%"));
		}

		return hotelContactDao.find(page, dc);
	}
	
	public List<HotelContact> find( HotelContact hotelContact) {
		DetachedCriteria dc = hotelContactDao.createDetachedCriteria();
		
	   	if(hotelContact.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelContact.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelContact.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelContact.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelContact.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getName())){
			dc.add(Restrictions.like("name", "%"+hotelContact.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getMobile())){
			dc.add(Restrictions.like("mobile", "%"+hotelContact.getMobile()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getTelephone())){
			dc.add(Restrictions.like("telephone", "%"+hotelContact.getTelephone()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getFax())){
			dc.add(Restrictions.like("fax", "%"+hotelContact.getFax()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelContact.getEmail())){
			dc.add(Restrictions.like("email", "%"+hotelContact.getEmail()+"%"));
		}
	   	if(hotelContact.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelContact.getCreateBy()));
	   	}
		if(hotelContact.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelContact.getCreateDate()));
		}
	   	if(hotelContact.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelContact.getUpdateBy()));
	   	}
		if(hotelContact.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelContact.getUpdateDate()));
		}
	   	if(hotelContact.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", hotelContact.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotelContact.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelContact.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelContactDao.find(dc);
	}
}
