/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRelDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialRelDaoImpl extends BaseDaoImpl<HotelPlPreferentialRel>  implements HotelPlPreferentialRelDao{
	@Override
	public HotelPlPreferentialRel getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPreferentialRel hotelPlPreferentialRel where hotelPlPreferentialRel.uuid=? and hotelPlPreferentialRel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPreferentialRel)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelPlPreferentialRel> getPreferentialRelsByPreferentialUuid(String preferentialUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT rel.id AS id,rel.uuid AS UUID,rel.hotel_pl_uuid AS hotelPlUuid,rel.island_uuid AS islandUuid,rel.hotel_uuid AS hotelUuid,rel.hotel_pl_preferential_uuid AS hotelPlPreferentialUuid, ");
		sb.append("rel.rel_hotel_pl_preferential_uuid AS relHotelPlPreferentialUuid, preferential.preferential_name AS relHotelPlPreferentialName ");
		sb.append("FROM hotel_pl_preferential_rel rel ");
		sb.append("LEFT JOIN hotel_pl_preferential preferential ON rel.rel_hotel_pl_preferential_uuid = preferential.uuid ");
		sb.append("where rel.hotel_pl_preferential_uuid=? and rel.delFlag=?");
		return (List<HotelPlPreferentialRel>) super.findCustomObjBySql(sb.toString(), HotelPlPreferentialRel.class, preferentialUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids) {
		if(CollectionUtils.isEmpty(preferentialUuids)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM hotel_pl_preferential_rel WHERE hotel_pl_preferential_uuid IN ");
		sb.append("(");
		for(String preferentialUuid : preferentialUuids) {
			sb.append("'" + preferentialUuid + "',");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		return super.createSqlQuery(sb.toString()).executeUpdate();
	}
	
}
