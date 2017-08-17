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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatterValue;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialMatterValueDaoImpl extends BaseDaoImpl<HotelQuotePreferentialMatterValue>  implements HotelQuotePreferentialMatterValueDao{
	@Override
	public HotelQuotePreferentialMatterValue getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue where hotelQuotePreferentialMatterValue.uuid=? and hotelQuotePreferentialMatterValue.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuotePreferentialMatterValue)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelQuotePreferentialMatterValue> findMatterValueListByPreferentialUuid(String preferentialUuid){
		List<HotelQuotePreferentialMatterValue> list = super.createQuery("from HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue where hotelQuotePreferentialMatterValue.hotelQuotePreferentialUuid=? and hotelQuotePreferentialMatterValue.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL).list();
		return list;
	}
}
