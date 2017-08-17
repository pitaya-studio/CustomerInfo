/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.dao.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteResultDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResult;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuoteResultDaoImpl extends BaseDaoImpl<HotelQuoteResult>  implements HotelQuoteResultDao{
	@Override
	public HotelQuoteResult getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuoteResult hotelQuoteResult where hotelQuoteResult.uuid=? and hotelQuoteResult.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuoteResult)entity;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getByQuoteUuid(String quoteUuid){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT hqr.preferential_price AS amount,hq.currency_id AS currencyId,c.currency_mark AS currencyText,hqr.type_uuid AS travelerType, ");
		sql.append("case hqr.price_type  when 1 then tt.name WHEN 2 THEN hgt.name WHEN 3 THEN '混住费用' WHEN 4 THEN '打包价格' END AS travelerTypeText, ");
		sql.append("IFNULL(hqr.price,0) - IFNULL(hqr.preferential_price,0) AS preferAmount ");//计算优惠的金额
		sql.append("FROM hotel_quote_result hqr LEFT JOIN hotel_quote hq ON hqr.hotel_quote_uuid = hq.uuid AND hq.delFlag=0 ");
		sql.append("LEFT JOIN currency c ON c.currency_id = hq.currency_id AND c.del_flag = 0 ");
		sql.append("LEFT JOIN traveler_type tt ON hqr.type_uuid = tt.uuid  LEFT JOIN hotel_guest_type hgt ON hqr.type_uuid = hgt.uuid ");
		sql.append("WHERE hqr.delFlag=? AND hqr.hotel_quote_condition_uuid = ? ");
		return findBySql(sql.toString(), Map.class,BaseEntity.DEL_FLAG_NORMAL, quoteUuid);
	}
	@Override
	public Object getDates(String uuid){
		String sql = " SELECT MIN(hqrd.in_date) AS beginDate,MAX(hqrd.in_date) AS endDate FROM   hotel_quote_result_detail hqrd  WHERE hqrd.hotel_quote_condition_uuid =? ";
		Object result = createSqlQuery(sql,uuid).uniqueResult();
		return result;
	}
}
