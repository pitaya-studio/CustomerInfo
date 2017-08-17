package com.trekiz.admin.review.changePrice.common.service.impl;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderServiceForSaveAndModify;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.StatisticAnalysisService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.TravelerUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.changePrice.common.dao.IChangePriceReviewNewDao;
import com.trekiz.admin.review.changePrice.common.service.IChangePriceReviewService;
import com.trekiz.admin.review.changePrice.singleGroup.service.IChangePriceNewService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ChangePriceReviewServiceImpl extends BaseService implements IChangePriceReviewService {

	@Autowired
	private IChangePriceReviewNewDao changePriceReviewDao;
	
	@Autowired 
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private OrderCommonService orderCommonService;
	
	@Autowired
	private OrderServiceForSaveAndModify orderServiceForSaveAndModify;
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private TravelerDao travelerDao;
	
	@Autowired
	private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private TravelerService travelerService;

	
	/**
	 * 查询改价审核列表
	 */
	@Override
	public Page<Map<String, Object>> queryChangePriceReviewList(
			Map<String, Object> params,IChangePriceNewService changePriceService) {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(userId, companyUuid, Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
		//部门
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = "";
		if(deptIds != null && deptIds.size() > 0){
			int n = 0;
			for(String str : deptIds){
				if(n == 0){
					deptIdStr += str;
					n++;
				} else {
					deptIdStr += "," + str;
				}
			}
		}
		if("".equals(deptIdStr)){//给默认值
			deptIdStr = "-1,-2";
		}
		//产品
		Set<String> prds = userReviewPermissionResultForm.getProductTypeId();
		String prdStr = "";
		if(prds != null && prds.size() > 0){
			int n = 0;
			for(String str : prds){
				if(n == 0){
					prdStr += str;
					n++;
				} else {
					prdStr += "," + str;
				}
			}
		}
		if("".equals(prdStr)){//给默认值
			prdStr = "-1,-2";
		}
		StringBuffer querySql = new StringBuffer("");
		querySql.append("select r.order_no orderno,r.id reviewid ").//订单编号
			append(",r.order_id orderid ").//订单id
			append(",r.group_code groupcode ").//团号
			append(",r.group_id groupid ").//团期id
			append(",r.product_name productname ").//产品名称
			append(",r.product_id productid ").//产品id
			append(",r.product_type producttype ").//产品类型
			append(",r.create_date createdate ").//申请时间
			append(",r.create_by createby ").//审批发起人
			append(",r.agent agent ").//渠道商
			append(",r.operator operator ").//计调
			append(",r.traveller_id travellerid ").//游客id
			append(",tr.original_payPriceSerialNum payPriceSerialNum ").//游客结算价serialnum
//			append(",t.name travelername ").//游客姓名
			append(",r.last_reviewer lastreviewer ").//上一环节审批人
			append(",t.currencyId curidc ").//改价金额 币种
			append(",t.amount amountc ").//改价金额 数量
			append(",r.status status ").//审批状态
			append(" from review_new r left join review_process_money_amount t on t.reviewId = r.id left join traveler tr on r.traveller_id = tr.id ").//
			append(" where 1 = 1 and r.company_id = '" + companyUuid + "' and r.need_no_review_flag=0 and r.process_type = '" + Context.REVIEW_FLOWTYPE_CHANGE_PRICE + "' ").
			append(" and ((r.product_type in(" + prdStr + ") and r.dept_id in (" + deptIdStr + ")) or FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");//
		/*拼装查询条件*/
		Object groupCode = params.get("groupCode");
		if(groupCode != null && !"".equals(groupCode.toString().trim())){
			querySql.append(" and (r.group_code like '%" + groupCode.toString().trim()).
				append("%' or r.product_name like '%" + groupCode.toString().trim()).
				append("%' or r.order_no like '%" + groupCode.toString().trim() + "%') ");
		}
		Object productType = params.get("productType");
		if(productType != null && !"".equals(productType.toString()) && !"0".equals(productType.toString())){
			querySql.append(" and r.product_type = " + productType.toString() + " ");
		}
		Object agentId = params.get("agentId");
		if(agentId != null && !"".equals(agentId.toString())){
			querySql.append(" and r.agent = " + agentId.toString() + " ");
		}
		Object applyDateFrom = params.get("applyDateFrom");
		if(applyDateFrom != null && !"".equals(applyDateFrom.toString())){
			querySql.append(" and r.create_date >= '" + applyDateFrom.toString() + " 00:00:00' ");
		}
		Object applyDateTo = params.get("applyDateTo");
		if(applyDateTo != null && !"".equals(applyDateTo.toString())){
			querySql.append(" and r.create_date <= '" + applyDateTo.toString() + " 23:59:59' ");
		}
		Object applyPerson = params.get("applyPerson");
		if(applyPerson != null && !"".equals(applyPerson.toString())){
			querySql.append(" and r.create_by = " + applyPerson.toString() + " ");
		}
		Object operator = params.get("operator");
		if(operator != null && !"".equals(operator.toString())){
			querySql.append(" and r.operator = " + operator.toString() + " ");
		}
		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		Object reviewStatus = params.get("reviewStatus");
		if(reviewStatus != null && !"".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		Object cashConfirm = params.get("cashConfirm");
		if(cashConfirm != null && !"".equals(cashConfirm.toString()) && NumberUtils.isNumber(cashConfirm.toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		Object printStatus = params.get("printStatus");
		if(printStatus != null && !"".equals(printStatus.toString()) && NumberUtils.isNumber(printStatus.toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
		}
		/*状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批*/
		Object tabStatus = params.get("tabStatus");
		if(tabStatus != null && !"".equals(tabStatus.toString()) && NumberUtils.isNumber(tabStatus.toString()) && !Context.REVIEW_TAB_ALL.equals(tabStatus.toString())){
			int tabStatusInt = Integer.parseInt(tabStatus.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt){
//				querySql.append(" and FIND_IN_SET ('" + userId + "', (select l.create_by from review_log_new l where l.review_id = r.id and operation = 1 and l.active_flag = 1)) ");
				querySql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt){
				querySql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		
		Object paymentType = params.get("paymentType");
		if(paymentType != null && !"".equals(paymentType.toString())){
			querySql.append(" and 	r.agent in (select id from agentinfo WHERE paymentType = "+paymentType+") ");
		}
		//排序 默认按重要程度降序 按创建时间降序 
		querySql.append(" order by r.critical_level desc ");
		
		Object orderCreateDateSort = params.get("orderCreateDateSort");
		Object orderUpdateDateSort = params.get("orderUpdateDateSort");
		if(orderCreateDateSort != null && !"".equals(orderCreateDateSort.toString())){
			querySql.append(" ,r.create_date " + orderCreateDateSort.toString() + " ");
		} else if(orderUpdateDateSort != null && !"".equals(orderUpdateDateSort.toString())){
			querySql.append(" ,r.update_date " + orderUpdateDateSort.toString() + " ");
		} else {
			querySql.append(" ,r.create_date desc ");
		}
		//执行SQL查询出列表数据
		@SuppressWarnings("unchecked")
		Page<Map<String, Object>> page = changePriceReviewDao.findBySql((Page<Map<String, Object>>)params.get("pageP"), querySql.toString(), Map.class);
//				为列表数据组装审核变量
		List<Map<String, Object>> list = page.getList();
		Object reviewid = null;
		Object status = null;
		for(Map<String, Object> map : list){
			reviewid = map.get("reviewid");
			if(reviewid == null || "".equals(reviewid.toString())){
				continue;
			}
			map.put("groupcode", orderCommonService.getProductGroupCode(map.get("producttype"), map.get("productid"), map.get("groupid")));  // 360V3查询使用签证产品中团号，同时为了正确展示其他产品线类型的团号。
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewid.toString());
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewid.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if(status == null || StringUtils.isBlank(status.toString()) || !NumberUtils.isNumber(status.toString())){
				map.put("statusdesc" ,"无审批状态");
				continue;
			}
			if(ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())){
				Object cReviewer = map.get("currentReviewer");
				String person = "未分配审批人";
				if(cReviewer != null && !StringUtil.isBlank(cReviewer.toString())){
					person = getReviewerDesc(cReviewer);
					map.put("statusdesc" ,"待" + person + "审批");
				} else {
					map.put("statusdesc" ,person);
				}
			} else {
				map.put("statusdesc" ,getReviewStatus(Integer.parseInt(map.get("status").toString())));
			}
			reviewid = null;
			status = null;
			//t1t2-v3根据游客id获取改前价格、改后价格
			Traveler traveller = travelerService.findTravelerById(Long.parseLong(map.get("travellerid").toString()));
			//改前金额
			List<MoneyAmount> originalMoneyList = moneyAmountService.findAmountBySerialNum(traveller.getOriginalPayPriceSerialNum());
			map.put("originalMoneyList", originalMoneyList);
			List<Map<String, Object>> changePriceList = changePriceService.getReviewList(Long.parseLong(map.get("orderId").toString()),Integer.parseInt(map.get("productType").toString()));
			for (int i = 0; i < changePriceList.size(); i++) {
				Map<String, Object> mapChangePrice = changePriceList.get(i);
				if (Context.ORDER_TYPE_SP.toString().equals(map.get("producttype").toString())) {
					ProductOrderCommon productOrder = orderCommonService.getProductorderById(Long.parseLong(map.get("orderId").toString()));
					if (productOrder.getPriceType() == 2) {
						handlerMap(mapChangePrice, map, productOrder);
					} else {
						handlerMap(mapChangePrice, map, null);
					}
				} else {
					handlerMap(mapChangePrice, map, null);
				}
			}
		}
		page.setList(list);
		return page;
	}
	
	/**
	 * 获取当前审核人描述 由id转化为name
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
	 * 获取审核状态描述
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
	
	/**
	 * 根据条件查询机票订单详情
	 */
	@Override
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId, String prdType) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.queryAirticketReviewOrderDetail(prdOrderId, prdType);
	}
	
	/**
	 * 根据条件查询参团订单详情
	 */
	@Override
	public Map<String, Object> queryGrouporderDeatail(String prdOrderId) {
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.queryGroupReviewOrderDetail(prdOrderId);
	}

	/**
	 * 根据条件查询签证订单详情 
	 */
	@Override
	public Map<String, Object> queryVisaorderDeatail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.queryVisaReviewOrderDetail(prdOrderId);
	}
	
	/**
	 * 根据条件查询散拼订单详情 
	 */
	@Override
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.querySanPinReviewOrderDetail(prdOrderId);
	}
	
	@Override
	public boolean doChangePrice(Review review) {
	
		Map<String, Object> reviewDetail = reviewService.getReviewDetailMapByReviewId(review.getId().toString());
		Map<String, String> params = new HashMap<String, String>();
		params.put("travelerId", reviewDetail.get("travelerid") == null ? "" : reviewDetail.get("travelerid").toString());
		params.put("orderId", review.getOrderId());
		params.put("orderType", review.getProductType().toString());
		params.put("currencyId", reviewDetail.get("currencyid") == null ? "" : reviewDetail.get("currencyid").toString());
		params.put("amount", reviewDetail.get("changedprice") == null ? "" : reviewDetail.get("changedprice").toString());
		return doChangePrice(params);
	}

	/**
	 * 进行改价业务数据处理
	 * 成功true 失败false
	 */
	@Override
	@Transactional
	public boolean doChangePrice(Map<String, String> params) {
		
		String travelerId = params.get("travelerId");
		String orderId = params.get("orderId");
		String orderType = params.get("orderType");
		String currencyId = params.get("currencyId");
		String amount = params.get("amount");
		
		ProductOrderCommon orderCommon = null;
		
		if (Context.ORDER_STATUS_LOOSE.equals(orderType)) {
			orderCommon = orderCommonService.getProductorderById(Long.parseLong(orderId));
			if (orderCommon.getPriceType() == 2) {
				Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
				Double finalPrice = TravelerUtils.getTravelerChargeRate(orderCommon, currency, amount);
				amount = finalPrice.toString();
			}
		}

		if("0".equals(travelerId)) {//团队
			List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(orderId), Context.MONEY_TYPE_YSH, Integer.parseInt(orderType));
			boolean curFlag = false;//是否有这个币种标志
			if(amount2 == null || amount2.size() == 0){//判空保护
				return false;
			}
			for(MoneyAmount tempMoney : amount2){
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
					curFlag = true;
					tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest());
//					moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
					BigDecimal amountT = tempMoney.getAmount();
					BigDecimal exchangerate = tempMoney.getExchangerate();
					Long id = tempMoney.getId();
					tempMoney.setAmount(amountT);
					tempMoney.setExchangerate(exchangerate);
					tempMoney.setId(id);
					moneyAmountDao.save(tempMoney);
					break;
				}
			}
			if(curFlag == false){
				MoneyAmount ma = new MoneyAmount();
				ma.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
				ma.setCurrencyId(Integer.parseInt(currencyId));
				ma.setSerialNum(amount2.get(0).getSerialNum());
				ma.setBusindessType(1);
				ma.setOrderType(Integer.parseInt(orderType));
				ma.setMoneyType(Context.MONEY_TYPE_YSH);
				ma.setUid(Long.parseLong(orderId));
				moneyAmountService.saveOrUpdateMoneyAmount(ma);
			}
		} else if ("-1".equals(travelerId)){//订金
			List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(orderId), Context.MONEY_TYPE_DJ, Integer.parseInt(orderType));
			for(MoneyAmount tempMoney : amount2){
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
					tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest());
