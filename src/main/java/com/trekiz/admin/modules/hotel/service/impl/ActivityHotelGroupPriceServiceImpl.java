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

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupPriceDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupPriceInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupPriceQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupPriceServiceImpl  extends BaseService implements ActivityHotelGroupPriceService{
	@Autowired
	private ActivityHotelGroupPriceDao activityHotelGroupPriceDao;

	public void save (ActivityHotelGroupPrice activityHotelGroupPrice){
		super.setOptInfo(activityHotelGroupPrice, BaseService.OPERATION_ADD);
		activityHotelGroupPriceDao.saveObj(activityHotelGroupPrice);
	}
	
	public void save (ActivityHotelGroupPriceInput activityHotelGroupPriceInput){
		ActivityHotelGroupPrice activityHotelGroupPrice = activityHotelGroupPriceInput.getActivityHotelGroupPrice();
		super.setOptInfo(activityHotelGroupPrice, BaseService.OPERATION_ADD);
		activityHotelGroupPriceDao.saveObj(activityHotelGroupPrice);
	}
	
	public void update (ActivityHotelGroupPrice activityHotelGroupPrice){
		super.setOptInfo(activityHotelGroupPrice, BaseService.OPERATION_UPDATE);
		activityHotelGroupPriceDao.updateObj(activityHotelGroupPrice);
	}
	
	public ActivityHotelGroupPrice getById(java.lang.Integer value) {
		return activityHotelGroupPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroupPrice obj = activityHotelGroupPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroupPrice> find(Page<ActivityHotelGroupPrice> page, ActivityHotelGroupPriceQuery activityHotelGroupPriceQuery) {
		DetachedCriteria dc = activityHotelGroupPriceDao.createDetachedCriteria();
		
	   	if(activityHotelGroupPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupPriceQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupPriceQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getType())){
			dc.add(Restrictions.eq("type", activityHotelGroupPriceQuery.getType()));
		}
	   	if(activityHotelGroupPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupPriceQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityHotelGroupPriceQuery.getPrice()));
	   	}
	   	if(activityHotelGroupPriceQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityHotelGroupPriceQuery.getSort()));
	   	}
	   	if(activityHotelGroupPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupPriceQuery.getCreateBy()));
	   	}
		if(activityHotelGroupPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupPriceQuery.getCreateDate()));
		}
	   	if(activityHotelGroupPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupPriceQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getDelFlage())){
			dc.add(Restrictions.eq("delFlage", activityHotelGroupPriceQuery.getDelFlage()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupPriceDao.find(page, dc);
	}
	
	public List<ActivityHotelGroupPrice> find( ActivityHotelGroupPriceQuery activityHotelGroupPriceQuery) {
		DetachedCriteria dc = activityHotelGroupPriceDao.createDetachedCriteria();
		
	   	if(activityHotelGroupPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupPriceQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupPriceQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getType())){
			dc.add(Restrictions.eq("type", activityHotelGroupPriceQuery.getType()));
		}
	   	if(activityHotelGroupPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupPriceQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityHotelGroupPriceQuery.getPrice()));
	   	}
	   	if(activityHotelGroupPriceQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityHotelGroupPriceQuery.getSort()));
	   	}
	   	if(activityHotelGroupPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupPriceQuery.getCreateBy()));
	   	}
		if(activityHotelGroupPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupPriceQuery.getCreateDate()));
		}
	   	if(activityHotelGroupPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupPriceQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupPriceQuery.getDelFlage())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupPriceQuery.getDelFlage()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupPriceDao.find(dc);
	}
	
	public ActivityHotelGroupPrice getByUuid(String uuid) {
		return activityHotelGroupPriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroupPrice activityHotelGroupPrice = getByUuid(uuid);
		activityHotelGroupPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroupPrice);
	}

	@Override
	public List<ActivityHotelGroupPrice> getPriceListByGroupUuid(String uuid) {
		// TODO Auto-generated method stub
		
		return activityHotelGroupPriceDao.getPriceListByGroupUuid(uuid);
	}
	
	/**
	 * 根据团期uuid,酒店uuid查询ActivityHotelGroupPrice
	 * @param groupUuid
	 * @param hotelUuid
	 * @return
	 */
	public List<ActivityHotelGroupPrice> getPriceFilterTravel(String groupUuid,String hotelUuid){
		String sql = "select id,uuid,activity_hotel_uuid,activity_hotel_group_uuid," +
				     "type,currency_id,price,sort,createBy,createDate,updateBy,updateDate,delFlag " +
				     "from activity_hotel_group_price ahgp " +
				     "where ahgp.delFlag='0' "+
                     "and ahgp.activity_hotel_group_uuid = ? "+
                     "and ahgp.type in " +
                     "(select tt.uuid from traveler_type tt join hotel_traveler_type_relation httr on tt.uuid = httr.traveler_type_uuid "+ 
                     "where tt.status='1' and tt.delFlag='0' and httr.delFlag='0' and httr.hotel_uuid = ? )";
		return activityHotelGroupPriceDao.findBySql(sql, ActivityHotelGroupPrice.class, groupUuid,hotelUuid);
	}
}
