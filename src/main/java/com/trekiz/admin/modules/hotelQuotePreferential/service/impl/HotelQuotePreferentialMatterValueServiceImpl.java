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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatterValue;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialMatterValueInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialMatterValueQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialMatterValueService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialMatterValueServiceImpl  extends BaseService implements HotelQuotePreferentialMatterValueService{
	@Autowired
	private HotelQuotePreferentialMatterValueDao hotelQuotePreferentialMatterValueDao;

	public void save (HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue){
		super.setOptInfo(hotelQuotePreferentialMatterValue, BaseService.OPERATION_ADD);
		hotelQuotePreferentialMatterValueDao.saveObj(hotelQuotePreferentialMatterValue);
	}
	
	public void save (HotelQuotePreferentialMatterValueInput hotelQuotePreferentialMatterValueInput){
		HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue = hotelQuotePreferentialMatterValueInput.getHotelQuotePreferentialMatterValue();
		super.setOptInfo(hotelQuotePreferentialMatterValue, BaseService.OPERATION_ADD);
		hotelQuotePreferentialMatterValueDao.saveObj(hotelQuotePreferentialMatterValue);
	}
	
	public void update (HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue){
		super.setOptInfo(hotelQuotePreferentialMatterValue, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialMatterValueDao.updateObj(hotelQuotePreferentialMatterValue);
	}
	
	public HotelQuotePreferentialMatterValue getById(java.lang.Integer value) {
		return hotelQuotePreferentialMatterValueDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialMatterValue obj = hotelQuotePreferentialMatterValueDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialMatterValue> find(Page<HotelQuotePreferentialMatterValue> page, HotelQuotePreferentialMatterValueQuery hotelQuotePreferentialMatterValueQuery) {
		DetachedCriteria dc = hotelQuotePreferentialMatterValueDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialMatterValueQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialMatterValueQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialMatterValueQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialMatterValueQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialMatterValueQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialMatterValueQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialMatterUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialMatterUuid", hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialMatterUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getMyKeyvar())){
			dc.add(Restrictions.eq("myKeyvar", hotelQuotePreferentialMatterValueQuery.getMyKeyvar()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getMyValue())){
			dc.add(Restrictions.eq("myValue", hotelQuotePreferentialMatterValueQuery.getMyValue()));
		}
	   	if(hotelQuotePreferentialMatterValueQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialMatterValueQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialMatterValueQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialMatterValueQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialMatterValueQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialMatterValueQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialMatterValueQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialMatterValueQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialMatterValueQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialMatterValueDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialMatterValue> find( HotelQuotePreferentialMatterValueQuery hotelQuotePreferentialMatterValueQuery) {
		DetachedCriteria dc = hotelQuotePreferentialMatterValueDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialMatterValueQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialMatterValueQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialMatterValueQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialMatterValueQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialMatterValueQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialMatterValueQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialMatterUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialMatterUuid", hotelQuotePreferentialMatterValueQuery.getHotelQuotePreferentialMatterUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getMyKeyvar())){
			dc.add(Restrictions.eq("myKeyvar", hotelQuotePreferentialMatterValueQuery.getMyKeyvar()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getMyValue())){
			dc.add(Restrictions.eq("myValue", hotelQuotePreferentialMatterValueQuery.getMyValue()));
		}
	   	if(hotelQuotePreferentialMatterValueQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialMatterValueQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialMatterValueQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialMatterValueQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialMatterValueQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialMatterValueQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialMatterValueQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialMatterValueQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialMatterValueQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialMatterValueQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialMatterValueDao.find(dc);
	}
	
	public HotelQuotePreferentialMatterValue getByUuid(String uuid) {
		return hotelQuotePreferentialMatterValueDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue = getByUuid(uuid);
		hotelQuotePreferentialMatterValue.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialMatterValue);
	}
}
