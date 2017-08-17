package com.trekiz.admin.modules.cost.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;

/**
 * 支付DAO 接口 用来写SQL
 * @author Administrator
 *
 */
public interface IPaymanagerDao {

	/**
	 * 查询审核通过的支付列表
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	Page<Map<String, Object>> getReviewPayList(Map<String, Object> params);
	
	/**
	 * 查询环球行签证借款列表，目前环球行只支持签证借款
	 * @param params	     参数
	 * @param companyId   供应商ID
	 * @param page        数据信息
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findBorrowMoneyListTTSQZ(Map<String, String> params, Long companyId, 
			Page<Map<String, Object>> page);
	
	/**
	 * 查询单团借款列表
	 * @param params
	 * @param page
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findBorrowMoneyListDT(Map<String, String> params, Page<Map<String, Object>> page);
	
	/**
	 * 查询签证借款列表
	 * @param params
	 * @param page
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findBorrowMoneyListQZ(Map<String, String> params, Page<Map<String, Object>> page);
	
	/**
	 * 查询机票借款列表
	 * @param params
	 * @param page
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findBorrowMoneyListJP(Map<String, String> params, Page<Map<String, Object>> page);
	
	/**
	 * 查询机票,签证，单团类借款列表
	 * @param params
	 * @param page
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findBorrowMoneyListAll(Map<String, String> params, Page<Map<String, Object>> page);
	
	/**
	 * 查询审核通过的返佣支付列表
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Page<Map<String, Object>> getRebatePayList(
			Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response);
	
	/**
	 * 环球行签证借款支付记录
	 * @param batchId	批次ID
	 * @param orderType 订单类型
	 * @return
	 * @author shijun.liu
	 */
	public List<Map<String,Object>> getTTSQZPayRecord(Integer batchId, Integer orderType);
	
	/**
	 * 查询环球行借款付款未付款的数目
	 * @param companyId		供应商ID
	 * @return
	 * @author shijun.liu
	 */
	public long getBorrowMoneyNotPayedCountForTTS(Long companyId);
	
	/**
	 * 查询借款付款未付款的数目
	 * @param companyId		供应商ID
	 * @return
	 * @author shijun.liu
	 */
	public long getBorrowMoneyNotPayedCount(Long companyId);
}
