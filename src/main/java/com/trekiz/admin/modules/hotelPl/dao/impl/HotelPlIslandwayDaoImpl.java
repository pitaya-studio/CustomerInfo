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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlIslandwayDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlIslandwayDaoImpl extends BaseDaoImpl<HotelPlIslandway>  implements HotelPlIslandwayDao{
	@Override
	public HotelPlIslandway getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlIslandway hotelPlIslandway where hotelPlIslandway.uuid=? and hotelPlIslandway.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlIslandway)entity;
		}
		return null;
	}
	
	public Map<String, Map<String, List<HotelPlIslandway>>> findIslandWaysByHotelPlUuid(String hotelPlUuid) {
		List<HotelPlIslandway> islandWays = super.find("from HotelPlIslandway hotelPlIslandway where hotelPlIslandway.hotelPlUuid=? and hotelPlIslandway.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);

		Collections.sort(islandWays, new Comparator<HotelPlIslandway>(){
			@Override
			public int compare(HotelPlIslandway o1, HotelPlIslandway o2) {
				if(o1.getTravelerTypeUuid().equals(o2.getTravelerTypeUuid())) {
					return 0;
				} else if(o1.getTravelerTypeUuid().hashCode() > o2.getTravelerTypeUuid().hashCode()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		
		/*
		 *        Map<交通方式uuid, Map<日期文本, List<交通费用集合>>> 
		 * 数据结构Map<String, Map<String, List<HotelPlIslandway>>>
		 * 
		 * */
		Map<String, Map<String, List<HotelPlIslandway>>> islandWayMap = new LinkedHashMap<String, Map<String, List<HotelPlIslandway>>>();
		
		if(CollectionUtils.isNotEmpty(islandWays)) {
			for(HotelPlIslandway plIslandWay : islandWays) {
				//开始日期和结束日期拼接
				String startEndDate = plIslandWay.getStartDateString()+ "~" +plIslandWay.getEndDateString();
				
				Map<String, List<HotelPlIslandway>> map = new LinkedHashMap<String, List<HotelPlIslandway>>();
				List<HotelPlIslandway> entitys = new ArrayList<HotelPlIslandway>();
				
				//如果islandWayMap中没有此交通方式
				if(islandWayMap.get(plIslandWay.getIslandWay()) == null) {
					
					entitys.add(plIslandWay);
					//组装日期格式和交通费用集合
					map.put(startEndDate, entitys);
					islandWayMap.put(plIslandWay.getIslandWay(), map);
				} else {
					map = islandWayMap.get(plIslandWay.getIslandWay());
					//如果map中没有此开始结束日期，需要重新赋值
					if(map.get(startEndDate) == null) {
						entitys.add(plIslandWay);
						map.put(startEndDate, entitys);
					} else {
						map.get(startEndDate).add(plIslandWay);
					}
				}
			}
		}
		
		return islandWayMap;
	}
	
	public Map<String, List<HotelPlIslandway>> findHotelPlIslandwaysByHotelPlUuid(String hotelPlUuid) {
		List<HotelPlIslandway> islandWays = super.find("from HotelPlIslandway hotelPlIslandway where hotelPlIslandway.hotelPlUuid=? and hotelPlIslandway.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		Map<String, List<HotelPlIslandway>> islandWayMap = new LinkedHashMap<String, List<HotelPlIslandway>>();
		if(CollectionUtils.isNotEmpty(islandWays)) {
			for(HotelPlIslandway plIslandway: islandWays) {
				if(islandWayMap.get(plIslandway.getIslandWay()) == null) {
					List<HotelPlIslandway> hotelPlIslandway = new ArrayList<HotelPlIslandway>();
					hotelPlIslandway.add(plIslandway);
					
					islandWayMap.put(plIslandway.getIslandWay(), hotelPlIslandway);
				} else {
					islandWayMap.get(plIslandway.getIslandWay()).add(plIslandway);
				}
			}
		}
		
		return islandWayMap;
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的交通价格 add by zhanghao
	 * @return
	 */
	public List<HotelPlIslandway> getIslandWay4AutoQuotedPrice(String islandwayUuid,Date inDate,String travelerTypeUuid,String hotelPlUuid ){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,island_uuid,hotel_uuid,island_way,start_date,end_date,traveler_type_uuid,currency_id,amount,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_islandway " +
				"WHERE hotel_pl_uuid=? AND island_way=? AND start_date<=? AND end_date>=? AND traveler_type_uuid=? AND delFlag=? ");
		Object[] parameter = new Object[]{hotelPlUuid,islandwayUuid,DateUtils.date2String(inDate),DateUtils.date2String(inDate),travelerTypeUuid,BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlIslandway.class,parameter );
	}
	
	public int deletePlIslandwayByPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_islandway WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}
	
}
