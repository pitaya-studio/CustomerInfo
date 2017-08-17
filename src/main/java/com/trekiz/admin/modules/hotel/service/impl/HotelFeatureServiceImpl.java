/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.dao.HotelFeatureDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelFeature;
import com.trekiz.admin.modules.hotel.service.HotelFeatureService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelFeatureServiceImpl  extends BaseService implements HotelFeatureService{
	@Autowired
	private HotelFeatureDao hotelFeatureDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;

	public void save (HotelFeature hotelFeature,List<HotelAnnex> list){
		this.setOptInfo(hotelFeature, BaseService.OPERATION_ADD);
		hotelFeatureDao.saveObj(hotelFeature);
		
		if(list!=null){
			for(HotelAnnex ha:list){
				super.setOptInfo(ha, null);
				ha.setMainUuid(hotelFeature.getUuid());
				ha.setType(2);
				ha.setWholesalerId(Integer.parseInt(hotelFeature.getWholesalerId().toString()));
				hotelAnnexDao.saveObj(ha);
			}
		}
		
	}

	public void update (HotelFeature hotelFeature,List<HotelAnnex> list){
		this.setOptInfo(hotelFeature, BaseService.OPERATION_UPDATE);
		hotelFeatureDao.updateObj(hotelFeature);
		hotelAnnexDao.synDocInfo(hotelFeature.getUuid(), 2, Integer.parseInt(hotelFeature.getWholesalerId().toString()), list);
	}
	
	public void save (HotelFeature hotelFeature){
		this.setOptInfo(hotelFeature, BaseService.OPERATION_ADD);
		hotelFeatureDao.saveObj(hotelFeature);
	}
	
	public void update (HotelFeature hotelFeature){
		this.setOptInfo(hotelFeature, BaseService.OPERATION_UPDATE);
		hotelFeatureDao.updateObj(hotelFeature);
	}
	
	
	public HotelFeature getById(java.lang.Integer value) {
		return hotelFeatureDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelFeature obj = hotelFeatureDao.getById(value);
		obj.setDelFlag("1");
		hotelFeatureDao.updateObj(obj);
	}	
	
	public HotelFeature getByUuid(String value){
		return hotelFeatureDao.getByUuid(value);
	}
	
	public void removeByUuid(java.lang.String value){
		HotelFeature obj = hotelFeatureDao.getByUuid(value);
		if(obj!=null && obj.getWholesalerId()!=null && obj.getWholesalerId().equals(UserUtils.getCompanyIdForData())){
			obj.setDelFlag("1");
			hotelFeatureDao.updateObj(obj);
		}
	}
	
	public Page<HotelFeature> find(Page<HotelFeature> page, HotelFeature hotelFeature) {
		DetachedCriteria dc = hotelFeatureDao.createDetachedCriteria();
		
	   	if(hotelFeature.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelFeature.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelFeature.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelFeature.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFeature.getName())){
			dc.add(Restrictions.like("name", "%"+hotelFeature.getName()+"%"));
		}
	   	if(hotelFeature.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelFeature.getSort()));
	   	}
		if (StringUtils.isNotEmpty(hotelFeature.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelFeature.getDescription()+"%"));
		}
		if(hotelFeature.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelFeature.getWholesalerId()));
	   	}
	   	if(hotelFeature.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelFeature.getCreateBy()));
	   	}
		if(hotelFeature.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelFeature.getCreateDate()));
		}
	   	if(hotelFeature.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelFeature.getUpdateBy()));
	   	}
		if(hotelFeature.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelFeature.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelFeature.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelFeature.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelFeatureDao.find(page, dc);
	}
	
	public List<HotelFeature> find( HotelFeature hotelFeature) {
		DetachedCriteria dc = hotelFeatureDao.createDetachedCriteria();
		
	   	if(hotelFeature.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelFeature.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelFeature.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelFeature.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelFeature.getName())){
			dc.add(Restrictions.like("name", "%"+hotelFeature.getName()+"%"));
		}
	   	if(hotelFeature.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelFeature.getSort()));
	   	}
	   	if(hotelFeature.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelFeature.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelFeature.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelFeature.getDescription()+"%"));
		}
	   	if(hotelFeature.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelFeature.getCreateBy()));
	   	}
		if(hotelFeature.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelFeature.getCreateDate()));
		}
	   	if(hotelFeature.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelFeature.getUpdateBy()));
	   	}
		if(hotelFeature.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelFeature.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelFeature.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelFeature.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelFeatureDao.find(dc);
	}
}