//					moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
					BigDecimal amountT = tempMoney.getAmount();
					BigDecimal exchangerate = tempMoney.getExchangerate();
					Long id = tempMoney.getId();
					tempMoney.setAmount(amountT);
					tempMoney.setExchangerate(exchangerate);
					tempMoney.setId(id);
					moneyAmountDao.save(tempMoney);
					break;
				}
			}
		} else {//既不是团队改价 也不是订金改价 一定是游客改价
			//注释掉了 更改游客的结算价  如果是签证 应做对应处理 等签证功能完善
//			if(Context.ORDER_STATUS_VISA.equals(orderType)){//如果是签证的话 则更改游客的应收价
//				List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(travelerId), Context.MONEY_TYPE_JSJ, Integer.parseInt(orderType));
			//游客结算价改价
			Traveler traveler = travelerDao.findById(Long.parseLong(travelerId));
			if (traveler != null) {
				String payPriceSeria = traveler.getPayPriceSerialNum();
				String oldPayPriceString = moneyAmountService.getMoneyStr(payPriceSeria);
				// 记录结算价变动 for quauq
				String newPayPriceString = oldPayPriceString;
				List<MoneyAmount> tempMoneyList = moneyAmountDao.findAmountBySerialNum(payPriceSeria);
				if (CollectionUtils.isNotEmpty(tempMoneyList)) {
					// 游客结算价是否包含此币种
					boolean existCurrencyFlag = false;
					for (MoneyAmount tempMoney : tempMoneyList) {
						if (tempMoney != null && tempMoney.getCurrencyId() == Integer.parseInt(currencyId)) {
							tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
							Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
							tempMoney.setExchangerate(currency.getConvertLowest());
							moneyAmountDao.save(tempMoney);
							if (!existCurrencyFlag) {
								existCurrencyFlag = true;
							}
						}
					}
					// 如果游客结算价中不包含此币种则需要单独保存
					if (!existCurrencyFlag) {
						MoneyAmount travlerAmount = tempMoneyList.get(0);
						MoneyAmount moneyAmount = new MoneyAmount();
						BeanUtils.copyProperties(travlerAmount, moneyAmount);
						moneyAmount.setId(null);
						moneyAmount.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
						Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
						moneyAmount.setCurrencyId(currency.getId().intValue());
						moneyAmount.setExchangerate(currency.getConvertLowest());
						moneyAmount.setCreateTime(new Date());
						moneyAmountDao.save(moneyAmount);
					}
				}
				newPayPriceString = moneyAmountService.getMoneyStrFromAmountList("mark", tempMoneyList);
				
				if (Context.ORDER_TYPE_SP.toString().equals(orderType)) {
					if (Context.PRICE_TYPE_QUJ == orderCommon.getPriceType()) {			
						// 记录结算价变动日志
						StringBuffer logContent = new StringBuffer();
						logContent.append("QUAUQ订单");
						logContent.append("###");
						logContent.append("改价");
						logContent.append("###");
						logContent.append(traveler.getName() + "游客结算价从" + oldPayPriceString + "修改为" + newPayPriceString);
						this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, logContent.toString(), Context.log_state_add, traveler.getOrderType(), traveler.getOrderId());
					}
				}
			}
			
			//游客改价 同时更改订单的价格
			String totalMoneySerial = "";
			//团期类
			if (Context.ORDER_STATUS_SINGLE.equals(orderType) || Context.ORDER_STATUS_LOOSE.equals(orderType) ||
					Context.ORDER_STATUS_BIG_CUSTOMER.equals(orderType) || Context.ORDER_STATUS_FREE.equals(orderType) ||
					Context.ORDER_STATUS_STUDY.equals(orderType) || Context.ORDER_STATUS_CRUISE.equals(orderType)) {				
				orderCommon = orderCommonService.getProductorderById(Long.parseLong(orderId));
				if (orderCommon != null) {
					totalMoneySerial = orderCommon.getTotalMoney();					
				}
			//机票
			} else if (Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
				AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
				if (airticketOrder != null) {
					totalMoneySerial = airticketOrder.getTotalMoney();
				}
			} else {
				String sql = "UPDATE  visa_order vo LEFT JOIN money_amount ma  ON vo.total_money =ma.serialNum "
						+" SET ma.amount = ma.amount+"+amount+" WHERE vo.id="+orderId+" AND ma.currencyId="+currencyId;
				
				moneyAmountDao.updateBySql(sql);
			}

			Currency currency = currencyDao.findOne(Long.parseLong(currencyId)); // currency放到上方，下面的注释
			if (StringUtils.isNotBlank(totalMoneySerial)) {
				List<MoneyAmount> amount3 = moneyAmountService.findAmountBySerialNumAndCurrencyId(totalMoneySerial, Integer.parseInt(currencyId));

				if (CollectionUtils.isNotEmpty(amount3)) {
					for(int j=0; j<amount3.size(); j++){
						MoneyAmount tempMoney = amount3.get(j);
						if (tempMoney != null) {
							if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的订单应收价
								tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));							
//								Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
								tempMoney.setExchangerate(currency.getConvertLowest());
								moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
								break;
							} else if (j == amount3.size() - 1) {
								MoneyAmount tempMoneyNew = new MoneyAmount();
								BeanUtils.copyProperties(tempMoney, tempMoneyNew);
								tempMoneyNew.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));//金额
								tempMoneyNew.setCurrencyId(Integer.parseInt(currencyId));//币种
