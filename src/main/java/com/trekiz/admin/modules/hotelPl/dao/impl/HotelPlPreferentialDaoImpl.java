/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequire;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialDaoImpl extends BaseDaoImpl<HotelPlPreferential>  implements HotelPlPreferentialDao{
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	@Override
	public HotelPlPreferential getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPreferential hotelPlPreferential where hotelPlPreferential.uuid=? and hotelPlPreferential.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPreferential)entity;
		}
		return null;
	}
	
	public List<HotelPlPreferential> findPlPreferentialsByHotelPlUuid(String hotelPlUuid) {
		List<HotelPlPreferential> hotelPlPreferentials = super.find("from HotelPlPreferential hotelPlPreferential where hotelPlPreferential.hotelPlUuid=? and hotelPlPreferential.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
		if(CollectionUtils.isNotEmpty(hotelPlPreferentials)) {
			for(HotelPlPreferential hotelPlPreferential : hotelPlPreferentials) {
				if(StringUtils.isNotEmpty(hotelPlPreferential.getIslandWay())) {
					hotelPlPreferential.setIslandWayList(sysCompanyDictViewDao.findByUuids(hotelPlPreferential.getIslandWay().split(";")));
				}
			}
		}
		return hotelPlPreferentials;
	}
	
	
	/**
	 * 自动报价 根据条件筛选 符合条件的优惠信息 add by zhanghao
	 * 只筛选符合日期的优惠，具体是否适用会进一步做筛选
	 * @return
	 */
	public List<HotelPlPreferential> getHotelPlPreferentials4AutoQuotedPrice( HotelPlPreferentialQuery hotelPlPreferentialQuery){
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,island_uuid,hotel_uuid,preferential_name,booking_code,in_date,out_date,booking_start_date,booking_end_date,island_way,isRelation,description,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_preferential  " +
				"where hotel_pl_uuid=? and in_date<=? and out_date>=? and booking_start_date<=? and booking_end_date>=? and delFlag=?");
		Object[] parameter = new Object[]{hotelPlPreferentialQuery.getHotelPlUuid(), 
				DateUtils.date2String(hotelPlPreferentialQuery.getInDate()), DateUtils.date2String(hotelPlPreferentialQuery.getOutDate()), 
				DateUtils.date2String(hotelPlPreferentialQuery.getBookingStartDate()), DateUtils.date2String(hotelPlPreferentialQuery.getBookingEndDate()), 
				BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlPreferential.class,parameter );
		
		//return super.find("from HotelPlPreferential hotelPlPreferential where hotelPlPreferential.hotelPlUuid=? and hotelPlPreferential.inDate<=? and hotelPlPreferential.outDate>=? and hotelPlPreferential.bookingStartDate<=? and hotelPlPreferential.bookingEndDate>=? and hotelPlPreferential.delFlag=?", );
	}
	
	public int deleteNotContainPreferentialUuids(List<String> preferentialUuids, String hotelPlUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM hotel_pl_preferential WHERE hotel_pl_uuid = ? ");
		if(CollectionUtils.isNotEmpty(preferentialUuids)) {
			sb.append("AND uuid NOT IN(");
			for(String preferentialUuid : preferentialUuids) {
				sb.append("'" + preferentialUuid + "',");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
		}
			
		return super.createSqlQuery(sb.toString(), hotelPlUuid).executeUpdate();
	}
	
	public List<HotelPlPreferential> getRelPlPreferentialsByPlUuid(String hotelPlUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT plPreferential.id,plPreferential.uuid,plPreferential.hotel_pl_uuid,plPreferential.island_uuid,plPreferential.hotel_uuid,plPreferential.preferential_name,plPreferential.booking_code, ");
		sb.append("plPreferential.in_date,plPreferential.out_date,plPreferential.booking_start_date,plPreferential.booking_end_date,plPreferential.island_way, ");
		sb.append("plPreferential.isRelation,plPreferential.createBy,plPreferential.createDate,plPreferential.updateBy,plPreferential.updateDate,plPreferential.delFlag FROM hotel_pl_preferential plPreferential ");
		sb.append("LEFT JOIN hotel_pl_preferential_require plRequire ON  plRequire.hotel_pl_preferential_uuid = plPreferential.uuid  ");
		sb.append("WHERE plPreferential.hotel_pl_uuid=? AND plRequire.isSuperposition=? AND plPreferential.delFlag = ? ");
		List<HotelPlPreferential> relPlPreferentials = super.findBySql(sb.toString(), HotelPlPreferential.class, hotelPlUuid, HotelPlPreferentialRequire.IS_SUPERPOSITION_YES, BaseEntity.DEL_FLAG_NORMAL);
		List<HotelPlPreferential> relPlPreferentialList = new ArrayList<HotelPlPreferential>();
		BeanUtil.copyCollection(relPlPreferentialList, relPlPreferentials, HotelPlPreferential.class);
		
		return relPlPreferentialList;
	}
	
}
