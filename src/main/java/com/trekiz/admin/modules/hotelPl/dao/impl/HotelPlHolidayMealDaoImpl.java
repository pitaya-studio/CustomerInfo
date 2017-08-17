/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlHolidayMealDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlHolidayMeal;
import com.trekiz.admin.modules.hotelPl.query.HotelPlHolidayMealQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlHolidayMealDaoImpl extends BaseDaoImpl<HotelPlHolidayMeal>  implements HotelPlHolidayMealDao{
	@Override
	public HotelPlHolidayMeal getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlHolidayMeal hotelPlHolidayMeal where hotelPlHolidayMeal.uuid=? and hotelPlHolidayMeal.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlHolidayMeal)entity;
		}
		return null;
	}
	

	public List<HotelPlHolidayMeal> findPlHolidayMealsByHotelPlUuid(String hotelPlUuid) {
		 return super.find("from HotelPlHolidayMeal hotelPlHolidayMeal where hotelPlHolidayMeal.hotelPlUuid=? and hotelPlHolidayMeal.delFlag=? ", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public Map<String, List<HotelPlHolidayMeal>> findPlHolidayMealMapByHotelPlUuid(String hotelPlUuid) {
		 List<HotelPlHolidayMeal> mealList=super.find("from HotelPlHolidayMeal hotelPlHolidayMeal where hotelPlHolidayMeal.hotelPlUuid=? and hotelPlHolidayMeal.delFlag=? ", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		 Collections.sort(mealList, new Comparator<HotelPlHolidayMeal>(){
				@Override
				public int compare(HotelPlHolidayMeal o1, HotelPlHolidayMeal o2) {
					if(o1.getTravelerTypeUuid().equals(o2.getTravelerTypeUuid())) {
						return 0;
					} else if(o1.getTravelerTypeUuid().hashCode() > o2.getTravelerTypeUuid().hashCode()) {
						return -1;
					} else {
						return 1;
					}
				}
			});
		 Map<String, List<HotelPlHolidayMeal>> map=new HashMap<String, List<HotelPlHolidayMeal>>();
		 for(HotelPlHolidayMeal hph:mealList){
			 String startEndDate = hph.getStartDateString()+ "~" +hph.getEndDateString();
			 String key=hph.getHolidayMealName()+";"+startEndDate;
			 if(!map.containsKey(key)){
				 map.put(key, new ArrayList<HotelPlHolidayMeal>());
			 }
			 map.get(key).add(hph);
		 }
		 return map;
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的强制餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlHolidayMeal> getHotelPlHolidayMeal4AutoQuotedPrice(HotelPlHolidayMealQuery hotelPlHolidayMealQuery){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,island_uuid,hotel_uuid,hotel_meal_uuid,holidayMeal_uuid,holidayMeal_name,start_date,end_date,traveler_type_uuid,currency_id,amount,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_holidayMeal " +
				"where hotel_pl_uuid=? and start_date<=? and end_date>=? and traveler_type_uuid=? and delFlag=?");
		Object[] parameter = new Object[]{hotelPlHolidayMealQuery.getHotelPlUuid(),DateUtils.date2String(hotelPlHolidayMealQuery.getStartDate()),DateUtils.date2String(hotelPlHolidayMealQuery.getStartDate()),
				hotelPlHolidayMealQuery.getTravelerTypeUuid(),BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlHolidayMeal.class,parameter );
	}
	
	public int deletePlHolidayMealByPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_holidayMeal WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}
	
}
