/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;

import java.math.BigDecimal;
import java.util.*;

import com.trekiz.admin.modules.island.dao.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class IslandMoneyAmountDaoImpl extends BaseDaoImpl<IslandMoneyAmount>  implements IslandMoneyAmountDao{
	@Override
	public IslandMoneyAmount getByUuid(String uuid) {
		Object entity = super.createQuery("from IslandMoneyAmount islandMoneyAmount where islandMoneyAmount.uuid=? and islandMoneyAmount.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (IslandMoneyAmount)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<IslandMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum,Integer currencyId) {
		return super.createQuery("from IslandMoneyAmount where serialNum = ? and currencyId = ? and delFlag = ?", serialNum, currencyId, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	
	public boolean updateAmount(String uuid, double amount) {
		super.createQuery("update IslandMoneyAmount amount set amount.amount=? where amount.uuid=? and amount.delFlag=?", amount, uuid, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
		return true;
	}
		
	/**
	 * add by chy2015年6月19日14:13:57 根据reviewId查询moneyAmount
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IslandMoneyAmount> findAmountByReviewId(Long reviewId) {
		return super.createQuery("from IslandMoneyAmount where reviewId = ?  ", reviewId.intValue()).list();
	}
	
	/**
	 * add by chy2015年12月2日18:18:52 根据reviewId查询moneyAmount
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IslandMoneyAmount> findAmountByReviewUuId(String reviewId) {
		return super.createQuery("from IslandMoneyAmount where reviewUuid = ?  ", reviewId).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IslandMoneyAmount> findAmount(String serialNum) {
		return super.createQuery("from IslandMoneyAmount where serialNum = ?  ", serialNum).list();
	}

	@Override
	public boolean updateAmount(String uuid, BigDecimal amount) {
		super.createQuery("update IslandMoneyAmount amount set amount.amount=? where amount.uuid=? and amount.delFlag=?", Double.valueOf(amount.toString()), uuid, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
		return true;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IslandMoneyAmount> findAmount(String uuid, Integer moneyType) {
		return super.createQuery("from IslandMoneyAmount where business_uuid = ? and moneyType = ? ", uuid, moneyType).list();
	}
}
