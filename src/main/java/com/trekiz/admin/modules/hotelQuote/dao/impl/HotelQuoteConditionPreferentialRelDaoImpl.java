/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteConditionPreferentialRelDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionPreferentialRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuoteConditionPreferentialRelDaoImpl extends BaseDaoImpl<HotelQuoteConditionPreferentialRel>  implements HotelQuoteConditionPreferentialRelDao{
	@Override
	public HotelQuoteConditionPreferentialRel getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuoteConditionPreferentialRel hotelQuoteConditionPreferentialRel where hotelQuoteConditionPreferentialRel.uuid=? and hotelQuoteConditionPreferentialRel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuoteConditionPreferentialRel)entity;
		}
		return null;
	}
	
}
