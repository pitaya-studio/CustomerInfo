/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlMealriseDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlMealrise;
import com.trekiz.admin.modules.hotelPl.input.HotelPlMealriseInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlMealriseQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlMealriseService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlMealriseServiceImpl  extends BaseService implements HotelPlMealriseService{
	@Autowired
	private HotelPlMealriseDao hotelPlMealriseDao;

	public void save (HotelPlMealrise hotelPlMealrise){
		super.setOptInfo(hotelPlMealrise, BaseService.OPERATION_ADD);
		hotelPlMealriseDao.saveObj(hotelPlMealrise);
	}
	
	public void save (HotelPlMealriseInput hotelPlMealriseInput){
		HotelPlMealrise hotelPlMealrise = hotelPlMealriseInput.getHotelPlMealrise();
		super.setOptInfo(hotelPlMealrise, BaseService.OPERATION_ADD);
		hotelPlMealriseDao.saveObj(hotelPlMealrise);
	}
	
	public void update (HotelPlMealrise hotelPlMealrise){
		super.setOptInfo(hotelPlMealrise, BaseService.OPERATION_UPDATE);
		hotelPlMealriseDao.updateObj(hotelPlMealrise);
	}
	
	public HotelPlMealrise getById(java.lang.Integer value) {
		return hotelPlMealriseDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlMealrise obj = hotelPlMealriseDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlMealrise> find(Page<HotelPlMealrise> page, HotelPlMealriseQuery hotelPlMealriseQuery) {
		DetachedCriteria dc = hotelPlMealriseDao.createDetachedCriteria();
		
	   	if(hotelPlMealriseQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlMealriseQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlMealriseQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlMealriseQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlMealriseQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlMealriseQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelPlMealriseQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelMealRiseUuid())){
			dc.add(Restrictions.eq("hotelMealRiseUuid", hotelPlMealriseQuery.getHotelMealRiseUuid()));
		}
		if(hotelPlMealriseQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlMealriseQuery.getStartDate()));
		}
		if(hotelPlMealriseQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlMealriseQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlMealriseQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlMealriseQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlMealriseQuery.getCurrencyId()));
	   	}
	   	if(hotelPlMealriseQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlMealriseQuery.getAmount()));
	   	}
	   	if(hotelPlMealriseQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlMealriseQuery.getCreateBy()));
	   	}
		if(hotelPlMealriseQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlMealriseQuery.getCreateDate()));
		}
	   	if(hotelPlMealriseQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlMealriseQuery.getUpdateBy()));
	   	}
		if(hotelPlMealriseQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlMealriseQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlMealriseQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlMealriseDao.find(page, dc);
	}
	
	public List<HotelPlMealrise> find( HotelPlMealriseQuery hotelPlMealriseQuery) {
		DetachedCriteria dc = hotelPlMealriseDao.createDetachedCriteria();
		
	   	if(hotelPlMealriseQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlMealriseQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlMealriseQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlMealriseQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlMealriseQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlMealriseQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", hotelPlMealriseQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getHotelMealRiseUuid())){
			dc.add(Restrictions.eq("hotelMealRiseUuid", hotelPlMealriseQuery.getHotelMealRiseUuid()));
		}
		if(hotelPlMealriseQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlMealriseQuery.getStartDate()));
		}
		if(hotelPlMealriseQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlMealriseQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlMealriseQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlMealriseQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlMealriseQuery.getCurrencyId()));
	   	}
	   	if(hotelPlMealriseQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlMealriseQuery.getAmount()));
	   	}
	   	if(hotelPlMealriseQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlMealriseQuery.getCreateBy()));
	   	}
		if(hotelPlMealriseQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlMealriseQuery.getCreateDate()));
		}
	   	if(hotelPlMealriseQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlMealriseQuery.getUpdateBy()));
	   	}
		if(hotelPlMealriseQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlMealriseQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlMealriseQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlMealriseQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlMealriseDao.find(dc);
	}
	
	public HotelPlMealrise getByUuid(String uuid) {
		return hotelPlMealriseDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlMealrise hotelPlMealrise = getByUuid(uuid);
		hotelPlMealrise.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlMealrise);
	}
	
	public Map<String, Map<String, List<HotelPlMealrise>>> findPlMealRisesByHotelPlUuid(String hotelPlUuid) {
		return hotelPlMealriseDao.findPlMealRisesByHotelPlUuid(hotelPlUuid);
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的生餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlMealrise> getHotelPlMealrise4AutoQuotedPrice(HotelPlMealriseQuery hotelPlMealriseQuery){
		return hotelPlMealriseDao.getHotelPlMealrise4AutoQuotedPrice(hotelPlMealriseQuery);
	}
	
}
