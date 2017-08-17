/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.HotelDao;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRelHotelDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRelHotel;
import com.trekiz.admin.modules.island.dao.IslandDao;
import com.trekiz.admin.modules.island.entity.Island;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialRelHotelDaoImpl extends BaseDaoImpl<HotelPlPreferentialRelHotel>  implements HotelPlPreferentialRelHotelDao{
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private IslandDao islandDao;
	@Override
	public HotelPlPreferentialRelHotel getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel where hotelPlPreferentialRelHotel.uuid=? and hotelPlPreferentialRelHotel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPreferentialRelHotel)entity;
		}
		return null;
	}
	
	public HotelPlPreferentialRelHotel getRelHotelByPreferentialUuid(String preferentialUuid) {
		Object entity = super.createQuery("from HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel where hotelPlPreferentialRelHotel.hotelPlPreferentialUuid=? and hotelPlPreferentialRelHotel.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			HotelPlPreferentialRelHotel relHotel = (HotelPlPreferentialRelHotel)entity;
			if(StringUtils.isNotEmpty(relHotel.getIslandWay())) {
				relHotel.setIslandWayList(sysCompanyDictViewDao.findByUuids(relHotel.getIslandWay().split(";")));
			}
			
			//加载关联酒店名称（格式：海岛游名称+酒店名称）
			String hotelName = "";
			String islandName = "";
			Hotel hotel = hotelDao.getByUuid(relHotel.getHotelUuid());
			if(hotel != null) {
				hotelName = hotel.getNameCn();
				Island island = islandDao.getByUuid(hotel.getIslandUuid());
				if(island != null) {
					islandName = island.getIslandName();
				}
				relHotel.setHotelText(((StringUtils.isNotEmpty(islandName)) ? islandName + "+" : islandName) + hotelName);
			}
			
			return relHotel;
		}
		return null;
	}
	
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids) {
		if(CollectionUtils.isEmpty(preferentialUuids)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM hotel_pl_preferential_relHotel WHERE hotel_pl_preferential_uuid IN ");
		sb.append("(");
		for(String preferentialUuid : preferentialUuids) {
			sb.append("'" + preferentialUuid + "',");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		return super.createSqlQuery(sb.toString()).executeUpdate();
	}
	
}
