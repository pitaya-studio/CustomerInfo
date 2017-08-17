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
import com.trekiz.admin.modules.hotelPl.dao.HotelPlTaxPriceDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxPrice;
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlTaxPriceDaoImpl extends BaseDaoImpl<HotelPlTaxPrice>  implements HotelPlTaxPriceDao{
	@Override
	public HotelPlTaxPrice getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlTaxPrice hotelPlTaxPrice where hotelPlTaxPrice.uuid=? and hotelPlTaxPrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlTaxPrice)entity;
		}
		return null;
	}
	
	public List<HotelPlTaxPrice> findHotelPlTaxPricesByHotelPlUuid(String hotelPlUuid) {
		return super.find("from HotelPlTaxPrice hotelPlTaxPrice where hotelPlTaxPrice.hotelPlUuid=? and hotelPlTaxPrice.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 自动报价 根据条件筛选 符合条件的税金 add by zhanghao
	 * @return
	 */
	public List<HotelPlTaxPrice> getHotelPlTaxPrice4AutoQuotedPrice( HotelPlTaxPriceQuery hotelPlTaxPriceQuery){
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,hotel_pl_uuid,wholesaler_id,supplier_info_id,island_uuid,hotel_uuid,tax_type,tax_name,start_date,end_date,currency_id,amount,charge_type,createBy,createDate,updateBy,updateDate,delFlag FROM hotel_pl_taxPrice " +
				"where hotel_pl_uuid=? and start_date<=? and end_date>=? and tax_type=? and delFlag=?");
		Object[] parameter = new Object[]{hotelPlTaxPriceQuery.getHotelPlUuid(),DateUtils.date2String(hotelPlTaxPriceQuery.getStartDate()),
				DateUtils.date2String(hotelPlTaxPriceQuery.getStartDate()),hotelPlTaxPriceQuery.getTaxType(),
				BaseEntity.DEL_FLAG_NORMAL};
		return super.findBySql(sb.toString(), HotelPlTaxPrice.class,parameter );
	}
	
	public int deleteHotelPlTaxByHotelPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_taxPrice WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}
	
	
}
