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
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteConditionDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteCondition;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuoteConditionDaoImpl extends BaseDaoImpl<HotelQuoteCondition>  implements HotelQuoteConditionDao{
	@Override
	public HotelQuoteCondition getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuoteCondition hotelQuoteCondition where hotelQuoteCondition.uuid=? and hotelQuoteCondition.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuoteCondition)entity;
		}
		return null;
	}
	
}
