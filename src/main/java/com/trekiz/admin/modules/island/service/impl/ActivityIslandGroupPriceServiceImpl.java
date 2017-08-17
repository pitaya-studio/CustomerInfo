/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupAirlineDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupPriceDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupPriceInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupPriceQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupPriceServiceImpl  extends BaseService implements ActivityIslandGroupPriceService{
	@Autowired
	private ActivityIslandGroupPriceDao activityIslandGroupPriceDao;
	@Autowired
	private ActivityIslandGroupAirlineDao activityIslandGroupAirlineDao;

	public void save (ActivityIslandGroupPrice activityIslandGroupPrice){
		super.setOptInfo(activityIslandGroupPrice, BaseService.OPERATION_ADD);
		activityIslandGroupPriceDao.saveObj(activityIslandGroupPrice);
	}
	
	public void save (ActivityIslandGroupPriceInput activityIslandGroupPriceInput){
		ActivityIslandGroupPrice activityIslandGroupPrice = activityIslandGroupPriceInput.getActivityIslandGroupPrice();
		super.setOptInfo(activityIslandGroupPrice, BaseService.OPERATION_ADD);
		activityIslandGroupPriceDao.saveObj(activityIslandGroupPrice);
	}
	
	public void update (ActivityIslandGroupPrice activityIslandGroupPrice){
		super.setOptInfo(activityIslandGroupPrice, BaseService.OPERATION_UPDATE);
		activityIslandGroupPriceDao.updateObj(activityIslandGroupPrice);
	}
	
	public ActivityIslandGroupPrice getById(java.lang.Integer value) {
		return activityIslandGroupPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroupPrice obj = activityIslandGroupPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroupPrice> find(Page<ActivityIslandGroupPrice> page, ActivityIslandGroupPriceQuery activityIslandGroupPriceQuery) {
		DetachedCriteria dc = activityIslandGroupPriceDao.createDetachedCriteria();
		
	   	if(activityIslandGroupPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupPriceQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupPriceQuery.getActivityIslandGroupUuid()));
		}
	   	if(activityIslandGroupPriceQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", activityIslandGroupPriceQuery.getType()));
	   	}
	   	if(activityIslandGroupPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupPriceQuery.getCurrencyId()));
	   	}
	   	if(activityIslandGroupPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityIslandGroupPriceQuery.getPrice()));
	   	}
	   	if(activityIslandGroupPriceQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityIslandGroupPriceQuery.getSort()));
	   	}
	   	if(activityIslandGroupPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupPriceQuery.getCreateBy()));
	   	}
		if(activityIslandGroupPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupPriceQuery.getCreateDate()));
		}
	   	if(activityIslandGroupPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupPriceQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupPriceDao.find(page, dc);
	}
	
	public List<ActivityIslandGroupPrice> find( ActivityIslandGroupPriceQuery activityIslandGroupPriceQuery) {
		DetachedCriteria dc = activityIslandGroupPriceDao.createDetachedCriteria();
		
	   	if(activityIslandGroupPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupPriceQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupPriceQuery.getActivityIslandGroupUuid()));
		}
	   	if(activityIslandGroupPriceQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", activityIslandGroupPriceQuery.getType()));
	   	}
	   	if(activityIslandGroupPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupPriceQuery.getCurrencyId()));
	   	}
	   	if(activityIslandGroupPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityIslandGroupPriceQuery.getPrice()));
	   	}
	   	if(activityIslandGroupPriceQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityIslandGroupPriceQuery.getSort()));
	   	}
	   	if(activityIslandGroupPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupPriceQuery.getCreateBy()));
	   	}
		if(activityIslandGroupPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupPriceQuery.getCreateDate()));
		}
	   	if(activityIslandGroupPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupPriceQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupPriceDao.find(dc);
	}
	
	public ActivityIslandGroupPrice getByUuid(String uuid) {
		return activityIslandGroupPriceDao.getByUuid(uuid);
	}
	
	public List<ActivityIslandGroupPrice> getByactivityIslandGroupUuid(String activityIslandGroupUuid){
		return activityIslandGroupPriceDao.find("from ActivityIslandGroupPrice where activityIslandGroupUuid=? and delFlag=?", activityIslandGroupUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroupPrice activityIslandGroupPrice = getByUuid(uuid);
		activityIslandGroupPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroupPrice);
	}
	
	public List<ActivityIslandGroupPrice> getPriceListByGroupUuid(String groupUuid) {
		List<ActivityIslandGroupPrice> groupPrices = activityIslandGroupPriceDao.getPriceListByGroupUuid(groupUuid);
		
		if(CollectionUtils.isNotEmpty(groupPrices)) {
			for(ActivityIslandGroupPrice groupPrice : groupPrices) {
				if(StringUtils.isNotEmpty(groupPrice.getActivityIslandGroupAirlineUuid())) {
					groupPrice.setGroupAirline(activityIslandGroupAirlineDao.getByUuid(groupPrice.getActivityIslandGroupAirlineUuid()));
				}
			}
		}
		
		return groupPrices;
	}
	public List<ActivityIslandGroupPrice> getPriceListByGroupUuid(String groupUuid,String hotelUuid) {
		List<ActivityIslandGroupPrice> groupPrices = this.getPriceFilterTravel(groupUuid, hotelUuid);
		
		if(CollectionUtils.isNotEmpty(groupPrices)) {
			for(ActivityIslandGroupPrice groupPrice : groupPrices) {
				if(StringUtils.isNotEmpty(groupPrice.getActivityIslandGroupAirlineUuid())) {
					groupPrice.setGroupAirline(activityIslandGroupAirlineDao.getByUuid(groupPrice.getActivityIslandGroupAirlineUuid()));
				}
			}
		}
		
		return groupPrices;
	}
	
	/**
	 * 根据团期uuid,酒店uuid查询ActivityIslandGroupPrice
	 * @param groupUuid
	 * @param hotelUuid
	 * @return
	 */
	public List<ActivityIslandGroupPrice> getPriceFilterTravel(String groupUuid,String hotelUuid){
		String sql = "select id,uuid,activity_island_uuid,activity_island_group_uuid,activity_island_group_airline_uuid," +
				     "type,currency_id,price,sort,createBy,createDate,updateBy,updateDate,delFlag " +
				     "from activity_island_group_price aigp " +
				     "where aigp.delFlag='0' "+
                     "and aigp.activity_island_group_uuid = ? "+
                     "and aigp.type in " +
                     "(select tt.uuid from traveler_type tt join hotel_traveler_type_relation httr on tt.uuid = httr.traveler_type_uuid "+ 
                     "where tt.status='1' and tt.delFlag='0' and httr.delFlag='0' and httr.hotel_uuid = ? )";
		return activityIslandGroupPriceDao.findBySql(sql, ActivityIslandGroupPrice.class, groupUuid,hotelUuid);
	}
}
