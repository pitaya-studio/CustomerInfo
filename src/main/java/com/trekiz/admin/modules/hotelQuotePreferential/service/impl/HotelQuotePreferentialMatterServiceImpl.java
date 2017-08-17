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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatter;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialMatterInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialMatterQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialMatterService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialMatterServiceImpl  extends BaseService implements HotelQuotePreferentialMatterService{
	@Autowired
	private HotelQuotePreferentialMatterDao hotelQuotePreferentialMatterDao;

	public void save (HotelQuotePreferentialMatter hotelQuotePreferentialMatter){
		super.setOptInfo(hotelQuotePreferentialMatter, BaseService.OPERATION_ADD);
		hotelQuotePreferentialMatterDao.saveObj(hotelQuotePreferentialMatter);
	}
	
	public void save (HotelQuotePreferentialMatterInput hotelQuotePreferentialMatterInput){
		HotelQuotePreferentialMatter hotelQuotePreferentialMatter = hotelQuotePreferentialMatterInput.getHotelQuotePreferentialMatter();
		super.setOptInfo(hotelQuotePreferentialMatter, BaseService.OPERATION_ADD);
		hotelQuotePreferentialMatterDao.saveObj(hotelQuotePreferentialMatter);
	}
	
	public void update (HotelQuotePreferentialMatter hotelQuotePreferentialMatter){
		super.setOptInfo(hotelQuotePreferentialMatter, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialMatterDao.updateObj(hotelQuotePreferentialMatter);
	}
	
	public HotelQuotePreferentialMatter getById(java.lang.Integer value) {
		return hotelQuotePreferentialMatterDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialMatter obj = hotelQuotePreferentialMatterDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialMatter> find(Page<HotelQuotePreferentialMatter> page, HotelQuotePreferentialMatterQuery hotelQuotePreferentialMatterQuery) {
		DetachedCriteria dc = hotelQuotePreferentialMatterDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialMatterQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialMatterQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialMatterQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialMatterQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialMatterQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialMatterQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialMatterQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getPreferentialTemplatesUuid())){
			dc.add(Restrictions.eq("preferentialTemplatesUuid", hotelQuotePreferentialMatterQuery.getPreferentialTemplatesUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelQuotePreferentialMatterQuery.getMemo()));
		}
	   	if(hotelQuotePreferentialMatterQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialMatterQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialMatterQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialMatterQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialMatterQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialMatterQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialMatterQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialMatterQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialMatterQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialMatterDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialMatter> find( HotelQuotePreferentialMatterQuery hotelQuotePreferentialMatterQuery) {
		DetachedCriteria dc = hotelQuotePreferentialMatterDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialMatterQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialMatterQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialMatterQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialMatterQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialMatterQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialMatterQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialMatterQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getPreferentialTemplatesUuid())){
			dc.add(Restrictions.eq("preferentialTemplatesUuid", hotelQuotePreferentialMatterQuery.getPreferentialTemplatesUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelQuotePreferentialMatterQuery.getMemo()));
		}
	   	if(hotelQuotePreferentialMatterQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialMatterQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialMatterQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialMatterQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialMatterQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialMatterQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialMatterQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialMatterQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialMatterQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialMatterDao.find(dc);
	}
	
	public HotelQuotePreferentialMatter getByUuid(String uuid) {
		return hotelQuotePreferentialMatterDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialMatter hotelQuotePreferentialMatter = getByUuid(uuid);
		hotelQuotePreferentialMatter.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialMatter);
	}
}
