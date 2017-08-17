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
public class HotelTopicServiceImpl  extends BaseService implements HotelTopicService{
	@Autowired
	private HotelTopicDao hotelTopicDao;

	public void save (HotelTopic hotelTopic){
		super.setOptInfo(hotelTopic, BaseService.OPERATION_ADD);
		hotelTopicDao.saveObj(hotelTopic);
	}
	
	public void update (HotelTopic hotelTopic){
		super.setOptInfo(hotelTopic, BaseService.OPERATION_UPDATE);
		hotelTopicDao.updateObj(hotelTopic);
	}
	
	public HotelTopic getById(java.lang.Integer value) {
		return hotelTopicDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelTopic obj = hotelTopicDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelTopic> find(Page<HotelTopic> page, HotelTopic hotelTopic) {
		DetachedCriteria dc = hotelTopicDao.createDetachedCriteria();
		
	   	if(hotelTopic.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTopic.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTopic.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelTopic.getUuid()+"%"));
		}
	   	if(hotelTopic.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelTopic.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelTopic.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelTopic.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelTopic.getViewUuid())){
			dc.add(Restrictions.like("viewUuid", "%"+hotelTopic.getViewUuid()+"%"));
		}
	   	if(hotelTopic.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelTopic.getWholesalerId()));
	   	}
	   	if(hotelTopic.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTopic.getCreateBy()));
	   	}
		if(hotelTopic.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTopic.getCreateDate()));
		}
	   	if(hotelTopic.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTopic.getUpdateBy()));
	   	}
		if(hotelTopic.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTopic.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTopic.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelTopic.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelTopicDao.find(page, dc);
	}
	
	public List<HotelTopic> find( HotelTopic hotelTopic) {
		DetachedCriteria dc = hotelTopicDao.createDetachedCriteria();
		
	   	if(hotelTopic.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTopic.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTopic.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelTopic.getUuid()+"%"));
		}
	   	if(hotelTopic.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelTopic.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelTopic.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelTopic.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelTopic.getViewUuid())){
			dc.add(Restrictions.like("viewUuid", "%"+hotelTopic.getViewUuid()+"%"));
		}
	   	if(hotelTopic.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelTopic.getWholesalerId()));
	   	}
	   	if(hotelTopic.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTopic.getCreateBy()));
	   	}
		if(hotelTopic.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTopic.getCreateDate()));
		}
	   	if(hotelTopic.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTopic.getUpdateBy()));
	   	}
		if(hotelTopic.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTopic.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTopic.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelTopic.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelTopicDao.find(dc);
	}
	
	public HotelTopic getByUuid(String uuid) {
		return hotelTopicDao.getByUuid(uuid);
	}
	
	public HotelTopic findByViewUuidAndCompany(String uuid, Integer companyId) {
		StringBuffer sb = new StringBuffer("from HotelTopic hotelTopic where hotelTopic.viewUuid = ? and hotelTopic.wholesalerId = ? and hotelTopic.delFlag="+BaseEntity.DEL_FLAG_NORMAL);
		List<HotelTopic> gotelTopics = hotelTopicDao.find(sb.toString(), uuid, companyId);
		if(gotelTopics != null && gotelTopics.size() == 1) {
			return gotelTopics.get(0);
		}
		return null;
	}
}
