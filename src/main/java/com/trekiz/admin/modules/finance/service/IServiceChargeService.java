package com.trekiz.admin.modules.finance.service;

import java.io.File;
import java.io.IOException;
import com.trekiz.admin.common.persistence.Page;

import java.util.Map;

import com.trekiz.admin.modules.order.entity.OrderServiceCharge;

import com.trekiz.admin.modules.finance.param.ServiceChargePayParam;
import com.trekiz.admin.modules.finance.result.ServiceChargePayListResult;
import freemarker.template.TemplateException;

/**
 * 服务费付款Service接口
 * @author  shijun.liu
 * @date    2016.08.30
 */
public interface IServiceChargeService {
	
	/**
	 * 获取服务费信息用以付款确认时显示
	 * @param orderId 订单id
	 * @param serviceChargeType 服务费类型
	 * @author yang.wang
	 * @date 2016.8.30
	 * */
	public Map<String, Object> getServiceCharge4Confirm(Long orderId, Integer serviceChargeType);

	/**
	 * 获取服务费付款列表
	 * @param page
	 * @author yudong.xu 2016.9.2
     */
	public void getServiceChargePayList(Page<ServiceChargePayListResult> page, ServiceChargePayParam param);

	/**
	 * 统计出当前公司未付款的服务费数量。
	 * @return
	 * @author yudong.xu 2016.9.14
	 */
	public Integer getServiceChargeCount();

	/**
	 * 获取服务费支出凭单信息
	 * @param orderId 订单id
	 * @param serviceChargeType 服务费类型
	 * @author yang.wang
	 * @date 2016.9.1
	 * */
	public Map<String, Object> getPrintPaymentInfo(Long orderId, Integer serviceChargeType);

	/**
	 * 创建支出凭单下载文件
	 * @author yang.wang
	 * @throws TemplateException
	 * @throws IOException
	 * @date 2016.9.2
	 * */
	public File createServiceChargePaymentDownloadFile(Map<String, Object> map) throws IOException, TemplateException;

	/**
	 * 根据id查找代收服务费的确认付款表中的数据
	 * @param serviceChargeIdStr
	 * @return
	 * @author chao.zhang
	 */
	public OrderServiceCharge getServiceChargePayById(String serviceChargeIdStr);
}
