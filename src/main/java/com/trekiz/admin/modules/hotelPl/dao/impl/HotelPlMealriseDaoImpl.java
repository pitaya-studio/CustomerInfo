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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.dao.HotelMealDao;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlMealriseDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlMealrise;
import com.trekiz.admin.modules.hotelPl.query.HotelPlMealriseQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlMealriseDaoImpl extends BaseDaoImpl<HotelPlMealrise>  implements HotelPlMealriseDao{
	@Autowired
	private HotelMealDao hotelMealDao; 
	
	@Override
	public HotelPlMealrise getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlMealrise hotelPlMealrise where hotelPlMealrise.uuid=? and hotelPlMealrise.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlMealrise)entity;
		}
		return null;
	}
	/**
	 * 
	 */
	public Map<String, Map<String, List<HotelPlMealrise>>> findPlMealRisesByHotelPlUuid(String hotelPlUuid) {
		List<HotelPlMealrise> plMealRises = super.find("from HotelPlMealrise hotelPlMealrise where hotelPlMealrise.hotelPlUuid=? and hotelPlMealrise.delFlag=? ", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		Collections.sort(plMealRises, new Comparator<HotelPlMealrise>() {

			@Override
			public int compare(HotelPlMealrise o1, HotelPlMealrise o2) {
				if(o1.getTravelerTypeUuid().equals(o2.getTravelerTypeUuid())){
					return 0;
				}else if(o1.getTravelerTypeUuid().hashCode()>o2.getTravelerTypeUuid().hashCode()){
					
					return -1;
				}else{
					return 1;
				}
			}
			
		});
		//Map<餐型的uuid,Map<升级餐型的Uuid,升餐费用的集合>>
		//Map<hotelMealUuid,Map<hotelMealRiseUuid,list<升餐费用的集合>>>
		Map<String, Map<String, List<HotelPlMealrise>>> mealRiseMaps=new LinkedHashMap<String, Map<String, List<HotelPlMealrise>>>();
		if(CollectionUtils.isNotEmpty(plMealRises)){
			for(HotelPlMealrise hotelPlRise:plMealRises){
				Map<String, List<HotelPlMealrise>> maps=new LinkedHashMap<String, List<HotelPlMealrise>>();
				List<HotelPlMealrise> mealRiseList=new ArrayList<HotelPlMealrise>();
				String linkDate = DateUtils.date2String(hotelPlRise.getStartDate(), DateUtils.DATE_PATTERN_YYYY_MM_DD) + "~" + DateUtils.date2String(hotelPlRise.getEndDate(), DateUtils.DATE_PATTERN_YYYY_MM_DD);
				String mealRiseLinkDate = hotelPlRise.getHotelMealRiseUuid()+"|"+linkDate;
				
				//如果mealRiseMaps的key不包含此餐型
				if(!mealRiseMaps.containsKey(hotelPlRise.getHotelMealUuid())){
					mealRiseList.add(hotelPlRise);
					maps.put(mealRiseLinkDate,mealRiseList );
					mealRiseMaps.put(hotelPlRise.getHotelMealUuid(), maps);
				}else {
					maps=mealRiseMaps.get(hotelPlRise.getHotelMealUuid());
					
					if(!maps.containsKey(mealRiseLinkDate)){
						mealRiseList.add(hotelPlRise);
						maps.put(mealRiseLinkDate,mealRiseList );
					}else{
						maps.get(mealRiseLinkDate).add(hotelPlRise);
					}
				}
			}
		}
		return mealRiseMaps;
	}
	
	public Map<String, List<HotelPlMealrise>> findHotelPlMealriseByHotelPlUuid(String hotelPlUuid) {
		Map<String, List<HotelPlMealrise>> hotelRiseMealMap = new LinkedHashMap<String, List<HotelPlMealrise>>();
		List<HotelPlMealrise> plMealRises = super.find("from HotelPlMealrise hotelPlMealrise where hotelPlMealrise.hotelPlUuid=? and hotelPlMealrise.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		if(CollectionUtils.isNotEmpty(plMealRises)) {
			for(HotelPlMealrise plMealRise : plMealRises){
				HotelMeal hotelMeal = hotelMealDao.getByUuid(plMealRise.getHotelMealRiseUuid());
				if(hotelMeal != null) {
					plMealRise.setHotelMealRiseText(hotelMeal.getMealName());
				}
				
				if(hotelRiseMealMap.get(plMealRise.getHotelMealUuid()) == null) {
					List<HotelPlMealrise> plMealRiseList = new ArrayList<HotelPlMealrise>();
					plMealRiseList.add(plMealRise);
					
					hotelRiseMealMap.put(plMealRise.getHotelMealUuid(), plMealRiseList);
				} else {
					hotelRiseMealMap.get(plMealRise.getHotelMealUuid()).add(plMealRise);
				}
			}
		}
		return hotelRiseMealMap;
	}
	
	
	/**
	 * 自动报价 根据条件筛选 符合条件的生餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlMealrise> getHotelPlMealrise4AutoQuotedPrice(HotelPlMealriseQuery hotelPlMealriseQuery){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,island_uuid,hotel_uuid,hotel_meal_uuid,hotel_meal_rise_uuid,start_date,end_date,traveler_type_uuid,currency_id,amount,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_mealrise " +
				"where hotel_pl_uuid=? and start_date<=? and end_date>=? and traveler_type_uuid=? " +
				"and hotel_meal_uuid=? and hotel_meal_rise_uuid=? and delFlag=?");
		Object[] parameter = new Object[]{hotelPlMealriseQuery.getHotelPlUuid(),DateUtils.date2String(hotelPlMealriseQuery.getStartDate()),DateUtils.date2String(hotelPlMealriseQuery.getStartDate()),
				hotelPlMealriseQuery.getTravelerTypeUuid(),hotelPlMealriseQuery.getHotelMealUuid(),hotelPlMealriseQuery.getHotelMealRiseUuid(),BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlMealrise.class,parameter );
	}
	
	public int deletePlMealriseByPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_mealrise WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}
	
}
