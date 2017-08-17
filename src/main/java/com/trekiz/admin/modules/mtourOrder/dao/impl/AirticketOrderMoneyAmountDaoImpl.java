/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao.impl;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderMoneyAmountDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Repository
@Transactional(readOnly = true)
public class AirticketOrderMoneyAmountDaoImpl extends BaseDaoImpl<AirticketOrderMoneyAmount>  implements AirticketOrderMoneyAmountDao{
	@Override
	public AirticketOrderMoneyAmount getByUuid(String uuid) {
		Object entity = super.createQuery("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.uuid=? and airticketOrderMoneyAmount.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderMoneyAmount)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AirticketOrderMoneyAmount> getBySerialNum(String serial) {
		List<AirticketOrderMoneyAmount> list = super.createQuery("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.serialNum=? and airticketOrderMoneyAmount.delFlag=?", serial,BaseEntity.DEL_FLAG_NORMAL).list();
		return list;
	}
	
	/**
	 * 根据订单id和款项类型（借款：1，退款：2，追加成本：3）获取机票订单moneyAmount
		 * @Title: getByOrderIdAndMoneyType
	     * @return List<AirticketOrderMoneyAmount>
	     * @author majiancheng       
	     * @date 2015-10-24 上午11:54:36
	 */
	public List<AirticketOrderMoneyAmount> getByOrderIdAndMoneyType(Integer orderId, Integer moneyType) {
		return super.find("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.airticketOrderId=? and airticketOrderMoneyAmount.moneyType=? and airticketOrderMoneyAmount.delFlag=?", orderId, moneyType, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 根据订单id获取相对应的机票订单moneyAmount（包括：借款：1，退款：2，追加成本：3）
		 * @Title: getByOrderId
	     * @return List<AirticketOrderMoneyAmount>
	     * @author majiancheng       
	     * @date 2015-11-1 下午2:27:11
	 */
	public List<AirticketOrderMoneyAmount> getByOrderId(Integer orderId) {
		return super.find("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.airticketOrderId=? and airticketOrderMoneyAmount.status=? and airticketOrderMoneyAmount.delFlag=?", orderId, AirticketOrderMoneyAmount.STATUS_CONFIRM, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 根据moneyAmountID更新付款状态
		 * @Title: updatePaystatusByMoneyAmountId
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-11-3 上午12:27:50
	 */
	public boolean updatePaystatusByMoneyAmountId(Integer paystatus, Integer id) {
		int count = super.createQuery("update AirticketOrderMoneyAmount set paystatus=? where id=?", paystatus, id).executeUpdate();
		
		return count>0;
	}

	public AirticketOrderMoneyAmount getById(Integer id) {
		Object entity = super.createQuery("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.id=? and airticketOrderMoneyAmount.delFlag=?", id, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderMoneyAmount)entity;
		}
		return null;
	}
	
	/**
	 * 根据订单id、申请状态和款项类型（借款：1，退款：2，追加成本：3）获取机票订单moneyAmount
	 * @Title: getByOrderIdAndMoneyType
	 * @return List<AirticketOrderMoneyAmount>
	 * @author majiancheng
	 * @date 2015-10-24 上午11:54:36
	 */
	public List<AirticketOrderMoneyAmount> getByOrderIdStatusAndMoneyType(Integer orderId, Integer status, Integer moneyType) {
		return super.find("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.airticketOrderId=? and airticketOrderMoneyAmount.status=? and airticketOrderMoneyAmount.moneyType=? and airticketOrderMoneyAmount.delFlag=?", orderId, status, moneyType, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 根据订单id和申请状态获取机票订单moneyAmount
	 * @Description: 
	 * @param @param orderId
	 * @param @param status
	 * @param @return   
	 * @return List<AirticketOrderMoneyAmount>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public List<AirticketOrderMoneyAmount> getByOrderIdAndStatus(Integer orderId, Integer status, Integer payStatus) {
		return super.find("from AirticketOrderMoneyAmount airticketOrderMoneyAmount where airticketOrderMoneyAmount.airticketOrderId=? and airticketOrderMoneyAmount.status=? and airticketOrderMoneyAmount.paystatus=? and airticketOrderMoneyAmount.delFlag=?", orderId, status, payStatus, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
