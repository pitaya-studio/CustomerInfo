package com.trekiz.admin.modules.cost.service;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.Model;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.cost.entity.ParamBean;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，应收，应付功能对应的Service
 * @author shijun.liu
 * @date 2015年11月12日
 */
public interface IReceivePayService {

	
	/**
	 * 查询应收账款账龄的数据
	 * @param paramBean		参数对象
	 * @param page  		分页对象
	 * @return
	 * @author shijun.liu
	 */
	public Page<Map<String, Object>> getReceive(Page<Map<String, Object>> page, ParamBean paramBean);
	
	/**
	 * 导出应收账款账龄数据
	 * @param paramBean		参数对象
	 * @param page			分页对象
	 * @return
	 * @author shijun.liu
	 */
	public Workbook downloadReceive(Page<Map<String, Object>> page, ParamBean paramBean);
	
	/**
	 * 查询应付账款列表
	 * @param request
	 * @param response
	 * @param model
	 * @return page
	 * @author zhaohaiming
	 * */
	public Page<Map<Object,Object>> getPayList(Page<Map<Object, Object>> page,HttpServletRequest request,HttpServletResponse response,Model model);
	
	/**
	 * 查询应付账款列表
	 * @param request
	 * @param response
	 * @param model
	 * @return page
	 * @author zhaohaiming
	 * */
	public Workbook downloadPayList(HttpServletRequest request,HttpServletResponse response,Model model);
	
	/**
	 * 
	* @Title: getPayListByPayDate
	* @Description: TODO(获取应收账款到期数据)
	* @param @return    设定文件
	* @return 返回类型
	* @throws
	 */
	public void getPayListByPayDate();
	
	/**
	 * 查询当前用户所在公司相应产品类型下的实收总额
	 * 实收总额 --> 订单收款，切位收款，签证订单收款，其他收入收款的达帐金额总和
	 * @param orderType			订单类型
	 * @param entity			参数
	 * @return
	 * @date 2015.12.22
	 * @author shijun.liu
	 */
	public Map<String, BigDecimal> getRealReceiveSumMoney(GroupManagerEntity entity, Integer orderType);
	
	/**
	 * 查询当前用户所在公司相应产品类型下的实付总额
	 * 实付总额 --> 成本付款，反佣付款，退款付款（不包括退签证押金）的总额(不考虑是否确认付款的状态)
	 * @param orderType			订单类型
	 * @param entity			参数
	 * @return
	 * @date 2015.12.22
	 * @author shijun.liu
	 */
	public Map<String, BigDecimal> getRealPayedSumMoney(GroupManagerEntity entity, Integer orderType);

	/**
	 * 查询某个团期(机票、签证是产品)下面的所有订单的应收总额(转换为人民币)
	 * @param orderType			订单类型
	 * @param activityId		产品或者团期ID
	 * @author shijun.liu
	 * @date 2016.03.31
	 */
	public BigDecimal getReceiveSumMoneyCNY(Integer orderType, String activityId);

	/**
	 * 查询某个团期(机票、签证是产品)下面的所有订单的实收总额(转换为人民币)
	 * @param orderType			订单类型
	 * @param activityId		产品或者团期ID
	 * @author shijun.liu
	 * @date 2016.03.31
	 */
	public BigDecimal getRealReceiveSumMoneyCNY(Integer orderType, String activityId);

	/**
	 *	查询某个团期(机票、签证是产品)下面的所有其他收入收款的总额(转换为人民币)
	 * @param orderType				订单类型
	 * @param activityId			产品或者团期ID
	 * @author shijun.liu
	 * @date 2016.03.31
	 */
	public BigDecimal getOtherIncomeSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed);

	/**
	 *	查询某个团期(机票、签证是产品)下面的付款审批通过的所有实际成本的总额(转换为人民币)
	 * @param orderType				订单类型
	 * @param activityId			产品或者团期ID
	 * @param isPayed 				是否已确认付款
	 * @author shijun.liu
	 * @date 2016.03.31
	 */
	public BigDecimal getCostSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed);

	/**
	 *	查询某个团期(机票、签证是产品)下面的退款审批通过的所有退款的总额(转换为人民币)
	 * @param orderType				订单类型
	 * @param activityId			产品或者团期ID
	 * @param isPayed 				是否已确认付款
	 * @author shijun.liu
	 * @date 2016.03.31
	 */
	public BigDecimal getRefundSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed);

	/**
	 *	查询某个团期(机票、签证是产品)下面的返佣审批通过的所有返佣的总额(转换为人民币)
	 * @param orderType				订单类型
	 * @param activityId			产品或者团期ID
	 * @param isPayed 				是否已确认付款
	 * @author shijun.liu
	 * @date 2016.03.31
	 */
	public BigDecimal getRebateSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed);

	/**
	 * 定时任务，生成借款的还款提醒
	 * @author yang.jiang 2016-4-14 13:47:04
	 */
	public void getRemaindListByRefundDate();

	/**
	 * 查询拉美图的团期实际总收款
	 * 针对于拉美图的用户，其实收金额是以结算单为准，所以需要重新写sql进行查询。
	 * @author yudong.xu --2016/4/14--10:27
	 */
	public BigDecimal getRealReceiveMoneyForLMT(Integer orderType,String activityId);

	/**
	 * 查询拉美图实际其他收入收款之和。
	 * @author yudong.xu --2016/4/14--20:01
	 */
	public BigDecimal getOtherIncomeForLMT(Integer orderType,String activityId);

	/**
	 * 针对拉美图的该团下实际返佣总额。
	 * @author yudong.xu --2016/4/14--17:09
	 */
	public BigDecimal getRealRebatesForLMT(Integer orderType,String activityId);

	/**
	 * 针对拉美图，查询其对应团号下的所有境、境外实际支付之和。
	 * @author yudong.xu --2016/4/14--17:32
	 */
	public BigDecimal getRealPayForLMT(Integer orderType,String activityId);

	/**
	 * 针对拉美图的团期实际总退款
	 * @author yudong.xu --2016/4/14--19:59
	 */
	public BigDecimal getRefundForLMT(Integer orderType,String activityId);

}
