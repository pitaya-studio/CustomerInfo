package com.trekiz.admin.modules.activity.service;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrder;
import com.trekiz.admin.modules.activity.repository.ActivityReserveOrderDao;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * 产品出团信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class ActivityReserveOrderService extends BaseService {

	//@Autowired
	//private ActivityGroupDao activityGroupDao;
	@Autowired
	private ActivityReserveOrderDao activityReserveOrderDao;
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityReserveOrderService#save(java.util.Set)
	 */
	
	public ActivityReserveOrder findOne(Long id) {
		return activityReserveOrderDao.findOne(id);
	}
	
	
	/**
	 *根据订单编号获取切位订单信息
	 *@author haiming.zhao
	 *@DateTime 2014-12-29 17:10:00
	 *@param orderNum   订单编号
	 *@return list
	 * */
	public List<ActivityReserveOrder> findActivityReserveOrderByOrderNum(String orderNum){
		return activityReserveOrderDao.findActivityReserveOrder(orderNum);
	}
	/**
	 * 
	* @Title: saveAsAcount 
	* @Description: TODO(切位订金达账功能) 
	* @param @param id
	* @return ActivityReserveOrder    返回类型 
	* @throws
	 */
	@Transactional(readOnly = false,rollbackFor={ServiceException.class})
    public ActivityReserveOrder saveAsAcount(Long id) {
		ActivityReserveOrder activityReserveOrder = activityReserveOrderDao.findOne(id);
		activityReserveOrder.setConfirm(Integer.parseInt(Context.DICT_TYPE_YES));
		activityReserveOrder.setPayMoney(activityReserveOrder.getOrderMoney());
		activityReserveOrder.setUpdateBy(UserUtils.getUser().getId());
		activityReserveOrder.setUpdateDate(new Date());
		activityReserveOrderDao.save(activityReserveOrder);
        return activityReserveOrder;
	}
	
	 /**
	  * 
	 * @Title: findReserveOrderList 
	 * @Description: TODO(切位订单列表) 
	 * @param @param type
	 * @param @param page
	 * @param @param orderBy
	 * @param @param orderShowType
	 * @param @param map
	 * @param @param common 
	 * @return Page<Map<Object,Object>>    返回类型 
	 * @throws
	  */
	 public Page<Map<Object, Object>> findReserveOrderList(String type, Page<Map<Object, Object>> page,
	    		String orderBy, String orderShowType, Map<String,String> map, DepartmentCommon common) {
		 StringBuffer sqls = new StringBuffer();
		 //团号
		 String groupCode = map.get("groupCode");
		 //团队类型
		 String orderType = map.get("orderS");
		 //订单号
		 String orderNum = map.get("orderNum");
		 //银行到账日期
		 String accountDateBegin = map.get("accountDateBegin");
		 String accountDateEnd = map.get("accountDateEnd");
		 //是否到账
		 String isAccounted = map.get("isAccounted");
		 //渠道
		 String agentId = map.get("agentId");
		 //销售
		 String saler = map.get("saler");
		 //计调
		 String jd = map.get("jdUserId");
		 //付款日期
		 String createDateBegin = map.get("createDateBegin");
		 String createDateEnd = map.get("createDateEnd");
		 //打印状态
//		 String printFlag = map.get("printFlag");
		 //来款单位
//		 String payerName = map.get("payerName");
		 //付款金额
		 String payAmountStrat = map.get("payAmountStrat");
		 String payAmountEnd = map.get("payAmountEnd");
		 //收款银行
//		 String toBankName = map.get("toBankNname");
		 //下单人
		 String creator = map.get("creator");
		 //支付方式
		 String payType = map.get("payType");
		 //出团日期
		 String groupOpenDateBegin = map.get("groupOpenDateBegin");
		 String groupOpenDateEnd = map.get("groupOpenDateEnd");
		 //收款确认日期
		 String receiptConfirmDateBegin = map.get("receiptConfirmDateBegin");
		 String receiptConfirmDateEnd = map.get("receiptConfirmDateEnd");
		 //渠道结款方式
		 String paymentType = map.get("paymentType");
		 
		 String jdConditions = "";//散拼、机票计调查询条件
		 if(StringUtils.isNotBlank(jd)){
			 jdConditions = " AND p.createBy = " + jd;
		 }
		 long companyId = UserUtils.getUser().getCompany().getId();
		 sqls.append(" SELECT tmp.payType, tmp.orderType, tmp.orderId, tmp.orderNum, tmp.productId, tmp.acitivityName,")
			 .append(" tmp.activityGroupId, tmp.groupCode, tmp.createDate, tmp.updateDate, tmp.startDate, ")
			 .append(" tmp.endDate, tmp.orderStatus, tmp.orderMoney, tmp.payMoney, tmp.confirm, tmp.saleId,")
			 .append(" tmp.agentId, tmp.agentName, tmp.createBy, tmp.groupOpenDate, tmp.operator, tmp.reserveType FROM ")
			 .append(" (SELECT o.payType, ").append(Context.ORDER_TYPE_SP).append(" AS orderType, o.id orderId, o.orderNum, ")
			 .append("  o.srcActivityId AS productId, p.acitivityName, o.activityGroupId, g.groupCode, o.createDate, ")
			 .append("  o.updateDate, o.startDate, o.endDate, o.orderStatus, o.orderMoney, o.payMoney, o.confirm, o.saleId, ")
			 .append("  ai.id agentId, ai.agentName, o.createBy, g.groupOpenDate, p.createBy AS operator, reserveType FROM ")
			 .append(" travelactivity p, activitygroup g, activityreserveorder o, agentinfo ai ")
			 .append(" WHERE p.id = o.srcActivityId AND g.id = o.activityGroupId ")
			 .append(" AND g.srcActivityId = o.srcActivityId ").append(" AND p.id = g.srcActivityId ").append(jdConditions)
			 .append(" AND ai.id = o.agentId AND p.proCompany = ").append(companyId).append(" AND o.reserveType = 0 ")
			 .append(" UNION ")
			 .append(" SELECT o.payType, ").append(Context.ORDER_TYPE_JP).append(" AS orderType, o.id AS orderId, o.orderNum,")
			 .append(" p.id AS productId, ' ' AS acitivityName, o.activityGroupId, p.group_code AS groupCode, o.createDate, ")
			 .append(" o.updateDate, o.startDate, o.endDate, o.orderStatus, o.orderMoney, o.payMoney, o.confirm, o.saleId,")
			 .append(" ai.id agentId, ai.agentName, o.createBy, p.outTicketTime AS groupOpenDate, p.createBy AS operator, ")
			 .append(" reserveType FROM activity_airticket p, activityreserveorder o, agentinfo ai ")
			 .append(" WHERE p.id = o.srcActivityId AND ai.id = o.agentId ").append(jdConditions)
			 .append(" AND p.proCompany = ").append(companyId).append(" AND o.reserveType = 1 ) tmp where 1=1 ");
		 //团号
		 if(StringUtils.isNotBlank(groupCode)){
			 sqls.append(" AND tmp.groupCode like '%").append(groupCode).append("%' ");
		 }
		 //团队类型
		 if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ALL != Integer.parseInt(orderType)){
			 sqls.append(" AND tmp.orderType=").append(orderType);
		 }
		 //订单编号
		 if(StringUtils.isNotBlank(orderNum)){
			 sqls.append(" AND tmp.orderNum like '%").append(orderNum).append("%' ");
		 }
		 //银行到账日期
		 if(StringUtils.isNotBlank(accountDateBegin)){
			 sqls.append(" AND tmp.updateDate >= '").append(accountDateBegin).append(" 00:00:00' ");
		 }
		 if(StringUtils.isNotBlank(accountDateEnd)){
			 sqls.append(" AND tmp.updateDate <= '").append(accountDateEnd).append(" 23:59:59' ");
		 }
		 //是否到账
		 if("Y".equalsIgnoreCase(isAccounted)){
			 sqls.append(" AND tmp.confirm = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
		 }else if("N".equalsIgnoreCase(isAccounted)){
			 sqls.append(" AND (tmp.confirm != ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ)
					 .append(" OR tmp.confirm IS NULL) ");
		 }
		 //渠道
		 if(StringUtils.isNotBlank(agentId)){
			 sqls.append(" AND tmp.agentId =").append(agentId);
		 }
		 //销售
		 if(StringUtils.isNotBlank(saler)){
			 sqls.append(" AND tmp.saleId =").append(saler);
		 }
		 //付款日期
		 if(StringUtils.isNotBlank(createDateBegin)){
			 sqls.append(" AND tmp.createDate >='").append(createDateBegin).append(" 00:00:00' ");
		 }
		 if(StringUtils.isNotBlank(createDateEnd)){
			 sqls.append(" AND tmp.createDate <='").append(createDateEnd).append(" 23:59:59' ");
		 }
		 //打印状态  无此条件
		 //来款单位  无此条件
		 //付款金额
		 if(StringUtils.isNotBlank(payAmountStrat)){
			 sqls.append(" AND tmp.orderMoney>=").append(payAmountStrat);
		 }
		 if(StringUtils.isNotBlank(payAmountEnd)){
			 sqls.append(" AND tmp.orderMoney<=").append(payAmountEnd);
		 }
		 //收款银行  无此条件
		 //下单人
		 if(StringUtils.isNotBlank(creator)){
			 sqls.append(" AND tmp.createBy = ").append(creator);
		 }
		 //支付方式
		 if("3".equals(payType)){
			 sqls.append(" AND tmp.payType IN (3,5) ");
		 }else if("8".equals(payType)){
			 sqls.append(" AND tmp.payType IN (2,8) ");
		 }else if(StringUtils.isNotBlank(payType)){
			 sqls.append(" AND tmp.payType =").append(payType);
		 }
		 // 需求180 出团日期(日信观光) modify by wangyang 2016.2.29
		 if(StringUtils.isNotBlank(groupOpenDateBegin)){
			 sqls.append(" AND tmp.groupOpenDate >='").append(groupOpenDateBegin).append(" 00:00:00' ");
		 }
		 if(StringUtils.isNotBlank(groupOpenDateEnd)){
			 sqls.append(" AND tmp.groupOpenDate <='").append(groupOpenDateEnd).append(" 23:59:59' ");
		 }
		 //收款确认日期筛选
		 //需求0405，经产品确认，收款确认日期与银行到账日期取同一个字段updateDate  modify by wangyang 2016.5.5
		 if(StringUtils.isNotBlank(receiptConfirmDateBegin)){
			 sqls.append(" and tmp.confirm = 1 ")
			 	 .append(" and tmp.updateDate >= '").append(receiptConfirmDateBegin).append(" 00:00:00' ");
			 if(StringUtils.isNotBlank(receiptConfirmDateEnd)){
				 sqls.append(" and tmp.updateDate <= '").append(receiptConfirmDateEnd).append(" 23:59:59' ");
			 }
		 }else{
			 if(StringUtils.isNotBlank(receiptConfirmDateEnd)){
				 sqls.append(" and tmp.updateDate <= '").append(receiptConfirmDateEnd).append(" 23:59:59' ")
				 	 .append(" and tmp.confirm = 1 ");
			 }
		 }
		 //渠道结款方式
		 if(StringUtils.isNotBlank(paymentType)){
			 sqls.append(" and tmp.agentId in (select id from agentinfo where paymentType = "+ paymentType +") ");
		 }
		 
		 
		 if(StringUtils.isBlank(orderBy)){
			 orderBy = "updateDate desc";
		 }
		 //如果是Excel导出
		 if("exportExcel".equals(map.get("exportExcel"))){
			 page.setMaxSize(Integer.MAX_VALUE);
		 }
		page.setOrderBy(orderBy);
		Page<Map<Object, Object>> pageMap = activityReserveOrderDao.findBySql(page, sqls.toString(), Map.class);
		return pageMap;
	 }
}
