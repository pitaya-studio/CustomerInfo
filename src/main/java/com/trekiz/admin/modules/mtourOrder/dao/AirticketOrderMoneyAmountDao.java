/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketOrderMoneyAmountDao  extends BaseDao<AirticketOrderMoneyAmount> {
	
	public AirticketOrderMoneyAmount getByUuid(String uuid);
	
	
	/**
	 * 根据serialnum获取金额信息
	 * @param serial
	 * @return list
	 * @author zhaohaiming
	 * */
	public List<AirticketOrderMoneyAmount> getBySerialNum(String serial);
	
	/**
	 * 根据订单id和款项类型（借款：1，退款：2，追加成本：3）获取机票订单moneyAmount
		 * @Title: getByOrderIdAndMoneyType
	     * @return List<AirticketOrderMoneyAmount>
	     * @author majiancheng       
	     * @date 2015-10-24 上午11:54:36
	 */
	public List<AirticketOrderMoneyAmount> getByOrderIdAndMoneyType(Integer orderId, Integer moneyType);
	
	/**
	 * 根据订单id获取相对应的机票订单moneyAmount（包括：借款：1，退款：2，追加成本：3）
		 * @Title: getByOrderId
	     * @return List<AirticketOrderMoneyAmount>
	     * @author majiancheng       
	     * @date 2015-11-1 下午2:27:11
	 */
	public List<AirticketOrderMoneyAmount> getByOrderId(Integer orderId);
	
	/**
	 * 根据moneyAmountID更新付款状态
		 * @Title: updatePaystatusByMoneyAmountId
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-11-3 上午12:27:50
	 */
	public boolean updatePaystatusByMoneyAmountId(Integer paystatus, Integer id);
	/**
	 * 根据Id获取对象信息
	 * @param id
	 * @return AirticketOrderMoneyAmount
	 * @author zhaohaiming
	 * @date 2015-11-6
	 * */
	public AirticketOrderMoneyAmount getById(Integer id); 
	
	/**
	 * 根据订单id、申请状态和款项类型（借款：1，退款：2，追加成本：3）获取机票订单moneyAmount
		 * @Title: getByOrderIdAndMoneyType
	     * @return List<AirticketOrderMoneyAmount>
	     * @author majiancheng       
	     * @date 2015-10-24 上午11:54:36
	 */
	public List<AirticketOrderMoneyAmount> getByOrderIdStatusAndMoneyType(Integer orderId, Integer status, Integer moneyType);
	
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
	public List<AirticketOrderMoneyAmount> getByOrderIdAndStatus(Integer orderId, Integer status, Integer payStatus);
	
}
