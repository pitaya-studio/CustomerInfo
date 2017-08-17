/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialRelDaoImpl extends BaseDaoImpl<HotelQuotePreferentialRel>  implements HotelQuotePreferentialRelDao{
	@Override
	public HotelQuotePreferentialRel getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialRel hotelQuotePreferentialRel where hotelQuotePreferentialRel.uuid=? and hotelQuotePreferentialRel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuotePreferentialRel)entity;
		}
		return null;
	}
	
	public List<HotelQuotePreferentialRel> getPreferentialRelsByPreferentialUuid(String preferentialUuid) {
		return super.find("from HotelQuotePreferentialRel hotelQuotePreferentialRel where hotelQuotePreferentialRel.hotelQuotePreferentialUuid=? and hotelQuotePreferentialRel.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
}
