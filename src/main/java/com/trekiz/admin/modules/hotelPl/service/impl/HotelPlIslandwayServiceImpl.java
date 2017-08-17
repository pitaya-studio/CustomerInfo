/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.Date;
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
import com.trekiz.admin.modules.hotelPl.dao.HotelPlIslandwayDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;
import com.trekiz.admin.modules.hotelPl.input.HotelPlIslandwayInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlIslandwayQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlIslandwayService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlIslandwayServiceImpl  extends BaseService implements HotelPlIslandwayService{
	@Autowired
	private HotelPlIslandwayDao hotelPlIslandwayDao;

	public void save (HotelPlIslandway hotelPlIslandway){
		super.setOptInfo(hotelPlIslandway, BaseService.OPERATION_ADD);
		hotelPlIslandwayDao.saveObj(hotelPlIslandway);
	}
	
	public void save (HotelPlIslandwayInput hotelPlIslandwayInput){
		HotelPlIslandway hotelPlIslandway = hotelPlIslandwayInput.getHotelPlIslandway();
		super.setOptInfo(hotelPlIslandway, BaseService.OPERATION_ADD);
		hotelPlIslandwayDao.saveObj(hotelPlIslandway);
	}
	
	public void update (HotelPlIslandway hotelPlIslandway){
		super.setOptInfo(hotelPlIslandway, BaseService.OPERATION_UPDATE);
		hotelPlIslandwayDao.updateObj(hotelPlIslandway);
	}
	
	public HotelPlIslandway getById(java.lang.Integer value) {
		return hotelPlIslandwayDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlIslandway obj = hotelPlIslandwayDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlIslandway> find(Page<HotelPlIslandway> page, HotelPlIslandwayQuery hotelPlIslandwayQuery) {
		DetachedCriteria dc = hotelPlIslandwayDao.createDetachedCriteria();
		
	   	if(hotelPlIslandwayQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlIslandwayQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlIslandwayQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlIslandwayQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlIslandwayQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlIslandwayQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlIslandwayQuery.getIslandWay()));
		}
		if(hotelPlIslandwayQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlIslandwayQuery.getStartDate()));
		}
		if(hotelPlIslandwayQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlIslandwayQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlIslandwayQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlIslandwayQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlIslandwayQuery.getCurrencyId()));
	   	}
	   	if(hotelPlIslandwayQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlIslandwayQuery.getAmount()));
	   	}
	   	if(hotelPlIslandwayQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlIslandwayQuery.getCreateBy()));
	   	}
		if(hotelPlIslandwayQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlIslandwayQuery.getCreateDate()));
		}
	   	if(hotelPlIslandwayQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlIslandwayQuery.getUpdateBy()));
	   	}
		if(hotelPlIslandwayQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlIslandwayQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlIslandwayQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlIslandwayDao.find(page, dc);
	}
	
	public List<HotelPlIslandway> find( HotelPlIslandwayQuery hotelPlIslandwayQuery) {
		DetachedCriteria dc = hotelPlIslandwayDao.createDetachedCriteria();
		
	   	if(hotelPlIslandwayQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlIslandwayQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlIslandwayQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlIslandwayQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlIslandwayQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlIslandwayQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlIslandwayQuery.getIslandWay()));
		}
		if(hotelPlIslandwayQuery.getStartDate()!=null){
			dc.add(Restrictions.eq("startDate", hotelPlIslandwayQuery.getStartDate()));
		}
		if(hotelPlIslandwayQuery.getEndDate()!=null){
			dc.add(Restrictions.eq("endDate", hotelPlIslandwayQuery.getEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getTravelerTypeUuid())){
			dc.add(Restrictions.eq("travelerTypeUuid", hotelPlIslandwayQuery.getTravelerTypeUuid()));
		}
	   	if(hotelPlIslandwayQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlIslandwayQuery.getCurrencyId()));
	   	}
	   	if(hotelPlIslandwayQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelPlIslandwayQuery.getAmount()));
	   	}
	   	if(hotelPlIslandwayQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlIslandwayQuery.getCreateBy()));
	   	}
		if(hotelPlIslandwayQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlIslandwayQuery.getCreateDate()));
		}
	   	if(hotelPlIslandwayQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlIslandwayQuery.getUpdateBy()));
	   	}
		if(hotelPlIslandwayQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlIslandwayQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlIslandwayQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlIslandwayQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlIslandwayDao.find(dc);
	}
	
	public HotelPlIslandway getByUuid(String uuid) {
		return hotelPlIslandwayDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlIslandway hotelPlIslandway = getByUuid(uuid);
		hotelPlIslandway.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlIslandway);
	}
	
	public Map<String, Map<String, List<HotelPlIslandway>>> findIslandWaysByHotelPlUuid(String hotelPlUuid) {
		return hotelPlIslandwayDao.findIslandWaysByHotelPlUuid(hotelPlUuid);
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的交通价格 add by zhanghao
	 * @return
	 */
	public List<HotelPlIslandway> getIslandWay4AutoQuotedPrice(String islandwayUuid,Date inDate,String travelerTypeUuid,String hotelPlUuid ){
		return hotelPlIslandwayDao.getIslandWay4AutoQuotedPrice( islandwayUuid, inDate, travelerTypeUuid, hotelPlUuid );
	}
	
}
