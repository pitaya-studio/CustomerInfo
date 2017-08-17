/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelMoneyAmountDaoImpl extends BaseDaoImpl<HotelMoneyAmount>  implements HotelMoneyAmountDao{
	@Override
	public HotelMoneyAmount getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelMoneyAmount hotelMoneyAmount where hotelMoneyAmount.uuid=? and hotelMoneyAmount.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelMoneyAmount)entity;
		}
		return null;
	}
	

	public boolean updateAmount(String uuid, double amount) {
		super.createQuery("update HotelMoneyAmount amount set amount.amount=? where amount.uuid=? and amount.delFlag=?", amount, uuid, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
		return true;
	}
	
	@Override
	public boolean updateAmount(String uuid, BigDecimal amount) {
		super.createQuery("update HotelMoneyAmount amount set amount.amount=? where amount.uuid=? and amount.delFlag=?", Double.valueOf(amount.toString()), uuid, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HotelMoneyAmount> findAmountBySerialNumAndCurrencyId(
			String serialNum, Integer currencyId) {
		return super.createQuery("from HotelMoneyAmount where serialNum = ? and currencyId = ? and delFlag = ?", serialNum, currencyId, BaseEntity.DEL_FLAG_NORMAL).list();
	}

	/**
	 * add by chy 2015年6月19日14:05:55
	 * 根据reviewId查询记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HotelMoneyAmount> getHotelMoneyAmountByReviewid(Long reviewId) {
		return super.createQuery("from HotelMoneyAmount where reviewId =  ? ", reviewId.intValue()).list();
	}
	
	/**
	 * add by chy 2015年12月2日18:10:39
	 * 根据reviewId查询记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HotelMoneyAmount> getHotelMoneyAmountByReviewUuid(String reviewId) {
		return super.createQuery("from HotelMoneyAmount where reviewUuid =  ? ", reviewId).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HotelMoneyAmount> findAmount(String uuid) {
		return super.createQuery("from HotelMoneyAmount where serialNum = ? ", uuid).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<HotelMoneyAmount> findAmount(String uuid, Integer moneyType) {
		return super.createQuery("from HotelMoneyAmount where business_uuid = ? and moneyType = ? ", uuid, moneyType).list();
	}
	
}
