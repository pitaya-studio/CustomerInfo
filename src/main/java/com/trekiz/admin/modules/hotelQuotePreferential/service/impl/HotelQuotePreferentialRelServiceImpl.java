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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRel;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRelInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRelQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialRelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialRelServiceImpl  extends BaseService implements HotelQuotePreferentialRelService{
	@Autowired
	private HotelQuotePreferentialRelDao hotelQuotePreferentialRelDao;

	public void save (HotelQuotePreferentialRel hotelQuotePreferentialRel){
		super.setOptInfo(hotelQuotePreferentialRel, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRelDao.saveObj(hotelQuotePreferentialRel);
	}
	
	public void save (HotelQuotePreferentialRelInput hotelQuotePreferentialRelInput){
		HotelQuotePreferentialRel hotelQuotePreferentialRel = hotelQuotePreferentialRelInput.getHotelQuotePreferentialRel();
		super.setOptInfo(hotelQuotePreferentialRel, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRelDao.saveObj(hotelQuotePreferentialRel);
	}
	
	public void update (HotelQuotePreferentialRel hotelQuotePreferentialRel){
		super.setOptInfo(hotelQuotePreferentialRel, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialRelDao.updateObj(hotelQuotePreferentialRel);
	}
	
	public HotelQuotePreferentialRel getById(java.lang.Integer value) {
		return hotelQuotePreferentialRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialRel obj = hotelQuotePreferentialRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialRel> find(Page<HotelQuotePreferentialRel> page, HotelQuotePreferentialRelQuery hotelQuotePreferentialRelQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRelDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialRelQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRelQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getRelHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("relHotelQuotePreferentialUuid", hotelQuotePreferentialRelQuery.getRelHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuotePreferentialRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRelQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRelQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRelQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRelDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialRel> find( HotelQuotePreferentialRelQuery hotelQuotePreferentialRelQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRelDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRelQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialRelQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRelQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRelQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getRelHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("relHotelQuotePreferentialUuid", hotelQuotePreferentialRelQuery.getRelHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuotePreferentialRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRelQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRelQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRelQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRelDao.find(dc);
	}
	
	public HotelQuotePreferentialRel getByUuid(String uuid) {
		return hotelQuotePreferentialRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialRel hotelQuotePreferentialRel = getByUuid(uuid);
		hotelQuotePreferentialRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialRel);
	}
}
