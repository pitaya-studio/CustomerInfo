/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.HashMap;
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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupLowpriceDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupLowpriceInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupLowpriceQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupLowpriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupLowpriceServiceImpl  extends BaseService implements ActivityHotelGroupLowpriceService{
	@Autowired
	private ActivityHotelGroupLowpriceDao activityHotelGroupLowpriceDao;
	

	public void save (ActivityHotelGroupLowprice activityHotelGroupLowprice){
		super.setOptInfo(activityHotelGroupLowprice, BaseService.OPERATION_ADD);
		activityHotelGroupLowpriceDao.saveObj(activityHotelGroupLowprice);
	}
	
	public void save (ActivityHotelGroupLowpriceInput activityHotelGroupLowpriceInput){
		ActivityHotelGroupLowprice activityHotelGroupLowprice = activityHotelGroupLowpriceInput.getActivityHotelGroupLowprice();
		super.setOptInfo(activityHotelGroupLowprice, BaseService.OPERATION_ADD);
		activityHotelGroupLowpriceDao.saveObj(activityHotelGroupLowprice);
	}
	
	public void update (ActivityHotelGroupLowprice activityHotelGroupLowprice){
		super.setOptInfo(activityHotelGroupLowprice, BaseService.OPERATION_UPDATE);
		activityHotelGroupLowpriceDao.updateObj(activityHotelGroupLowprice);
	}
	
	public ActivityHotelGroupLowprice getById(java.lang.Integer value) {
		return activityHotelGroupLowpriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroupLowprice obj = activityHotelGroupLowpriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroupLowprice> find(Page<ActivityHotelGroupLowprice> page, ActivityHotelGroupLowpriceQuery activityHotelGroupLowpriceQuery) {
		DetachedCriteria dc = activityHotelGroupLowpriceDao.createDetachedCriteria();
		
	   	if(activityHotelGroupLowpriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupLowpriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupLowpriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupLowpriceQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupLowpriceQuery.getActivityHotelGroupUuid()));
		}
	   	if(activityHotelGroupLowpriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupLowpriceQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupLowpriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityHotelGroupLowpriceQuery.getPrice()));
	   	}
	   	if(activityHotelGroupLowpriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupLowpriceQuery.getCreateBy()));
	   	}
		if(activityHotelGroupLowpriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupLowpriceQuery.getCreateDate()));
		}
	   	if(activityHotelGroupLowpriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupLowpriceQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupLowpriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupLowpriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupLowpriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupLowpriceDao.find(page, dc);
	}
	
	public List<ActivityHotelGroupLowprice> find( ActivityHotelGroupLowpriceQuery activityHotelGroupLowpriceQuery) {
		DetachedCriteria dc = activityHotelGroupLowpriceDao.createDetachedCriteria();
		
	   	if(activityHotelGroupLowpriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupLowpriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupLowpriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupLowpriceQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupLowpriceQuery.getActivityHotelGroupUuid()));
		}
	   	if(activityHotelGroupLowpriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupLowpriceQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupLowpriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityHotelGroupLowpriceQuery.getPrice()));
	   	}
	   	if(activityHotelGroupLowpriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupLowpriceQuery.getCreateBy()));
	   	}
		if(activityHotelGroupLowpriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupLowpriceQuery.getCreateDate()));
		}
	   	if(activityHotelGroupLowpriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupLowpriceQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupLowpriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupLowpriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupLowpriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupLowpriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupLowpriceDao.find(dc);
	}
	
	public ActivityHotelGroupLowprice getByUuid(String uuid) {
		return activityHotelGroupLowpriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroupLowprice activityHotelGroupLowprice = getByUuid(uuid);
		activityHotelGroupLowprice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroupLowprice);
	}

	@Override
	public List<ActivityHotelGroupLowprice> getLowPriceListByGroupUuid(
			String uuid) {
		
		return activityHotelGroupLowpriceDao.getLowPriceListByGroupUuid(uuid);
	}
	
	@Override
	public void getPriceList(String uuid) {
		List<ActivityHotelGroupPrice> priceList=activityHotelGroupLowpriceDao.getLowpriceListByUuid(uuid);
		List<ActivityHotelGroupPrice> newPriceList=activityHotelGroupLowpriceDao.getLowpriceListByUuid(uuid);
		Map<Integer, Integer> map=new HashMap<Integer, Integer>();
		if(priceList!=null){
			for(ActivityHotelGroupPrice price:priceList){
				if(!map.containsKey(price.getCurrencyId())){
					map.put(price.getCurrencyId(), price.getCurrencyId());
				}else{
					newPriceList.remove(price);
				}
			}
		}
		List<ActivityHotelGroupLowprice> lowPriceList=getLowprice(uuid);
		if(lowPriceList!=null){
		activityHotelGroupLowpriceDao.updateBySql("update activity_hotel_group_lowprice set delFlag = "+BaseEntity.DEL_FLAG_DELETE+" where activity_hotel_uuid = ?", uuid);}
		for(ActivityHotelGroupPrice ret:newPriceList){
			ActivityHotelGroupLowprice low = new ActivityHotelGroupLowprice();
			low.setActivityHotelUuid(ret.getActivityHotelUuid());
			low.setActivityHotelGroupUuid(ret.getActivityHotelGroupUuid());
			low.setPrice(ret.getPrice());
			low.setCurrencyId(ret.getCurrencyId());
			super.setOptInfo(low, BaseService.OPERATION_ADD);
			activityHotelGroupLowpriceDao.saveObj(low);
		}
	}

	public List<ActivityHotelGroupLowprice> getLowprice(String uuid) {
		List<ActivityHotelGroupLowprice> lowPriceList=activityHotelGroupLowpriceDao.findBySql("select t.* from activity_hotel_group_lowprice t where t.activity_hotel_uuid=? and delFlag=?",ActivityHotelGroupLowprice.class,uuid,BaseEntity.DEL_FLAG_NORMAL);
		return lowPriceList;
	}

	

	

	
}