//								Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
								tempMoneyNew.setExchangerate(currency.getConvertLowest());//汇率
								tempMoneyNew.setSerialNum(totalMoneySerial);//uuid	
								tempMoneyNew.setCreateTime(new Date());
								moneyAmountDao.saveObj(tempMoneyNew);
							}
						}
					}
				} else {
					List<MoneyAmount> tempMoneyList = moneyAmountDao.findAmountBySerialNum(totalMoneySerial);
					MoneyAmount travlerAmount = tempMoneyList.get(0);
					MoneyAmount moneyAmount = new MoneyAmount();
					BeanUtils.copyProperties(travlerAmount, moneyAmount);
					moneyAmount.setId(null);
					moneyAmount.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
//					Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
					moneyAmount.setCurrencyId(currency.getId().intValue());
					moneyAmount.setExchangerate(currency.getConvertLowest());
					moneyAmount.setCreateTime(new Date());
					moneyAmountDao.save(moneyAmount);
				}
			}
			// quauq订单重新计算结算价服务费
			if (Context.ORDER_STATUS_LOOSE.equals(orderType)) {
				ProductOrderCommon order = orderCommonService.getProductorderById(Long.parseLong(orderId));
				if (order != null && Context.PRICE_TYPE_QUJ == order.getPriceType()) {
					orderServiceForSaveAndModify.setOrderChargePrice(order, false);
				}
			}
		}
		
		//-------by------junhao.zhao-----2017-03-24-----------改价之后，签证与团类要将表order_data_statistics中对应的收客人数与订单金额修改---开始---
		if(null != orderId && null != orderType){
			switch (orderType) {
			case Context.ORDER_STATUS_VISA:
				orderDateSaveOrUpdateDao.updatePeopleAndMoney(Long.valueOf(orderId), Integer.valueOf(orderType));
				break;
			default:
				orderDateSaveOrUpdateDao.updatePeopleAndMoneyPro(Long.valueOf(orderId), Integer.valueOf(orderType));
			}
		}
		//-------by------junhao.zhao-----2017-03-24-----------改价之后，签证与团类要将表order_data_statistics中对应的收客人数与订单金额修改---结束---
		
		return true;
	}
	
	/**
	 * 通过改价得到改后金额
	 * @param mapChangePrice
	 * @param map
     */
	private void handlerMap(Map<String, Object> mapChangePrice, Map<String, Object> map, ProductOrderCommon productOrder) {
		//列表中的reviewUUID
		String reviewId = map.get("reviewid") == null ? "" : map.get("reviewid").toString();
		//costchange表的reviewUUID
		String id = mapChangePrice.get("id") == null ? "" : mapChangePrice.get("id").toString();
		String prices = mapChangePrice.get("prices").toString();
		String[] priceArr = prices.split(",");
		String currencyIds = mapChangePrice.get("currencyIds").toString();
		String[] currencyIdArr = currencyIds.split(",");
		Map<String, Object> moneyMap = new HashMap<String, Object>();
		for (int j = 0; j < currencyIdArr.length; j++) {
			String currencyId = currencyIdArr[j];
			BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
			moneyMap.put(currencyId, priceSum.add(new BigDecimal(priceArr[j].toString())));
		}

		List<Map<String, Object>> moneyList = new ArrayList<Map<String, Object>>();
		for (Map.Entry<String, Object> entry : moneyMap.entrySet()) {
			Map<String, Object> oneMap = new HashMap<String, Object>();
			
			if (null != productOrder) {
				Currency currency = currencyDao.findById(Long.parseLong(entry.getKey()));
				oneMap.put("key", entry.getKey());
				oneMap.put("value", TravelerUtils.getTravelerChargeRate(productOrder, currency, entry.getValue().toString()));
			} else {
				oneMap.put("key", entry.getKey());
				oneMap.put("value", entry.getValue());
			}
			moneyList.add(oneMap);
		}
		if(reviewId.equals(id))
			map.put("moneyList", moneyList);
	}
}
