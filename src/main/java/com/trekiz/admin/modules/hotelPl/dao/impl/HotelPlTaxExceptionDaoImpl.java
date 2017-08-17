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

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlTaxExceptionDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxExceptionQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlTaxExceptionDaoImpl extends BaseDaoImpl<HotelPlTaxException>  implements HotelPlTaxExceptionDao{
	@Override
	public HotelPlTaxException getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlTaxException hotelPlTaxException where hotelPlTaxException.uuid=? and hotelPlTaxException.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlTaxException)entity;
		}
		return null;
	}
	
	public List<HotelPlTaxException> findTaxExceptionsByHotelPlUuids(String hotelPlUuid) {
		return super.find("from HotelPlTaxException hotelPlTaxException where hotelPlTaxException.hotelPlUuid=? and hotelPlTaxException.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	/**
	 * 自动报价 根据条件筛选 符合条件的税金 add by zhanghao
	 * @return
	 */
	public List<HotelPlTaxException> getHotelPlTaxException4AutoQuotedPrice( HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,island_uuid,hotel_uuid,exception_type,exception_name,start_date,end_date,tax_type,travel_type,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_taxException " +
				"where hotel_pl_uuid=? and start_date<=? and end_date>=?   and travel_type like '%" +hotelPlTaxExceptionQuery.getTravelType()+
				"%' and exception_type=? AND delFlag=? ");
		Object[] parameter = new Object[]{hotelPlTaxExceptionQuery.getHotelPlUuid(),DateUtils.date2String(hotelPlTaxExceptionQuery.getStartDate()),DateUtils.date2String(hotelPlTaxExceptionQuery.getStartDate()),hotelPlTaxExceptionQuery.getExceptionType(),BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlTaxException.class,parameter );
	}
	
	public int deleteHotelPlTaxByHotelPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_taxException WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}
}
