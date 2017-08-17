package com.trekiz.admin.modules.mtourfinance.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.mtourfinance.json.BigCode;
import com.trekiz.admin.modules.mtourfinance.json.CostRecordJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.OrderDetailJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.SettlementJsonBean;
import com.trekiz.admin.modules.mtourfinance.pojo.AccountAgeParam;
import com.trekiz.admin.modules.mtourfinance.pojo.ReceiveOrderListParam;

import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: FinanceDao
* @Description: TODO(美途国际财务通用接口)
* @author kai.xiao
* @date 2015年10月15日 下午7:26:59
*
 */
@SuppressWarnings("rawtypes")
public interface FinanceDao extends BaseDao{
    /**
     * 获取机票订单的渠道和下单人部分信息，确认单使用
     * @param id 机票订单Id
     * @return list
     * @author zhaohaiming
     * **/
	public List<Map<String,Object>> fetchinfo(Integer id);
	
	/**
	 * 成本录入列表
	 * @author zhankui.zong
	 * @param orderId
	 * @return
	 */
	public List<CostRecordJsonBean> getCostRecordList(Integer orderId);
	
	/**
	 * 其他收入录入列表
	 * @author zhankui.zong
	 * @param orderId
	 * @return
	 */
	public List<CostRecordJsonBean> getOtherCostRecordList(Integer orderId);
	
	/**
	 * 提交成本录入/其他收入录入
	 * @author zhankui.zong
	 * @param id
	 */
	public void submitCostRecord(Integer id);
	
	/**
	 * 撤回成本录入/其他收入录入
	 * @author zhankui.zong
	 * @param id
	 */
	public void cancelCostRecord(Integer id);
	
	/**
	 * 大编号列表
	 * @author zhankui.zong
	 * @param orderId
	 * @return
	 */
	public List<BigCode> getBigCodeList(Integer orderId);
	
	/**
	 * 根据订单ID，查询美途国际结算单数据
	 * @param orderId    订单ID
	 * @return
	 * @author shijun.liu
	 */
	public SettlementJsonBean getSettlementInfo(Long orderId);
	
	/**
	 * 订单明细
	 * @author gao
	 * 2015年10月23日
	 * @param channelId
	 * @return
	 */
	public List<OrderDetailJsonBean> getOrderDetail(String channelId);

	/**
	 * 付款-款项明细-成本录入
	 * @author zhankui.zong
	 * @param costId
	 * @return
	 */
	public List<Map<String, Object>> getPaymentCost(Integer costId);
	
	
	/**
	 * 账龄查询(美途国际)
	 * @param accountAgeParam
	 * @param companyId		公司ID
	 * @return 数据对象和总条数
	 * @author shijun.liu
	 */
	public Map<String, Object> getAccountAgeList(AccountAgeParam accountAgeParam, Long companyId);
	
	/**
	 * 账龄查询(非美途国际，可能是华尔远航或者其他)
	 * @param accountAgeParam
	 * @param companyId		公司ID
	 * @return 数据对象和总条数
	 * @author shijun.liu
	 */
	public Map<String, Object> getAccountAgeListNotMtour(AccountAgeParam accountAgeParam, Long companyId);
	
	/**
	 * 结算单锁定
	 * @param orderId
	 * @author shijun.liu
	 * @date	2016.01.08
	 */
	public void lockSettlement(Long orderId);
	
	/**
	 * 结算单解锁
	 * @param orderId
	 * @author shijun.liu
	 * @date	2016.01.13
	 */
	public void unlockSettlement(Long orderId);
	
	/**
	 * 更新机票订单的付款状态
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public boolean updateOrderRefundFlag(Long orderId);

	/**
	 * 财务中心-收款-订单列表
	 * @param param
	 * @return 数据对象和总条数
	 * @author shijun.liu
	 */
	public Map<String, Object> receiveOrderList(ReceiveOrderListParam param);
}
