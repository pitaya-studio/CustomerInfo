package com.trekiz.admin.modules.cost.repository;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.cost.entity.ParamBean;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，应收，应付功能对应的Dao接口
 * @author shijun.liu
 * @date 2015年11月12日
 */
public interface IReceivePayDao extends BaseDao<Map<String,Object>> {

	/**
	 * 查询应收账款账龄的数据
	 * @param paramBean
	 * @return
	 * @author shijun.liu
	 */
	public Page<Map<String, Object>> getReceive(Page<Map<String, Object>> page, ParamBean paramBean);
	
	/**
	 * 
	* @Title: getPayListByPayDate
	* @Description: TODO(获取应收账款到期数据)
	* @param @param page
	* @param @return    设定文件
	* @return List<Map<Object, Object>>    返回类型
	* @throws
	 */
	public List<Map<Object, Object>> getPayListByPayDate();
	
	/**
	 * 
	* @Title: findUserIdByReceivePay
	* @Description: TODO(获取应收账款到期的消息可查看人员包括：财务、销售、计调、总经理)
	* @param companyId 批发商id
	* @param @return    设定文件
	* @return List<Long>    返回类型
	* @throws
	 */
	public List<Long> findUserIdByReceivePay(Long companyId);
	
	/**
	 * 查询当前公司相应产品类型下订单的所有达帐金额
	 * @param orderType			订单类型 ,orderType == 0 表示所有团期，（单团，散拼，游学，大客户，自由行，油轮)
	 * @param entity			参数
	 * @return
	 * @date 2015.12.22
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getActivityOrderRealReceiveSumMoney(GroupManagerEntity entity, Integer orderType);
	
	/**
	 * 查询当前公司签证产品下订单的所有达帐金额
	 * @param 	entity		参数
	 * @return
	 * @date 2015.12.22
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getVisaOrderRealReceiveSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询当前公司机票产品下订单的所有达帐金额
	 * @param	entity		参数
	 * @return
	 * @date 2015.12.22
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getAirticketOrderRealReceiveSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询散拼切位收款的达帐金额(目前只有机票和散拼有切位)
	 * @param	entity		参数
	 * @return
	 * @date 2015.12.23
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getSPReserveReceiveSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询机票切位收款的达帐金额(目前只有机票和散拼有切位)
	 * @param	entity		参数
	 * @return
	 * @date 2015.12.23
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getAirticketReserveReceiveSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询团期类其他收入收款的达帐总额
	 * @param orderType				订单类型 orderType == 0 表示所有团期，（单团，散拼，游学，大客户，自由行，油轮)
	 * @param entity				参数
	 * @return
	 * @date 2015.12.23
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getActivityOtherIncomeRecevieSumMoney(GroupManagerEntity entity, Integer orderType);
	
	/**
	 * 查询机票其他收入收款的达帐总额
	 * @param		entity		参数
	 * @return
	 * @date 2015.12.23
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getAirticketOtherIncomeRecevieSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询签证其他收入收款的达帐总额
	 * @param	entity		参数
	 * @return
	 * @date 2015.12.23
	 * @author shijun.liu
	 */
	public List<Map<String, Object>> getVisaOtherIncomeRecevieSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询团期类产品成本付款总金额
	 * @param orderType			订单类型 orderType == 0 表示所有团期，（单团，散拼，游学，大客户，自由行，油轮)
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getActivityCostPayedSumMoney(GroupManagerEntity entity, Integer orderType);
	
	/**
	 * 查询机票产品成本付款总金额
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getAirticketCostPayedSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询签证产品成本付款总金额
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getVisaCostPayedSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询团期类产品退款付款总金额
	 * @param orderType			订单类型 orderType == 0 表示所有团期，机票，签证（单团，散拼，游学，大客户，自由行，油轮, 机票，签证)
	 * @param entity			参数
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getActivityRefundPayedSumMoney(GroupManagerEntity entity, Integer orderType);
	
	/**
	 * 查询机票产品退款付款总金额
	 * @param	entity 		参数
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getAirticketRefundPayedSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询机票产品退款付款总金额(针对新行者批发商:老审批 --> 退款审批流程包括，1和16 该方法只查询16的情况)
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getAirticketRefundPayedSumMoneyForXXZ(GroupManagerEntity entity);
	
	/**
	 * 查询签证产品退款付款总金额
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getVisaRefundPayedSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询团期类产品反佣付款总金额
	 * @param orderType			订单类型 orderType == 0 表示所有团期，机票，签证（单团，散拼，游学，大客户，自由行，油轮, 机票，签证)
	 * @param	entity 			参数
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getActivityRebatePayedSumMoney(GroupManagerEntity entity, Integer orderType);
	
	/**
	 * 查询机票产品反佣付款总金额
	 * @param	entity		参数
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getAirticketRebatePayedSumMoney(GroupManagerEntity entity);
	
	/**
	 * 查询签证产品反佣付款总金额
	 * @param	entity		参数
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.23
	 */
	public List<Map<String, Object>> getVisaRebatePayedSumMoney(GroupManagerEntity entity);
}
