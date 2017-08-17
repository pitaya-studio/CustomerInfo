package com.trekiz.admin.review.privilege.common.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.review.common.utils.ReviewUtils;

/**
 * 优惠审批service
 * @author xu.wang
 * @Date 2015-12-3
 */
@Service
@Transactional(readOnly = true)
public class PrivilegeReviewService extends BaseService {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private SystemService systemService;
	@Autowired
    private UserReviewPermissionChecker permissionChecker;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private OrderCommonService orderCommonService;
	
	/**
	 * @Description 优惠审批记录查询
	 * @author xu.wang
	 * @Date 2016-01-21
	 */
	public Page<Map<String, Object>> getPrivilegeReviewList(Page<Map<String, Object>> pagePara, Map<String, String> params) {
		
		//获取优惠审批查询sql
		String sql = getReviewSql();
		//获取优惠审批查询条件sql
		String whereSql = getReviewWhereSql(params);
		//排序
		String orderBy = params.get("orderBy");
		if (StringUtils.isNotBlank(orderBy)) {
			pagePara.setOrderBy(orderBy);
		}else{
			pagePara.setOrderBy("r.create_date DESC");
		}
		//执行SQL查询出列表数据
		Page<Map<String, Object>> page = moneyAmountDao.findBySql(pagePara, sql + whereSql, Map.class);
		//为列表数据组装审批变量和价格变量
		Object reviewId;
		Object status;
		List<Map<String, Object>> list = page.getList();
		for(int i= 0 ;i<list.size();i++){
	    	Map<String, Object> map = list.get(i);
			reviewId = map.get("reviewId");
			if (StringUtils.isBlank((String)reviewId)) {
				list.remove(i);
				continue;
			}
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewId.toString());
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewId.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if (status == null || StringUtils.isBlank(status.toString()) || !NumberUtils.isNumber(status.toString())) {
				map.put("statusdesc" ,"无审批状态");
				continue;
			}
			if (ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())) {
				Object cReviewer = map.get("currentReviewer");
				String person = "未分配审批人";
				if (cReviewer != null && !StringUtils.isBlank(cReviewer.toString())) {
					person = getReviewerDesc(cReviewer);
					map.put("statusdesc" ,"待" + person + "审批");
				} else {
					map.put("statusdesc" ,person);
				}
			} else {
				map.put("statusdesc" ,getReviewStatus(Integer.parseInt(map.get("status").toString())));
			}
			//申请的优惠
			Map<String,BigDecimal> previlegeMap = getCurrencyMap((String)map.get("previlegeCurrencyMarks"), (String)map.get("previlegeAmounts"));
			if(previlegeMap!=null){
				map.put("previlege", transMapToString(previlegeMap));
			}
		}
		page.setList(list);
		return page;
	}
	
	
	/**
	 * @Description 获取优惠审批查询sql
	 * @author xu.wang
	 * @Date 2016-01-21
	 */
	private String getReviewSql() {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(
				userId, companyUuid, Context.REBATES_FLOW_TYPE);
		//部门
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = "";
		if (deptIds != null && deptIds.size() > 0) {
			int n = 0;
			for (String str : deptIds) {
				if (n == 0) {
					deptIdStr += str;
					n++;
				} else {
					deptIdStr += "," + str;
				}
			}
		}
		if ("".equals(deptIdStr)) {//给默认值
			deptIdStr = "-1,-2";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT r.order_no orderNum, p.delFlag isOrdered, r.id reviewId, r.order_id orderId, r.group_code groupCode, r.group_id groupId, ").
			append("r.product_name productName, r.product_id productId, r.product_type productType, r.create_date createDate, ").
			append("r.create_by createBy, r.agent agent, r.agent_name agentName, r.operator operator, r.traveller_id travellerId, r.last_reviewer lastReviewer, ").
			append("GROUP_CONCAT(rpma.currencyId) AS previlegeCurrencyIds,GROUP_CONCAT(rpma.amount) AS previlegeAmounts, ").
			append("GROUP_CONCAT(c.currency_name) AS previlegeCurrencyNames,GROUP_CONCAT(c.currency_mark) AS previlegeCurrencyMarks ").
			append("FROM  review_new r LEFT JOIN  review_process_money_amount rpma ON r.extend_1 = rpma.serial_number ").
			append("LEFT JOIN productorder p ON r.order_id = p.id AND r.order_no = p.orderNum ").
			append("LEFT JOIN currency c ON c.currency_id = rpma.currencyId ").
			append("WHERE r.process_type = '21' AND r.del_flag = 0 AND r.company_id ='" + companyUuid + "' ").
			append("AND ( r.dept_id IN (" + deptIdStr + ") OR FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");
		return sql.toString();
	}
	
	/**
	 * @Description 获取优惠审批查询条件sql
	 * @author xu.wang
	 * @Date 2016-01-21
	 */
	private String getReviewWhereSql(Map<String, String> params) {
		StringBuffer whereSql = new StringBuffer();
		//产品编号或团期编号或订单编号
		String groupCode = params.get("groupCode");
		if (StringUtils.isNotBlank(groupCode)) {
			whereSql.append(" and (r.group_code like '%" + groupCode + "%' or r.product_name like '%" + groupCode +
					"%' or r.order_no like '%" + groupCode + "%') ");
		}
		//产品类型
	    /*String productType = params.get("productType");
		if (StringUtils.isNotBlank(productType) && !"0".equals(productType)) {
			whereSql.append(" and r.product_type = " + productType + " ");
		}*/
		//渠道
		String agentId = params.get("agentId");
		if (StringUtils.isNotBlank(agentId)) {
			whereSql.append(" and r.agent = " + agentId + " ");
		}
		//申请开始时间
		String applyDateFrom = params.get("applyDateFrom");
		if (StringUtils.isNotBlank(applyDateFrom)) {
			whereSql.append(" and r.create_date >= '" + applyDateFrom + " 00:00:00' ");
		}
		//申请结束时间
		String applyDateTo = params.get("applyDateTo");
		if (StringUtils.isNotBlank(applyDateTo)) {
			whereSql.append(" and r.create_date <= '" + applyDateTo + " 23:59:59' ");
		}
		//申请人
		String applyPerson = params.get("applyPerson");
		if (StringUtils.isNotBlank(applyPerson)) {
			whereSql.append(" and r.create_by = " + applyPerson + " ");
		}
		//计调
		/*String operator = params.get("operator");
		if (StringUtils.isNotBlank(operator)) {
			whereSql.append(" and r.operator = " + operator + " ");
		}*/
		//审批状态  空 为全部 1 审批中 2 已通过 0 未通过
		String reviewStatus = params.get("reviewStatus");
		if (StringUtils.isNotBlank(reviewStatus) && NumberUtils.isNumber(reviewStatus)) {
			whereSql.append(" and r.status = " + Integer.parseInt(reviewStatus) + " ");
		}
		/*//优惠开始金额
		String rebatesDiffBegin = params.get("rebatesDiffBegin");
		if (StringUtils.isNotBlank(rebatesDiffBegin)) {
			whereSql.append(" and t.rebatesDiff >= " + rebatesDiffBegin + " ");
		}
		//优惠结束金额
		String rebatesDiffEnd = params.get("rebatesDiffEnd");
		if (StringUtils.isNotBlank(rebatesDiffEnd)) {
			whereSql.append(" and t.rebatesDiff <= " + rebatesDiffEnd + " ");
		}*/
		//优惠出纳确认
		String payStatus = params.get("payStatus");
		if (StringUtils.isNotBlank(payStatus)) {
			whereSql.append(" and r.pay_status = " + payStatus + " ");
		}
		//状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批
		Long userId = UserUtils.getUser().getId();
		String tabStatus = params.get("tabStatus");
		if (StringUtils.isBlank(tabStatus)) {
			tabStatus = "1";
			params.put("tabStatus", tabStatus);
		}
		if (StringUtils.isNotBlank(tabStatus) && NumberUtils.isNumber(tabStatus) && !Context.REVIEW_TAB_ALL.equals(tabStatus)) {
			int tabStatusInt = Integer.parseInt(tabStatus);
			if (Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt) {
				whereSql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt) {
				whereSql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt) {
				whereSql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		//分组
		whereSql.append(" GROUP BY r.id");
		return whereSql.toString();
	}
	
	/**
	 * 获取当前审批人描述 由id转化为name
	 * @param cReviewer
	 * @return
	 */
	private String getReviewerDesc(Object cReviewer) {
		String reviewers = cReviewer.toString();
		String[] reviewArr = reviewers.split(",");
		String result = "";
		int n = 0;
		String tName = "";
		for(String temp : reviewArr){
			if(StringUtils.isBlank(temp)){
				continue;
			}
			tName = UserUtils.getUserNameById(Long.parseLong(temp));
			if(n == 0){
				result += tName;
			} else {
				result += "," + tName;
			}
		}
		return result;
	}
	
	/**
	 * 获取审批状态描述
	 * @param status
	 * @return
	 */
	private String getReviewStatus(Integer status) {
		if(ReviewConstant.REVIEW_STATUS_CANCELED == status){
			return ReviewConstant.REVIEW_STATUS_CANCELED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_PASSED == status){
			return ReviewConstant.REVIEW_STATUS_PASSED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_REJECTED == status){
			return ReviewConstant.REVIEW_STATUS_REJECTED_DES;
		} 
		return "无";
	}
	
	private Map<String,BigDecimal> getCurrencyMap(String mark,String amount){
		if(StringUtils.isNotBlank(mark)&&StringUtils.isNotBlank(amount)){
			String[] markArray = mark.split(",");
			String[] amountArray = amount.split(",");
			Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();
			for(int i=0;i<amountArray.length;i++){
				if(!map.containsKey(markArray[i])){
					map.put(markArray[i], new BigDecimal(amountArray[i]));
				}else{
					map.put(markArray[i], map.get(markArray[i]).add(new BigDecimal(amountArray[i])));
				}
			}
			return map;
		}else{
			return null;
		}
	}
	
	/** 
	 * 方法名称:transMapToString 
	 * 传入参数:map 
	 * 返回值:String 形如 username'chenziwen+password'1234 
	*/  
	@SuppressWarnings("all")
	private String transMapToString(Map map){  
	  java.util.Map.Entry entry;  
	  StringBuffer sb = new StringBuffer();  
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			entry = (java.util.Map.Entry) iterator.next();
			sb.append(entry.getKey().toString())
					.append(null == entry.getValue() ? "" : entry.getValue()
							.toString()).append(iterator.hasNext() ? "+" : "");
		}
	  return sb.toString();  
	}  
	
	/**
	 * @Description 优惠审批
	 * @param reviewId 审批id
	 * @param strResult 通过或驳回的标识
	 * @param denyReason 驳回原因或备注
	 * @author xu.wang
	 * @throws Exception 
	 * @Date 2016-01-21
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, String> privilegeReview(String reviewId, String strResult, String denyReason, HttpServletRequest request) {
		// 返回值
		Map<String, String> result = Maps.newHashMap();
		// 获取用户ID和公司ID
		String userId = UserUtils.getUser().getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		
		ReviewResult reviewResult;
		if (strResult.equals(ReviewConstant.REVIEW_OPERATION_PASS.toString())) {
			// 审批通过
			reviewResult = reviewService.approve(userId, companyId, null, permissionChecker, reviewId, denyReason, null);
		} else {
			// 审批驳回
			reviewResult = reviewService.reject(userId, companyId, null, reviewId, denyReason, null);
		}
		if (!reviewResult.getSuccess()) {
			result.put("result", "fail");
			result.put("msg", reviewResult.getMessage());
		} else {
			result.put("result", "success");
		}
		// 如果审批通过并且当前层级为最高层级 则更改对应业务数据
		if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewId);
			List<Map<String,Object>> travelerlist = getPrivilegeTravelerInfo(reviewMap);
			Object orderNum = reviewMap.get("orderNo");
			updatePrice(travelerlist, orderNum.toString());
			
		}
		return result;
	}
	
	private List<Map<String,Object>> getPrivilegeTravelerInfo(Map<String, Object> reviewMap){
		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		if(null!=reviewMap.get("travellerId")){
			String[] travellerId =  reviewMap.get("travellerId").toString().split(",");
			String[] currencyId =  reviewMap.get("currencyId").toString().split(",");
			String[] sqyhPrice =  reviewMap.get("sqyhPrice").toString().split(",");//申请的优惠金额
			for (int i = 0; i < travellerId.length; i++) {
				Map<String,Object>  rdMap = new  HashMap<String, Object>();
				rdMap.put("currencyId", currencyId[i]);
				rdMap.put("sqyhPrice", sqyhPrice[i]);
				rdMap.put("travellerId", travellerId[i]);
				rdlist.add(rdMap);
			}
		}
		return rdlist;
	}
	
	/**
	 * 更改游客的同行结算价和累计优惠总金额
	 */
	private void updatePrice(List<Map<String,Object>> travelerlist, String orderNum){
		if(CollectionUtils.isNotEmpty(travelerlist)){
			String sql = "SELECT ma.id,ma.serialNum,ma.currencyId,ma.amount,ma.exchangerate,ma.uid,ma.moneyType,ma.orderType,ma.businessType, ";
			sql+="ma.createdBy,ma.createTime,ma.delFlag,ma.reviewId,ma.review_uuid,ma.orderPaySerialNum,ma.payed_accounted_uuid ";
			sql+="FROM money_amount ma INNER JOIN traveler t ON ma.serialNum=t.payPriceSerialNum WHERE t.id=?";
			for(int i=0;i<travelerlist.size();i++){
				Map<String,Object> info = travelerlist.get(i);
				List<MoneyAmount> moneyList = moneyAmountDao.findBySql(sql, MoneyAmount.class, info.get("travellerId"));
				if(CollectionUtils.isNotEmpty(moneyList)){
					for(MoneyAmount money : moneyList){
						if(money.getCurrencyId().equals(Integer.valueOf((String)info.get("currencyId")))){
							money.setAmount(money.getAmount().subtract(new BigDecimal((String)info.get("sqyhPrice"))));
							moneyAmountDao.updateObj(money);
						}
					}
				}
				//更改累计优惠金额
				Traveler t = travelerDao.findById(Long.valueOf((String)info.get("travellerId")));
				BigDecimal totalPrice = t.getReviewedDiscountPrice()==null?new BigDecimal("0.00"):t.getReviewedDiscountPrice();
				t.setReviewedDiscountPrice(totalPrice.add(new BigDecimal((String)info.get("sqyhPrice"))));
				travelerDao.updateObj(t);
				//更改订单总结算价
				List<ProductOrderCommon> productOrderList = orderCommonService.findByOrderNum4Discount(orderNum);
				if(productOrderList != null && productOrderList.size() > 0) {
					ProductOrderCommon productOrderCommon = productOrderList.get(0);
					String totalMoney = productOrderCommon.getTotalMoney();
					List<MoneyAmount> moneyAmountList = moneyAmountDao.findAmountListBySerialNum(totalMoney);
					if(CollectionUtils.isNotEmpty(moneyAmountList)){
						for (MoneyAmount moneyAmount : moneyAmountList) {
							if(moneyAmount.getCurrencyId().equals(Integer.valueOf((String)info.get("currencyId")))) {
								moneyAmount.setAmount(moneyAmount.getAmount().subtract(new BigDecimal((String)info.get("sqyhPrice"))));
								moneyAmountDao.updateObj(moneyAmount);
							}
						}
					}
					
				}
			}
		}
	}
	
	/**
	 * @Description 批量审批
	 * @author xu.wang
	 * @Date 2015-12-9
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, String> batchReview(String revids, String remark, String v, HttpServletRequest request) {
		
		// 返回值
		Map<String, String> result = Maps.newHashMap();
		result.put("result", "success");
		
		String reviewIds[] = revids.split(",");
		
		for (String reviewId : reviewIds) {
			if (StringUtils.isNotBlank(reviewId)) {
				try {
					result = privilegeReview(reviewId.split("@")[0], v, remark, request);
					if (!"success".equals(result.get("result").toString())) {
						result.put("result", "fail");
						if ( StringUtils.isNotBlank(result.get("msg"))) {
							result.put("msg", result.get("msg"));
							throw new Exception(result.get("msg"));
						} else {
							result.put("msg", "未知错误");
							throw new Exception("未知错误");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return result;
				}
			}
		}
		return result;
	}
	
}