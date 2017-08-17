/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupLowpriceDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupPriceDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupLowprice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupLowpriceInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupLowpriceQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupLowpriceService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupLowpriceServiceImpl  extends BaseService implements ActivityIslandGroupLowpriceService{
	@Autowired
	private ActivityIslandGroupLowpriceDao activityIslandGroupLowpriceDao;
	@Autowired
	private ActivityIslandGroupPriceDao activityIslandGroupPriceDao;
	@Autowired
	private TravelerTypeService travelerTypeService;

	public void save (ActivityIslandGroupLowprice activityIslandGroupLowprice){
		super.setOptInfo(activityIslandGroupLowprice, BaseService.OPERATION_ADD);
		activityIslandGroupLowpriceDao.saveObj(activityIslandGroupLowprice);
	}
	
	public void save (ActivityIslandGroupLowpriceInput activityIslandGroupLowpriceInput){
		ActivityIslandGroupLowprice activityIslandGroupLowprice = activityIslandGroupLowpriceInput.getActivityIslandGroupLowprice();
		super.setOptInfo(activityIslandGroupLowprice, BaseService.OPERATION_ADD);
		activityIslandGroupLowpriceDao.saveObj(activityIslandGroupLowprice);
	}
	
	public void update (ActivityIslandGroupLowprice activityIslandGroupLowprice){
		super.setOptInfo(activityIslandGroupLowprice, BaseService.OPERATION_UPDATE);
		activityIslandGroupLowpriceDao.updateObj(activityIslandGroupLowprice);
	}
	
	public ActivityIslandGroupLowprice getById(java.lang.Integer value) {
		return activityIslandGroupLowpriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroupLowprice obj = activityIslandGroupLowpriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroupLowprice> find(Page<ActivityIslandGroupLowprice> page, ActivityIslandGroupLowpriceQuery activityIslandGroupLowpriceQuery) {
		DetachedCriteria dc = activityIslandGroupLowpriceDao.createDetachedCriteria();
		
	   	if(activityIslandGroupLowpriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupLowpriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupLowpriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupLowpriceQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupLowpriceQuery.getActivityIslandGroupUuid()));
		}
	   	if(activityIslandGroupLowpriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupLowpriceQuery.getCurrencyId()));
	   	}
	   	if(activityIslandGroupLowpriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityIslandGroupLowpriceQuery.getPrice()));
	   	}
	   	if(activityIslandGroupLowpriceQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityIslandGroupLowpriceQuery.getSort()));
	   	}
	   	if(activityIslandGroupLowpriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupLowpriceQuery.getCreateBy()));
	   	}
		if(activityIslandGroupLowpriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupLowpriceQuery.getCreateDate()));
		}
	   	if(activityIslandGroupLowpriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupLowpriceQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupLowpriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupLowpriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupLowpriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupLowpriceDao.find(page, dc);
	}
	
	public List<ActivityIslandGroupLowprice> find( ActivityIslandGroupLowpriceQuery activityIslandGroupLowpriceQuery) {
		DetachedCriteria dc = activityIslandGroupLowpriceDao.createDetachedCriteria();
		
	   	if(activityIslandGroupLowpriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupLowpriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupLowpriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupLowpriceQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupLowpriceQuery.getActivityIslandGroupUuid()));
		}
	   	if(activityIslandGroupLowpriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupLowpriceQuery.getCurrencyId()));
	   	}
	   	if(activityIslandGroupLowpriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityIslandGroupLowpriceQuery.getPrice()));
	   	}
	   	if(activityIslandGroupLowpriceQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityIslandGroupLowpriceQuery.getSort()));
	   	}
	   	if(activityIslandGroupLowpriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupLowpriceQuery.getCreateBy()));
	   	}
		if(activityIslandGroupLowpriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupLowpriceQuery.getCreateDate()));
		}
	   	if(activityIslandGroupLowpriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupLowpriceQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupLowpriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupLowpriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupLowpriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupLowpriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupLowpriceDao.find(dc);
	}
	
	public ActivityIslandGroupLowprice getByUuid(String uuid) {
		return activityIslandGroupLowpriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroupLowprice activityIslandGroupLowprice = getByUuid(uuid);
		activityIslandGroupLowprice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroupLowprice);
	}
	
	public List<ActivityIslandGroupLowprice> getbyactivityIslandGroupUuid(String activityIslandGroupUuid){
		return activityIslandGroupLowpriceDao.findBySql("from ActivityIslandGroupLowprice where activityIslandGroupUuid=? and delFlag=?", activityIslandGroupUuid,BaseEntity.DEL_FLAG_NORMAL);
	}

	@Override
	public List<ActivityIslandGroupPrice> getLowPrice(String activityIslandUuid) {
		List<ActivityIslandGroupPrice> priceList=activityIslandGroupPriceDao.getLowPriceListByUuid(activityIslandUuid);
		Map<Integer,List<ActivityIslandGroupPrice>> map = new HashMap<Integer,List<ActivityIslandGroupPrice>>();
		int companyId = UserUtils.getUser().getCompany().getId().intValue();
		if(CollectionUtils.isNotEmpty(priceList)) {
			for(ActivityIslandGroupPrice groupPrice : priceList) {
				boolean flag=travelerTypeService.findIsExistBySysTravelerType(groupPrice.getType(), TravelerType.ALIAS_ADULT_UUID, companyId);
				if(flag){
					Integer currencyId=groupPrice.getCurrencyId();
					if(!map.containsKey(currencyId)){
						map.put(currencyId,new ArrayList<ActivityIslandGroupPrice>());
					}
					map.get(currencyId).add(groupPrice);
				}
			}
		}
		
		List<ActivityIslandGroupPrice> result = new ArrayList<ActivityIslandGroupPrice>();
		for(Integer i:map.keySet()){
			double price = 0.0;
			for(ActivityIslandGroupPrice group:map.get(i)){
				price = group.getPrice();
				
			}
		}
		return result;
		
	}

	@Override
	public void getLowPriceList(String uuid) {
		
		List<ActivityIslandGroupPrice> lowPrice=activityIslandGroupLowpriceDao.getLowPriceList(uuid);
		List<ActivityIslandGroupPrice> newPriceList=activityIslandGroupLowpriceDao.getLowPriceList(uuid);
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		if(lowPrice!=null){
			for(ActivityIslandGroupPrice price:lowPrice){
				if(!map.containsKey(price.getCurrencyId())){
					map.put(price.getCurrencyId(), price.getCurrencyId());
				}else{
					newPriceList.remove(price);
				}
			}
		}
		List<ActivityIslandGroupLowprice> lowPriceList=getLowprice(uuid);
		if(lowPriceList!=null){
			activityIslandGroupLowpriceDao.updateBySql("update activity_island_group_lowprice set delFlag =? where activity_island_uuid=?", BaseEntity.DEL_FLAG_DELETE,uuid);
		}
		for(ActivityIslandGroupPrice al:newPriceList){
			ActivityIslandGroupLowprice low=new ActivityIslandGroupLowprice();
			low.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
			low.setActivityIslandGroupUuid(al.getActivityIslandGroupUuid());
			low.setActivityIslandUuid(uuid);
			low.setCurrencyId(al.getCurrencyId());
			low.setPrice(al.getPrice());
			super.setOptInfo(low, BaseService.OPERATION_ADD);
			activityIslandGroupLowpriceDao.saveObj(low);
		}
	}

		public  List<ActivityIslandGroupLowprice> getLowprice(String uuid) {
			List<ActivityIslandGroupLowprice> lowPriceList=activityIslandGroupLowpriceDao.findBySql("select t.* from activity_hotel_group_lowprice t where t.activity_hotel_uuid=? and delFlag=?",ActivityIslandGroupLowprice.class, uuid,BaseEntity.DEL_FLAG_NORMAL);
			return lowPriceList;
		}

	
}
