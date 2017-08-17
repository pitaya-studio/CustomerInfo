/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;

import com.trekiz.admin.modules.hotel.dao.HotelGuestTypeDao;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPrice;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPriceQuery;

import java.util.*;

import com.trekiz.admin.modules.hotelPl.dao.*;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPriceDaoImpl extends BaseDaoImpl<HotelPlPrice>  implements HotelPlPriceDao{
	@Autowired
	private HotelGuestTypeDao hotelGuestTypeDao;
	
	@Override
	public HotelPlPrice getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPrice hotelPlPrice where hotelPlPrice.uuid=? and hotelPlPrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPrice)entity;
		}
		return null;
	}
	
	public Map<String, Map<String, List<HotelPlPrice>>> findPlPricesByHotelPlUuid(String hotelPlUuid) {
		List<HotelPlPrice> hotelPlPrices = super.find("from HotelPlPrice hotelPlPrice where hotelPlPrice.hotelPlUuid=? and hotelPlPrice.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		Collections.sort(hotelPlPrices, new Comparator<HotelPlPrice>(){
			@Override
			public int compare(HotelPlPrice o1, HotelPlPrice o2) {
				if(o1.getHotelGuestTypeUuid().equals(o2.getHotelGuestTypeUuid())) {
					return 0;
				} else if(o1.getHotelGuestTypeUuid().hashCode() > o2.getHotelGuestTypeUuid().hashCode()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		
		//过滤掉当前系统中不存在住客类型的优惠信息
		List<HotelGuestType> hotelGuestTypes = hotelGuestTypeDao.findByWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		List<String> hotelGuestTypeUuids = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(hotelGuestTypes)) {
			for(HotelGuestType hotelGuestType : hotelGuestTypes) {
				hotelGuestTypeUuids.add(hotelGuestType.getUuid());
			}
		}
		
		Iterator<HotelPlPrice> hotelPlPriceIter = hotelPlPrices.iterator();
		while(hotelPlPriceIter.hasNext()) {
			if(!hotelGuestTypeUuids.contains(hotelPlPriceIter.next().getHotelGuestTypeUuid())) {
				hotelPlPriceIter.remove();
			}
		}
		
		//Map<hotelRoomUuid,Map<日期,价单价格集合>>
		Map<String, Map<String, List<HotelPlPrice>>> priceMap = new LinkedHashMap<String, Map<String, List<HotelPlPrice>>>();
		
		if(CollectionUtils.isNotEmpty(hotelPlPrices)) {
			for(HotelPlPrice plPrice : hotelPlPrices) {
				//开始日期和结束日期拼接
				String startEndDate = plPrice.getStartDateString()+ "~" +plPrice.getEndDateString();
				
				Map<String, List<HotelPlPrice>> map = new LinkedHashMap<String, List<HotelPlPrice>>();
				List<HotelPlPrice> entitys = new ArrayList<HotelPlPrice>();
				String key=plPrice.getHotelRoomUuid()+","+plPrice.getHotelMealUuids();
				
				//如果priceMap中没有此房型
				if(!priceMap.containsKey(key)) {
					
					entitys.add(plPrice);
					//组装日期格式和交通费用集合
					map.put(startEndDate, entitys);
					priceMap.put(key, map);
				} else {
					map = priceMap.get(key);
					//如果map中没有此开始结束日期，需要重新赋值
					if(map.get(startEndDate) == null) {
						entitys.add(plPrice);
						map.put(startEndDate, entitys);
					} else {
						map.get(startEndDate).add(plPrice);
					}
				}
			}
		}
		
		return priceMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, List<HotelPlPriceJsonBean>> findHotelPlPricesByHotelPlUuid(String hotelPlUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT UUID,");
		sb.append("hotel_pl_uuid         AS hotelPlUuid,");
		sb.append("hotel_room_uuid       AS hotelRoomUuid,");
		sb.append("hotel_meal_uuids      AS hotelMealUuids,");
		sb.append("start_date            AS startDate,");
		sb.append("end_date              AS endDate,");
		sb.append("hotel_guest_type_uuid AS hotelGuestTypeUuid,");
		sb.append("currency_id           AS currencyId,");
		sb.append("amount                AS amount,");
		sb.append("price_type            AS priceType ");
		sb.append("FROM hotel_pl_price where hotel_pl_uuid=? and delFlag=?");
		List<HotelPlPriceJsonBean> hotelPlPrices = null;
		try{
			hotelPlPrices = (List<HotelPlPriceJsonBean>) super.findCustomObjBySql(sb.toString(), HotelPlPriceJsonBean.class, hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
			Collections.sort(hotelPlPrices, new Comparator<HotelPlPriceJsonBean>(){
				@Override
				public int compare(HotelPlPriceJsonBean o1, HotelPlPriceJsonBean o2) {
					if(o1.getHotelGuestTypeUuid().equals(o2.getHotelGuestTypeUuid())) {
						return 0;
					} else if(o1.getHotelGuestTypeUuid().hashCode() > o2.getHotelGuestTypeUuid().hashCode()) {
						return -1;
					} else {
						return 1;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Map<String, List<HotelPlPriceJsonBean>> priceMap = new LinkedHashMap<String, List<HotelPlPriceJsonBean>>();
		if(CollectionUtils.isNotEmpty(hotelPlPrices)) {
			for(HotelPlPriceJsonBean plPrice : hotelPlPrices) {
				if(priceMap.get(plPrice.getHotelRoomUuid()) == null) {
					List<HotelPlPriceJsonBean> plPrices = new ArrayList<HotelPlPriceJsonBean>();
					plPrices.add(plPrice);
					priceMap.put(plPrice.getHotelRoomUuid(), plPrices);
				} else {
					priceMap.get(plPrice.getHotelRoomUuid()).add(plPrice);
				}
			}
		}
		
		return priceMap;
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的房费 add by zhanghao
	 * @return
	 */
	public List<HotelPlPrice> getHotelPlPriceQuery4AutoQuotedPrice(HotelPlPriceQuery hotelPlPriceQuery){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,island_uuid,hotel_uuid,hotel_room_uuid,hotel_meal_uuids,start_date,end_date,hotel_guest_type_uuid,currency_id,amount,price_type,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_price  " +
				"where hotel_pl_uuid=? and start_date<=? and end_date>=? and hotel_guest_type_uuid=? " +
				"and hotel_room_uuid=? and hotel_meal_uuids like ? and price_type=? and delFlag=?");
		Object[] parameter = new Object[]{hotelPlPriceQuery.getHotelPlUuid(),DateUtils.date2String(hotelPlPriceQuery.getStartDate()),
				DateUtils.date2String(hotelPlPriceQuery.getStartDate()),hotelPlPriceQuery.getHotelGuestTypeUuid(),
				hotelPlPriceQuery.getHotelRoomUuid(),"%"+hotelPlPriceQuery.getHotelMealUuids()+"%",hotelPlPriceQuery.getPriceType(),BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlPrice.class,parameter );
	}
	
	public int deleteHotelPlPriceByHotelPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_price WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}

	@Override
	public List<HotelPlPrice> getPriceList(String hotelPlUuid) {
		
		List<HotelPlPrice> hotelPlPrices = super.find("from HotelPlPrice hotelPlPrice where hotelPlPrice.hotelPlUuid=? and hotelPlPrice.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		
		return hotelPlPrices;
	}
}
