/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.dao.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferential;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialDaoImpl extends BaseDaoImpl<HotelQuotePreferential>  implements HotelQuotePreferentialDao{
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	
	@Override
	public HotelQuotePreferential getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuotePreferential hotelQuotePreferential where hotelQuotePreferential.uuid=? and hotelQuotePreferential.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuotePreferential)entity;
		}
		return null;
	}
	@Override
	public List<HotelPlPreferential> findQuotePreferentialsByHotelQuoteUuid(String hotelQuoteUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  hqp.*  FROM hotel_quote_condition_preferential_rel hqcpr LEFT JOIN hotel_quote_preferential hqp ON hqcpr.hotel_quote_preferential_uuid = hqp.uuid  AND  hqp.delFlag=0 ");
		sql.append("WHERE hqcpr.hotel_quote_condition_uuid = ? AND hqp.delFlag= 0 ");
		List<HotelPlPreferential> hotelPlPreferentials = findBySql(sql.toString(), HotelPlPreferential.class, hotelQuoteUuid);
		if(CollectionUtils.isNotEmpty(hotelPlPreferentials)) {
			for(HotelPlPreferential hotelQuotePreferential : hotelPlPreferentials) {
				if(StringUtils.isNotEmpty(hotelQuotePreferential.getIslandWay())) {
					hotelQuotePreferential.setIslandWayList(sysCompanyDictViewDao.findByUuids(hotelQuotePreferential.getIslandWay().split(";")));
				}
			}
		}
		return hotelPlPreferentials;
	}
}
