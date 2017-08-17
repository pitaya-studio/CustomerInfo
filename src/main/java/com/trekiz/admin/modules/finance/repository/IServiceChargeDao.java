package com.trekiz.admin.modules.finance.repository;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.finance.param.ServiceChargePayParam;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;

/**
 *  服务费付款的DAO类
 *  @author     shijun.liu
 *  @date       2016.08.30
 */
public interface IServiceChargeDao extends BaseDao {
	
	/**
	 * 获取服务费信息用以付款确认信息展示
	 * @param orderId 订单id
	 * @author yang.wang
	 * @date 2016.8.31
	 * */
	public List<Map<String, Object>> getServiceCharge4Confirm(Long orderId, Integer serviceChargeType);

	/**
	 * 获取服务费支出凭单信息
	 * @param orderId 订单id
	 * @param serviceChargeType 服务费类型
	 * @author yang.wang
	 * @date 2016.9.1
	 * */
	public List<Map<String, Object>> getPrintPaymentInfo(Long orderId, Integer serviceChargeType);

	/**
	 * 查询服务费付款列表
	 * @param page
	 * @author yudong.xu 2016.9.1
     */
	public void getServiceChargePayList(Page page, ServiceChargePayParam param);

	/**
	 * 统计出当前公司未付款的服务费数量。
	 * @return
	 * @author yudong.xu 2016.9.14
     */
	public Integer getServiceChargeCount();
}
